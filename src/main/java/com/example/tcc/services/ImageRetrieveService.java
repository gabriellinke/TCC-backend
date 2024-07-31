package com.example.tcc.services;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.UUID;

@Service
public class ImageRetrieveService {
    @Value("${tcc.disk.images-directory}")
    private String imageDirectory;

    public Resource retrieveImage(String filename) {
        return this.retrieve(this.imageDirectory, filename);
    }

    public Resource retrieve(String directory, String filename) {
        Path imagePath = Path.of(directory, filename);

        try {
            Resource resource = new UrlResource(imagePath.toUri());
            if (resource.exists() || resource.isReadable()) {
                return resource;
            }
            return null;
        } catch (MalformedURLException e) {
            System.err.println(e.getMessage());
            return null;
        }
    }
}