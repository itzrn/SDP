package com.example.autocheckouts;

public class Modal {
    String name;
    String price;
    int count;
    int img;
    String timeStamp;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public int getImg() {
        return img;
    }

    public void setImg(int img) {
        this.img = img;
    }

    public String getTimeStamp() {
        return timeStamp;
    }

    public void setTimeStamp(String timeStamp) {
        this.timeStamp = timeStamp;
    }

    public Modal(){}

    public Modal(String timeStamp , String name,String price,int img,int count) {
        this.name = name;
        this.price = price;
        this.count = count;
        this.img = img;
        this.timeStamp = timeStamp;
    }
}
