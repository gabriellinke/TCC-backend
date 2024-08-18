package com.example.tcc.controllers;

import com.example.tcc.dto.CreateFileResponseDto;
import com.example.tcc.models.FileModel;
import com.example.tcc.services.FileCreationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/file")
public class FileController {
    @Autowired
    private FileCreationService fileCreationService;

    @PostMapping
    public ResponseEntity<CreateFileResponseDto> create() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        FileModel file = fileCreationService.create((Long) authentication.getPrincipal());
        CreateFileResponseDto response = new CreateFileResponseDto(file.getId(), file.getUserId());
        return ResponseEntity.ok(response);
    }
}