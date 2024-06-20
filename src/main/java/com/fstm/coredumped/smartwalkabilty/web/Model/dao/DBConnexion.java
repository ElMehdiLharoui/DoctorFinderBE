package com.fstm.coredumped.smartwalkabilty.web.Model.dao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DBConnexion {
    private DBConnexion() {
    }

    private static Connection conn = null;

    public static Connection getCon() {
        try {
            if (conn == null || conn.isClosed()) conn = createConn();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return conn;
    }

    private static Connection createConn() {
        String DB_SERVER = System.getenv("DB_SMART_URL");
        String DB_USER = System.getenv("DB_USER");
        String DB_PASSWORD = System.getenv("DB_PASSWORD");
        String DB_SMART_URL;

        if (DB_SERVER == null || DB_SERVER.isEmpty()){
            DB_SMART_URL = "jdbc:postgresql://localhost:5432/DB_SMARTWALK";
            DB_USER = "hp";
            DB_PASSWORD = "123";
        }
        else
            DB_SMART_URL = "jdbc:postgresql://" + DB_SERVER;

        try {
            DriverManager.registerDriver(new org.postgresql.Driver());
            return DriverManager.getConnection(DB_SMART_URL, DB_USER, DB_PASSWORD);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}
