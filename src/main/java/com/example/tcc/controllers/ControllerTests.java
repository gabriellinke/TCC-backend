package com.example.tcc.controllers;

import com.amazonaws.services.s3.model.Bucket;
import com.amazonaws.services.s3.model.S3ObjectSummary;
import com.example.tcc.Representation.BucketObjectRepresentation;
import com.example.tcc.services.AWSS3Service;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.layout.Document;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

@CrossOrigin(origins = "*")
@RestController
@RequestMapping("/buckets")
@RequiredArgsConstructor
public class ControllerTests {
    private final AWSS3Service s3Service;

    @GetMapping
    public List<String> listBuckets(){
        var buckets = s3Service.listBuckets();
        return buckets.stream().map(Bucket::getName).collect(Collectors.toList());
    }

    @PostMapping(value = "/objects")
    public void createObject(@RequestBody BucketObjectRepresentation representation) throws IOException {
        s3Service.putObject(representation);
    }

    @PostMapping(value = "/image")
    public void createImage(@RequestParam String filename, @RequestParam MultipartFile image) throws IOException {
        s3Service.putImage(filename, image);
    }

    @PostMapping(value = "/file")
    public void createFile(@RequestParam String filename) {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        PdfWriter writer = new PdfWriter(outputStream);
        PdfDocument pdfDoc = new PdfDocument(writer);
        Document document = new Document(pdfDoc);

        // Add content to the PDF document (for example, a simple paragraph)
        document.add(new com.itextpdf.layout.element.Paragraph("Hello, S3! This is a test PDF."));

        // Close the document to flush content to the outputStream
        document.close();

        s3Service.putPDF(filename, outputStream);
    }

    @GetMapping(value = "/objects/{objectName}")
    public File downloadObject(@PathVariable String objectName) {
        s3Service.downloadObject(objectName);
        return new File("./" + objectName);
    }

    @PatchMapping(value = "/{bucketSourceName}/objects/{objectName}/{bucketTargetName}")
    public void moveObject(@PathVariable String bucketSourceName, @PathVariable String objectName, @PathVariable String bucketTargetName) {
        s3Service.moveObject(bucketSourceName, objectName, bucketTargetName);
    }

    @GetMapping(value = "/objects")
    public List<String> listObjects() {
        return s3Service.listObjects().stream().map(S3ObjectSummary::getKey).collect(Collectors.toList());
    }

    @DeleteMapping(value = "/objects/{objectName}")
    public void deleteObject(@PathVariable String objectName) {
        s3Service.deleteObject(objectName);
    }

    @DeleteMapping(value = "/objects")
    public void deleteObject(@RequestBody List<String> objects) {
        s3Service.deleteMultipleObjects(objects);
    }

}