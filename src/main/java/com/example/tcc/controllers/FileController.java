package com.example.tcc.controllers;

import com.example.tcc.dto.AssetDetailsDto;
import com.example.tcc.responses.CreateFileResponseDto;
import com.example.tcc.models.FileModel;
import com.example.tcc.services.*;
import lombok.AllArgsConstructor;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

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
    private final FileRetrieveService fileRetrieveService;
    private final FileByUserRetrieveService fileByUserRetrieveService;

    @PostMapping
    public ResponseEntity<?> create() {
        Long userId = (Long) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if(fileCreationService.canCreateFile(userId)) {
            FileModel file = fileCreationService.create(userId);
            CreateFileResponseDto response = new CreateFileResponseDto(file.getId(), file.getUserId());
            return ResponseEntity.ok(response);
        }
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Usuário já tem um arquivo sendo criado");
    }

    @GetMapping("/{fileId}/assets")
    public ResponseEntity<List<AssetDetailsDto>> get(@PathVariable Long fileId) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if(!permissionCheckService.checkPermissionForFile((Long)authentication.getPrincipal(), fileId))
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();

        List<AssetDetailsDto> response = assetDetailsService.getAssetsWithURL(fileId);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/{fileId}/confirm")
    public ResponseEntity<?> confirm(@PathVariable Long fileId) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if(!permissionCheckService.checkPermissionForFile((Long)authentication.getPrincipal(), fileId))
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();

        try {
            FileModel file = fileConfirmationService.confirm(fileId);
            return ResponseEntity.ok(file);
        } catch (Error | IOException e) {
            if(Objects.equals(e.getMessage(), "Invalid assets")) {
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
            } else if(Objects.equals(e.getMessage(), "File not found")) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
            }
            return ResponseEntity.internalServerError().build();
        }
    }

    @GetMapping
    public ResponseEntity<?> get() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        Long userId = (Long) authentication.getPrincipal();

        try {
            List<FileModel> response = fileByUserRetrieveService.retrieve(userId);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
    }

    @GetMapping("/{filename}")
    public ResponseEntity<Resource> retrieve(@PathVariable String filename) {
        Resource file = fileRetrieveService.retrieveFile(filename);
        if (file != null && (file.exists() || file.isReadable())) {
            return ResponseEntity.ok()
                    .header(HttpHeaders.CONTENT_DISPOSITION, "inline; filename=\"" + file.getFilename() + "\"")
                    .contentType(MediaType.APPLICATION_PDF)
                    .body(file);
        }
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
    }
}