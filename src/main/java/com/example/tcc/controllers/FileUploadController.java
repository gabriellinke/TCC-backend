package com.example.tcc.controllers;

import com.example.tcc.services.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.awt.image.BufferedImage;
import java.util.Objects;

@RestController
@RequestMapping("/image")
public class FileUploadController {
    @Autowired
    private FileUploadService fileUploadService;
    @Autowired
    private BarcodeReaderService barcodeReaderService;
    @Autowired
    private BarcodeDetectionService barcodeDetectionService;
    @Autowired
    private OCRService ocrService;
    @Autowired
    private ImageLabelingService imageLabelingService;

    @PostMapping
    public void upload(@RequestParam MultipartFile image) {
        String path = fileUploadService.saveImage(image);
        BufferedImage bufferedImage = barcodeDetectionService.detectBarcode(path);
        String assetNumber = getAssetNumber(bufferedImage);

        if(assetNumber != null & !Objects.equals(assetNumber, "")) {
            imageLabelingService.drawString(path, assetNumber);
        } else {
            System.err.println("Não foi possível obter o número de patrimônio");
        }
    }

    private String getAssetNumber(BufferedImage bufferedImage) {
        String barcode = barcodeReaderService.read(bufferedImage);
        if(barcode != null) {
            System.out.println(barcode + "1.0");
            return barcode;
        }

        String number = ocrService.performOCR(bufferedImage);
        System.out.println(number);
        return number;
    }
}