package com.java1.fullsail.vestiruyaalpha.activity.model;

import java.io.Serializable;

public class ItemModel2 implements Serializable {
    private String backDetail;
    private String dressType;
    private String fabric;
    private String neckline;
    private String slevee;
    private  String strap;
    private String embellish;

    public String getBackDetail() {
        return backDetail;
    }

    public void setBackDetail(String backDetail) {
        this.backDetail = backDetail;
    }

    public String getDressType() {
        return dressType;
    }

    public void setDressType(String dressType) {
        this.dressType = dressType;
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

    public String getSlevee() {
        return slevee;
    }

    public void setSlevee(String slevee) {
        this.slevee = slevee;
    }

    public String getStrap() {
        return strap;
    }

    public void setStrap(String strap) {
        this.strap = strap;
    }

    public String getEmbellish() {
        return embellish;
    }

    public void setEmbellish(String embellish) {
        this.embellish = embellish;
    }
}
