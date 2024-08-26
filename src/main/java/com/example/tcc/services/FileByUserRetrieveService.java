package com.example.tcc.services;

import com.example.tcc.models.FileModel;
import com.example.tcc.repositories.FileRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;

import java.util.List;

import static com.example.tcc.util.GetResource.getResource;

@Service
@RequiredArgsConstructor
public class FileByUserRetrieveService {
    @Value("${tcc.backend.base-url}")
    private String baseURL;
    private final FileRepository fileRepository;

    public List<FileModel> retrieve(Long userId) {
        List<FileModel> files = fileRepository.findByUserId(userId);
        for(FileModel file : files) {
            file.setFilename(baseURL+ "file/" +file.getFilename());
        }
        return files;
    }

}