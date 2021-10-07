package com.example.ecommerce.entity;


import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.Document;

@Document(indexName = "myntra")
public class Product {

    @Id
    private String id;

    private String title;

    private Long variant_price;

    private String brand;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Long getVariantPrice() {
        return variant_price;
    }

    public void setVariantPrice(Long variantPrice) {
        this.variant_price = variantPrice;
    }

    public String getBrand() {
        return brand;
    }

    public void setBrand(String brand) {
        this.brand = brand;
    }

    @Override
    public String toString() {
        return "Product{" +
                "id='" + id + '\'' +
                ", title='" + title + '\'' +
                ", variantPrice='" + variant_price + '\'' +
                ", brand='" + brand + '\'' +
                '}';
    }
}
