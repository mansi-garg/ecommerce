package com.example.ecommerce.dto;

public class ProductDTO {

    private String title;

    private Long variantPrice;

    private String brand;

    private String size;

    private String gender;

    private String color;

    private String image;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Long getVariantPrice() {
        return variantPrice;
    }

    public void setVariantPrice(Long variantPrice) {
        this.variantPrice = variantPrice;
    }

    public String getBrand() {
        return brand;
    }

    public void setBrand(String brand) {
        this.brand = brand;
    }

    public String getSize() {
        return size;
    }

    public void setSize(String size) {
        this.size = size;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    @Override
    public String toString() {
        return "ProductDTO{" +
                "title='" + title + '\'' +
                ", variantPrice=" + variantPrice +
                ", brand='" + brand + '\'' +
                ", size='" + size + '\'' +
                ", gender='" + gender + '\'' +
                ", color='" + color + '\'' +
                ", image='" + image + '\'' +
                '}';
    }
}
