package com.example.tcc.services;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import javax.imageio.ImageIO;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

@Service
public class OCRService {

    @Value("${python.path}")
    private String pythonPath;

    @Value("${ocr.script.path}")
    private String ocrScriptPath;

    @Value("${tcc.disk.images-directory}")
    private String imageDirectory;

    public String performOCR(BufferedImage image) {
        Path tempImagePath = null;
        try {
            // Save the BufferedImage to a temporary file
            tempImagePath = Files.createTempFile(Paths.get(imageDirectory), "temp_image", ".jpg");
            ImageIO.write(image, "jpg", tempImagePath.toFile());

            // Build the command to call the Python script
            ProcessBuilder processBuilder = new ProcessBuilder(
                    pythonPath,
                    ocrScriptPath,
                    "-i", tempImagePath.toString()
            );

            // Start the process
            Process process = processBuilder.start();

            // Capture the output from the process
            StringBuilder output = new StringBuilder();
            try (BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()))) {
                String line;
                while ((line = reader.readLine()) != null) {
                    output.append(line).append("\n");
                }
            }

            // Wait for the process to complete and check the exit status
            int exitCode = process.waitFor();
            if (exitCode != 0) {
                throw new RuntimeException("Python script execution failed with exit code: " + exitCode);
            }

            return output.toString().trim();

        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
            return null;
        } finally {
            // Clean up the temporary file
            if (tempImagePath != null) {
                try {
                    Files.deleteIfExists(tempImagePath);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
