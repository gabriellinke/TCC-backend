package com.example.tcc.models;

import jakarta.persistence.*;

@Entity
@Table(name = "Assets")
public class AssetModel {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "user_id", nullable = false)
    private Long userId;

    @OneToOne
    @JoinColumn(name = "main_image_id")
    private ImageModel mainImage;

    @Column(name = "asset_number", nullable = false)
    private String assetNumber;

    @Column(name = "former_asset_number")
    private String formerAssetNumber;

    @Column(nullable = false)
    private String description;

    @Column(name = "conservation_state")
    private String conservationState;

    @Column(nullable = false)
    private String situation;

    @Column(nullable = false)
    private String place;

    @Column(nullable = false)
    private String responsible;

    @Column(nullable = false)
    private Boolean consolidated;

    public AssetModel() {}

    public AssetModel(Long userId, ImageModel mainImage, String assetNumber, String formerAssetNumber, String description, String conservationState, String situation, String place, String responsible, Boolean consolidated) {
        this.userId = userId;
        this.mainImage = mainImage;
        this.assetNumber = assetNumber;
        this.formerAssetNumber = formerAssetNumber;
        this.description = description;
        this.conservationState = conservationState;
        this.situation = situation;
        this.place = place;
        this.responsible = responsible;
        this.consolidated = consolidated;
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

    public ImageModel getMainImage() {
        return mainImage;
    }

    public void setMainImage(ImageModel mainImage) {
        this.mainImage = mainImage;
    }

    public String getAssetNumber() {
        return assetNumber;
    }

    public void setAssetNumber(String assetNumber) {
        this.assetNumber = assetNumber;
    }

    public String getFormerAssetNumber() {
        return formerAssetNumber;
    }

    public void setFormerAssetNumber(String formerAssetNumber) {
        this.formerAssetNumber = formerAssetNumber;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getConservationState() {
        return conservationState;
    }

    public void setConservationState(String conservationState) {
        this.conservationState = conservationState;
    }

    public String getSituation() {
        return situation;
    }

    public void setSituation(String situation) {
        this.situation = situation;
    }

    public String getPlace() {
        return place;
    }

    public void setPlace(String place) {
        this.place = place;
    }

    public String getResponsible() {
        return responsible;
    }

    public void setResponsible(String responsible) {
        this.responsible = responsible;
    }

    public Boolean getConsolidated() {
        return consolidated;
    }

    public void setConsolidated(Boolean consolidated) {
        this.consolidated = consolidated;
    }
}
