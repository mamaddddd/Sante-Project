package com.example.sante.Model;

public class VaccineModel {
    private String VaccineID,Title,Price;

    public VaccineModel() {
    }

    public VaccineModel(String vaccineID, String title, String price) {
        VaccineID = vaccineID;
        Title = title;
        Price = price;
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
}
