package com.example.tcc.controllers;

import com.example.tcc.dto.AssetDetailsDto;
import com.example.tcc.responses.CreateFileResponseDto;
import com.example.tcc.models.FileModel;
import com.example.tcc.services.*;
import com.example.tcc.util.ErrorResponse;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
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
    private final FileByUserRetrieveService fileByUserRetrieveService;

    @PostMapping
    public ResponseEntity<?> create() {
        Long userId = (Long) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if(fileCreationService.canCreateFile(userId)) {
            FileModel file = fileCreationService.create(userId);
            CreateFileResponseDto response = new CreateFileResponseDto(file.getId(), file.getUserId());
            return ResponseEntity.ok(response);
        }
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new ErrorResponse("Usuário já tem um arquivo sendo criado"));
    }

    @GetMapping("/{fileId}/assets")
    public ResponseEntity<List<AssetDetailsDto>> get(@PathVariable Long fileId) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if(!permissionCheckService.checkPermissionForFile((Long)authentication.getPrincipal(), fileId))
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();

        List<AssetDetailsDto> response = assetDetailsService.getAssetsWithURL(fileId);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/{fileId}/confirm")
    public ResponseEntity<?> confirm(@PathVariable Long fileId) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if(!permissionCheckService.checkPermissionForFile((Long)authentication.getPrincipal(), fileId))
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();

        try {
            FileModel file = fileConfirmationService.confirm(fileId);
            return ResponseEntity.ok(file);
        } catch (Error | IOException e) {
            if(Objects.equals(e.getMessage(), "Bens inválidos")) {
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new ErrorResponse(e.getMessage()));
            } else if(Objects.equals(e.getMessage(), "Arquivo não encontrado")) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ErrorResponse(e.getMessage()));
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
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new ErrorResponse(e.getMessage()));
        }
    }
}