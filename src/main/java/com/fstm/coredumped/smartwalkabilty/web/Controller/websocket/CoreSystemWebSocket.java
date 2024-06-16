package com.fstm.coredumped.smartwalkabilty.web.Controller.websocket;


import com.fstm.coredumped.smartwalkabilty.common.controller.*;
import com.fstm.coredumped.smartwalkabilty.common.model.service.response.BasicResponse;
import com.fstm.coredumped.smartwalkabilty.core.server.RequestHandler;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import javax.websocket.*;
import javax.websocket.server.ServerEndpoint;
import java.io.IOException;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.concurrent.TimeUnit;

@ServerEndpoint("/ws-core")
public class CoreSystemWebSocket {
    private final Gson gson = new Gson();

    @OnOpen
    public void onOpen(Session session) {
        System.out.println("WebSocket connection opened: " + session.getId());
    }

    @OnMessage
    public void onMessage(String message, Session session) throws IOException {
        try {
            // Parse the JSON message
            JsonObject jsonObject = JsonParser.parseString(message).getAsJsonObject();
            String type = jsonObject.get("type").getAsString();
            Object req = parseRequest(jsonObject, type);

            if (req == null) {
                session.getBasicRemote().sendText(gson.toJson(BasicResponse.fail("Invalid request type.")));
                return;
            }

            LocalDateTime d1 = LocalDateTime.now();

            Object response = new RequestHandler().handleRequest(req);
            String responseMessage = gson.toJson(response);

            session.getBasicRemote().sendText(responseMessage);

            LocalDateTime d2 = LocalDateTime.now();
            long durationMillis = Duration.between(d1, d2).toMillis();

            System.out.println(" ==== Work has finished ===");
            System.out.println("It took: " + durationMillis + " millis");
            System.out.println("In seconds: " + TimeUnit.MILLISECONDS.toSeconds(durationMillis) + " seconds");

        } catch (Exception e) {
            e.printStackTrace();
            session.getBasicRemote().sendText(gson.toJson(BasicResponse.fail(e.getMessage())));
        }
    }

    @OnClose
    public void onClose(Session session) {
        System.out.println("WebSocket connection closed: " + session.getId());
    }

    @OnError
    public void onError(Session session, Throwable throwable) {
        System.err.println("WebSocket error on session " + session.getId() + ": " + throwable.getMessage());
    }

    private Object parseRequest(JsonObject jsonObject, String type) {
        return switch (type) {
            case "DangerReq" -> gson.fromJson(jsonObject, DangerReq.class);
            case "ShortestPathReq" -> gson.fromJson(jsonObject, ShortestPathReq.class);
            case "ShortestPathWithAnnounces" -> gson.fromJson(jsonObject, ShortestPathWithAnnounces.class);
            case "RequestPerimetreAnnonce" -> gson.fromJson(jsonObject, RequestPerimetreAnnonce.class);
            case "DeclareDangerReq" -> gson.fromJson(jsonObject, DeclareDangerReq.class);
            case "ReserveRequest" -> gson.fromJson(jsonObject, ReserveRequest.class);
            case "CommentRequest" -> gson.fromJson(jsonObject, CommentRequest.class);
            case "GetCommentsRequest" -> gson.fromJson(jsonObject, GetCommentsRequest.class);
            default -> null;  // Unknown type
        };
    }

}
