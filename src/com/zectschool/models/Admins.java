package com.zectschool.models;

import com.zectschool.db.DB;
import com.zectschool.utils.MD5;

import java.util.ArrayList;
import java.util.UUID;

public class Admins{
    private String matriculeAdmin;
    private String phone;
    private String email;
    private Compte compte;


    public Admins(String matriculeAdmin, String phone, String email, Compte compte) {
        this.matriculeAdmin = matriculeAdmin;
        this.phone = phone;
        this.email = email;
        this.compte = compte;
    }

    public String getMatriculeAdmin() {
        return matriculeAdmin;
    }

    public void setMatriculeAdmin(String matriculeAdmin) {
        this.matriculeAdmin = matriculeAdmin;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Compte getCompte() {
        return compte;
    }


    public void setCompte(Compte compte) {
        this.compte = compte;
    }

    public static Admins getById(String matriculeAdmin){
        DB DB = new DB();
        Admins admin = null;
        if (!DB.connect()) return null;

        try{
            String rq = "select * from administrateurs where matriculeAdmin = ?";
            DB.ps = DB.con.prepareStatement(rq);
            DB.ps.setString(1,matriculeAdmin);
            DB.rs = DB.ps.executeQuery();

            if (DB.rs.next()){
                String phone = DB.rs.getString("phone");
                String email = DB.rs.getString("email");
                Integer compteId = DB.rs.getInt("compteId");
                admin =  new Admins(matriculeAdmin,phone,email,Compte.getById(compteId));
            }

        }catch (Exception e){
            e.printStackTrace();
            com.zectschool.db.DB.error("admin select error");
        }

        return admin;
    }

    public static ArrayList<Admins> getAll() {
        ArrayList<Admins> adminList = new ArrayList<Admins>();
        DB DB = new DB();

        if (DB.connect()) return null;
        String rq = "SELECT * FROM administrateurs";
        try {
            DB.ps = DB.con.prepareStatement(rq);
            DB.rs = DB.ps.executeQuery();
            while (DB.rs.next()) {
                String matriculeAdmin = DB.rs.getString("matriculeAdmin");
                adminList.add(Admins.getById(matriculeAdmin));
            }
        }catch (Exception e){
            e.printStackTrace();
        }
        return adminList;
    }

    public boolean delete() {
        return this.compte.delete();
    }

    public boolean save() {
        DB DB = new DB();
        if (!DB.connect()) com.zectschool.db.DB.error("save connection error");

        String rq = "insert into administrateurs(matriculeAdmin,phone,email,compteId) values (?,?,?,?)";
        try {
            DB.ps = DB.con.prepareStatement(rq);

            DB.ps.setString(1, this.matriculeAdmin);
            DB.ps.setString(2, this.phone);
            DB.ps.setString(3, this.email);
            DB.ps.setInt(4, this.compte.getCompteId());
            DB.ps.executeUpdate();
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            com.zectschool.db.DB.error("save Adminsinsertion error");
        }
        return false;
    }

    public static Admins authenticateAdmin(String id,String pwd){
        pwd = MD5.hash(pwd);
        DB DB = new DB();
        Admins admin = null;
        if (!DB.connect()) DB.error("connect auth Adminserror");
        try {
            String rq = "select * from administrateurs a JOIN comptes c ON a.compteId = c.compteId where matriculeAdmin = ? and password = ?";
            DB.ps = DB.con.prepareStatement(rq);
            DB.ps.setString(1,id);
            DB.ps.setString(2,pwd);
            DB.rs = DB.ps.executeQuery();
            if (DB.rs.next())  admin = Admins.getById(DB.rs.getString("matriculeAdmin"));

        }catch (Exception e){e.printStackTrace();}
        return admin;
    }
    public boolean update() {
        DB DB = new DB();
        if (!DB.connect()) com.zectschool.db.DB.error("update connection error");

        String rq = "update administrateurs set phone = ?, email = ?, compteId = ? where matriculeAdmin = ?";
        try {
            DB.ps = DB.con.prepareStatement(rq);

            DB.ps.setString(1, this.phone);
            DB.ps.setString(2, this.email);
            DB.ps.setInt(3, this.compte.getCompteId());
            DB.ps.setString(4, this.matriculeAdmin);
            DB.ps.executeUpdate();
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            com.zectschool.db.DB.error("update Adminserror");
        }
        return false;
    }

    public static ArrayList<Admins> rechercher(String query) {
        DB DB = new DB();
        ArrayList<Admins> adminList =new ArrayList<Admins>();

        if (!DB.connect()) return  null;

        try {
            String rq = "SELECT * FROM administrateurs a " +
                    "JOIN comptes c ON a.compteId = c.compteId " +
                    "WHERE matriculeAdmin LIKE ? " +
                    "OR phone LIKE ? " +
                    "OR c.nom LIKE ? " +
                    "OR c.prenom LIKE ? " +
                    "OR a.email LIKE ? " +
                    "OR c.adresse LIKE ? " +
                    "OR c.sexe LIKE ?";
            DB.ps = DB.con.prepareStatement(rq);
            String searchQuery = "%" + query + "%";
            for (int i = 1; i <= 7; i++) {
                DB.ps.setString(i, searchQuery);
            }
            DB.rs = DB.ps.executeQuery();

            while (DB.rs.next()) {
                String matriculeAdmin = DB.rs.getString("matriculeAdmin");
                adminList.add(Admins.getById(matriculeAdmin));
            }
        } catch (Exception e) {
            e.printStackTrace();

        }

        return adminList;
    }


    public static String generateMatricule(){
        return "AD"+UUID.randomUUID().toString().substring(0,8).toUpperCase();
    }

}
