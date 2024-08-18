package com.example.tcc.controllers;

import com.example.tcc.services.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.awt.image.BufferedImage;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/image")
public class ImageController {
    @Autowired
    private ImageUploadService imageUploadService;
    @Autowired
    private ImageRetrieveService imageRetrieveService;
    @Autowired
    private BarcodeReaderService barcodeReaderService;
    @Autowired
    private BarcodeDetectionService barcodeDetectionService;
    @Autowired
    private OCRService ocrService;
    @Autowired
    private ImageLabelingService imageLabelingService;

    @Value("${tcc.backend.base-url}")
    private String baseURL;

    @PostMapping("/upload")
    public ResponseEntity<Map<String, String>> upload(@RequestParam MultipartFile image) {
        String path = imageUploadService.saveImage(image);
        BufferedImage bufferedImage = barcodeDetectionService.detectBarcode(path);
        String assetNumber = getAssetNumber(bufferedImage);

        if(assetNumber != null && !assetNumber.isBlank()) {
            imageLabelingService.drawString(path, assetNumber);
            String imagePath = baseURL + "image/" + path.substring(path.lastIndexOf('/') + 1);

            Map<String, String> response = new HashMap<>();
            response.put("path", imagePath);
            response.put("assetNumber", assetNumber);

            return ResponseEntity.ok(response);
        } else {
            System.err.println("Não foi possível obter o número de patrimônio");
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }

    @GetMapping("/{filename}")
    public ResponseEntity<Resource> upload(@PathVariable String filename) {
        Resource image = imageRetrieveService.retrieveImage(filename);
        if (image != null && (image.exists() || image.isReadable())) {
            return ResponseEntity.ok()
                    .header(HttpHeaders.CONTENT_DISPOSITION, "inline; filename=\"" + image.getFilename() + "\"")
                    .contentType(MediaType.IMAGE_JPEG)
                    .body(image);
        }
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
    }

    private String getAssetNumber(BufferedImage bufferedImage) {
        String barcode = barcodeReaderService.read(bufferedImage);
        if(barcode != null) {
            System.out.println(barcode + ", 1.0");
            return barcode;
        }

        String number = ocrService.performOCR(bufferedImage);
        System.out.println(number);
        return number.split(",")[0].trim();
    }
}