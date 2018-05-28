package com.ats.traymanagement.model;

import java.util.List;

/**
 * Created by MAXADMIN on 8/3/2018.
 */

public class VehicleInTrayStatus {

    private Integer frId;
    private List<TrayMgtDetailsList> trayMgtDetailsList;

    public Integer getFrId() {
        return frId;
    }

    public void setFrId(Integer frId) {
        this.frId = frId;
    }

    public List<TrayMgtDetailsList> getTrayMgtDetailsList() {
        return trayMgtDetailsList;
    }

    public void setTrayMgtDetailsList(List<TrayMgtDetailsList> trayMgtDetailsList) {
        this.trayMgtDetailsList = trayMgtDetailsList;
    }

    @Override
    public String toString() {
        return "VehicleInTrayStatus{" +
                "frId=" + frId +
                ", trayMgtDetailsList=" + trayMgtDetailsList +
                '}';
    }
}
