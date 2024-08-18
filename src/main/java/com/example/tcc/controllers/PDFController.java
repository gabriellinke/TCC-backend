package com.example.tcc.controllers;

import com.example.tcc.requests.PDFRequestDto;
import com.example.tcc.services.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/generate")
public class PDFController {
    @Autowired
    private PDFGenerationService pdfGenerationService;

    @PostMapping
    public void generate(@RequestBody PDFRequestDto requestDto) {
        pdfGenerationService.saveFile(requestDto.getAssetNumber(), requestDto.getPath());
    }
}