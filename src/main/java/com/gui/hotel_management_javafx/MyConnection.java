package com.gui.hotel_management_javafx;


import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class MyConnection {

    //Connection to the SQlite database

    private final String url;

    public MyConnection() {
        this.url = "jdbc:sqlite:HotelDatabase.db";
    }

    public Connection createConnection() throws SQLException {
        return DriverManager.getConnection(this.url);
    }

}

