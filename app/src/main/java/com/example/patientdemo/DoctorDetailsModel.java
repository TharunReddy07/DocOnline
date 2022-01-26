package com.example.patientdemo;

public class DoctorDetailsModel {

    String name, username, email, mobile, degree, specialization, experience, hospital, price;

    public DoctorDetailsModel() {
    }

    public DoctorDetailsModel(String name, String username, String email, String mobile, String degree,
                              String specialization, String experience, String hospital, String price) {
        this.name = name;
        this.username = username;
        this.email = email;
        this.mobile = mobile;
        this.degree = degree;
        this.specialization = specialization;
        this.experience = experience;
        this.hospital = hospital;
        this.price = price;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getDegree() {
        return degree;
    }

    public void setDegree(String degree) {
        this.degree = degree;
    }

    public String getSpecialization() {
        return specialization;
    }

    public void setSpecialization(String specialization) {
        this.specialization = specialization;
    }

    public String getExperience() {
        return experience;
    }

    public void setExperience(String experience) {
        this.experience = experience;
    }

    public String getHospital() {
        return hospital;
    }

    public void setHospital(String hospital) {
        this.hospital = hospital;
    }
}
