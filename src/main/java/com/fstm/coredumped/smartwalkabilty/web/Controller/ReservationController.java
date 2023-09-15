package com.fstm.coredumped.smartwalkabilty.web.Controller;

import com.fstm.coredumped.smartwalkabilty.common.controller.ReserveRequest;
import com.fstm.coredumped.smartwalkabilty.web.Controller.DTOS.GetReservationsBySiteDTO;
import com.fstm.coredumped.smartwalkabilty.common.model.service.ReservationService;
import com.fstm.coredumped.smartwalkabilty.web.Controller.DTOS.ResepenseDTO;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.Reader;


public class ReservationController extends HttpServlet {
    ReservationService reservationService = new ReservationService();
    static final Gson gson = new GsonBuilder().setDateFormat("MMM dd, yyyy HH:mm:ss").create();

    @Override
    protected void doOptions(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setContentType("application/json");

        try {
            GetReservationsBySiteDTO DTO= gson.fromJson(request.getReader(), GetReservationsBySiteDTO.class);
            if(DTO == null) {
                super.doOptions(request, response);
            }
            else
            {
                String s1=reservationService.getServ(DTO);
                if(s1.contains("invalid"))response.setStatus(400);
                response.getWriter().println(s1);
                System.out.println(s1);
            }
        }
        catch (Exception e)
        {
            e.printStackTrace();
            response.setStatus(400);
            response.getWriter().println("{ \"mes\":\"  Exception Happened \" }");
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        resp.setContentType("application/json");
        ResepenseDTO s1=new ResepenseDTO();
        try {
            ReserveRequest reserveRequest = gson.fromJson(req.getReader(), ReserveRequest.class);
            if(!reserveRequest.verifyEverythingIsNotNull()){
                s1.setTempDeReservation( "Error: missing information");
            }else {
                s1 = reservationService.CreateReservation(reserveRequest);
            }
            if(s1.getTempDeReservation().contains("Error"))resp.setStatus(400);
            String jsonResponse = gson.toJson(s1);
            resp.getWriter().println(jsonResponse);
        }catch (Exception e){
            e.printStackTrace();
            resp.setStatus(400);
            resp.getWriter().println("{ \"mes\":\"  Exception Happened \" }");
        }
    }

    @Override
    protected void doPut(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException
    {
        resp.setContentType("application/json");
        String s1 = null;
        try {
            GetReservationsBySiteDTO Blob = gson.fromJson(req.getReader(), GetReservationsBySiteDTO.class);
            if(Blob.getStatus()!=null)
            {
               s1= reservationService.updateReservationStatut(Blob);
            }
            else{
                s1 = reservationService.updateReservationMontant(Blob);
            }
            if(s1.contains("invalid"))resp.setStatus(400);
            resp.getWriter().println(s1);
        }catch (Exception e){
            System.err.println(e);
            resp.setStatus(400);
            resp.getWriter().println("{ \"mes\":\"  Exception Happened \" }");
        }
    }



    }


