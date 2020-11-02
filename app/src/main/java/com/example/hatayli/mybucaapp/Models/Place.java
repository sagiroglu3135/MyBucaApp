package com.example.hatayli.mybucaapp.Models;


import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.Comparator;

//Bu Class internetten alınan mekan bilgilerinin modeli olarak kullanılmaktadır.
public class Place implements Serializable {

//    private boolean isActive;
//    private double latitude;
//    private double longitude;
//    private double userRating;
//    private String name;
//    private String phoneNumber;
@SerializedName("name")
@Expose
public String name;
    @SerializedName("latitude")
    @Expose
    public Double latitude;
    @SerializedName("longitude")
    @Expose
    public Double longitude;
    @SerializedName("userRating")
    @Expose
    public Double userRating;
    @SerializedName("phoneNumber")
    @Expose
    public String phoneNumber;
    @SerializedName("isActive")
    @Expose
    public Boolean isActive;

    private float distanceFromUser = -1;

    public boolean isActive() {
        return isActive;
    }

    public void setActive(boolean active) {
        isActive = active;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public double getUserRating() {
        return userRating;
    }

    public void setUserRating(double userRating) {
        this.userRating = userRating;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public float getDistanceFromUser() {
        return distanceFromUser;
    }

    public void setDistanceFromUser(float distance)
    {
        this.distanceFromUser = distance;
    }

    //İsime göre sıralamak için.
    public static Comparator<Place> sortByName= new Comparator<Place>() {
        @Override
        public int compare(Place o1, Place o2) {
            return o1.getName().compareTo(o2.getName());
        }
    };

    //Kullanıcı puanına göre sıralamk için.
    public static Comparator<Place> sortByUserRating= new Comparator<Place>() {
        @Override
        public int compare(Place o1, Place o2) {
            return Double.compare(o2.getUserRating(), o1.getUserRating());
        }
    };

    //mesafeye göre sıralamak için.
    public static Comparator<Place> sortByDistance = new Comparator<Place>() {
        @Override
        public int compare(Place o1, Place o2) {
            return Float.compare(o1.getDistanceFromUser(), o2.getDistanceFromUser());
        }
    };

}
