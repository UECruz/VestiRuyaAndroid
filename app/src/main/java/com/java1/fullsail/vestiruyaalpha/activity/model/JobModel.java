package com.java1.fullsail.vestiruyaalpha.activity.model;

import java.io.Serializable;
import java.util.List;

public class JobModel implements Serializable {
    private boolean canEdit;
    private boolean isJobConfirmed;
    private String photoImage;
    private String price;
    private String tailorID;
    private String userid;
    private String username;
    public List<InterestsShownModel> interestsShown;
    public ItemModel items;
    public String key;

    public List<InterestsShownModel> getInterestsShown() {
        return interestsShown;
    }

    public void setInterestsShown(List<InterestsShownModel> interestsShown) {
        this.interestsShown = interestsShown;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public boolean getCanEdit() {
        return canEdit;
    }

    public void setCanEdit(boolean canEdit) {
        this.canEdit = canEdit;
    }

    public boolean getIsJobConfirmed() {
        return isJobConfirmed;
    }

    public void setIsJobConfirmed(boolean isJobConfirmed) {
        this.isJobConfirmed = isJobConfirmed;
    }

    public String getPhotoImage() {
        return photoImage;
    }

    public void setPhotoImage(String photoImage) {
        this.photoImage = photoImage;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getTailorID() {
        return tailorID;
    }

    public void setTailorID(String tailorID) {
        this.tailorID = tailorID;
    }

    public String getUserid() {
        return userid;
    }

    public void setUserid(String userid) {
        this.userid = userid;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public ItemModel getItemmodel() {
        return items;
    }

    public void setItemmodel(ItemModel items) {
        this.items = items;
    }
}
