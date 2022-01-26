package com.example.patientdemo;

public class ActiveAppointmentsModel {

    String name, image, time, price;

    public ActiveAppointmentsModel() {
    }

    public ActiveAppointmentsModel(String name, String image, String time, String price) {
        this.name = name;
        this.image = image;
        this.time = time;
        this.price = price;
    }

    public String getName() {
        return name;
    }

    public String getImage() {
        return image;
    }

    public String getTime() {
        return time;
    }

    public String getPrice() {
        return price;
    }
}
