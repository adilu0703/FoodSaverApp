package com.example.fyp_khanabachao;

public class ModelUser {
    String name, email, uid, username, phone;

    public ModelUser() {


    }

    public ModelUser(String name, String email, String uid, String username, String phone) {
        this.name = name;
        this.email = email;
        this.uid = uid;
        this.username = username;
        this.phone = phone;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }
}