package com.example.tcc.services;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;

import static com.example.tcc.util.GetResource.getResource;

@Service
public class ImageRetrieveService {
    @Value("${tcc.disk.images-directory}")
    private String imageDirectory;

    public Resource retrieveImage(String filename) {
        return this.retrieve(this.imageDirectory, filename);
    }

    public Resource retrieve(String directory, String filename) {
        return getResource(directory, filename);
    }
}