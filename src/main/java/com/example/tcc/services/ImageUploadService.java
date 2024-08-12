package com.example.tcc.services;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
public class ImageUploadService {
    @Value("${tcc.disk.images-directory}")
    private String imageDirectory;

    public String saveImage(MultipartFile photo) {
        return this.save(this.imageDirectory, photo);
    }

    public String save(String directory, MultipartFile file) {
        try {
            Path uploadPath = Paths.get(directory).toAbsolutePath().normalize();
            if (!Files.exists(uploadPath)) {
                Files.createDirectories(uploadPath);
            }

            // Gera um nome de arquivo único com a extensão .jpg
            String uniqueFilename = UUID.randomUUID().toString() + ".jpg";
            Path filePath = uploadPath.resolve(uniqueFilename);

            // Converte e salva a imagem no formato JPEG
            File convertedFile = new File(filePath.toString());
            file.transferTo(convertedFile);

            return uniqueFilename;
        } catch (IOException e) {
            throw new RuntimeException("Erro no upload do arquivo", e);
        }
    }
}