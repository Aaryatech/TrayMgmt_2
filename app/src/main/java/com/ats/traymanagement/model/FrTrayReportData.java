package com.ats.traymanagement.model;

public class FrTrayReportData {

    private String dateStr;
    private Integer openingSmall;
    private Integer openingLead;
    private Integer openingBig;
    private Integer receivedSmall;
    private Integer receivedLead;
    private Integer receivedBig;
    private Integer returnSmall;
    private Integer returnLead;
    private Integer returnBig;
    private Integer balSmall;
    private Integer balLead;
    private Integer balBig;

    public String getDateStr() {
        return dateStr;
    }

    public void setDateStr(String dateStr) {
        this.dateStr = dateStr;
    }

    public Integer getOpeningSmall() {
        return openingSmall;
    }

    public void setOpeningSmall(Integer openingSmall) {
        this.openingSmall = openingSmall;
    }

    public Integer getOpeningLead() {
        return openingLead;
    }

    public void setOpeningLead(Integer openingLead) {
        this.openingLead = openingLead;
    }

    public Integer getOpeningBig() {
        return openingBig;
    }

    public void setOpeningBig(Integer openingBig) {
        this.openingBig = openingBig;
    }

    public Integer getReceivedSmall() {
        return receivedSmall;
    }

    public void setReceivedSmall(Integer receivedSmall) {
        this.receivedSmall = receivedSmall;
    }

    public Integer getReceivedLead() {
        return receivedLead;
    }

    public void setReceivedLead(Integer receivedLead) {
        this.receivedLead = receivedLead;
    }

    public Integer getReceivedBig() {
        return receivedBig;
    }

    public void setReceivedBig(Integer receivedBig) {
        this.receivedBig = receivedBig;
    }

    public Integer getReturnSmall() {
        return returnSmall;
    }

    public void setReturnSmall(Integer returnSmall) {
        this.returnSmall = returnSmall;
    }

    public Integer getReturnLead() {
        return returnLead;
    }

    public void setReturnLead(Integer returnLead) {
        this.returnLead = returnLead;
    }

    public Integer getReturnBig() {
        return returnBig;
    }

    public void setReturnBig(Integer returnBig) {
        this.returnBig = returnBig;
    }

    public Integer getBalSmall() {
        return balSmall;
    }

    public void setBalSmall(Integer balSmall) {
        this.balSmall = balSmall;
    }

    public Integer getBalLead() {
        return balLead;
    }

    public void setBalLead(Integer balLead) {
        this.balLead = balLead;
    }

    public Integer getBalBig() {
        return balBig;
    }

    public void setBalBig(Integer balBig) {
        this.balBig = balBig;
    }

    @Override
    public String toString() {
        return "FrTrayReportData{" +
                "dateStr='" + dateStr + '\'' +
                ", openingSmall=" + openingSmall +
                ", openingLead=" + openingLead +
                ", openingBig=" + openingBig +
                ", receivedSmall=" + receivedSmall +
                ", receivedLead=" + receivedLead +
                ", receivedBig=" + receivedBig +
                ", returnSmall=" + returnSmall +
                ", returnLead=" + returnLead +
                ", returnBig=" + returnBig +
                ", balSmall=" + balSmall +
                ", balLead=" + balLead +
                ", balBig=" + balBig +
                '}';
    }
}
