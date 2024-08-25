package com.example.tcc.controllers;

import com.example.tcc.services.*;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/image")
@RequiredArgsConstructor
public class ImageController {
    private final PermissionCheckService permissionCheckService;
    private final ImageRetrieveService imageRetrieveService;

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
}