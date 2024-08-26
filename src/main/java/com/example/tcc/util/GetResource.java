package com.example.tcc.util;

import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;

import java.net.MalformedURLException;
import java.nio.file.Path;

public class GetResource {
    static public Resource getResource(String directory, String filename) {
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
