package com.ats.traymanagement.model;

/**
 * Created by MAXADMIN on 19/2/2018.
 */

public class TrayMgmtHeaderData {

    private boolean error;
    private String message;
    private int tranId;
    private String tranDate;
    private int vehId;
    private int drvId;
    private int routeId;
    private String vehNo;
    private String vehOuttime;
    private String vehIntime;
    private float vehOutkm;
    private float vehInkm;
    private float vehRunningKm;
    private float diesel;
    private int vehStatus;
    private int delStatus;
    private String extraTrayOut;
    private String extraTrayIn;
    private int vehIsRegular;
    private int isSameDay;

    public TrayMgmtHeaderData() {
    }

    public TrayMgmtHeaderData(int tranId, String tranDate, int vehId, int drvId, int routeId, String vehNo, String vehOuttime, String vehIntime, float vehOutkm, float vehInkm, float vehRunningKm, float diesel, int vehStatus, int delStatus, String extraTrayOut, String extraTrayIn, int vehIsRegular,int isSameDay) {
        this.tranId = tranId;
        this.tranDate = tranDate;
        this.vehId = vehId;
        this.drvId = drvId;
        this.routeId = routeId;
        this.vehNo = vehNo;
        this.vehOuttime = vehOuttime;
        this.vehIntime = vehIntime;
        this.vehOutkm = vehOutkm;
        this.vehInkm = vehInkm;
        this.vehRunningKm = vehRunningKm;
        this.diesel = diesel;
        this.vehStatus = vehStatus;
        this.delStatus = delStatus;
        this.extraTrayOut = extraTrayOut;
        this.extraTrayIn = extraTrayIn;
        this.vehIsRegular = vehIsRegular;
        this.isSameDay=isSameDay;
    }

    public int getIsSameDay() {
        return isSameDay;
    }

    public void setIsSameDay(int isSameDay) {
        this.isSameDay = isSameDay;
    }

    public boolean isError() {
        return error;
    }

    public void setError(boolean error) {
        this.error = error;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
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

    public int getRouteId() {
        return routeId;
    }

    public void setRouteId(int routeId) {
        this.routeId = routeId;
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

    public String getExtraTrayOut() {
        return extraTrayOut;
    }

    public void setExtraTrayOut(String extraTrayOut) {
        this.extraTrayOut = extraTrayOut;
    }

    public String getExtraTrayIn() {
        return extraTrayIn;
    }

    public void setExtraTrayIn(String extraTrayIn) {
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
        return "TrayMgmtHeaderData{" +
                "error=" + error +
                ", message='" + message + '\'' +
                ", tranId=" + tranId +
                ", tranDate='" + tranDate + '\'' +
                ", vehId=" + vehId +
                ", drvId=" + drvId +
                ", routeId=" + routeId +
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
