package com.example.tcc.services;

import org.springframework.stereotype.Service;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

@Service
public class ImageLabelingService {
    public void drawString(String path, String inputString) {
        try {
            final BufferedImage image = ImageIO.read(new File(path));

            Graphics g = image.getGraphics();
            g.setFont(g.getFont().deriveFont(30f));
            g.setColor(Color.YELLOW);  // Definir a cor do texto para amarelo
            g.drawString(inputString, 5, 30);
            g.dispose();

            ImageIO.write(image, "jpg", new File(path));
        } catch (IOException e) {
            // Trata a exceção de IO, por exemplo, se o arquivo não puder ser criado
            System.err.println("Erro ao salvar a imagem: " + e.getMessage());
            e.printStackTrace();
        }
    }
}
