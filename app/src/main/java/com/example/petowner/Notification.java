package com.example.petowner;
// Notification Model class
public class Notification {
    private String userid;
    private String text;
    private String hire;
    private boolean gotHired;

    public Notification() {
    }

    public Notification(String userid, String text, String hire, boolean gotHired) {
        this.userid = userid;
        this.text = text;
        this.hire = hire;
        this.gotHired = gotHired;
    }

    public String getUserid() {
        return userid;
    }

    public void setUserid(String userid) {
        this.userid = userid;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getHire() {
        return hire;
    }

    public void setHire(String hire) {
        this.hire = hire;
    }

    public boolean isGotHired() {
        return gotHired;
    }

    public void setGotHired(boolean gotHired) {
        this.gotHired = gotHired;
    }
}
