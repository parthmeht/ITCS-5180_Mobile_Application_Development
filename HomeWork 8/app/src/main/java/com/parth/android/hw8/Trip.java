package com.parth.android.hw8;

import java.io.Serializable;
import java.util.ArrayList;

public class Trip implements Serializable {

    String id,name, userId;
    String city;
    Double cityLat;
    Double cityLng;
    ArrayList<Restaurant> restaurants;

    public Trip(String id, String name, String userId, String city, Double cityLat, Double cityLng, ArrayList<Restaurant> restaurants) {
        this.id = id;
        this.name = name;
        this.userId = userId;
        this.city = city;
        this.cityLat = cityLat;
        this.cityLng = cityLng;
        this.restaurants = restaurants;
    }

    public Trip(){

    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public Double getCityLat() {
        return cityLat;
    }

    public void setCityLat(Double cityLat) {
        this.cityLat = cityLat;
    }

    public Double getCityLng() {
        return cityLng;
    }

    public void setCityLng(Double cityLng) {
        this.cityLng = cityLng;
    }

    public ArrayList<Restaurant> getRestaurants() {
        return restaurants;
    }

    public void setRestaurants(ArrayList<Restaurant> restaurants) {
        this.restaurants = restaurants;
    }

    @Override
    public String toString() {
        return "Trip{" +
                "id='" + id + '\'' +
                ", name='" + name + '\'' +
                ", userId='" + userId + '\'' +
                ", city='" + city + '\'' +
                ", cityLat=" + cityLat +
                ", cityLng=" + cityLng +
                ", restaurants=" + restaurants +
                '}';
    }
}
