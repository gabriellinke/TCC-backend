package com.example.tcc.models;

import jakarta.persistence.*;

@Entity
@Table(name = "Images")
public class ImageModel {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "asset_id")
    private AssetModel asset;

    @Column(nullable = false)
    private String filename;

    public ImageModel() {}

    public ImageModel(AssetModel asset, String filename) {
        this.asset = asset;
        this.filename = filename;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public AssetModel getAsset() {
        return asset;
    }

    public void setAsset(AssetModel asset) {
        this.asset = asset;
    }

    public String getFilename() {
        return filename;
    }

    public void setFilename(String filename) {
        this.filename = filename;
    }
}
