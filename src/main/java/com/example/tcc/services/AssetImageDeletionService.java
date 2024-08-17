package com.example.tcc.services;

import com.example.tcc.models.ImageModel;
import com.example.tcc.repositories.ImageRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.File;
import java.util.List;
import java.util.NoSuchElementException;

@Service
@RequiredArgsConstructor
public class AssetImageDeletionService {
    @Value("${tcc.disk.images-directory}")
    private String imageDirectory;
    private final ImageRepository imageRepository;

    public void delete(String filename) {
        List<ImageModel> image = imageRepository.findByFilename(filename);

        if (!image.isEmpty()) {
            imageRepository.delete(image.getFirst());
            deleteFileFromSystem(imageDirectory + "/" + filename);
        } else {
            throw new NoSuchElementException("Image with filename " + filename + " not found");
        }
    }

    private void deleteFileFromSystem(String filePath) {
        File file = new File(filePath);
        if (file.exists()) {
            file.delete();
        }
    }
}

