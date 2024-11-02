package com.example.tcc.services;

import com.example.tcc.util.CustomMultipartFile;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

@Service
@RequiredArgsConstructor
public class ImageLabelingService {
    private final AWSS3Service s3Service;

    public void drawString(String filename, String inputString) {
        try {
            byte[] imageBytes = s3Service.downloadImage(filename);

            if (imageBytes == null) {
                System.err.println("Erro ao obter imagem do S3.");
                return;
            }

            final BufferedImage image = ImageIO.read(new ByteArrayInputStream(imageBytes));

            Graphics g = image.getGraphics();
            g.setFont(g.getFont().deriveFont(30f));
            g.setColor(Color.RED);  // Definir a cor do texto para amarelo
            g.drawString(inputString, 5, 30);
            g.dispose();

            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            ImageIO.write(image, "jpg", baos);
            imageBytes = baos.toByteArray();

            MultipartFile file = new CustomMultipartFile(filename, filename, "image/jpg", imageBytes);

            s3Service.putImage(filename, file);
        } catch (IOException e) {
            // Trata a exceção de IO, por exemplo, se o arquivo não puder ser criado
            System.err.println("Erro ao salvar a imagem: " + e.getMessage());
            e.printStackTrace();
        }
    }
}
