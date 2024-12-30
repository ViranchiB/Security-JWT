package com.StudySecurity.Exception;

import org.springframework.http.HttpStatus;

public class APIException extends RuntimeException {

    private HttpStatus status;
    private String msg;

    public HttpStatus getStatus() {
        return status;
    }

    public void setStatus(HttpStatus status) {
        this.status = status;
    }

    @Override
    public String getMessage() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public APIException() {
    }

    public APIException(HttpStatus status, String message) {
        this.status = status;
        this.msg = message;
    }

    public APIException(String message, HttpStatus status, String msg) {
        super(message);
        this.status = status;
        this.msg = msg;
    }
}
