package com.fstm.coredumped.smartwalkabilty.common.model.service.response;

import java.io.Serializable;

public class BasicResponse<T> implements Serializable {
    private T data;
    private String message;
    private boolean success;

    public BasicResponse() {
    }

    public BasicResponse(T data, String message, boolean success) {
        this.data = data;
        this.message = message;
        this.success = success;
    }

    public static <T> BasicResponse<T> success(T data) {
        return new BasicResponse<>(data, "", true);
    }

    public static <T> BasicResponse<T> fail(String message) {
        return new BasicResponse<>(null, message, false);
    }
}
