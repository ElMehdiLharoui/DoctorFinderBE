package com.fstm.coredumped.smartwalkabilty.common.controller;

import com.fstm.coredumped.smartwalkabilty.common.model.bo.GeoPoint;

public abstract class RequestPerimetre extends Request {
    private final double perimetre;

    public RequestPerimetre(double perimetre, GeoPoint actualPoint) {
        super(actualPoint);
        this.perimetre = perimetre;
    }

    public double getPerimetre() {
        return perimetre;
    }
}
