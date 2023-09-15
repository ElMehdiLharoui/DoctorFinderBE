package com.fstm.coredumped.smartwalkabilty.common.controller;

import java.util.Date;

public class ReserveRequest implements IRequest
{

    private int idSite;
    private String cin;
    private String fName;
    private String LName;
    private Date date;
    private String Phone;
    private String selectedTime;

    public ReserveRequest(int idSite, String cin, String fName, String LName, Date date ,String Phone,String selectedtime) {
        this.idSite = idSite;
        this.cin = cin;
        this.fName = fName;
        this.LName = LName;
        this.date = date;
        this.Phone = Phone;
        this.selectedTime = selectedtime;
    }

    public ReserveRequest() {
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public int getIdSite() {
        return idSite;
    }

    public void setIdSite(int idSite) {
        this.idSite = idSite;
    }

    public String getCin() {
        return cin;
    }

    public void setCin(String cin) {
        this.cin = cin;
    }

    public String getfName() {
        return fName;
    }

    public void setfName(String fName) {
        this.fName = fName;
    }

    public String getLName() {
        return LName;
    }

    public void setLName(String LName) {
        this.LName = LName;
    }

    public String getPhone() {
        return Phone;
    }

    public void setPhone(String phone) {
        Phone = phone;
    }

    public boolean verifyEverythingIsNotNull(){
        return cin != null && LName != null && fName != null && idSite != 0 && date != null && Phone !=null;
    }

    public String getSelectedTime() {
        return selectedTime;
    }

    public void setSelectedTime(String selectedTime) {
        this.selectedTime = selectedTime;
    }
}