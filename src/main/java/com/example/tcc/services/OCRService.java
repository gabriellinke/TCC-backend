package com.example.tcc.services;

import com.example.tcc.dto.AssetNumberRecognitionDto;
import com.example.tcc.util.CustomMultipartFile;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.*;
import org.springframework.http.client.MultipartBodyBuilder;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.MultipartFile;

import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import javax.imageio.ImageIO;

@Service
public class OCRService {

    @Value("${ocr.server.url}")
    private String ocrServerUrl;

    private final RestTemplate restTemplate;

    public OCRService() {
        this.restTemplate = new RestTemplate();
    }

    public AssetNumberRecognitionDto performOCR(BufferedImage image) {
        try {
            // Convert BufferedImage to byte array
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            ImageIO.write(image, "png", baos);
            byte[] imageBytes = baos.toByteArray();

            // Create MultipartFile
            MultipartFile file = new CustomMultipartFile("file", "image.jpg", "image/jpg", imageBytes);

            // Create headers for multipart request
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.MULTIPART_FORM_DATA);

            MultiValueMap<String, Object> parts = new LinkedMultiValueMap<>();
            parts.add("file", file.getResource());

            HttpEntity<MultiValueMap<String, Object>> requestEntity =
                    new HttpEntity<>(parts, headers);

            ResponseEntity<AssetNumberRecognitionDto> response = restTemplate.exchange(
                    ocrServerUrl + "/upload-image/",
                    HttpMethod.POST,
                    requestEntity,
                    AssetNumberRecognitionDto.class
            );

            if (response.getStatusCode() == HttpStatus.OK) {
                return response.getBody();
            } else {
                throw new RuntimeException("Failed to get a valid response from OCR server");
            }

        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }
}
