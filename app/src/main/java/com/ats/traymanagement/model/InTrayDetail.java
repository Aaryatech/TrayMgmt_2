package com.ats.traymanagement.model;

public class InTrayDetail {

    private Integer intrayId;
    private Integer tranDetailId;
    private Integer tranId;
    private Integer frId;
    private String intrayDate;
    private Integer exInt1;
    private Integer exInt2;
    private Object exVar1;
    private Object exVar2;
    private Integer delStatus;
    private Integer intrayBig;
    private Integer intrayLead;
    private Integer tranIntrayId;
    private Integer intraySmall;

    public InTrayDetail() {
    }


    public InTrayDetail(Integer intrayId, Integer tranDetailId, Integer tranId, Integer frId, String intrayDate, Integer exInt1, Integer exInt2, Object exVar1, Object exVar2, Integer delStatus, Integer intrayBig, Integer intrayLead, Integer tranIntrayId, Integer intraySmall) {
        this.intrayId = intrayId;
        this.tranDetailId = tranDetailId;
        this.tranId = tranId;
        this.frId = frId;
        this.intrayDate = intrayDate;
        this.exInt1 = exInt1;
        this.exInt2 = exInt2;
        this.exVar1 = exVar1;
        this.exVar2 = exVar2;
        this.delStatus = delStatus;
        this.intrayBig = intrayBig;
        this.intrayLead = intrayLead;
        this.tranIntrayId = tranIntrayId;
        this.intraySmall = intraySmall;
    }

    public Integer getIntrayId() {
        return intrayId;
    }

    public void setIntrayId(Integer intrayId) {
        this.intrayId = intrayId;
    }

    public Integer getTranDetailId() {
        return tranDetailId;
    }

    public void setTranDetailId(Integer tranDetailId) {
        this.tranDetailId = tranDetailId;
    }

    public Integer getTranId() {
        return tranId;
    }

    public void setTranId(Integer tranId) {
        this.tranId = tranId;
    }

    public Integer getFrId() {
        return frId;
    }

    public void setFrId(Integer frId) {
        this.frId = frId;
    }

    public String getIntrayDate() {
        return intrayDate;
    }

    public void setIntrayDate(String intrayDate) {
        this.intrayDate = intrayDate;
    }

    public Integer getExInt1() {
        return exInt1;
    }

    public void setExInt1(Integer exInt1) {
        this.exInt1 = exInt1;
    }

    public Integer getExInt2() {
        return exInt2;
    }

    public void setExInt2(Integer exInt2) {
        this.exInt2 = exInt2;
    }

    public Object getExVar1() {
        return exVar1;
    }

    public void setExVar1(Object exVar1) {
        this.exVar1 = exVar1;
    }

    public Object getExVar2() {
        return exVar2;
    }

    public void setExVar2(Object exVar2) {
        this.exVar2 = exVar2;
    }

    public Integer getDelStatus() {
        return delStatus;
    }

    public void setDelStatus(Integer delStatus) {
        this.delStatus = delStatus;
    }

    public Integer getIntrayBig() {
        return intrayBig;
    }

    public void setIntrayBig(Integer intrayBig) {
        this.intrayBig = intrayBig;
    }

    public Integer getIntrayLead() {
        return intrayLead;
    }

    public void setIntrayLead(Integer intrayLead) {
        this.intrayLead = intrayLead;
    }

    public Integer getTranIntrayId() {
        return tranIntrayId;
    }

    public void setTranIntrayId(Integer tranIntrayId) {
        this.tranIntrayId = tranIntrayId;
    }

    public Integer getIntraySmall() {
        return intraySmall;
    }

    public void setIntraySmall(Integer intraySmall) {
        this.intraySmall = intraySmall;
    }

    @Override
    public String toString() {
        return "InTrayDetail{" +
                "intrayId=" + intrayId +
                ", tranDetailId=" + tranDetailId +
                ", tranId=" + tranId +
                ", frId=" + frId +
                ", intrayDate='" + intrayDate + '\'' +
                ", exInt1=" + exInt1 +
                ", exInt2=" + exInt2 +
                ", exVar1=" + exVar1 +
                ", exVar2=" + exVar2 +
                ", delStatus=" + delStatus +
                ", intrayBig=" + intrayBig +
                ", intrayLead=" + intrayLead +
                ", tranIntrayId=" + tranIntrayId +
                ", intraySmall=" + intraySmall +
                '}';
    }

}
