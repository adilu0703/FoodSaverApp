package com.example.fyp_khanabachao;

public class ConsumerRegisterHelperClass {
    String name,username,email,phone,password,usertype,uid;
    Float rating;

    public ConsumerRegisterHelperClass() {
    }

    public ConsumerRegisterHelperClass(String name, String username, String email, String phone, String usertype, String uid, Float rating ) {
        this.name = name;
        this.username = username;
        this.email = email;
        this.phone = phone;
        this.usertype = usertype;
        this.uid = uid;
        this.rating=rating;


       // this.password = password;

    }



    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }


    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getUsertype() {
        return usertype;
    }

    public void setUsertype(String usertype) {
        this.usertype = usertype;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public Float getRating() {
        return rating;
    }

    public void setRating(Float rating) {
        this.rating = rating;
    }
    /** public String getPassword() {
         return password;
     }

     public void setPassword(String password) {
         this.password = password;
     }*/
}
