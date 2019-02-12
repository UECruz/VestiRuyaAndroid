package com.java1.fullsail.vestiruyaalpha.activity.model;

import java.io.Serializable;

public class InterestsShownModel implements Serializable {

    private boolean isJobAccepted;
    private String tailorID;
    private String tailorLoc;
    private String tailorPic;
    private String tailorname;
    private String key;

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public boolean isJobAccepted() {
        return isJobAccepted;
    }

    public void setJobAccepted(boolean jobAccepted) {
        isJobAccepted = jobAccepted;
    }

    public String getTailorID() {
        return tailorID;
    }

    public void setTailorID(String tailorID) {
        this.tailorID = tailorID;
    }

    public String getTailorLoc() {
        return tailorLoc;
    }

    public void setTailorLoc(String tailorLoc) {
        this.tailorLoc = tailorLoc;
    }

    public String getTailorPic() {
        return tailorPic;
    }

    public void setTailorPic(String tailorPic) {
        this.tailorPic = tailorPic;
    }

    public String getTailorname() {
        return tailorname;
    }

    public void setTailorname(String tailorname) {
        this.tailorname = tailorname;
    }
}
