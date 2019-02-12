package com.java1.fullsail.vestiruyaalpha.activity.model;

import java.io.Serializable;

public class Measurement  implements Serializable{
    String userId;
    String username;
    MeasureItem measureItem = new MeasureItem();

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public MeasureItem getMeasureItem() {
        return measureItem;
    }

    public void setMeasureItem(MeasureItem measureItem) {
        this.measureItem = measureItem;
    }

    public static class MeasureItem implements Serializable {
        String arm, bust, chest, height, leg, neck, waist;

        public String getArm() {
            return arm;
        }

        public void setArm(String arm) {
            this.arm = arm;
        }

        public String getBust() {
            return bust;
        }

        public void setBust(String bust) {
            this.bust = bust;
        }

        public String getChest() {
            return chest;
        }

        public void setChest(String chest) {
            this.chest = chest;
        }

        public String getHeight() {
            return height;
        }

        public void setHeight(String height) {
            this.height = height;
        }

        public String getLeg() {
            return leg;
        }

        public void setLeg(String leg) {
            this.leg = leg;
        }

        public String getNeck() {
            return neck;
        }

        public void setNeck(String neck) {
            this.neck = neck;
        }

        public String getWaist() {
            return waist;
        }

        public void setWaist(String waist) {
            this.waist = waist;
        }
    }
}
