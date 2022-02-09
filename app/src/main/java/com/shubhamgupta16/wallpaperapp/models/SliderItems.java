package com.shubhamgupta16.wallpaperapp.models;

public class SliderItems {
    //set to String, if you want to add image url from internet
    private int image;
    SliderItems(int image) {
        this.image = image;
    }
    public int getImage() {
        return image;
    }
}
