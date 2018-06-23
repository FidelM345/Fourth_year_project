package com.example.thebeast.afyahelp;

/**
 * Created by Fidel M Omolo on 3/31/2018.
 */

public class BlogPost_model_class extends BlogPostId {

    public String fname,lname,imageuri,location,blood_group,weight,age,email,gender;


    public BlogPost_model_class() {
    }


    public BlogPost_model_class(String fname, String lname, String imageuri, String location, String blood_group, String weight, String age, String email, String gender) {
        this.fname = fname;
        this.lname = lname;
        this.imageuri = imageuri;
        this.location = location;
        this.blood_group = blood_group;
        this.weight = weight;
        this.age = age;
        this.email = email;
        this.gender = gender;
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

    public String getWeight() {
        return weight;
    }

    public void setWeight(String weight) {
        this.weight = weight;
    }

    public String getAge() {
        return age;
    }

    public void setAge(String age) {
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
