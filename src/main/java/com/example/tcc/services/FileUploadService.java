package com.example.tcc.services;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
public class FileUploadService {
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

            // Gera um nome de arquivo único
            String originalFilename = file.getOriginalFilename();
            String fileExtension = originalFilename.substring(originalFilename.lastIndexOf("."));
            String uniqueFilename = UUID.randomUUID().toString() + fileExtension;

            Path filePath = uploadPath.resolve(uniqueFilename);
            file.transferTo(filePath.toFile());

            return filePath.toString();

        } catch (IOException e) {
            throw new RuntimeException("Erro no upload do arquivo", e);
        }
    }
}