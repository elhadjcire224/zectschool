package com.zectschool.models;

import com.zectschool.db.DB;
import com.zectschool.utils.FileManager;
import com.zectschool.utils.MD5Hash;

import java.io.File;
import java.util.ArrayList;

public class Compte {
    public String getPhoto() {
        return photo;
    }

    public void setPhoto(String photo) {
        this.photo = photo;
    }

    private Integer compteId;
    private String password;
    private String nom;
    private String prenom;
    private String adresse;
    private String sexe;

    private String role;

    private String photo;


    private static Compte c;
    public Compte(Integer compteId, String password, String nom, String prenom, String adresse, String sexe, String role, String photo) {
        this.compteId = compteId;
        this.password = password;
        this.nom = nom;
        this.prenom = prenom;
        this.adresse = adresse;
        this.sexe = sexe;
        this.role = role;
        this.photo = photo;
    }

    public Integer getCompteId() {
        return compteId;
    }

    public void setCompteId(Integer compteId) {
        this.compteId = compteId;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getNom() {
        return nom;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public String getPrenom() {
        return prenom;
    }

    public void setPrenom(String prenom) {
        this.prenom = prenom;
    }

    public String getAdresse() {
        return adresse;
    }

    public void setAdresse(String adresse) {
        this.adresse = adresse;
    }

    public String getSexe() {
        return sexe;
    }

    public void setSexe(String sexe) {
        this.sexe = sexe;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }


    public static Compte getById(Integer matriculeId){
        DB DB = new DB();
        Compte compte = null;
        if(DB.connect()){
            String rq = "select * from comptes where compteId = ?";
            try {
                DB.ps = DB.con.prepareStatement(rq);
                DB.ps.setInt(1, matriculeId);
                DB.rs = DB.ps.executeQuery();
                if (DB.rs.next()) {
                    String role = DB.rs.getString("role");
                    String nom = DB.rs.getString("nom");
                    String prenom = DB.rs.getString("prenom");
                    Integer compteId = DB.rs.getInt("compteId");
                    String sexe = DB.rs.getString("sexe");
                    String password = DB.rs.getString("password");
                    String adresse = DB.rs.getString("adresse");
                    String photo = DB.rs.getString("photo");
                    compte = new Compte(compteId, password, nom, prenom, adresse, sexe, role, photo);
                }

            } catch (Exception e) {
                e.printStackTrace();
                com.zectschool.db.DB.error("get by id selection error");
            }

        }
        return  compte;
    }
    public static ArrayList<Compte> getAll(){
        DB DB = new DB();
        ArrayList<Compte> compteList = new ArrayList<Compte>();
        if(!DB.connect()){DB.error("error connexion ");};
        String rq = "select * from comptes";
        try {
            DB.ps = DB.con.prepareStatement(rq);
            DB.rs = DB.ps.executeQuery();
            while (DB.rs.next()) {
                Integer compteId = DB.rs.getInt("compteId");
                Compte compte =  Compte.getById(compteId);
                compteList.add(compte);
                System.out.println(compte.getCompteId());
            }
        } catch (Exception e) {
            e.printStackTrace();
            DB.error("get all insertion error");
        }

        return  compteList;
    }

    public boolean delete(){

        DB DB = new DB();
        if (!DB.connect()) DB.error("delete connexion error");
        String rq = "delete from compte where id = ?";
        try {
            DB.ps = DB.con.prepareStatement(rq);
            DB.ps.setInt(1,this.compteId);
            FileManager.deleteImage(this.photo);
            return DB.ps.executeUpdate() > 1;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    public boolean save(File selectedFile, String role){
        this.photo = FileManager.saveImage(selectedFile,getPrenom(),role);
        DB DB = new DB();
        if (!DB.connect()) com.zectschool.db.DB.error("save connection error");

        String rq = "insert into comptes(password,nom,prenom,adresse,sexe,role,photo) values(?,?,?,?,?,?,?)";
        try {
            DB.ps = DB.con.prepareStatement(rq);

            DB.ps.setString(1,MD5Hash.hash(this.password));
            DB.ps.setString(2,this.nom);
            DB.ps.setString(3,this.prenom);
            DB.ps.setString(4,this.adresse);
            DB.ps.setString(5,this.sexe);
            DB.ps.setString(6,this.role);
            DB.ps.setString(7,this.photo);
            DB.ps.executeUpdate();
            this.compteId = getLast();


            return true;
        } catch (Exception e) {
            e.printStackTrace();
            com.zectschool.db.DB.error("save insertion error");
        }
        return false;
    }

    public boolean update() {
        DB DB = new DB();
        if (!DB.connect()) com.zectschool.db.DB.error("update connection error");

        String rq = "update comptes set password = ?, nom = ?, prenom = ?, adresse = ?, sexe = ?, role = ? where compteId = ?";
        try {
            DB.ps = DB.con.prepareStatement(rq);

            DB.ps.setString(1, MD5Hash.hash(this.password));
            DB.ps.setString(2, this.nom);
            DB.ps.setString(3, this.prenom);
            DB.ps.setString(4, this.adresse);
            DB.ps.setString(5, this.sexe);
            DB.ps.setString(6, this.role);
            DB.ps.setInt(7, this.compteId);
            DB.ps.executeUpdate();
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            com.zectschool.db.DB.error("update compte error");
        }
        return false;
    }



    public static Integer getLast(){
        DB DB = new DB();
        if (!DB.connect()) com.zectschool.db.DB.error("get last db error");
        try{
            DB.ps = DB.con.prepareStatement("select compteId from comptes order by compteId desc limit 1");
            DB.rs = DB.ps.executeQuery();
            if (DB.rs.next()) return DB.rs.getInt("compteId");
        }catch (Exception e){
            com.zectschool.db.DB.error("get last sql error");
            e.printStackTrace();
        }
        return -1;
    }


}
