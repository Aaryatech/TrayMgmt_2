package com.ats.traymanagement.model;

/**
 * Created by MAXADMIN on 26/2/2018.
 */

public class ErrorMessage {

    private boolean error;
    private String message;

    public String getMessage() {
        return message;
    }
    public void setMessage(String message) {
        this.message = message;
    }
    public boolean isError() {
        return error;
    }
    public void setError(boolean error) {
        this.error = error;
    }
    @Override
    public String toString() {
        return "info [error=" + error + ", message=" + message + "]";
    }
}
