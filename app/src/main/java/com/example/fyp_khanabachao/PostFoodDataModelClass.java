package com.example.fyp_khanabachao;

public class PostFoodDataModelClass {
    private String PPitemName;
    private String PPitemQuanity;
    private String PPitemTime;
    private String ppuserId;
    private String PPitemAddress;
    private String PPitemCity;
    private String PPuserName;
    private String PPfoodStatus;
    private String PPrequesterId;
    private String PPkey;

    public PostFoodDataModelClass() {
    }

    public PostFoodDataModelClass(String PPitemName, String PPitemQuanity, String PPitemTime, String ppuserId, String PPitemAddress, String PPitemCity, String PPuserName,String PPkey, String PPfoodStatus, String PPrequesterId) {
        this.PPitemName = PPitemName;
        this.PPitemQuanity = PPitemQuanity;
        this.PPitemTime = PPitemTime;
        this.ppuserId = ppuserId;
        this.PPitemAddress = PPitemAddress;
        this.PPitemCity = PPitemCity;
        this.PPuserName = PPuserName;
        this.PPfoodStatus= PPfoodStatus;
        this.PPrequesterId= PPrequesterId;
        this.PPkey = PPkey;
    }

    public String getPPfoodStatus() {
        return PPfoodStatus;
    }

    public void setPPfoodStatus(String PPfoodStatus) {
        this.PPfoodStatus = PPfoodStatus;
    }

    public String getPPrequesterId() {
        return PPrequesterId;
    }

    public void setPPrequesterId(String PPrequesterId) {
        this.PPrequesterId = PPrequesterId;
    }

    public String getPPitemName() {
        return PPitemName;
    }

    public void setPPitemName(String PPitemName) {
        this.PPitemName = PPitemName;
    }

    public String getPPitemQuanity() {
        return PPitemQuanity;
    }

    public void setPPitemQuanity(String PPitemQuanity) {
        this.PPitemQuanity = PPitemQuanity;
    }

    public String getPPitemTime() {
        return PPitemTime;
    }

    public void setPPitemTime(String PPitemTime) {
        this.PPitemTime = PPitemTime;
    }

    public String getPpuserId() {
        return ppuserId;
    }

    public void setPpuserId(String ppuserId) {
        this.ppuserId = ppuserId;
    }


    public String getPPitemAddress() {
        return PPitemAddress;
    }

    public void setPPitemAddress(String PPitemAddress) {
        this.PPitemAddress = PPitemAddress;
    }

    public String getPPitemCity() {
        return PPitemCity;
    }

    public void setPPitemCity(String PPitemCity) {
        this.PPitemCity = PPitemCity;
    }

    public String getPPuserName() {
        return PPuserName;
    }

    public void setPPuserName(String PPuserName) {
        this.PPuserName = PPuserName;
    }

    public String getPPkey() {
        return PPkey;
    }

    public void setPPkey(String PPkey) {
        this.PPkey = PPkey;
    }
}