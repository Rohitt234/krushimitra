package com.krushimitra.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;

import java.time.LocalDateTime;

@Entity
@Table(name = "crops")
public class Crop {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @NotBlank
    private String name;
    
    private String description;
    private String season;
    private String soilType;
    private String climate;
    private String waterRequirement;
    private String growthDuration;
    private String yieldPerHectare;
    private String marketPrice;
    private String imageUrl;
    
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    
    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
        updatedAt = LocalDateTime.now();
    }
    
    @PreUpdate
    protected void onUpdate() {
        updatedAt = LocalDateTime.now();
    }
    
    // Getters and Setters
    public Long getId() {
        return id;
    }
    
    public void setId(Long id) {
        this.id = id;
    }
    
    public String getName() {
        return name;
    }
    
    public void setName(String name) {
        this.name = name;
    }
    
    public String getDescription() {
        return description;
    }
    
    public void setDescription(String description) {
        this.description = description;
    }
    
    public String getSeason() {
        return season;
    }
    
    public void setSeason(String season) {
        this.season = season;
    }
    
    public String getSoilType() {
        return soilType;
    }
    
    public void setSoilType(String soilType) {
        this.soilType = soilType;
    }
    
    public String getClimate() {
        return climate;
    }
    
    public void setClimate(String climate) {
        this.climate = climate;
    }
    
    public String getWaterRequirement() {
        return waterRequirement;
    }
    
    public void setWaterRequirement(String waterRequirement) {
        this.waterRequirement = waterRequirement;
    }
    
    public String getGrowthDuration() {
        return growthDuration;
    }
    
    public void setGrowthDuration(String growthDuration) {
        this.growthDuration = growthDuration;
    }
    
    public String getYieldPerHectare() {
        return yieldPerHectare;
    }
    
    public void setYieldPerHectare(String yieldPerHectare) {
        this.yieldPerHectare = yieldPerHectare;
    }
    
    public String getMarketPrice() {
        return marketPrice;
    }
    
    public void setMarketPrice(String marketPrice) {
        this.marketPrice = marketPrice;
    }
    
    public String getImageUrl() {
        return imageUrl;
    }
    
    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }
    
    public LocalDateTime getCreatedAt() {
        return createdAt;
    }
    
    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }
    
    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }
    
    public void setUpdatedAt(LocalDateTime updatedAt) {
        this.updatedAt = updatedAt;
    }
}
