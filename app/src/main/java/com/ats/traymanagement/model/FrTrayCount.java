package com.ats.traymanagement.model;

/**
 * Created by MAXADMIN on 20/2/2018.
 */

public class FrTrayCount {

    private int trayType;
    private String trayName;
    private float noOfTray;

    public int getTrayType() {
        return trayType;
    }

    public void setTrayType(int trayType) {
        this.trayType = trayType;
    }

    public String getTrayName() {
        return trayName;
    }

    public void setTrayName(String trayName) {
        this.trayName = trayName;
    }

    public float getNoOfTray() {
        return noOfTray;
    }

    public void setNoOfTray(float noOfTray) {
        this.noOfTray = noOfTray;
    }

    @Override
    public String toString() {
        return "FrTrayCount{" +
                "trayType=" + trayType +
                ", trayName='" + trayName + '\'' +
                ", noOfTray=" + noOfTray +
                '}';
    }
}
