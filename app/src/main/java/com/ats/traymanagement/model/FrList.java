package com.ats.traymanagement.model;

import java.util.List;

public class FrList {
    private List<FrIdNamesList> frIdNamesList = null;
    private Info1 info;

    public List<FrIdNamesList> getFrIdNamesList() {
        return frIdNamesList;
    }

    public void setFrIdNamesList(List<FrIdNamesList> frIdNamesList) {
        this.frIdNamesList = frIdNamesList;
    }

    public Info1 getInfo() {
        return info;
    }

    public void setInfo(Info1 info) {
        this.info = info;
    }

    @Override
    public String toString() {
        return "FrList{" +
                "frIdNamesList=" + frIdNamesList +
                ", info=" + info +
                '}';
    }
}
