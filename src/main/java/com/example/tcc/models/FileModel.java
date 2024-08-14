package com.example.tcc.models;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Setter
@Getter
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

}
