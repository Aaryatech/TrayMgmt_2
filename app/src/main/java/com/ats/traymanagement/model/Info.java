package com.ats.traymanagement.model;

/**
 * Created by MAXADMIN on 17/2/2018.
 */

public class Info {

    private String message;
    private boolean isError;

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public boolean isError() {
        return isError;
    }

    public void setError(boolean error) {
        isError = error;
    }

    @Override
    public String toString() {
        return "Info{" +
                "message='" + message + '\'' +
                ", isError=" + isError +
                '}';
    }
}
