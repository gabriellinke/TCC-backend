package com.example.tcc.services;

import com.example.tcc.models.AssetModel;
import com.example.tcc.models.FileAssetModel;
import com.example.tcc.repositories.AssetRepository;
import com.example.tcc.repositories.FileAssetRepository;
import com.example.tcc.responses.AssetInfoResponseDto;
import com.example.tcc.responses.CreateAssetResponseDto;
import com.itextpdf.io.image.ImageData;
import com.itextpdf.io.image.ImageDataFactory;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.Cell;
import com.itextpdf.layout.element.Image;
import com.itextpdf.layout.element.Paragraph;
import com.itextpdf.layout.element.Table;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.FileOutputStream;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Objects;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class AssetConfirmationService {
    private final AssetInfoService assetInfoService;
    private final AssetDeletionService assetDeletionService;
    private final AssetRepository assetRepository;
    private final FileAssetRepository fileAssetRepository;

    private static String removeLeadingZeros(String numberStr) {
        if (numberStr == null || numberStr.isEmpty()) {
            return numberStr;
        }
        return numberStr.replaceFirst("^0+(?!$)", "");
    }

    private Boolean isAssetConditionValid(AssetInfoResponseDto assetInfo) {
        return (Objects.equals(assetInfo.getEstadoConservacao(), "BOM") && Objects.equals(assetInfo.getSituacao(), "EM USO"));
    }

    private Boolean isOnlyOneResponsible(AssetInfoResponseDto assetInfo, Long fileId) {
        List<String> responsibles = assetRepository.findAssetResponsiblesByFileId(fileId);

        for (String responsible : responsibles) {
            if (!responsible.isBlank() && !responsible.equals(assetInfo.getResponsavel())) {
                return false; // Encontrou um responsável diferente
            }
        }

        return true; // Todos os assets têm o mesmo responsável
    }

    private void updateAsset(AssetModel asset, AssetInfoResponseDto assetInfo) {
        asset.setAssetNumber(assetInfo.getTombo());
        asset.setFormerAssetNumber(assetInfo.getTomboAntigo());
        asset.setPlace(assetInfo.getLocal());
        asset.setDescription(assetInfo.getDescricao());
        asset.setConservationState(assetInfo.getEstadoConservacao());
        asset.setSituation(assetInfo.getSituacao());
        asset.setResponsible(assetInfo.getResponsavel());
        assetRepository.save(asset);
    }

    // TODO: Retornar erro de acordo com qual validação falhou
    public void confirm(String token, String assetNumber, Long assetId) {
        try {
            Optional<FileAssetModel> fileAsset = fileAssetRepository.findFileIdByAssetId(assetId);

            if (fileAsset.isEmpty()) {
                throw new NoSuchElementException("Asset ID não associado a nenhum arquivo");
            }
            Long fileId = fileAsset.get().getFile().getId();

            AssetInfoResponseDto assetInfo = assetInfoService.getAssetInfo(token, removeLeadingZeros(assetNumber));
            if(assetInfo == null || !isAssetConditionValid(assetInfo) || !isOnlyOneResponsible(assetInfo, fileId)) {
                assetDeletionService.delete(assetId);
                return;
            }

            AssetModel asset = fileAsset.get().getAsset();
            updateAsset(asset, assetInfo);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
