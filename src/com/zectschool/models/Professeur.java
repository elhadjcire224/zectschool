package com.zectschool.models;

import com.zectschool.db.DB;
import com.zectschool.models.abstracts.Compte;
import com.zectschool.utils.MD5Hash;
import com.zectschool.utils.ShowAlert;
import javafx.scene.control.Alert;

import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;

public class Professeur{
    private String matriculeProf;
    private String phone;
    private String email;
    private Integer hoairesTotal;
    private Integer hoairesNonPayes;
    private Double tauxHoaire;
    private Compte compte;

    public String getMatriculeProf() {
        return matriculeProf;
    }

    public void setMatriculeProf(String matriculeProf) {
        this.matriculeProf = matriculeProf;
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

    public Integer getHoairesTotal() {
        return hoairesTotal;
    }

    public void setHoairesTotal(Integer hoairesTotal) {
        this.hoairesTotal = hoairesTotal;
    }

    public Integer getHoairesNonPayes() {
        return hoairesNonPayes;
    }

    public void setHoairesNonPayes(Integer hoairesNonPayes) {
        this.hoairesNonPayes = hoairesNonPayes;
    }

    public Double getTauxHoaire() {
        return tauxHoaire;
    }

    public void setTauxHoaire(Double tauxHoaire) {
        this.tauxHoaire = tauxHoaire;
    }

    public Compte getCompte() {
        return compte;
    }

    public void setCompte(Compte compte) {
        this.compte = compte;
    }

    public Professeur(String matriculeProf, String phone, String email, Integer hoairesTotal, Integer hoairesNonPayes, Double tauxHoaire, Compte compte) {
        this.matriculeProf = matriculeProf;
        this.phone = phone;
        this.email = email;
        this.hoairesTotal = hoairesTotal;
        this.hoairesNonPayes = hoairesNonPayes;
        this.tauxHoaire = tauxHoaire;
        this.compte = compte;
    }

    public static Professeur getById(String matriculeProf){
        DB DB = new DB();
        Professeur prof = null;
        if (!DB.connect()) DB.closeDB();

        try{
            String rq = "select * from professeurs where matriculeProf = ?";
            DB.ps = DB.con.prepareStatement(rq);
            DB.rs = DB.ps.executeQuery();

            String phone = DB.rs.getString("phone");
            String email = DB.rs.getString("email");
            Integer hoairesTotal = DB.rs.getInt("hoairesTotal");
            Integer hoairesNonPayes = DB.rs.getInt("hoairesNonPayes");
            Double tauxHoaire = DB.rs.getDouble("tauxHoaire");
            Integer compteId = DB.rs.getInt("compteId");
            prof = new Professeur(matriculeProf,phone,email,hoairesTotal,hoairesNonPayes,tauxHoaire,Compte.getById(compteId));
        }catch (Exception e){
            e.printStackTrace();
            DB.error("Professeur insert error");
        }finally {DB.closeDB();}

        return prof;
    }

    public static ArrayList<Professeur> getAll() {
        ArrayList<Professeur>profsList = new ArrayList<>();
        DB DB = new DB();

        if (DB.connect()) DB.closeDB();
        String rq = "SELECT * FROM professeurs";
        try {
            DB.ps = DB.con.prepareStatement(rq);
            DB.rs = DB.ps.executeQuery();
            while (DB.rs.next()) {
                String matriculeProf = DB.rs.getString("matriculeProf");
                profsList.add(Professeur.getById(matriculeProf));
            }
        }catch (Exception e){
            e.printStackTrace();
            ShowAlert.show(Alert.AlertType.INFORMATION,null,"Professeur db","Professeur get all probleme");
        }
        finally {
            DB.closeDB();
        }
        return  profsList;
    }

    public boolean delete() {
        return this.compte.delete();
    }

    public boolean save() {
        DB DB = new DB();
        if (!DB.connect()) com.zectschool.db.DB.error("save connection error");

        String rq = "insert into professeurs (matriculeProf,phone,email,hoairesTotal,hoairesNonPayes,tauxHoaire,compteId) values (?,?,?,?,?,?,?);";
        try {
            DB.ps = DB.con.prepareStatement(rq);

            DB.ps.setString(1, this.matriculeProf);
            DB.ps.setString(2, this.phone);
            DB.ps.setString(3, this.email);
            DB.ps.setInt(4,this.hoairesTotal);
            DB.ps.setInt(5,this.hoairesNonPayes);
            DB.ps.setDouble(6,this.tauxHoaire);
            DB.ps.setInt(7, this.compte.getCompteId());
            return DB.ps.executeUpdate() > 0;

        } catch (Exception e) {
            e.printStackTrace();
            com.zectschool.db.DB.error("save Professeur error");
        } finally {
            DB.closeDB();
        }
        return false;
    }
    public static Professeur authenticateProf(String id,String pwd){
        pwd = MD5Hash.hash(pwd);
        DB DB = new DB();
        Professeur Professeur = null;
        if (!DB.connect()) DB.error("connect auth Prof error");
        try {
            String rq = "select * from professeurs a JOIN comptes c ON a.compteId = c.compteId where matriculeProf = ? and password = ?";
            DB.ps = DB.con.prepareStatement(rq);
            DB.ps.setString(1,id);
            DB.ps.setString(2,pwd);
            DB.rs = DB.ps.executeQuery();
            if (DB.rs.next())  Professeur = Professeur.getById(DB.rs.getString("matriculeProf"));

        }catch (Exception e){e.printStackTrace();}
        finally {
            DB.closeDB();
        }
        return Professeur;
    }

}
