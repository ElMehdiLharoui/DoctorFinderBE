package com.fstm.coredumped.smartwalkabilty.web.Controller.DTOS;

import com.fstm.coredumped.smartwalkabilty.common.model.bo.Reservation;
import com.fstm.coredumped.smartwalkabilty.web.Model.bo.Annonce;
import com.fstm.coredumped.smartwalkabilty.web.Model.bo.Image;
import com.fstm.coredumped.smartwalkabilty.web.Model.bo.Site;
import com.fstm.coredumped.smartwalkabilty.web.Model.dao.DAOCategorie;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class GetReservationsBySiteDTO {
    String Token;
    private Date date;
    private int site;
    private int id;
    private Float montant;
    private String Status;
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Float getMontant() {
        return montant;
    }

    public void setMontant(Float montant) {
        this.montant = montant;
    }

    public String getStatus() {
        return Status;
    }

    public void setStatus(String status) {
        Status = status;
    }
/*
    public ReservationDTO(int id, Date date, String tempDeReservation, String CIN, String lName, String fName, String status, float montant, int numberInLine, int site) {
        this.id = id;
        this.date = date;
        this.tempDeReservation = tempDeReservation;
        this.CIN = CIN;
        this.lName = lName;
        this.fName = fName;
        this.status = status;
        this.montant = montant;
        this.numberInLine = numberInLine;
        this.site = site;
    }*/
   /* public void FillAnnonceFromBlob(Reservation reservation) throws ParseException {
        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        Site siteO = new Site();
        siteO.setId(site);
        reservation.setSite(siteO);
        reservation.setDate(new java.util.Date(String.valueOf(date)).getTime());

    }
*/

    public String getToken() {
        return Token;
    }

    public void setToken(String token) {
        Token = token;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public int getSite() {
        return site;
    }

    public void setSite(int site) {
        this.site = site;
    }

    public boolean VerifyAttirbutMontant(){
        if(date == null || site == 0 ||montant == null   ){
            return false;
        }
        return true;
    }

    public void FillReservationMontantFromBlob(Reservation reservation) throws ParseException {
        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");

        reservation.setId(id);
        reservation.setMontant(montant);

    }
}
