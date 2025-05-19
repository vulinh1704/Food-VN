package com.food_vn.model.api_responses;

import com.food_vn.lib.app_const.API_RESPONSE;

public class ApiResponse<T> {
    private boolean success = API_RESPONSE.API_SUCCESS;
    private String message;
    private T data;
    private int status;

    public ApiResponse(boolean success, String message, T data, int status) {
        this.success = success;
        this.message = message;
        this.data = data;
        this.status = status;
    }

    public ApiResponse(boolean success, String message, int status) {
        this.success = success;
        this.message = message;
        this.status = status;
    }

    public ApiResponse(String message, int status) {
        this.message = message;
        this.status = status;
    }

    public ApiResponse(String message, T data, int status) {
        this.message = message;
        this.data = data;
        this.status = status;
    }

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }
}