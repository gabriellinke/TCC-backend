package com.example.tcc.controllers;

import com.example.tcc.models.PDFRequest;
import com.example.tcc.services.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/generate")
public class PDFController {
    @Autowired
    private PDFGenerationService pdfGenerationService;

    @PostMapping
    public void generate(@RequestBody PDFRequest requestDto) {
        pdfGenerationService.saveFile(requestDto.getAssetNumber(), requestDto.getPath());
    }
}