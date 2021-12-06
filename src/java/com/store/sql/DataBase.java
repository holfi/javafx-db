package com.store.sql;

import java.sql.Connection;
import java.sql.DriverManager;

public class DataBase {

    public static Connection getConnection() {
        String url = "jdbc:postgresql://localhost:5432/xsd_store";
        String user = "postgres";
        String password = "1";

        try {
            return DriverManager.getConnection(url, user, password);
        }
        catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }

}
