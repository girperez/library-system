/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package database;

import java.sql.Connection;
import java.sql.DriverManager;

/**
 *
 * @author Gerryl
 */
public class DatabaseConnect {
    private String driver;
    private String url;
    private String username;
    private String password;
    private Connection conn;
    
    public DatabaseConnect(){
        driver = "com.mysql.jdbc.Driver";
        url = "jdbc:mysql://localhost:3306/corp_library_db";
        username = "root";
        password = "";
    }
    
    protected Connection getConnection(){
        try {
            Class.forName(driver);
            
            conn = DriverManager.getConnection(url, username, password);
            return conn;
        } catch (Exception ex) {
            System.out.println("Connection Failed: "+ ex);
        }
        
        return null;
    }
    
    protected void closeConnection(){
        try {
            conn.close();
        } catch (Exception ex) {
            System.out.println("Closing connection failed: "+ ex);
        }
    }
}
