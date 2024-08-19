package com.example.tcc.controllers;

import com.example.tcc.dto.AssetDetailsDto;
import com.example.tcc.models.ImageModel;
import com.example.tcc.repositories.AssetRepository;
import com.example.tcc.repositories.ImageRepository;
import com.example.tcc.responses.CreateFileResponseDto;
import com.example.tcc.models.FileModel;
import com.example.tcc.services.AssetDetailsService;
import com.example.tcc.services.FileCreationService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/file")
@AllArgsConstructor
public class FileController {
    private final FileCreationService fileCreationService;
    private final AssetDetailsService assetDetailsService;

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
}