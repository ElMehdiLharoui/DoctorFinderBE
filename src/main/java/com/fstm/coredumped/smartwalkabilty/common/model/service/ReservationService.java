package com.fstm.coredumped.smartwalkabilty.common.model.service;

import com.fstm.coredumped.smartwalkabilty.common.controller.ReserveRequest;
import com.fstm.coredumped.smartwalkabilty.common.utils.TimeCalcules;
import com.fstm.coredumped.smartwalkabilty.common.model.bo.Reservation;
import com.fstm.coredumped.smartwalkabilty.common.model.dao.DAOReservation;
import com.fstm.coredumped.smartwalkabilty.web.Controller.DTOS.GetReservationsBySiteDTO;
import com.fstm.coredumped.smartwalkabilty.web.Controller.DTOS.NumberAndTimeDTO;
import com.fstm.coredumped.smartwalkabilty.web.Controller.DTOS.ResepenseDTO;
import com.fstm.coredumped.smartwalkabilty.web.Controller.websocket.SiteSimpleDataWebSocket;
import com.fstm.coredumped.smartwalkabilty.web.Controller.websocket.SiteTimeAvailableWebSocket;
import com.fstm.coredumped.smartwalkabilty.web.Model.Service.JWTgv;
import com.fstm.coredumped.smartwalkabilty.web.Model.bo.Site;
import com.fstm.coredumped.smartwalkabilty.web.Model.dao.DAOSite;
import com.fstm.coredumped.smartwalkabilty.web.Model.dao.ReservationDAO;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.sql.Date;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class ReservationService {

    private final DAOReservation daoReservation;
    static final Gson gson = new GsonBuilder().setDateFormat("MMM dd, yyyy HH:mm:ss").create();

    public ReservationService() {
        this.daoReservation = new DAOReservation();
    }

    public ResepenseDTO CreateReservation(ReserveRequest obj){
        Reservation reservation = daoReservation.getReservationBySiteAndCINAndDate(obj.getIdSite(),obj.getCin(),new Date(obj.getDate().getTime()));

        if(reservation != null){

           return  fillObject(obj,"Error: you already have a reservation at "+ reservation.getTempDeReservation());
        }
        int numberInLine = daoReservation.countReservationsByDateAndSite(new Date(obj.getDate().getTime()),obj.getIdSite()) + 1;

        reservation = createReservationFromRequest(obj);
        reservation.setNumberInLine(numberInLine);

        boolean created = daoReservation.Create(reservation);
        if(created)
        {
            new Thread(()->{
                SiteSimpleDataWebSocket.prodCastChange(obj.getIdSite());
                SiteTimeAvailableWebSocket.prodCastChange(obj.getIdSite(),obj.getDate());
            }).start();


            return fillObject(obj,reservation.getTempDeReservation());
        }

        return fillObject(obj,"Error: Unknown Error");
    }
    private ResepenseDTO fillObject(ReserveRequest obj,String temp) {
        ResepenseDTO reservation = new ResepenseDTO();
        reservation.setCreatedAt(new java.util.Date()); // Set la date actuelle
        reservation.setCIN(obj.getCin());
        reservation.setlName(obj.getLName());
        reservation.setfName(obj.getfName());
        reservation.setDate(obj.getDate());
        if (reservation.getSite() != null) {
            reservation.getSite().setId(obj.getIdSite());
        } else {
            // Créez un nouvel objet Site et définissez son ID
            Site site = new Site();
            site.setId(obj.getIdSite());
            reservation.setSite(site);
        }
        reservation.setTelephone_number(obj.getPhone());
         reservation.setTempDeReservation(temp);
        return reservation;
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
        reservation.setTempDeReservation(obj.getSelectedTime());
        return reservation;
    }

    private String calculateReservationTime(int site,int countReservations) {
        Site siteObject = DAOSite.getDaoSite().findById(site);
        int Result = countReservations * siteObject.getDureeviste();
        int debut = TimeCalcules.stringHourToMinutes(siteObject.getHeuredebut());
        int tempReservation = Result + debut;
        if (tempReservation <= TimeCalcules.stringHourToMinutes(siteObject.getHeurefin())) {
            return TimeCalcules.minutesToStringHour(tempReservation-siteObject.getDureeviste());
        }
        return "Error: The day is fully reserved";
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

    public NumberAndTimeDTO GetNumberInQueueAndTime(int idSite)
    {
        NumberAndTimeDTO DTO = new NumberAndTimeDTO();
        try {
            Site siteObject = DAOSite.getDaoSite().findById(idSite);
            Collection <Reservation> reservations = ReservationDAO.getDaoReservation().getReservationBySiteAndDate(idSite,new Date(new java.util.Date().getTime()));
            siteObject.setReservations(reservations);

            List<String> timesAvailable = siteObject.calculateAvailableTimesToReserveInStringHours(new java.util.Date());

            if(timesAvailable.size() == 0) DTO.setTempdereservation("Fully Booked");
            else DTO.setTempdereservation(timesAvailable.get(0));
            DTO.setNumberinline(reservations.size());
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        return DTO;
    }

    public List<String> GetAvailableTimes(int idSite, java.util.Date date)
    {
        try {
            Site siteObject = DAOSite.getDaoSite().findById(idSite);
            Collection <Reservation> reservations = ReservationDAO.getDaoReservation().getReservationBySiteAndDate(idSite,new Date(date.getTime()));
            siteObject.setReservations(reservations);

            return siteObject.calculateAvailableTimesToReserveInStringHours(date);
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        return new ArrayList<>();
    }
}
