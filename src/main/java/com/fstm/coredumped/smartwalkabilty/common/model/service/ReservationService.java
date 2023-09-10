package com.fstm.coredumped.smartwalkabilty.common.model.service;

import com.fstm.coredumped.smartwalkabilty.common.controller.ReserveRequest;
import com.fstm.coredumped.smartwalkabilty.common.model.bo.Reservation;
import com.fstm.coredumped.smartwalkabilty.common.model.dao.DAOReservation;
import com.fstm.coredumped.smartwalkabilty.web.Controller.DTOS.GetReservationsBySiteDTO;
import com.fstm.coredumped.smartwalkabilty.web.Controller.DTOS.MobileDTO;
import com.fstm.coredumped.smartwalkabilty.web.Model.Service.JWTgv;
import com.fstm.coredumped.smartwalkabilty.web.Model.bo.Site;
import com.fstm.coredumped.smartwalkabilty.web.Model.dao.DAOSite;
import com.fstm.coredumped.smartwalkabilty.web.Model.dao.ReservationDAO;
import com.google.gson.Gson;

import java.sql.Date;
import java.util.Collection;

public class ReservationService {

    private final DAOReservation daoReservation;

    public ReservationService() {
        this.daoReservation = new DAOReservation();
    }

    public String CreateReservation(ReserveRequest obj){
        Reservation reservation = daoReservation.getReservationBySiteAndCINAndDate(obj.getIdSite(),obj.getCin(),new Date(obj.getDate().getTime()));
        if(reservation != null){
            return "Error: you already have a reservation at "+ reservation.getTempDeReservation();
        }
        int numberInLine = daoReservation.countReservationsByDateAndSite(new Date(obj.getDate().getTime()),obj.getIdSite()) + 1;
        String reservationTime = calculateReservationTime( obj.getIdSite(),numberInLine);

        if(reservationTime.contains("Error"))return reservationTime;

        reservation = createReservationFromRequest(obj);
        reservation.setTempDeReservation(reservationTime);
        reservation.setNumberInLine(numberInLine);

        boolean created = daoReservation.Create(reservation);
        if(created) return reservationTime;
        return "Error: Unknown Error";
    }
    public Reservation createReservationFromRequest(ReserveRequest obj){
        Reservation reservation = new Reservation();
        reservation.setCreatedAt(new java.util.Date()); // Set la date actuelle
        reservation.setCIN(obj.getCin());
        reservation.setlName(obj.getLName());
        reservation.setfName(obj.getfName());
        reservation.setDate(obj.getDate());
        reservation.setSite(new Site());
        reservation.getSite().setId(obj.getIdSite());
        reservation.setStatus("Reserved");
        reservation.setMontant(0.0F);
        reservation.setTelephone_number(obj.getPhone());

        return reservation;
    }

    private String calculateReservationTime(int site,int countReservations) {
        Site siteObject = DAOSite.getDaoSite().findById(site);
        int Result = countReservations * siteObject.getDureeviste();
        int debut = stringHourToMinutes(siteObject.getHeuredebut());
        int tempReservation = Result + debut;
        if (tempReservation <= stringHourToMinutes(siteObject.getHeurefin())) {
            return minutesToStringHour(tempReservation-siteObject.getDureeviste());
        }
        return "Error: The day is fully reserved";
    }

    private int stringHourToMinutes(String heureDebut) {
        String[] parts = heureDebut.split(":");
        return Integer.parseInt(parts[0]) * 60 + Integer.parseInt(parts[1]);
    }

    private String minutesToStringHour(int minutes)
    {
        int hours = minutes / 60;
        minutes = minutes - hours * 60;
        return new StringBuilder().append(hours).append(":").append(minutes).toString();
    }

    JWTgv jwTgv=new JWTgv();
    public String getServ(GetReservationsBySiteDTO blob){
        Gson s=new Gson();
        if(jwTgv.verifyToken(blob.getToken())) {
            System.out.println(blob.getSite()!=0 );
            if (blob.getSite()!=0 && blob.getDate()!=null) {
                //to be replaced by DTO
                Collection<Reservation> reservation =  ReservationDAO.getDaoReservation().getReservationBySiteAndDate(blob.getSite(),new Date(blob.getDate().getTime()));
                return s.toJson(reservation);
            }
            else
            {
                return s.toJson(ReservationDAO.getDaoReservation().Retrieve());
            }
        }else {
            return  "{ \"mes\":\" invalid Token  \" }";
        }
    }
    public String updateReservationStatut(GetReservationsBySiteDTO blob){
        if(!jwTgv.verifyToken(blob.getToken()))return "{ \"mes\":\" invalid Token  \" }";
        if(!ReservationDAO.getDaoReservation().checkExiste(blob.getId())){
            return "{ \"mes\":\" invalid : id doesn't exist \" }";
        }
        if (blob.getStatus()!=null) {
            Reservation reservation = new Reservation();
            try {
                reservation.setId(blob.getId());
                reservation.setStatus(blob.getStatus());
                ReservationDAO.getDaoReservation().update(reservation);
                return  "{ \"id\":\" " + reservation.getId() + " \" }";//return id if everything is okay
            } catch (Exception e) {
                System.err.println(e);
                return  "{ \"mes\":\" invalid info \" }";
            }
        } else return  "{ \"mes\":\" invalid info \" }";
    }

    public String updateReservationMontant(GetReservationsBySiteDTO blob){
        if(!jwTgv.verifyToken(blob.getToken()))return "{ \"mes\":\" invalid Token  \" }";
        if(!ReservationDAO.getDaoReservation().checkExiste(blob.getId())){
            return "{ \"mes\":\" invalid : id doesn't exist \" }";
        }
        if (blob.getMontant()!=null) {
            Reservation reservation = new Reservation();
            try {
                reservation.setId(blob.getId());
                reservation.setMontant(blob.getMontant());
                ReservationDAO.getDaoReservation().updateMontant(reservation);
                return  "{ \"id\":\" " + reservation.getId() + " \" }";//return id if everything is okay
            } catch (Exception e) {
                System.err.println(e);
                return  "{ \"mes\":\" invalid info \" }";
            }
        } else return  "{ \"mes\":\" invalid info \" }";
    }

    public void GetNumberInQueueAndTime(int idSite)
    {
        Gson s = new Gson();
        try {
            Collection <Reservation> reservation =  ReservationDAO.getDaoReservation().getReservationBySiteAndDate(idSite,new Date(new java.util.Date().getTime()));


            MobileDTO DTO = new MobileDTO();
            //DTO.setNumberinline(reservation.cl);

        }catch (Exception e){

        }


    }
}
