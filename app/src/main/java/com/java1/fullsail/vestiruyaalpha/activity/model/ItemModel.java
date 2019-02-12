package com.java1.fullsail.vestiruyaalpha.activity.model;

import java.io.Serializable;

public class ItemModel implements Serializable {
    private String backdetail;
    private String bodytype;
    private String embellishment;
    private String fabric;
    private String neckline;
    private String sleeves;
    private String straps;

    public String getBackdetail() {
        return backdetail;
    }

    public void setBackdetail(String backdetail) {
        this.backdetail = backdetail;
    }

    public String getBodytype() {
        return bodytype;
    }

    public void setBodytype(String bodytype) {
        this.bodytype = bodytype;
    }

    public String getEmbellishment() {
        return embellishment;
    }

    public void setEmbellishment(String embellishment) {
        this.embellishment = embellishment;
    }

    public String getFabric() {
        return fabric;
    }

    public void setFabric(String fabric) {
        this.fabric = fabric;
    }

    public String getNeckline() {
        return neckline;
    }

    public void setNeckline(String neckline) {
        this.neckline = neckline;
    }

    public String getSleeves() {
        return sleeves;
    }

    public void setSleeves(String sleeves) {
        this.sleeves = sleeves;
    }

    public String getStraps() {
        return straps;
    }

    public void setStraps(String straps) {
        this.straps = straps;
    }
}
