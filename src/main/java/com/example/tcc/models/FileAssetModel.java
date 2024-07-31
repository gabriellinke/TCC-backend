package com.example.tcc.models;

import jakarta.persistence.*;

@Entity
@Table(name = "FilesAssets")
public class FileAssetModel {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "file_id", nullable = false)
    private FileModel file;

    @ManyToOne
    @JoinColumn(name = "asset_id", nullable = false)
    private AssetModel asset;

    public FileAssetModel() {}

    public FileAssetModel(FileModel file, AssetModel asset) {
        this.file = file;
        this.asset = asset;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public FileModel getFile() {
        return file;
    }

    public void setFile(FileModel file) {
        this.file = file;
    }

    public AssetModel getAsset() {
        return asset;
    }

    public void setAsset(AssetModel asset) {
        this.asset = asset;
    }
}
