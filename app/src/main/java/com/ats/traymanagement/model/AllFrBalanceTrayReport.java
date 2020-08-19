package com.ats.traymanagement.model;

public class AllFrBalanceTrayReport {
    private int tranDetailId;
    private int tranId;
    private int frId;
    private int balanceBig;
    private int balanceSmall;
    private int balanceLead;
    private int balanceExtra;
    private String frName;

    public int getTranDetailId() {
        return tranDetailId;
    }

    public void setTranDetailId(int tranDetailId) {
        this.tranDetailId = tranDetailId;
    }

    public int getTranId() {
        return tranId;
    }

    public void setTranId(int tranId) {
        this.tranId = tranId;
    }

    public int getFrId() {
        return frId;
    }

    public void setFrId(int frId) {
        this.frId = frId;
    }

    public int getBalanceBig() {
        return balanceBig;
    }

    public void setBalanceBig(int balanceBig) {
        this.balanceBig = balanceBig;
    }

    public int getBalanceSmall() {
        return balanceSmall;
    }

    public void setBalanceSmall(int balanceSmall) {
        this.balanceSmall = balanceSmall;
    }

    public int getBalanceLead() {
        return balanceLead;
    }

    public void setBalanceLead(int balanceLead) {
        this.balanceLead = balanceLead;
    }

    public int getBalanceExtra() {
        return balanceExtra;
    }

    public void setBalanceExtra(int balanceExtra) {
        this.balanceExtra = balanceExtra;
    }

    public String getFrName() {
        return frName;
    }

    public void setFrName(String frName) {
        this.frName = frName;
    }

    @Override
    public String toString() {
        return "AllFrBalanceTrayReport [tranDetailId=" + tranDetailId + ", tranId=" + tranId + ", frId=" + frId
                + ", balanceBig=" + balanceBig + ", balanceSmall=" + balanceSmall + ", balanceLead=" + balanceLead
                + ", balanceExtra=" + balanceExtra + ", frName=" + frName + "]";
    }
}
