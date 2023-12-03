package com.bsuir.verghel.basketball.model.entities.ball;

import java.math.BigDecimal;
import java.util.Objects;


public class Ball {
    private Long id;
    private String brand;
    private String model;
    private BigDecimal price;
    private BigDecimal weight;
    private String material;
    private Integer size;
    private BigDecimal circumference;
    private String imageUrl;
    private String description;

    public Ball() {
    }

    public Ball(Long id, String brand, String model, BigDecimal price, BigDecimal weight, String material, Integer size, BigDecimal circumference, String imageUrl, String description) {
        this.id = id;
        this.brand = brand;
        this.model = model;
        this.price = price;
        this.weight = weight;
        this.material = material;
        this.size = size;
        this.circumference = circumference;
        this.imageUrl = imageUrl;
        this.description = description;
    }

    public Ball(Long id, String brand, String model, BigDecimal price, String imageUrl) {
        this.id = id;
        this.brand = brand;
        this.model = model;
        this.price = price;
        this.imageUrl = imageUrl;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getBrand() {
        return brand;
    }

    public void setBrand(String brand) {
        this.brand = brand;
    }

    public String getModel() {
        return model;
    }

    public void setModel(String model) {
        this.model = model;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }
    public BigDecimal getWeight() {
        return weight;
    }
    public void setWeight(BigDecimal weight) {
        this.weight = weight;
    }

    public String getMaterial() {
        return material;
    }

    public void setMaterial(String material) {
        this.material = material;
    }
    public Integer getSize() {
        return size;
    }

    public void setSize(Integer size) {
        this.size = size;
    }
    public BigDecimal getCircumference() {
        return circumference;
    }
    public void setCircumference(BigDecimal circumference) {
        this.circumference = circumference;
    }
    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Ball ball = (Ball) o;
        return id.equals(ball.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
