package com.example.tcc.services;

import com.example.tcc.models.FileModel;
import com.example.tcc.repositories.FileRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class FileCreationService {
    private final FileRepository fileRepository;

    public FileCreationService(FileRepository fileRepository) {
        this.fileRepository = fileRepository;
    }

    public FileModel create(Long userId) {
        return fileRepository.save(new FileModel(userId));
    }

    public Boolean canCreateFile(Long userId) {
        List<FileModel> userFiles = fileRepository.findByUserId(userId);
        List<FileModel> notConsolidatedFiles = userFiles.stream().filter(file -> !file.getConsolidated()).toList();
        return notConsolidatedFiles.isEmpty();
    }
}
