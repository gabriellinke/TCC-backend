package com.example.tcc.services;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;

import static com.example.tcc.util.GetResource.getResource;

@Service
public class FileRetrieveService {
    @Value("${tcc.disk.files-directory}")
    private String fileRepository;

    public Resource retrieveFile(String filename) {
        return this.retrieve(this.fileRepository, filename);
    }

    public Resource retrieve(String directory, String filename) {
        return getResource(directory, filename);
    }

}