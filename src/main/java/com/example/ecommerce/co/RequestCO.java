package com.example.ecommerce.co;

import java.util.List;

public class RequestCO {

    private String searchString;

    private String sortBy;

    private List<String> price;

    private List<String> color;

    private List<String> brand;

    private String size;

    private String gender;

    public String getSearchString() {
        return searchString;
    }

    public void setSearchString(String searchString) {
        this.searchString = searchString;
    }

    public String getSortBy() {
        return sortBy;
    }

    public void setSortBy(String sortBy) {
        this.sortBy = sortBy;
    }

    public List<String> getPrice() {
        return price;
    }

    public void setPrice(List<String> price) {
        this.price = price;
    }

    public List<String> getColor() {
        return color;
    }

    public void setColor(List<String> color) {
        this.color = color;
    }

    public List<String> getBrand() {
        return brand;
    }

    public void setBrand(List<String> brand) {
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

    @Override
    public String toString() {
        return "RequestCO{" +
                "searchString='" + searchString + '\'' +
                ", sortBy='" + sortBy + '\'' +
                ", price=" + price +
                ", color=" + color +
                ", brand=" + brand +
                ", size='" + size + '\'' +
                ", gender='" + gender + '\'' +
                '}';
    }
}
