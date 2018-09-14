package com.example.thebeast.afyahelp;



public class Search_model {
    int image;
    String description;


    public Search_model(int image, String description) {
        this.image = image;
        this.description = description;
    }


    public int getImage() {
        return image;
    }

    public void setImage(int image) {
        this.image = image;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

}
