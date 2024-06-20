package com.fstm.coredumped.smartwalkabilty.common.controller;

import com.fstm.coredumped.smartwalkabilty.common.model.bo.GeoPoint;

public abstract class Request extends BaseRequest {
    private final GeoPoint actualPoint;

    protected Request(GeoPoint actualPoint) {
        this.actualPoint = actualPoint;
    }

    public GeoPoint getActualPoint() {
        return actualPoint;
    }
}
