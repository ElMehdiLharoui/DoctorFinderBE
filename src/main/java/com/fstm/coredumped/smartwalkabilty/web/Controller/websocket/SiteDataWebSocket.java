package com.fstm.coredumped.smartwalkabilty.web.Controller.websocket;

import javax.websocket.OnClose;
import javax.websocket.OnMessage;
import javax.websocket.OnOpen;
import javax.websocket.Session;
import javax.websocket.server.ServerEndpoint;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@ServerEndpoint("/webSocket-site")
public class SiteDataWebSocket
{
    private static final Hashtable<Session,Integer> sessionSites = new Hashtable<>();
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
            int queueNumber = 0;
            String timeAvailable = "";
            /*
                get here the number in Queue
                and timeAvailable (using the service with a single method that return an object DTO)
            * */
            String returnMessage = "NumberInQueue~"+queueNumber;
            session.getAsyncRemote().sendText(returnMessage);
            returnMessage = "TimeAvailable~"+timeAvailable;
            session.getAsyncRemote().sendText(returnMessage);
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }
    @OnClose
    public void onClose(Session session)
    {
        sessionSites.remove(session);
    }

    public static void prodCastChange(int idSite,String message)
    {
        List<Session> sessions = sessionSites.entrySet().stream().filter(x-> x.getValue() == idSite).map(Map.Entry::getKey).collect(Collectors.toList());

        for (Session session : sessions) {
            session.getAsyncRemote().sendText(message);
        }
    }
}
