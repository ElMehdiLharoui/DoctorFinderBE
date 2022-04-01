package com.fstm.coredumped.smartwalkabilty.core.server;

import com.fstm.coredumped.smartwalkabilty.common.controller.PerimetreReq;
import com.fstm.coredumped.smartwalkabilty.common.controller.Request;
import com.fstm.coredumped.smartwalkabilty.common.controller.ShortestPathReq;
import com.fstm.coredumped.smartwalkabilty.core.geofencing.model.bo.Geofencing;
import com.fstm.coredumped.smartwalkabilty.core.routing.model.bo.Routage;
import com.fstm.coredumped.smartwalkabilty.web.Model.bo.Annonce;
import com.fstm.coredumped.smartwalkabilty.web.Model.bo.Site;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;

public class ClientHandler implements Runnable{
    private Socket clientSocket;
    private Routage routage;
    LocalDateTime d1;
    LocalDateTime d2;

    public ClientHandler(Socket s){
        this.clientSocket = s;
    }

    @Override
    public void run() {
        try {
            ObjectOutputStream oos = new ObjectOutputStream(clientSocket.getOutputStream());
            ObjectInputStream ois = new ObjectInputStream(clientSocket.getInputStream());

            Object req = ois.readObject();


            d1 = LocalDateTime.now();

            if(req instanceof ShortestPathReq){
                System.out.println("["+d1+"] starting routing ...");
                routage = new Routage((ShortestPathReq) req);
                routage.calculerChemins();
                oos.writeObject(routage.getChemins());
            }
            else if(req instanceof PerimetreReq){
                System.out.println("["+d1+"] user request announces in Radius: starting geofencing ...");
                PerimetreReq req1 = (PerimetreReq) req;
                // handle the case where the user requested juste the available announces
                // in a given Radius
                List<Site> list = Geofencing.findAllAnnoncesByRadius(req1.getActualPoint(), req1.getPerimetre(),req1.getCategorie());
                oos.writeObject(list);
            }

            d2 = LocalDateTime.now();
            long d = Duration.between(d1, d2).toMillis();

            System.out.println(" ==== Work has finished ===");
            System.out.println("it tooks: "+ d + " millis");
            System.out.println("in seconds: "+ TimeUnit.MICROSECONDS.toSeconds(d) + " seconds");

            oos.flush();
        } catch (IOException e) {
            System.out.println("IO Problems: "+e.getMessage());
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            System.out.println("Class not found "+ e.getMessage());
        }
    }
}
