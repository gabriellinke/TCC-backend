package com.example.tcc.controllers;

import com.example.tcc.services.BarcodeDetectionService;
import com.example.tcc.services.BarcodeReaderService;
import com.example.tcc.services.FileUploadService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.awt.image.BufferedImage;

@RestController
@RequestMapping("/image")
public class FileUploadController {

    @Autowired
    private FileUploadService fileUploadService;

    @Autowired
    private BarcodeReaderService barcodeReaderService;

    @Autowired
    private BarcodeDetectionService barcodeDetectionService;

    @PostMapping
    public void upload(@RequestParam MultipartFile image) {
        String path = fileUploadService.saveImage(image);
        BufferedImage bufferedImage = barcodeDetectionService.detectBarcode(path);
        String barcode = barcodeReaderService.read(bufferedImage);
        System.out.println(barcode);
    }

}