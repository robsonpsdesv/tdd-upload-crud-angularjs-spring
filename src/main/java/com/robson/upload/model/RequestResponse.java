package com.robson.upload.model;

public class RequestResponse {

    private boolean success;
    private String mensage;

    public RequestResponse(boolean success, String mensage) {
        this.success = success;
        this.mensage = mensage;
    }

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public String getMensage() {
        return mensage;
    }

    public void setMensage(String mensage) {
        this.mensage = mensage;
    }

}
