package com.nh.gasmap;

public class GasStation {
    private String mBrand; //銘柄
    private double mLongitude; //緯度
    private double mLatitude; //経度

    public void setBrand(String brand) {
        mBrand = brand;
    }

    public String getBrand() {
        return mBrand;
    }

    public void setLongitude(double longitude) {
        mLongitude = longitude;
    }

    public double getLongitude() {
        return mLongitude;
    }

    public void setLatitude(double latitude) {
        mLatitude = latitude;
    }

    public double getLatitude() {
        return mLatitude;
    }

}
