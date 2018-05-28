package com.ats.traymanagement.model;

/**
 * Created by MAXADMIN on 20/2/2018.
 */

public class TrayMgmtHeaderDisplayList {

    private int tranId;
    private String tranDate;
    private int vehId;
    private int drvId;
    private String driverName;
    private int routeId;
    private String routeName;
    private String vehNo;
    private String vehOuttime;
    private String vehIntime;
    private float vehOutkm;
    private float vehInkm;
    private float vehRunningKm;
    private float diesel;
    private int vehStatus;
    private int delStatus;
    private int extraTrayOut;
    private int extraTrayIn;
    private int vehIsRegular;
    private int isSameDay;

    public int getIsSameDay() {
        return isSameDay;
    }

    public void setIsSameDay(int isSameDay) {
        this.isSameDay = isSameDay;
    }

    public int getTranId() {
        return tranId;
    }

    public void setTranId(int tranId) {
        this.tranId = tranId;
    }

    public String getTranDate() {
        return tranDate;
    }

    public void setTranDate(String tranDate) {
        this.tranDate = tranDate;
    }

    public int getVehId() {
        return vehId;
    }

    public void setVehId(int vehId) {
        this.vehId = vehId;
    }

    public int getDrvId() {
        return drvId;
    }

    public void setDrvId(int drvId) {
        this.drvId = drvId;
    }

    public String getDriverName() {
        return driverName;
    }

    public void setDriverName(String driverName) {
        this.driverName = driverName;
    }

    public int getRouteId() {
        return routeId;
    }

    public void setRouteId(int routeId) {
        this.routeId = routeId;
    }

    public String getRouteName() {
        return routeName;
    }

    public void setRouteName(String routeName) {
        this.routeName = routeName;
    }

    public String getVehNo() {
        return vehNo;
    }

    public void setVehNo(String vehNo) {
        this.vehNo = vehNo;
    }

    public String getVehOuttime() {
        return vehOuttime;
    }

    public void setVehOuttime(String vehOuttime) {
        this.vehOuttime = vehOuttime;
    }

    public String getVehIntime() {
        return vehIntime;
    }

    public void setVehIntime(String vehIntime) {
        this.vehIntime = vehIntime;
    }

    public float getVehOutkm() {
        return vehOutkm;
    }

    public void setVehOutkm(float vehOutkm) {
        this.vehOutkm = vehOutkm;
    }

    public float getVehInkm() {
        return vehInkm;
    }

    public void setVehInkm(float vehInkm) {
        this.vehInkm = vehInkm;
    }

    public float getVehRunningKm() {
        return vehRunningKm;
    }

    public void setVehRunningKm(float vehRunningKm) {
        this.vehRunningKm = vehRunningKm;
    }

    public float getDiesel() {
        return diesel;
    }

    public void setDiesel(float diesel) {
        this.diesel = diesel;
    }

    public int getVehStatus() {
        return vehStatus;
    }

    public void setVehStatus(int vehStatus) {
        this.vehStatus = vehStatus;
    }

    public int getDelStatus() {
        return delStatus;
    }

    public void setDelStatus(int delStatus) {
        this.delStatus = delStatus;
    }

    public int getExtraTrayOut() {
        return extraTrayOut;
    }

    public void setExtraTrayOut(int extraTrayOut) {
        this.extraTrayOut = extraTrayOut;
    }

    public int getExtraTrayIn() {
        return extraTrayIn;
    }

    public void setExtraTrayIn(int extraTrayIn) {
        this.extraTrayIn = extraTrayIn;
    }

    public int getVehIsRegular() {
        return vehIsRegular;
    }

    public void setVehIsRegular(int vehIsRegular) {
        this.vehIsRegular = vehIsRegular;
    }

    @Override
    public String toString() {
        return "TrayMgmtHeaderDisplayList{" +
                "tranId=" + tranId +
                ", tranDate='" + tranDate + '\'' +
                ", vehId=" + vehId +
                ", drvId=" + drvId +
                ", driverName='" + driverName + '\'' +
                ", routeId=" + routeId +
                ", routeName='" + routeName + '\'' +
                ", vehNo='" + vehNo + '\'' +
                ", vehOuttime='" + vehOuttime + '\'' +
                ", vehIntime='" + vehIntime + '\'' +
                ", vehOutkm=" + vehOutkm +
                ", vehInkm=" + vehInkm +
                ", vehRunningKm=" + vehRunningKm +
                ", diesel=" + diesel +
                ", vehStatus=" + vehStatus +
                ", delStatus=" + delStatus +
                ", extraTrayOut=" + extraTrayOut +
                ", extraTrayIn=" + extraTrayIn +
                ", vehIsRegular=" + vehIsRegular +
                ", isSameDay=" + isSameDay +
                '}';
    }
}
