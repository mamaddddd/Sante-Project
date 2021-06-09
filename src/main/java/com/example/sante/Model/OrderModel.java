package com.example.sante.Model;

public class OrderModel {

    private String VaccineID,Title,Price,Date;

    public OrderModel() {
    }

    public OrderModel(String vaccineID, String title, String price, String date) {
        VaccineID = vaccineID;
        Title = title;
        Price = price;
        Date = date;
    }

    public String getVaccineID() {
        return VaccineID;
    }

    public void setVaccineID(String vaccineID) {
        VaccineID = vaccineID;
    }

    public String getTitle() {
        return Title;
    }

    public void setTitle(String title) {
        Title = title;
    }

    public String getPrice() {
        return Price;
    }

    public void setPrice(String price) {
        Price = price;
    }

    public String getDate() {
        return Date;
    }

    public void setDate(String date) {
        Date = date;
    }
}

