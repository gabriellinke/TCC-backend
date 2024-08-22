package com.example.tcc.controllers;

import com.example.tcc.dto.AssetDetailsDto;
import com.example.tcc.responses.CreateFileResponseDto;
import com.example.tcc.models.FileModel;
import com.example.tcc.services.*;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.List;
import java.util.Objects;

@RestController
@RequestMapping("/file")
@AllArgsConstructor
public class FileController {
    private final FileCreationService fileCreationService;
    private final FileConfirmationService fileConfirmationService;
    private final AssetDetailsService assetDetailsService;
    private final PermissionCheckService permissionCheckService;

    @PostMapping
    public ResponseEntity<CreateFileResponseDto> create() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        FileModel file = fileCreationService.create((Long) authentication.getPrincipal());
        CreateFileResponseDto response = new CreateFileResponseDto(file.getId(), file.getUserId());
        return ResponseEntity.ok(response);
    }

    @GetMapping("/{fileId}/assets")
    public ResponseEntity<List<AssetDetailsDto>> get(@PathVariable Long fileId) {
        List<AssetDetailsDto> response = assetDetailsService.getAssetsWithURL(fileId);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/{fileId}/confirm")
    public ResponseEntity<FileModel> confirm(@PathVariable Long fileId) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if(!permissionCheckService.checkPermissionForFile((Long)authentication.getPrincipal(), fileId))
            return ResponseEntity.status(401).build();

        try {
            FileModel file = fileConfirmationService.confirm(fileId);
            return ResponseEntity.ok(file);
        } catch (Error | IOException e) {
            if(Objects.equals(e.getMessage(), "Invalid assets")) {
                return ResponseEntity.internalServerError().build();
            } else if(Objects.equals(e.getMessage(), "File not found")) {
                return ResponseEntity.notFound().build();
            }
            return ResponseEntity.internalServerError().build();
        }
    }
}