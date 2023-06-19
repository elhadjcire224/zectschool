package com.zectschool.models;

import com.zectschool.db.DB;
import com.zectschool.models.abstracts.Compte;
import com.zectschool.utils.MD5Hash;
import com.zectschool.utils.ShowAlert;
import javafx.scene.control.Alert;

import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;

public class Comptable  {
    private String matriculeComptable;
    private String phone;
    private String email;
    private Compte compte;


    public Comptable (String matriculeComptable, String phone, String email, Compte compte) {
        this.matriculeComptable = matriculeComptable;
        this.phone = phone;
        this.email = email;
        this.compte = compte;
    }

    public String getmatriculeComptable() {
        return matriculeComptable;
    }

    public void setmatriculeComptable(String matriculeComptable) {
        this.matriculeComptable = matriculeComptable;
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

    public static Comptable  getById(String matriculeComptable){
        DB DB = new DB();
        Comptable comptable = null;
        if (!DB.connect()) DB.closeDB();

        try{
            String rq = "select * from comptable where matriculeComptable = ?";
            DB.ps = DB.con.prepareStatement(rq);
            DB.rs = DB.ps.executeQuery();

            String phone = DB.rs.getString("phone");
            String email = DB.rs.getString("email");
            Integer compteId = DB.rs.getInt("compteId");


            comptable = new Comptable(matriculeComptable,phone,email,Compte.getById(compteId));
        }catch (Exception e){
            e.printStackTrace();
            DB.error("comptable insert error");
        }finally {DB.closeDB();}

        return comptable;
    }

    public static ArrayList<Comptable> getAll() {
        ArrayList<Comptable> comptableList = new ArrayList<>();
        DB DB = new DB();

        if (DB.connect()) DB.closeDB();
        String rq = "SELECT * FROM comptable";
        try {
            DB.ps = DB.con.prepareStatement(rq);
            DB.rs = DB.ps.executeQuery();
            while (DB.rs.next()) {
                String matriculeComptable = DB.rs.getString("matriculeComptable");
                comptableList.add(Comptable.getById(matriculeComptable));
            }
        }catch (Exception e){
            e.printStackTrace();
            ShowAlert.show(Alert.AlertType.INFORMATION,null,"admin db","admin get all probleme");
        }
        finally {
            DB.closeDB();
        }
        return comptableList;
    }

    public boolean delete() {
        return this.compte.delete();
    }

    public boolean save() {
        DB DB = new DB();
        if (!DB.connect()) com.zectschool.db.DB.error("save connection error");

        String rq = "insert into comptable (matriculeComptable,phone,email,compteId) values (?,?,?,?)";
        try {
            DB.ps = DB.con.prepareStatement(rq);

            DB.ps.setString(1, this.matriculeComptable);
            DB.ps.setString(2, this.phone);
            DB.ps.setString(3, this.email);
            DB.ps.setInt(4, this.compte.getCompteId());
            DB.ps.executeUpdate();
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            com.zectschool.db.DB.error("save Comptable  insertion error");
        } finally {
            DB.closeDB();
        }
        return false;
    }
    public static Comptable  authenticateComptable(String id,String pwd){
        pwd = MD5Hash.hash(pwd);
        DB DB = new DB();
        Comptable  Comptable  = null;
        if (!DB.connect()) DB.error("connect auth comptable error");
        try {
            String rq = "select * from comptable a JOIN comptes c ON a.compteId = c.compteId where matriculeComptable = ? and password = ?";
            DB.ps = DB.con.prepareStatement(rq);
            DB.ps.setString(1,id);
            DB.ps.setString(2,pwd);
            DB.rs = DB.ps.executeQuery();
            if (DB.rs.next())   Comptable  = Comptable.getById(DB.rs.getString("matriculeComptable"));

        }catch (Exception e){e.printStackTrace();}
        finally {
            DB.closeDB();
        }
        return Comptable ;
    }

}
