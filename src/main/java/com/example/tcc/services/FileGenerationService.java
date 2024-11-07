package com.example.tcc.services;

import com.example.tcc.dto.AssetDetailsDto;
import com.itextpdf.io.image.ImageData;
import com.itextpdf.io.image.ImageDataFactory;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.*;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.UUID;

@Service
public class FileGenerationService {
    @Value("${tcc.disk.files-directory}")
    private String filesDirectory;

    public String saveFile(List<AssetDetailsDto> assets) throws IOException {
        return this.save(this.filesDirectory, assets);
    }

    private void addImage(Document document, String path) {
        try {
            ImageData imgData = ImageDataFactory.create(path);
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
            table.addCell(new Cell().add(new Paragraph(data.getAssetNumber() != null ? data.getAssetNumber() : "")));
            table.addCell(new Cell().add(new Paragraph(data.getFormerAssetNumber() != null ? data.getFormerAssetNumber() : "")));
            table.addCell(new Cell().add(new Paragraph(data.getDescription() != null ? data.getDescription() : "")));
            table.addCell(new Cell().add(new Paragraph(data.getConservationState() != null ? data.getConservationState() : "")));
            table.addCell(new Cell().add(new Paragraph(data.getSituation() != null ? data.getSituation() : "")));
            table.addCell(new Cell().add(new Paragraph(data.getPlace() != null ? data.getPlace() : "")));
            table.addCell(new Cell().add(new Paragraph(data.getResponsible() != null ? data.getResponsible() : "")));
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

    public String save(String directory, List<AssetDetailsDto> assets) throws IOException {
        try {
            Path uploadPath = Paths.get(directory).toAbsolutePath().normalize();
            if (!Files.exists(uploadPath)) {
                Files.createDirectories(uploadPath);
            }

            // Gera um nome de arquivo único com a extensão .jpg
            String uniqueFilename = UUID.randomUUID() + ".pdf";
            String filePath = uploadPath.resolve(uniqueFilename).toString();

            // Cria o documento PDF
            PdfWriter writer = new PdfWriter(new FileOutputStream(filePath));
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
            return uniqueFilename;
        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        }
    }
}
