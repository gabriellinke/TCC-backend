package com.example.tcc.controllers;

import com.example.tcc.dto.CreateAssetResponseDto;
import com.example.tcc.services.AssetCreationService;
import com.example.tcc.services.AssetDeletionService;
import com.example.tcc.services.AssetImageAdditionService;
import com.example.tcc.services.AssetImageDeletionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/asset")
public class AssetController {
    @Autowired
    private AssetCreationService assetCreationService;
    @Autowired
    private AssetDeletionService assetDeletionService;
    @Autowired
    private AssetImageAdditionService assetImageAdditionService;
    @Autowired
    private AssetImageDeletionService assetImageDeletionService;

    @PostMapping
    public ResponseEntity<CreateAssetResponseDto> create(@RequestParam Long fileId, @RequestParam MultipartFile image) {
        CreateAssetResponseDto response = assetCreationService.createAndRecognize(fileId, image);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/add-image")
    public ResponseEntity<Map<String, String>> add(@RequestParam Long assetId, @RequestParam MultipartFile image) {
        String filepath = assetImageAdditionService.add(assetId, image);
        Map<String, String> response = new HashMap<>();
        response.put("path", filepath);
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/delete-image/{filename}")
    public ResponseEntity<Void> deleteImage(@PathVariable String filename) {
        assetImageDeletionService.delete(filename);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/{assetId}")
    public ResponseEntity<Void> deleteAsset(@PathVariable Long assetId) {
        assetDeletionService.delete(assetId);
        return ResponseEntity.noContent().build();
    }
}