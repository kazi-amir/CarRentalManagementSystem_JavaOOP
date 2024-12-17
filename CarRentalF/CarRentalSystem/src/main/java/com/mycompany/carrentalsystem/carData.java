/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.carrentalsystem;

import java.sql.Date;

/**
 *
 * @author amir
 */
public class carData {
    private Integer carId;
    private String brand;
    private String model;
    private Double price;
    private String status;
    private Date date;
    
    public carData(int carId, String brand, String model, Double price, String status, Date date){
        this.carId = carId;
        this.brand = brand;
        this.model = model;
        this.price = price;
        this.status = status;
        this.date = date;

    }
    public int getCarId(){
        return carId;
    }
    public String getBrand(){
        return brand;
    }
    public String getModel(){
        return model;
    }
    public Double getPrice(){
        return price;
    }
    public String getStatus(){
        return status;
    }
    public Date getDate(){
        return date;
    }
    public String getCarIDstring() {
        return ""+carId;
    }
}
