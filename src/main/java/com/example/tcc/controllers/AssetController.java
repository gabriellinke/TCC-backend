package com.example.tcc.controllers;

import com.example.tcc.requests.AssetConfirmationRequestDto;
import com.example.tcc.requests.AuthRequestDto;
import com.example.tcc.responses.CreateAssetResponseDto;
import com.example.tcc.services.*;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/asset")
@AllArgsConstructor
public class AssetController {
    private final AssetCreationService assetCreationService;
    private final AssetConfirmationService assetConfirmationService;
    private final AssetDeletionService assetDeletionService;
    private final AssetImageAdditionService assetImageAdditionService;
    private final AssetImageDeletionService assetImageDeletionService;

    @PostMapping
    public ResponseEntity<CreateAssetResponseDto> create(@RequestParam Long fileId, @RequestParam MultipartFile image) {
        CreateAssetResponseDto response = assetCreationService.createAndRecognize(fileId, image);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/confirm/{assetId}")
    public ResponseEntity<Void> confirm(@PathVariable Long assetId, @RequestBody AssetConfirmationRequestDto requestDto) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        assetConfirmationService.confirm(authentication.getCredentials().toString(), requestDto.getAssetNumber(), assetId);
        return ResponseEntity.ok().build();
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