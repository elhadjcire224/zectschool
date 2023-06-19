package com.zectschool.db;

import javax.swing.*;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class DB {
    public PreparedStatement ps;
    public Connection con;
    public ResultSet rs;

    public boolean connect() {
        try {
            Class.forName("com.mysql.jdbc.Driver");
            String url = "jdbc:mysql://localhost:3306/zectschool"; // Remplacez "db_name" par le nom de votre base de données MySQL
            String username = "root"; // Remplacez "username" par votre nom d'utilisateur MySQL
            String password = "625551421MariaDB"; // Remplacez "password" par votre mot de passe MySQL

            con = DriverManager.getConnection(url, username, password);
            if (!con.isClosed()) return true;
        } catch (Exception e) {
            e.printStackTrace();
            DB.error("oppening db error"); 
        }
        return false;
    }


    public static void error(String content) {
       
    }
}
