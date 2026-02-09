/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package config;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DatabaseConnection {
    
    public static Connection getConnection() throws SQLException {
        try {
            // Load SQLite JDBC Driver
            Class.forName("org.sqlite.JDBC");
            
            // SQLite database file path
            String url = "jdbc:sqlite:carsapp.db";  // or full path: "C:/path/to/carsapp.db"
            
            return DriverManager.getConnection(url);
            
        } catch (ClassNotFoundException e) {
            throw new SQLException("SQLite JDBC Driver not found!", e);
        }
    }
}