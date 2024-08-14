package com.example.tcc.services;

import com.example.tcc.models.AssetModel;
import com.example.tcc.repositories.AssetRepository;
import com.example.tcc.repositories.FileAssetRepository;
import com.example.tcc.repositories.ImageRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.File;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class AssetDeletionService {
    @Value("${tcc.disk.images-directory}")
    private String imageDirectory;
    private final AssetRepository assetRepository;
    private final FileAssetRepository fileAssetRepository;
    private final ImageRepository imageRepository;

    @Transactional
    public void delete(Long assetId) {
        Optional<AssetModel> asset = assetRepository.findById(assetId);
        if (asset.isPresent()) {
            // Obtenha os filenames das imagens relacionadas
            List<String> filenames = imageRepository.findFilenamesByAssetId(assetId);
            filenames.add(asset.get().getMainImage());

            // Delete as imagens do filesystem
            for (String filename : filenames) {
                deleteFileFromSystem(imageDirectory + "/" + filename);
            }

            // Deleta as imagens relacionadas
            imageRepository.deleteByAssetId(assetId);

            // Deleta os arquivos relacionados
            fileAssetRepository.deleteByAssetId(assetId);

            // Deleta o próprio asset
            assetRepository.deleteById(assetId);
        }


    }

    private void deleteFileFromSystem(String filePath) {
        File file = new File(filePath);
        if (file.exists()) {
            file.delete();
        }
    }
}
