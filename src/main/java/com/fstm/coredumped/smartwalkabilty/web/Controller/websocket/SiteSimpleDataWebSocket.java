package com.fstm.coredumped.smartwalkabilty.web.Controller.websocket;

import com.fstm.coredumped.smartwalkabilty.common.model.service.ReservationService;
import com.fstm.coredumped.smartwalkabilty.web.Controller.DTOS.NumberAndTimeDTO;

import javax.websocket.OnClose;
import javax.websocket.OnMessage;
import javax.websocket.OnOpen;
import javax.websocket.Session;
import javax.websocket.server.ServerEndpoint;
import java.io.IOException;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@ServerEndpoint("/webSocket-site")
public class SiteSimpleDataWebSocket
{
    private static final Hashtable<Session,Integer> sessionSites = new Hashtable<>();
    private static final ReservationService reservationService = new ReservationService();
    @OnOpen
    public void onOpen(Session session) {
        sessionSites.put(session,0);
        System.out.println("WebSocket connection opened");
    }

    @OnMessage
    public void onMessage(String message, Session session) {
        try {
            int idSite = Integer.parseInt(message);
            sessionSites.put(session,idSite);
            NumberAndTimeDTO numberAndTimeDTO = reservationService.GetNumberInQueueAndTime(idSite);

            SendNumberAndTimeToClient(session, numberAndTimeDTO);
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }


    private static void SendNumberAndTimeToClient(Session session, NumberAndTimeDTO numberAndTimeDTO) throws IOException {
        int queueNumber = numberAndTimeDTO.getNumberinline();
        String timeAvailable = numberAndTimeDTO.getTempdereservation();

        String returnMessage = "NumberInQueue~"+queueNumber;
        session.getBasicRemote().sendText(returnMessage);
        returnMessage = "TimeAvailable~"+timeAvailable;
        session.getBasicRemote().sendText(returnMessage);
    }

    @OnClose
    public void onClose(Session session)
    {
        sessionSites.remove(session);
    }

    public static void prodCastChange(int idSite)
    {
        List<Session> sessions = sessionSites.entrySet().stream().filter(x-> x.getValue() == idSite).map(Map.Entry::getKey).collect(Collectors.toList());
        if(sessions.size() != 0){
            NumberAndTimeDTO numberAndTimeDTO = reservationService.GetNumberInQueueAndTime(idSite);
            for (Session session : sessions) {
                try {
                    SendNumberAndTimeToClient(session,numberAndTimeDTO);
                }catch (Exception ex){
                    ex.printStackTrace();
                }
            }
        }

    }
}
