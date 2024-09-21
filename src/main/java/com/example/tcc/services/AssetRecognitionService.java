package com.example.tcc.services;

import com.example.tcc.dto.AssetNumberRecognitionDto;
import com.example.tcc.models.AssetModel;
import com.example.tcc.models.FileAssetModel;
import com.example.tcc.repositories.AssetRepository;
import com.example.tcc.repositories.FileAssetRepository;
import com.example.tcc.responses.CreateAssetResponseDto;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.awt.image.BufferedImage;
import java.nio.file.Paths;
import java.util.NoSuchElementException;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class AssetRecognitionService {
    @Value("${tcc.disk.images-directory}")
    private String imageDirectory;
    @Value("${tcc.backend.base-url}")
    private String baseURL;
    private final AssetRepository assetRepository;
    private final FileAssetRepository fileAssetRepository;
    private final BarcodeDetectionService barcodeDetectionService;
    private final AssetNumberService assetNumberService;

    public CreateAssetResponseDto recognize(Long assetId) {
        Optional<AssetModel> asset = assetRepository.findById(assetId);
        Optional<FileAssetModel> fileAsset = fileAssetRepository.findByAssetId(assetId);
        if (asset.isPresent() && fileAsset.isPresent()) {
            AssetModel assetModel = asset.get();
            String filename = assetModel.getMainImage().substring(assetModel.getMainImage().lastIndexOf("/") + 1);
            String path = Paths.get(imageDirectory).toAbsolutePath().normalize().resolve(filename).toString();
            BufferedImage bufferedImage = barcodeDetectionService.detectBarcode(path);
            AssetNumberRecognitionDto assetInfo = assetNumberService.getAssetNumberAndConfidenceLevel(bufferedImage);

            return new CreateAssetResponseDto(fileAsset.get().getFile().getId(), assetModel.getId(), baseURL+"image/"+filename, assetInfo.getAssetNumber(), assetInfo.getConfidenceLevel());
        } else {
            throw new NoSuchElementException("Bem com id " + assetId + " não encontrado");
        }
    }
}
