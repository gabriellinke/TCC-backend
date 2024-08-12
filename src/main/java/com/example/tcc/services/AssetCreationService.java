package com.example.tcc.services;

import com.example.tcc.dto.AssetNumberRecognitionDto;
import com.example.tcc.dto.CreateAssetResponseDto;
import com.example.tcc.models.AssetModel;
import com.example.tcc.models.FileAssetModel;
import com.example.tcc.models.FileModel;
import com.example.tcc.repositories.AssetRepository;
import com.example.tcc.repositories.FileAssetRepository;
import com.example.tcc.repositories.FileRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.awt.image.BufferedImage;
import java.nio.file.Paths;
import java.util.NoSuchElementException;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class AssetCreationService {
    @Value("${tcc.disk.images-directory}")
    private String imageDirectory;
    @Value("${tcc.backend.base-url}")
    private String baseURL;
    private final AssetRepository assetRepository;
    private final FileAssetRepository fileAssetRepository;
    private final FileRepository fileRepository;
    private final ImageUploadService imageUploadService;
    private final BarcodeDetectionService barcodeDetectionService;
    private final AssetNumberService assetNumberService;

    public CreateAssetResponseDto createAndRecognize(Long fileId, MultipartFile image) {
        String filename = imageUploadService.saveImage(image);
        String path = Paths.get(imageDirectory).toAbsolutePath().normalize().resolve(filename).toString();
        BufferedImage bufferedImage = barcodeDetectionService.detectBarcode(path);
        AssetNumberRecognitionDto assetInfo = assetNumberService.getAssetNumberAndConfidenceLevel(bufferedImage);

        AssetModel asset = assetRepository.save(new AssetModel(filename));
        Optional<FileModel> file = fileRepository.findById(fileId);

        if (file.isPresent()) {
            FileAssetModel fileAsset = fileAssetRepository.save(new FileAssetModel(file.get(), asset));
            return new CreateAssetResponseDto(fileId, asset.getId(), baseURL+"image/"+filename, assetInfo.getAssetNumber(), assetInfo.getConfidenceLevel());
        } else {
            // Tratar o caso onde o FileModel não existe, por exemplo:
            throw new NoSuchElementException("File with id " + fileId + " not found");
        }


    }
}
