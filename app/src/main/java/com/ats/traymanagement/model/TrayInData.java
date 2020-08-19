package com.ats.traymanagement.model;

public class TrayInData {
    private Integer frId;
    private Integer outtrayBig;
    private Integer outtraySmall;
    private Integer outtrayLead;
    private Integer intrayBig;
    private Integer intraySmall;
    private Integer intrayLead;
    private Integer balanceBig;
    private Integer balanceSmall;
    private Integer balanceLead;
    private String frName;
    private Object frCode;

    public Integer getFrId() {
        return frId;
    }

    public void setFrId(Integer frId) {
        this.frId = frId;
    }

    public Integer getOuttrayBig() {
        return outtrayBig;
    }

    public void setOuttrayBig(Integer outtrayBig) {
        this.outtrayBig = outtrayBig;
    }

    public Integer getOuttraySmall() {
        return outtraySmall;
    }

    public void setOuttraySmall(Integer outtraySmall) {
        this.outtraySmall = outtraySmall;
    }

    public Integer getOuttrayLead() {
        return outtrayLead;
    }

    public void setOuttrayLead(Integer outtrayLead) {
        this.outtrayLead = outtrayLead;
    }

    public Integer getIntrayBig() {
        return intrayBig;
    }

    public void setIntrayBig(Integer intrayBig) {
        this.intrayBig = intrayBig;
    }

    public Integer getIntraySmall() {
        return intraySmall;
    }

    public void setIntraySmall(Integer intraySmall) {
        this.intraySmall = intraySmall;
    }

    public Integer getIntrayLead() {
        return intrayLead;
    }

    public void setIntrayLead(Integer intrayLead) {
        this.intrayLead = intrayLead;
    }

    public Integer getBalanceBig() {
        return balanceBig;
    }

    public void setBalanceBig(Integer balanceBig) {
        this.balanceBig = balanceBig;
    }

    public Integer getBalanceSmall() {
        return balanceSmall;
    }

    public void setBalanceSmall(Integer balanceSmall) {
        this.balanceSmall = balanceSmall;
    }

    public Integer getBalanceLead() {
        return balanceLead;
    }

    public void setBalanceLead(Integer balanceLead) {
        this.balanceLead = balanceLead;
    }

    public String getFrName() {
        return frName;
    }

    public void setFrName(String frName) {
        this.frName = frName;
    }

    public Object getFrCode() {
        return frCode;
    }

    public void setFrCode(Object frCode) {
        this.frCode = frCode;
    }


    @Override
    public String toString() {
        return "TrayInData{" +
                "frId=" + frId +
                ", outtrayBig=" + outtrayBig +
                ", outtraySmall=" + outtraySmall +
                ", outtrayLead=" + outtrayLead +
                ", intrayBig=" + intrayBig +
                ", intraySmall=" + intraySmall +
                ", intrayLead=" + intrayLead +
                ", balanceBig=" + balanceBig +
                ", balanceSmall=" + balanceSmall +
                ", balanceLead=" + balanceLead +
                ", frName='" + frName + '\'' +
                ", frCode=" + frCode +
                '}';
    }
}
