package com.example.tcc.models;

import jakarta.persistence.*;

@Entity
@Table(name = "Assets")
public class AssetModel {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "asset_number")
    private String assetNumber;

    @Column(name = "main_image")
    private String mainImage;

    @Column(name = "former_asset_number")
    private String formerAssetNumber;

    private String description;

    @Column(name = "conservation_state")
    private String conservationState;

    private String situation;

    private String place;

    private String responsible;

    private Boolean consolidated;

    public AssetModel() {}

    public AssetModel(String mainImage, String assetNumber, String formerAssetNumber, String description, String conservationState, String situation, String place, String responsible, Boolean consolidated) {
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

    public AssetModel(String mainImage) {
        this.mainImage = mainImage;
        this.assetNumber = "";
        this.formerAssetNumber = "";
        this.description = "";
        this.conservationState = "";
        this.situation = "";
        this.place = "";
        this.responsible = "";
        this.consolidated = false;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getMainImage() {
        return mainImage;
    }

    public void setMainImage(String mainImage) {
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
