package com.example.fyp_khanabachao;

public class FoodDataModelClass {
    private String itemName;
    private String itemDescription;
    private String itemPrice;
    private String itemQuanity;
    private String itemExpiry;
    private String itemImage;
    private String userId;
    private String itemSource;
    private String itemDelivery;
    private String itemAddress;
    private String itemCity;
    private String uploaderName;

    private String key; // for parent node (time)
    private String foodStatus; // for request
    private String myCurrentDateTime;
    private String requesterId;


    public FoodDataModelClass() {
    }

    public FoodDataModelClass(String itemName, String itemDescription, String itemPrice, String itemQuanity, String itemExpiry, String itemImage, String userId, String itemSource, String itemDelivery, String itemAddress, String itemCity, String foodStatus, String myCurrentDateTime, String requesterId, String uploaderName) {
        this.itemName = itemName;
        this.itemDescription = itemDescription;
        this.itemPrice = itemPrice;
        this.itemQuanity = itemQuanity;
        this.itemExpiry = itemExpiry;
        this.itemImage = itemImage;
        this.userId = userId;
        this.itemSource = itemSource;
        this.itemDelivery = itemDelivery;
        this.itemAddress = itemAddress;
        this.itemCity = itemCity;
        this.foodStatus = foodStatus;
        this.myCurrentDateTime = myCurrentDateTime;
        this.requesterId = requesterId;
        this.uploaderName = uploaderName;
    }



    public String getItemName() {
        return itemName;
    }

    public void setItemName(String itemName) {
        this.itemName = itemName;
    }

    public String getItemDescription() {
        return itemDescription;
    }

    public void setItemDescription(String itemDescription) {
        this.itemDescription = itemDescription;
    }

    public String getItemPrice() {
        return itemPrice;
    }

    public void setItemPrice(String itemPrice) {
        this.itemPrice = itemPrice;
    }

    public String getItemQuanity() {
        return itemQuanity;
    }

    public void setItemQuanity(String itemQuanity) {
        this.itemQuanity = itemQuanity;
    }

    public String getItemExpiry() {
        return itemExpiry;
    }

    public void setItemExpiry(String itemExpiry) {
        this.itemExpiry = itemExpiry;
    }

    public String getItemImage() {
        return itemImage;
    }

    public void setItemImage(String itemImage) {
        this.itemImage = itemImage;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getItemSource() {
        return itemSource;
    }

    public void setItemSource(String itemSource) {
        this.itemSource = itemSource;
    }

    public String getItemDelivery() {
        return itemDelivery;
    }

    public void setItemDelivery(String itemDelivery) {
        this.itemDelivery = itemDelivery;
    }

    public String getItemAddress() {
        return itemAddress;
    }

    public void setItemAddress(String itemAddress) {
        this.itemAddress = itemAddress;
    }

    public String getItemCity() {
        return itemCity;
    }

    public void setItemCity(String itemCity) {
        this.itemCity = itemCity;
    }

    public String getUploaderName() {
        return uploaderName;
    }

    public void setUploaderName(String uploaderName) {
        this.uploaderName = uploaderName;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getFoodStatus() {
        return foodStatus;
    }

    public void setFoodStatus(String foodStatus) {
        this.foodStatus = foodStatus;
    }

    public String getMyCurrentDateTime() {
        return myCurrentDateTime;
    }

    public void setMyCurrentDateTime(String myCurrentDateTime) {
        this.myCurrentDateTime = myCurrentDateTime;
    }

    public String getRequesterId() {
        return requesterId;
    }

    public void setRequesterId(String requesterId) {
        this.requesterId = requesterId;
    }


}
