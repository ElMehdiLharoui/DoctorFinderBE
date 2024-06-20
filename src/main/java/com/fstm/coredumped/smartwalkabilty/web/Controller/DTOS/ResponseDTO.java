package com.fstm.coredumped.smartwalkabilty.web.Controller.DTOS;

import com.fstm.coredumped.smartwalkabilty.web.Model.bo.Site;

import java.io.Serializable;
import java.util.Date;

public class ResponseDTO implements Serializable {
    private int id;
    private Date date;
    private String tempDeReservation;
    private Date createdAt;
    private String CIN;
    private String lName;
    private String fName;
    private int numberInLine;
    private Site site;

    public String getTelephone_number() {
        return telephone_number;
    }

    public void setTelephone_number(String telephone_number) {
        this.telephone_number = telephone_number;
    }

    private String telephone_number;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public String getTempDeReservation() {
        return tempDeReservation;
    }

    public void setTempDeReservation(String tempDeReservation) {
        this.tempDeReservation = tempDeReservation;
    }

    public Date getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Date createdAt) {
        this.createdAt = createdAt;
    }

    public String getCIN() {
        return CIN;
    }

    public void setCIN(String CIN) {
        this.CIN = CIN;
    }

    public String getlName() {
        return lName;
    }

    public void setlName(String lName) {
        this.lName = lName;
    }

    public String getfName() {
        return fName;
    }

    public void setfName(String fName) {
        this.fName = fName;
    }

    public int getNumberInLine() {
        return numberInLine;
    }

    public void setNumberInLine(int numberInLine) {
        this.numberInLine = numberInLine;
    }


    public Site getSite() {
        return site;
    }

    public void setSite(Site site) {
        this.site = site;
    }
}
