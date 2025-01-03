package com.example.duan.src.models;

public class User {

    private String username;
    private String email;
    private String address;
    private String phone;
    private String password;  // Thêm trường mật khẩu

    // Constructor với đầy đủ tham số
    public User(String username, String email, String address, String phone, String password) {
        this.username = username;
        this.email = email;
        this.address = address;
        this.phone = phone;
        this.password = password;  // Khởi tạo mật khẩu
    }

    // Constructor mặc định (yêu cầu của Firebase)
    public User() {
        // Mặc định không có tham số
    }

    // Getter và Setter cho username
    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    // Getter và Setter cho email
    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    // Getter và Setter cho address
    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    // Getter và Setter cho phone
    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    // Getter và Setter cho password
    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
