package com.example.tcc.services;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.*;
import com.example.tcc.Representation.BucketObjectRepresentation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.FileUtils;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;
import java.util.List;

@Slf4j
@Component
@RequiredArgsConstructor
public class AWSS3Service {
    private final String bucketName;
    private final AmazonS3 amazonS3Client;

    public List<Bucket> listBuckets(){
        return amazonS3Client.listBuckets();
    }

    //Object level operations
    public void putObject(BucketObjectRepresentation representation) throws IOException {

        String objectName = representation.getObjectName();
        String objectValue = representation.getText();

        File file = new File("." + File.separator + objectName);
        FileWriter fileWriter = new FileWriter(file, false);
        PrintWriter printWriter = new PrintWriter(fileWriter);
        printWriter.println(objectValue);
        printWriter.flush();
        printWriter.close();

        try {
            amazonS3Client.putObject(new PutObjectRequest(this.bucketName, objectName, file));
        } catch (Exception e){
            log.error("Some error has ocurred.", e);
            throw e;
        }
    }

    public void putImage(String objectName, MultipartFile multipartFile) throws IOException {
        try {
            // Set the metadata for the S3 object (e.g., content type, size)
            ObjectMetadata metadata = new ObjectMetadata();
            metadata.setContentLength(multipartFile.getSize());
            metadata.setContentType("image/jpeg");

            // Upload the file directly from the MultipartFile's input stream
            amazonS3Client.putObject(
                    new PutObjectRequest(this.bucketName, objectName, multipartFile.getInputStream(), metadata)
            );
        } catch (Exception e) {
            log.error("An error occurred while uploading the file.", e);
            throw e;
        }
    }

    public void putPDF(String objectName, ByteArrayOutputStream outputStream) {
        try {
            // Prepare metadata for S3 upload
            ObjectMetadata metadata = new ObjectMetadata();
            metadata.setContentLength(outputStream.size());
            metadata.setContentType("application/pdf");

            // Upload PDF to S3 using ByteArrayInputStream
            ByteArrayInputStream inputStream = new ByteArrayInputStream(outputStream.toByteArray());
            amazonS3Client.putObject(new PutObjectRequest(this.bucketName, objectName, inputStream, metadata));

        } catch (Exception e) {
            log.error("Error while creating or uploading PDF to S3", e);
        }
    }

    public List<S3ObjectSummary> listObjects(){
        ObjectListing objectListing = amazonS3Client.listObjects(this.bucketName);
        return objectListing.getObjectSummaries();
    }

    public void downloadObject(String objectName){
        S3Object s3object = amazonS3Client.getObject(this.bucketName, objectName);
        S3ObjectInputStream inputStream = s3object.getObjectContent();
        try {
            FileUtils.copyInputStreamToFile(inputStream, new File("." + File.separator + objectName));
        } catch (IOException e) {
            log.error(e.getMessage());
        }
    }

    public byte[] downloadImage(String objectName) {
        S3Object s3object = amazonS3Client.getObject(this.bucketName, objectName);
        S3ObjectInputStream inputStream = s3object.getObjectContent();

        try (ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream()) {
            byte[] buffer = new byte[1024];
            int bytesRead;
            while ((bytesRead = inputStream.read(buffer)) != -1) {
                byteArrayOutputStream.write(buffer, 0, bytesRead);
            }
            return byteArrayOutputStream.toByteArray();
        } catch (IOException e) {
            log.error("Erro ao baixar o objeto do S3: " + e.getMessage());
            return null;
        } finally {
            try {
                inputStream.close();
            } catch (IOException e) {
                log.error("Erro ao fechar o InputStream: " + e.getMessage());
            }
        }
    }

    public void deleteObject(String objectName){
        amazonS3Client.deleteObject(this.bucketName, objectName);
    }

    public void deleteMultipleObjects(List<String> objects){
        DeleteObjectsRequest delObjectsRequests = new DeleteObjectsRequest(this.bucketName)
                .withKeys(objects.toArray(new String[0]));
        amazonS3Client.deleteObjects(delObjectsRequests);
    }

    public void moveObject(String bucketSourceName, String objectName, String bucketTargetName){
        amazonS3Client.copyObject(
                bucketSourceName,
                objectName,
                bucketTargetName,
                objectName
        );
    }

}