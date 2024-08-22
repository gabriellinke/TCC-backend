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
    private final PermissionCheckService permissionCheckService;

    @PostMapping
    public ResponseEntity<CreateAssetResponseDto> create(@RequestParam Long fileId, @RequestParam MultipartFile image) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if(!permissionCheckService.checkPermissionForFile((Long)authentication.getPrincipal(), fileId))
            return ResponseEntity.status(401).build();

        CreateAssetResponseDto response = assetCreationService.createAndRecognize(fileId, image);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/confirm/{assetId}")
    public ResponseEntity<Void> confirm(@PathVariable Long assetId, @RequestBody AssetConfirmationRequestDto requestDto) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if(!permissionCheckService.checkPermissionForAsset((Long)authentication.getPrincipal(), assetId))
            return ResponseEntity.status(401).build();

        assetConfirmationService.confirm(authentication.getCredentials().toString(), requestDto.getAssetNumber(), assetId);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/add-image")
    public ResponseEntity<Map<String, String>> add(@RequestParam Long assetId, @RequestParam MultipartFile image) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if(!permissionCheckService.checkPermissionForAsset((Long)authentication.getPrincipal(), assetId))
            return ResponseEntity.status(401).build();

        String filepath = assetImageAdditionService.add(assetId, image);
        Map<String, String> response = new HashMap<>();
        response.put("path", filepath);
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/delete-image/{filename}")
    public ResponseEntity<Void> deleteImage(@PathVariable String filename) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if(!permissionCheckService.checkPermissionForImageFilename((Long)authentication.getPrincipal(), filename))
            return ResponseEntity.status(401).build();

        assetImageDeletionService.delete(filename);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/{assetId}")
    public ResponseEntity<Void> deleteAsset(@PathVariable Long assetId) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if(!permissionCheckService.checkPermissionForAsset((Long)authentication.getPrincipal(), assetId))
            return ResponseEntity.status(401).build();

        assetDeletionService.delete(assetId);
        return ResponseEntity.noContent().build();
    }
}