package com.example.tcc.controllers;

import com.example.tcc.requests.AssetConfirmationRequestDto;
import com.example.tcc.responses.CreateAssetResponseDto;
import com.example.tcc.services.*;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.multipart.MultipartFile;
import lombok.AllArgsConstructor;

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
    public ResponseEntity<?> create(@RequestParam Long fileId, @RequestParam MultipartFile image) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if(!permissionCheckService.checkPermissionForFile((Long)authentication.getPrincipal(), fileId))
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();

        try {
            CreateAssetResponseDto response = assetCreationService.createAndRecognize(fileId, image);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
    }

    @PostMapping("/confirm/{assetId}")
    public ResponseEntity<?> confirm(@PathVariable Long assetId, @RequestBody AssetConfirmationRequestDto requestDto) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if(!permissionCheckService.checkPermissionForAsset((Long)authentication.getPrincipal(), assetId))
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();

        try {
            assetConfirmationService.confirm(authentication.getCredentials().toString(), requestDto.getAssetNumber(), assetId);
            return ResponseEntity.ok().build();
        } catch (HttpClientErrorException e) {
            if (e.getStatusCode() == HttpStatus.UNAUTHORIZED) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
            } else if (e.getStatusCode() == HttpStatus.NOT_FOUND) {
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Não foi possível encontrar bem com esse número de patrimônio");
            }
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
    }

    @PostMapping("/add-image")
    public ResponseEntity<Map<String, String>> add(@RequestParam Long assetId, @RequestParam MultipartFile image) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if(!permissionCheckService.checkPermissionForAsset((Long)authentication.getPrincipal(), assetId))
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();

        String filepath = assetImageAdditionService.add(assetId, image);
        Map<String, String> response = new HashMap<>();
        response.put("path", filepath);
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/delete-image/{filename}")
    public ResponseEntity<Void> deleteImage(@PathVariable String filename) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if(!permissionCheckService.checkPermissionForImageFilename((Long)authentication.getPrincipal(), filename))
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();

        assetImageDeletionService.delete(filename);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/{assetId}")
    public ResponseEntity<Void> deleteAsset(@PathVariable Long assetId) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if(!permissionCheckService.checkPermissionForAsset((Long)authentication.getPrincipal(), assetId))
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();

        assetDeletionService.delete(assetId);
        return ResponseEntity.noContent().build();
    }
}