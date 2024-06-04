package com.fstm.coredumped.smartwalkabilty.core.server;

import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;

public class Server {
    public static void main(String[] args) {
        String portEnv = System.getenv("PORT");
        int port = Integer.parseInt(portEnv);
        try (ServerSocket server = new ServerSocket(port)) {

            while (true) {
                System.out.printf("Server is Listening on port : %s%n", port);

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
