package com.fstm.coredumped.smartwalkabilty.common.controller;

import com.fstm.coredumped.smartwalkabilty.common.controller.ReserveRequest;
import com.fstm.coredumped.smartwalkabilty.common.model.bo.Reservation;
import com.fstm.coredumped.smartwalkabilty.common.model.dao.DAOReservation;
import com.fstm.coredumped.smartwalkabilty.common.model.service.ReservationService;

public class Reservation_ctrl {
    ReservationService reservationService;
    public Reservation_ctrl() {
        this.reservationService =  new ReservationService();
    }
    public String ResevationCtrl(ReserveRequest reserveRequest)
    {
        if(!reserveRequest.verifyEverythingIsNotNull()){
           return "Error: missing information";
        }
       return reservationService.CreateReservation(reserveRequest);
    }

}
