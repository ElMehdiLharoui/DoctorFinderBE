package com.fstm.coredumped.smartwalkabilty.core.server;

import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.StreamCorruptedException;
import java.net.Socket;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.concurrent.TimeUnit;

public class ClientHandler implements Runnable {
    private final Socket clientSocket;
    LocalDateTime d1;
    LocalDateTime d2;

    public ClientHandler(Socket s) {
        this.clientSocket = s;
    }

    @Override
    public void run() {
        try (
                ObjectOutputStream oos = new ObjectOutputStream(clientSocket.getOutputStream());
                ObjectInputStream ois = new ObjectInputStream(clientSocket.getInputStream())
        ) {
            RequestHandler requestHandler = new RequestHandler();

            Object req = ois.readObject();
            d1 = LocalDateTime.now();
            System.out.println("[" + d1 + "] starting treating " + req.getClass() + " ...");


            Object response = requestHandler.handleRequest(req);  // Delegate handling
            oos.writeObject(response);

            d2 = LocalDateTime.now();
            long d = Duration.between(d1, d2).toMillis();

            System.out.println(" ==== Work has finished ===");
            System.out.println("it tooks: " + d + " millis");
            System.out.println("in seconds: " + TimeUnit.MICROSECONDS.toSeconds(d) + " seconds");

            oos.flush();
        } catch (StreamCorruptedException e) {
            System.err.println("Invalid stream header, ignoring: " + e.getMessage());
        } catch (Exception e) {
            System.out.println("Error " + e.getMessage());
            e.printStackTrace();
        }
    }
}
