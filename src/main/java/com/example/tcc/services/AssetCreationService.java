package com.example.tcc.services;

import com.example.tcc.dto.AssetDetailsDto;
import com.example.tcc.dto.AssetNumberRecognitionDto;
import com.example.tcc.responses.CreateAssetResponseDto;
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
import java.util.List;
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
    private final AssetDetailsService assetDetailsService;

    private Boolean areAssetsValid(List<AssetDetailsDto> assets) {
        for (AssetDetailsDto asset : assets) {
            if(asset.getAssetNumber().isBlank() || asset.getMainImage().isBlank() || asset.getImages().size() < 2) {
                return false;
            }
        }
        return true;
    }

    public CreateAssetResponseDto createAndRecognize(Long fileId, MultipartFile image) throws Exception {
        Optional<FileModel> file = fileRepository.findById(fileId);
        if (file.isPresent()) {
            List<AssetDetailsDto> assets = assetDetailsService.getAssets(fileId);
            if(!assets.isEmpty() && !areAssetsValid(assets)) { throw new Exception("Complete assets before adding a new one"); }

            String filename = imageUploadService.saveImage(image);
            String path = Paths.get(imageDirectory).toAbsolutePath().normalize().resolve(filename).toString();
            BufferedImage bufferedImage = barcodeDetectionService.detectBarcode(path);
            AssetNumberRecognitionDto assetInfo = assetNumberService.getAssetNumberAndConfidenceLevel(bufferedImage);

            AssetModel asset = assetRepository.save(new AssetModel(filename));
            fileAssetRepository.save(new FileAssetModel(file.get(), asset));
            return new CreateAssetResponseDto(fileId, asset.getId(), baseURL+"image/"+filename, assetInfo.getAssetNumber(), assetInfo.getConfidenceLevel());
        } else {
            throw new NoSuchElementException("File with id " + fileId + " not found");
        }
    }
}
