package com.example.tcc.services;

import com.example.tcc.models.FileModel;
import com.example.tcc.models.ImageModel;
import com.example.tcc.repositories.FileRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.UUID;

@Service
public class FileCreationService {
    private final FileRepository fileRepository;

    public FileCreationService(FileRepository fileRepository) {
        this.fileRepository = fileRepository;
    }

    public FileModel create(Long userId) {
        return fileRepository.save(new FileModel(userId));
    }
}
