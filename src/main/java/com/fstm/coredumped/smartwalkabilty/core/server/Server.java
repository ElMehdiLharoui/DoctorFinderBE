package com.fstm.coredumped.smartwalkabilty.core.server;

import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;

public class Server {
    public static void main(String[] args) {
        try (ServerSocket server = new ServerSocket(1337)) {

            while (true) {
                System.out.println("Server is Lestning " + InetAddress.getLocalHost().getHostAddress() + " port : 1337");


                Socket client = server.accept();
                System.out.println("New client connected: " + client.getInetAddress().getHostAddress());
                ClientHandler clientHandler = new ClientHandler(client);

                // launching a new client handler
                new Thread(clientHandler).start();
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
