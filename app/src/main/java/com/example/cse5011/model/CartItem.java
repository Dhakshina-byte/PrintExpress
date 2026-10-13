package com.example.cse5011.model;

public class CartItem {
    private int id;
    private String title;
    private String price;
    private String size;
    private int quantity;
    private int imageRes;
    private String date;

    public CartItem(int id, String title, String price, String size, int quantity, int imageRes, String date) {
        this.id = id;
        this.title = title;
        this.price = price;
        this.size = size;
        this.quantity = quantity;
        this.imageRes = imageRes;
        this.date = date;
    }

    public CartItem(int id, String title, String price, String size, int quantity, int imageRes) {
        this(id, title, price, size, quantity, imageRes, "");
    }

    public int getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public String getPrice() {
        return price;
    }

    public String getSize() {
        return size;
    }

    public int getQuantity() {
        return quantity;
    }

    public int getImageRes() {
        return imageRes;
    }

    public String getDate() {
        return date;
    }
}
