package com.ats.traymanagement.model;

public class LoginModel {

    private Driver driver;
    private User user;
    private Boolean error;
    private String msg;

    public Driver getDriver() {
        return driver;
    }

    public void setDriver(Driver driver) {
        this.driver = driver;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Boolean getError() {
        return error;
    }

    public void setError(Boolean error) {
        this.error = error;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    @Override
    public String toString() {
        return "LoginModel{" +
                "driver=" + driver +
                ", user=" + user +
                ", error=" + error +
                ", msg='" + msg + '\'' +
                '}';
    }

}
