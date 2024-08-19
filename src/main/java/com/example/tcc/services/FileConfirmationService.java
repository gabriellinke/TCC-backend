package com.example.tcc.services;

import com.example.tcc.dto.AssetDetailsDto;
import com.example.tcc.models.FileModel;
import com.example.tcc.repositories.FileRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor
public class FileConfirmationService {
    private final AssetDetailsService assetDetailsService;
    private final ImageLabelingService imageLabelingService;
    private final FileGenerationService fileGenerationService;
    private final FileRepository fileRepository;

    private void labelAllImages(List<AssetDetailsDto> assets) {
        for (AssetDetailsDto asset : assets) {
            // Label para a main image
            imageLabelingService.drawString(asset.getMainImage(), asset.getAssetNumber());

            // Label para cada imagem na lista de images
            for (String image : asset.getImages()) {
                imageLabelingService.drawString(image, asset.getAssetNumber());
            }
        }
    }

    private Boolean areAssetsValid(List<AssetDetailsDto> assets) {
        for (AssetDetailsDto asset : assets) {
            if(asset.getAssetNumber().isBlank() || asset.getMainImage().isBlank() || asset.getImages().size() < 2) {
                return false;
            }
        }
        return true;
    }

    private FileModel updateFIleOnDatabase(FileModel updatedFile, String filename) {
        updatedFile.setFilename(filename);
        updatedFile.setConsolidated(true);
        updatedFile.setConsolidatedAt(LocalDateTime.now());
        return fileRepository.save(updatedFile);
    }

    public FileModel confirm(Long fileId) throws Error, IOException {
        Optional<FileModel> file = fileRepository.findById(fileId);
        if(file.isPresent()) {
            List<AssetDetailsDto> assets = assetDetailsService.getAssetsWithLocalPath(fileId);
            if(assets.isEmpty() || !areAssetsValid(assets)) { throw new Error("Invalid assets"); }

            labelAllImages(assets);

            String filename = fileGenerationService.saveFile(assets);
            return updateFIleOnDatabase(file.get(), filename);
        }
        throw new Error("File not found");
    }
}
