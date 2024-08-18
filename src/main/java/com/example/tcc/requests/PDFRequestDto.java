package com.example.tcc.requests;

public class PDFRequestDto {
    private String assetNumber;
    private String path;

    // Getters e Setters
    public String getAssetNumber() {
        return assetNumber;
    }

    public void setAssetNumber(String assetNumber) {
        this.assetNumber = assetNumber;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }
}
