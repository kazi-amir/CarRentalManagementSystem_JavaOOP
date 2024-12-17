/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.carrentalsystem;

import java.sql.*;

/**
 *
 * @author amir
 */
public class database {

    /**
     *
     * @return
     */
    public static Connection connectDB(){
        try{
            Class.forName("com.mysql.cj.jdbc.Driver");
            
            Connection connect = DriverManager.getConnection("jdbc:mysql://localhost/CarRental","root", "");
            return connect;
        }catch(Exception e){e.printStackTrace();}
        return null;
        
    }
}
