package com.fstm.coredumped.smartwalkabilty.web.Controller.websocket;

import com.fstm.coredumped.smartwalkabilty.common.model.service.ReservationService;
import com.fstm.coredumped.smartwalkabilty.common.utils.DateHandling;
import com.fstm.coredumped.smartwalkabilty.web.Controller.DTOS.NumberAndTimeDTO;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import javax.websocket.OnClose;
import javax.websocket.OnMessage;
import javax.websocket.OnOpen;
import javax.websocket.Session;
import javax.websocket.server.ServerEndpoint;
import java.io.IOException;
import java.util.Date;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@ServerEndpoint("/ws-siteAvailableTimes")
public class SiteTimeAvailableWebSocket
{
    class DateSiteDTO
    {
        Date date;
        int siteId;
    }
    static final Gson gson = new GsonBuilder().setDateFormat("MMM dd, yyyy HH:mm:ss").create();

    private static final Hashtable<Session,DateSiteDTO> sessionSites = new Hashtable<>();
    private static final ReservationService reservationService = new ReservationService();
    @OnOpen
    public void onOpen(Session session) {
        sessionSites.put(session,new DateSiteDTO());
        System.out.println("WebSocket connection opened");
    }

    @OnMessage
    public void onMessage(String message, Session session) {
        try {
            DateSiteDTO dateSiteDTO = gson.fromJson(message,DateSiteDTO.class);
            sessionSites.put(session,dateSiteDTO);
            SendAvailableTimesToClient(session, dateSiteDTO);
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }

    private void SendAvailableTimesToClient(Session session, DateSiteDTO data) throws IOException {
        List<String> availableTimes = reservationService.GetAvailableTimes(data.siteId,data.date);

        String json = gson.toJson(availableTimes);
        session.getAsyncRemote().sendText(json);
    }

    @OnClose
    public void onClose(Session session)
    {
        sessionSites.remove(session);
    }

    public static void prodCastChange(int idSite,Date date)
    {
        List<Session> sessions = sessionSites.entrySet().stream().filter(x-> x.getValue().siteId == idSite && DateHandling.equalsByDateOnly(x.getValue().date,date)).map(Map.Entry::getKey).collect(Collectors.toList());

        if(sessions.size() != 0){
            List<String> availableTimes = reservationService.GetAvailableTimes(idSite,date);

            String message = gson.toJson(availableTimes);
            for (Session session : sessions) {
                session.getAsyncRemote().sendText(message);
            }
        }

    }
}
