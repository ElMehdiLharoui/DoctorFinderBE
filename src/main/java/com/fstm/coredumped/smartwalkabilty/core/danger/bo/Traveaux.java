package com.fstm.coredumped.smartwalkabilty.core.danger.bo;

public class Traveaux extends Danger
{
    @Override
    public double CalculateRisk() {
        return degree*2;
    }
    @Override
    public String toString() {
        return "Traveaux";
    }
}
