package com.fstm.coredumped.smartwalkabilty.common.controller;

import java.io.Serializable;

public abstract class BaseRequest implements Serializable {
    private String type = getClass().getSimpleName();

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}
