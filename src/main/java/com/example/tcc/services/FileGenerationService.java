package com.example.tcc.services;

import com.example.tcc.dto.AssetDetailsDto;
import com.itextpdf.io.image.ImageData;
import com.itextpdf.io.image.ImageDataFactory;
import com.itextpdf.io.source.ByteArrayOutputStream;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class FileGenerationService {
    private final AWSS3Service s3Service;

    public String saveFile(List<AssetDetailsDto> assets) {
        return this.save(assets);
    }

    private void addImage(Document document, String filename) {
        try {
            byte[] imageBytes = s3Service.downloadImage(filename);

            if (imageBytes == null) {
                System.err.println("Erro ao obter imagem do S3.");
                throw new RuntimeException("Erro ao obter imagem do S3.");
            }

            ImageData imgData = ImageDataFactory.create(imageBytes);
            Image img = new Image(imgData);
            document.add(img);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void addImages(Document document, AssetDetailsDto asset) {
        addImage(document, asset.getMainImage());
        endPage(document);
        for (String image : asset.getImages()) {
            addImage(document, image);
            addBlankSpace(document, 3);
        }
    }

    public void addBlankSpace(Document document, int numberOfLines) {
        for (int i = 0; i < numberOfLines; i++) {
            document.add(new Paragraph(" "));
        }
    }

    public void endPage(Document document) {
        document.add(new AreaBreak());
    }

    private Table createTableAndAddHeader() {
        // Criar uma tabela com 7 colunas
        Table table = new Table(7);

        // Adicionar cabeçalhos de coluna
        table.addCell(new Cell().add(new Paragraph("Tombo")));
        table.addCell(new Cell().add(new Paragraph("Tombo Antigo")));
        table.addCell(new Cell().add(new Paragraph("Descrição")));
        table.addCell(new Cell().add(new Paragraph("Estado de Conservação")));
        table.addCell(new Cell().add(new Paragraph("Situação")));
        table.addCell(new Cell().add(new Paragraph("Local")));
        table.addCell(new Cell().add(new Paragraph("Responsável")));

        return table;
    }

    private void addDataToTable(Table table, AssetDetailsDto data) {
            // Adicionar dados à tabela
            table.addCell(new Cell().add(new Paragraph(data.getAssetNumber())));
            table.addCell(new Cell().add(new Paragraph(data.getFormerAssetNumber())));
            table.addCell(new Cell().add(new Paragraph(data.getDescription())));
            table.addCell(new Cell().add(new Paragraph(data.getConservationState())));
            table.addCell(new Cell().add(new Paragraph(data.getSituation())));
            table.addCell(new Cell().add(new Paragraph(data.getPlace())));
            table.addCell(new Cell().add(new Paragraph(data.getResponsible())));
    }

    private void addTable(Document document, AssetDetailsDto asset) {
        try {
            Table table = createTableAndAddHeader();
            addDataToTable(table, asset);
            document.add(table);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public String save(List<AssetDetailsDto> assets) {
        try {
            // Gera um nome de arquivo único com a extensão .pdf
            String uniqueFilename = UUID.randomUUID() + ".pdf";

            // Cria o documento PDF
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            PdfWriter writer = new PdfWriter(baos);
            PdfDocument pdfDoc = new PdfDocument(writer);
            Document document = new Document(pdfDoc);

            // Processar cada asset
            for (AssetDetailsDto asset : assets) {
                if(!asset.getAssetNumber().isBlank()) {
                    if(asset != assets.getFirst()) endPage(document); // New page
                    addTable(document, asset);
                    addBlankSpace(document, 3);
                    addImages(document, asset);
                }
            }

            document.close();
            s3Service.putPDF(uniqueFilename, baos);
            return uniqueFilename;
        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        }
    }
}
