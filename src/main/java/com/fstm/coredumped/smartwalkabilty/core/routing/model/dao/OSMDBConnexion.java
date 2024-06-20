package com.fstm.coredumped.smartwalkabilty.core.routing.model.dao;

import java.sql.Connection;
import java.sql.DriverManager;

public class OSMDBConnexion {
    private OSMDBConnexion() {
    }

    private static Connection connection = null;

    public static Connection getConnection() {
        String DB_SERVER = System.getenv("DB_OSM_URL");
        String DB_USER = System.getenv("DB_USER");
        String DB_PASSWORD = System.getenv("DB_PASSWORD");
        String DB_OSM_URL;
        if (DB_SERVER == null || DB_SERVER.isBlank()) {
            DB_OSM_URL = "jdbc:postgresql://hp:123@localhost:5432/osmoffdb";
            DB_USER = "hp";
            DB_PASSWORD = "123";
        } else
            DB_OSM_URL = "jdbc:postgresql://" + DB_SERVER;

        try {
            if (connection == null || connection.isClosed()) {
                DriverManager.registerDriver(new org.postgresql.Driver());
                connection = DriverManager.getConnection(DB_OSM_URL, DB_USER, DB_PASSWORD);
                System.out.println("DB OSM opened successfully");
            }
            return connection;
        } catch (Exception e) {
            System.out.println("Error in connection: " + e.getMessage());
            return null;
        }
    }
}
