package com.example.tcc.services;

import com.example.tcc.dto.EspelhoPatrimonioResponseDto;
import com.itextpdf.io.image.ImageData;
import com.itextpdf.io.image.ImageDataFactory;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.Cell;
import com.itextpdf.layout.element.Image;
import com.itextpdf.layout.element.Paragraph;
import com.itextpdf.layout.element.Table;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.FileOutputStream;
import java.nio.file.Path;
import java.nio.file.Paths;

@Service
public class PDFGenerationService {
    @Value("${tcc.disk.files-directory}")
    private String filesDirectory;

    @Autowired
    private AuthService authService;
    @Autowired
    private EspelhoPatrimonioService espelhoPatrimonioService;

    private static String removeLeadingZeros(String numberStr) {
        if (numberStr == null || numberStr.isEmpty()) {
            return numberStr;
        }
        return numberStr.replaceFirst("^0+(?!$)", "");
    }

    public String saveFile(String assetNumber, String imagePath) {
        return this.save(this.filesDirectory, assetNumber, imagePath);
    }

    private void addImage(Document document, Path path) {
        try {
            // Adiciona a imagem ao PDF
            ImageData imgData = ImageDataFactory.create(path.toAbsolutePath().toString());
            Image img = new Image(imgData);

            float scaler = ((document.getPdfDocument().getDefaultPageSize().getWidth() - document.getLeftMargin() - document.getRightMargin()) / img.getImageWidth()) * 100;
            //img.scalePercent(scaler);
            img.setAutoScale(true);

            document.add(img);
        } catch (Exception e) {
            e.printStackTrace();
        }
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

    private void addDataToTable(Table table, EspelhoPatrimonioResponseDto data) {
            // Adicionar dados à tabela
            table.addCell(new Cell().add(new Paragraph(data.getTombo())));
            table.addCell(new Cell().add(new Paragraph(data.getTomboAntigo())));
            table.addCell(new Cell().add(new Paragraph(data.getDescricao())));
            table.addCell(new Cell().add(new Paragraph(data.getEstadoConservacao())));
            table.addCell(new Cell().add(new Paragraph(data.getSituacao())));
            table.addCell(new Cell().add(new Paragraph(data.getLocal())));
            table.addCell(new Cell().add(new Paragraph(data.getResponsavel())));
    }


    private void addTable(Document document, String assetNumber) {
        /*try {
            String token = authService.login("gabriellinke@alunos.utfpr.edu.br", "gabriel12");
            EspelhoPatrimonioResponseDto response = espelhoPatrimonioService.getEspelhoPatrimonio(token, removeLeadingZeros(assetNumber));

            Table table = createTableAndAddHeader();
            addDataToTable(table, response);

            document.add(table);
        } catch (Exception e) {
            e.printStackTrace();
        }*/
    }

    public String save(String directory, String assetNumber, String imagePath) {
        String pdfPath = directory + File.separator + assetNumber + ".pdf";

        try {
            // Cria o documento PDF
            PdfWriter writer = new PdfWriter(new FileOutputStream(pdfPath));
            PdfDocument pdfDoc = new PdfDocument(writer);
            Document document = new Document(pdfDoc);

            addTable(document, assetNumber);

            Path path = Paths.get(imagePath).toAbsolutePath();
            addImage(document, path);

            document.close();
        } catch (Exception e) {
            e.printStackTrace();
            return "Erro ao salvar o arquivo: " + e.getMessage();
        }

        return pdfPath;
    }
}
