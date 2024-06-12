package com.fstm.coredumped.smartwalkabilty.common.controller;

import com.fstm.coredumped.smartwalkabilty.common.model.service.ReservationService;
import com.fstm.coredumped.smartwalkabilty.web.Controller.DTOS.ResponseDTO;

public class ReservationController {
    ReservationService reservationService;

    public ReservationController() {
        this.reservationService = new ReservationService();
    }

    public ResponseDTO handle(ReserveRequest reserveRequest) {
        ResponseDTO responseDTO = new ResponseDTO();

        if (!reserveRequest.verifyEverythingIsNotNull()) {
            responseDTO.setTempDeReservation("Error: missing information");
            return responseDTO;
        }
        return reservationService.CreateReservation(reserveRequest);
    }

}
