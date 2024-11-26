package com.example.tcc.services;

import com.example.tcc.models.FileModel;
import com.example.tcc.repositories.FileRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class FileByUserRetrieveService {
    private final String bucketURL;
    private final FileRepository fileRepository;

    public List<FileModel> retrieve(Long userId) {
        List<FileModel> files = fileRepository.findByUserId(userId);
        for(FileModel file : files) {
            file.setFilename(bucketURL + file.getFilename());
        }
        return files;
    }

    public List<FileModel> retrieveWithDateFilter(Long userId) {
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime tenDaysAgo = now.minusDays(10);

        List<FileModel> files = this.retrieve(userId);
        return files.stream()
                .filter(file -> (!file.getConsolidated()) || file.getConsolidatedAt() != null && file.getConsolidatedAt().isAfter(tenDaysAgo))
                .collect(Collectors.toList());
    }

}