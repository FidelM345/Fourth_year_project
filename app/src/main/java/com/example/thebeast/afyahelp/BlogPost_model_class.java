package com.example.thebeast.afyahelp;

/**
 * Created by Fidel M Omolo on 3/31/2018.
 */

public class BlogPost_model_class extends BlogPostId {

    public String fname,lname,imageuri,location,blood_group,email,gender,rhesus,
            thumburi,phone_no;

    Long age;
    Long weight;

    public BlogPost_model_class() {
    }


    public BlogPost_model_class(String fname, String lname, String imageuri,
                                String location, String blood_group, String email,
                                String gender, String rhesus, String thumburi,
                                String phone_no, Long age, Long weight) {
        this.fname = fname;
        this.lname = lname;
        this.imageuri = imageuri;
        this.location = location;
        this.blood_group = blood_group;
        this.email = email;
        this.gender = gender;
        this.rhesus = rhesus;
        this.thumburi = thumburi;
        this.phone_no = phone_no;
        this.age = age;
        this.weight = weight;
    }

    public String getPhone_no() {
        return phone_no;
    }

    public void setPhone_no(String phone_no) {
        this.phone_no = phone_no;
    }

    public String getThumburi() {
        return thumburi;
    }

    public void setThumburi(String thumburi) {
        this.thumburi = thumburi;
    }

    public String getRhesus() {
        return rhesus;
    }

    public void setRhesus(String rhesus) {
        this.rhesus = rhesus;
    }

    public String getFname() {
        return fname;
    }

    public void setFname(String fname) {
        this.fname = fname;
    }

    public String getLname() {
        return lname;
    }

    public void setLname(String lname) {
        this.lname = lname;
    }

    public String getImageuri() {
        return imageuri;
    }

    public void setImageuri(String imageuri) {
        this.imageuri = imageuri;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getBlood_group() {
        return blood_group;
    }

    public void setBlood_group(String blood_group) {
        this.blood_group = blood_group;
    }

    public Long getWeight() {
        return weight;
    }

    public void setWeight(Long weight) {
        this.weight = weight;
    }

    public Long getAge() {
        return age;
    }

    public void setAge(Long age) {
        this.age = age;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }
}
