package com.example.autocheckouts;

public class Items {
    String name;
    String price;
    int img;
    int quantity=1;

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

    public int getImg() {
        return img;
    }

    public void setImg(int img) {
        this.img = img;
    }


    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }
    public Items(){}

    public Items(String name, String price, int img) {
        this.name = name;
        this.price = price;
        this.img = img;
    }
}

