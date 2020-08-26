package com.ats.traymanagement.model;

public class FrIdNamesList {

    private Integer frId;
    private String frName;

    public FrIdNamesList() {
    }

    public FrIdNamesList(Integer frId, String frName) {
        this.frId = frId;
        this.frName = frName;
    }

    public Integer getFrId() {
        return frId;
    }

    public void setFrId(Integer frId) {
        this.frId = frId;
    }

    public String getFrName() {
        return frName;
    }

    public void setFrName(String frName) {
        this.frName = frName;
    }


    @Override
    public String toString() {
        return "FrIdNamesList{" +
                "frId=" + frId +
                ", frName='" + frName + '\'' +
                '}';
    }

}
