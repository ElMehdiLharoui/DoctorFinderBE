package com.fstm.coredumped.smartwalkabilty.web.Controller;

import com.fstm.coredumped.smartwalkabilty.web.Controller.DTOS.GetReservationsBySiteDTO;
import com.fstm.coredumped.smartwalkabilty.common.model.service.ReservationService;

import com.fstm.coredumped.smartwalkabilty.web.Model.bo.blobs.AnnonceBlob;
import com.google.gson.Gson;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;


public class ReservationController extends HttpServlet {
    ReservationService reservationService = new ReservationService();

    @Override
    protected void doOptions(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setContentType("application/json");
        Gson s = new Gson();

        try {
            GetReservationsBySiteDTO DTO=s.fromJson(request.getReader(), GetReservationsBySiteDTO.class);
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
    protected void doPut(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException
    {
        resp.setContentType("application/json");
        String s1 = null;
        Gson s = new Gson();
        try {
            GetReservationsBySiteDTO Blob = s.fromJson(req.getReader(), GetReservationsBySiteDTO.class);
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


