package com.java1.fullsail.vestiruyaalpha.activity.model;

public class TailorJob extends BaseModel {

    private String customerId;
    public boolean isAccepted;
    public boolean isConfimed;
    private String name;
    private String pics;
    private String price;
    private String userId;
    private ItemModel2 items;
    private String date;
    private String picUrl;

    public String getPicUrl() {
        return picUrl;
    }

    public void setPicUrl(String picUrl) {
        this.picUrl = picUrl;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public boolean getisAccepted() {
        return isAccepted;
    }

    public void setAccepted(boolean accepted) {
        isAccepted = accepted;
    }

    public boolean getisConfimed() {
        return isConfimed;
    }

    public void setConfimed(boolean confimed) {
        isConfimed = confimed;
    }

    public String getCustomerId() {
        return customerId;
    }

    public void setCustomerId(String customerId) {
        this.customerId = customerId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPics() {
        return pics;
    }

    public void setPics(String pics) {
        this.pics = pics;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public ItemModel2 getItems() {
        return items;
    }

    public void setItems(ItemModel2 items) {
        this.items = items;
    }
}
