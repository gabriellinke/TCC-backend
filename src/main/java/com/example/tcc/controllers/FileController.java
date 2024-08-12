package com.example.tcc.controllers;

import com.example.tcc.dto.CreateFileRequestDto;
import com.example.tcc.dto.CreateFileResponseDto;
import com.example.tcc.models.FileModel;
import com.example.tcc.services.FileCreationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/file")
public class FileController {
    @Autowired
    private FileCreationService fileCreationService;

    @PostMapping
    public ResponseEntity<CreateFileResponseDto> create(@RequestBody CreateFileRequestDto requestDto) {
        FileModel file = fileCreationService.create(requestDto.getUserId());
        CreateFileResponseDto response = new CreateFileResponseDto(file.getId(), file.getUserId());
        return ResponseEntity.ok(response);
    }
}