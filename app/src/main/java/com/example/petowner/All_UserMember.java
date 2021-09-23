package com.example.petowner;
/* Model class */
public class All_UserMember {
    String full_name;
    String phone_no;
    String address;
    String nid;
    String url;
    String uid;
    String availability;

    public All_UserMember() {
    }

    public All_UserMember(String full_name, String phone_no, String address, String nid, String url, String uid, String availability) {
        this.full_name = full_name;
        this.phone_no = phone_no;
        this.address = address;
        this.nid = nid;
        this.url = url;
        this.uid = uid;
        this.availability = availability;
    }

    public String getAvailability() {
        return availability;
    }

    public void setAvailability(String availability) {
        this.availability = availability;
    }

    public String getFull_name() {
        return full_name;
    }

    public void setFull_name(String full_name) {
        this.full_name = full_name;
    }

    public String getPhone_no() {
        return phone_no;
    }

    public void setPhone_no(String phone_no) {
        this.phone_no = phone_no;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getNid() {
        return nid;
    }

    public void setNid(String nid) {
        this.nid = nid;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }
}
