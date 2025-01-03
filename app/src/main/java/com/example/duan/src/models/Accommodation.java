package com.example.duan.src.models;

import java.io.Serializable;
import java.util.List;

public class Accommodation implements Serializable {
    private String name;          // Tên khách sạn
    private String location;      // Địa chỉ khách sạn
    private double rating;        // Đánh giá trung bình
    private int reviews;          // Số lượng đánh giá
    private double pricePerDay;   // Giá mỗi ngày
    private int daysUse;          // Số ngày sử dụng
    private double totalPrice;    // Tổng giá
    private String contact;       // Email liên hệ
    private String phone;         // Số điện thoại liên hệ
    private String description;   // Mô tả chi tiết
    private List<String> imageUrls; // Danh sách URL hình ảnh

    // Constructor rỗng bắt buộc cho Firebase
    public Accommodation() {
    }

    // Constructor đầy đủ
    public Accommodation(String name, String location, double rating, int reviews,
                         double pricePerDay, int daysUse, double totalPrice,
                         String contact, String phone, String description,
                         List<String> imageUrls) {
        this.name = name;
        this.location = location;
        this.rating = rating;
        this.reviews = reviews;
        this.pricePerDay = pricePerDay;
        this.daysUse = daysUse;
        this.totalPrice = totalPrice;
        this.contact = contact;
        this.phone = phone;
        this.description = description;
        this.imageUrls = imageUrls;
    }

    // Getter và Setter cho từng thuộc tính
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public double getRating() {
        return rating;
    }

    public void setRating(double rating) {
        this.rating = rating;
    }

    public int getReviews() {
        return reviews;
    }

    public void setReviews(int reviews) {
        this.reviews = reviews;
    }

    public double getPricePerDay() {
        return pricePerDay;
    }

    public void setPricePerDay(double pricePerDay) {
        this.pricePerDay = pricePerDay;
    }

    public int getDaysUse() {
        return daysUse;
    }

    public void setDaysUse(int daysUse) {
        this.daysUse = daysUse;
    }

    public double getTotalPrice() {
        return totalPrice;
    }

    public void setTotalPrice(double totalPrice) {
        this.totalPrice = totalPrice;
    }

    public String getContact() {
        return contact;
    }

    public void setContact(String contact) {
        this.contact = contact;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public List<String> getImageUrls() {
        return imageUrls;
    }

    public void setImageUrls(List<String> imageUrls) {
        this.imageUrls = imageUrls;
    }

    @Override
    public String toString() {
        return "Accommodation{" +
                "name='" + name + '\'' +
                ", location='" + location + '\'' +
                ", rating=" + rating +
                ", reviews=" + reviews +
                ", pricePerDay=" + pricePerDay +
                ", daysUse=" + daysUse +
                ", totalPrice=" + totalPrice +
                ", contact='" + contact + '\'' +
                ", phone='" + phone + '\'' +
                ", description='" + description + '\'' +
                ", imageUrls=" + imageUrls +
                '}';
    }
}
