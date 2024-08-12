package com.example.tcc.services;

import com.example.tcc.dto.AssetNumberRecognitionDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.awt.image.BufferedImage;
import java.util.Objects;

@Service
@RequiredArgsConstructor
public class AssetNumberService {
    private final BarcodeReaderService barcodeReaderService;
    private final OCRService ocrService;

    public AssetNumberRecognitionDto getAssetNumberAndConfidenceLevel(BufferedImage bufferedImage) {
        String barcode = barcodeReaderService.read(bufferedImage);
        if (barcode != null) {
            System.out.println(barcode + ", 1.0");
            return new AssetNumberRecognitionDto(barcode, "1.0");
        }

        String number = ocrService.performOCR(bufferedImage);
        System.out.println(number);
        if(number != null && !Objects.equals(number, "")) {
            return new AssetNumberRecognitionDto(number.split(",")[0].trim(), number.split(",")[1].trim());
        }

        return new AssetNumberRecognitionDto("", "0.0");
    }
}
