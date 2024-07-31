package com.example.tcc.models;

import jakarta.persistence.*;

@Entity
@Table(name = "AssetsImages")
public class AssetImageModel {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "asset_id", nullable = false)
    private AssetModel asset;

    @ManyToOne
    @JoinColumn(name = "image_id", nullable = false)
    private ImageModel image;

    public AssetImageModel() {}

    public AssetImageModel(AssetModel asset, ImageModel image) {
        this.asset = asset;
        this.image = image;
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

    public ImageModel getImage() {
        return image;
    }

    public void setImage(ImageModel image) {
        this.image = image;
    }
}
