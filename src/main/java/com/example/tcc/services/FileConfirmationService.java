package com.example.tcc.services;

import com.example.tcc.dto.AssetDetailsDto;
import com.example.tcc.models.FileModel;
import com.example.tcc.repositories.FileRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class FileConfirmationService {
    private final String bucketURL;
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

    private FileModel updateFileOnDatabase(FileModel updatedFile, List<AssetDetailsDto> assets, String filename) {
        updatedFile.setFilename(filename);
        updatedFile.setAssetQuantity((long) assets.size());
        updatedFile.setResponsible(assets.getFirst().getResponsible());
        updatedFile.setConsolidated(true);
        updatedFile.setConsolidatedAt(LocalDateTime.now());
        FileModel response = fileRepository.save(updatedFile);
        response.setFilename(bucketURL+filename);
        return response;
    }

    public FileModel confirm(Long fileId) throws Error, IOException {
        Optional<FileModel> file = fileRepository.findById(fileId);
        if(file.isPresent()) {
            List<AssetDetailsDto> assets = assetDetailsService.getAssets(fileId);
            if(assets.isEmpty() || !areAssetsValid(assets)) { throw new Error("Bens inválidos"); }

            labelAllImages(assets);

            String filename = fileGenerationService.saveFile(assets);
            return updateFileOnDatabase(file.get(), assets, filename);
        }
        throw new Error("File not found");
    }
}
