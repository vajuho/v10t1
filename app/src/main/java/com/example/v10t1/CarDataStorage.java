package com.example.v10t1;

import java.util.ArrayList;

public class CarDataStorage {
    public String city;
    public int year;
    public ArrayList<CarData> carData = new ArrayList<>();
    private static CarDataStorage storage = null;
    private CarDataStorage() {
    }
    public static CarDataStorage getInstance() {
        if(storage == null) {
            storage = new CarDataStorage();
        }
        return storage;
    }
    public String getCity() {
        return city;
    }

    public int getYear() {
        return year;
    }

    public ArrayList<CarData> getCarData() {
        return carData;
    }
    public void setCity(String city) {
        this.city = city;
    }

    public void setYear(int year) {
        this.year = year;
    }
    public void clearData() {
        carData.clear();
        city = "";
        year = 0;
    }
    public void addCarData(CarData data) {
        carData.add(data);
    }
}
