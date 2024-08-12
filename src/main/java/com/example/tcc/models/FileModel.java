package com.example.tcc.models;

import jakarta.persistence.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "Files")
public class FileModel {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "user_id", nullable = false)
    private Long userId;

    @Column(nullable = false)
    private String filename;

    @Column(nullable = false)
    private Boolean consolidated;

    @Column(name = "consolidated_at")
    private LocalDateTime consolidatedAt;

    public FileModel() {}

    public FileModel(Long userId, String filename, Boolean consolidated, LocalDateTime consolidatedAt) {
        this.userId = userId;
        this.filename = filename;
        this.consolidated = consolidated;
        this.consolidatedAt = consolidatedAt;
    }

    public FileModel(Long userId) {
        this.userId = userId;
        this.filename = "";
        this.consolidated = false;
        this.consolidatedAt = null;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public String getFilename() {
        return filename;
    }

    public void setFilename(String filename) {
        this.filename = filename;
    }

    public Boolean getConsolidated() {
        return consolidated;
    }

    public void setConsolidated(Boolean consolidated) {
        this.consolidated = consolidated;
    }

    public LocalDateTime getConsolidatedAt() {
        return consolidatedAt;
    }

    public void setConsolidatedAt(LocalDateTime consolidatedAt) {
        this.consolidatedAt = consolidatedAt;
    }
}
