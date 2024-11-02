package com.example.tcc.services;

import com.example.tcc.models.FileModel;
import com.example.tcc.repositories.FileRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

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

}