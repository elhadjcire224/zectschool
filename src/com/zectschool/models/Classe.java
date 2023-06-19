package com.zectschool.models;

import com.zectschool.db.DB;
import com.zectschool.utils.ShowAlert;
import javafx.scene.control.Alert;

import java.util.ArrayList;

public class Classe {
    private Integer classeId;
    private String nom;
    private Double montant;
    private Integer nombreEleves;
    private Integer nombreProfs;
    private Integer nombreMatieres;

    public Integer getClasseId() {
        return classeId;
    }

    public void setClasseId(Integer classeId) {
        this.classeId = classeId;
    }

    public String getNom() {
        return nom;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public Double getMontant() {
        return montant;
    }

    public void setMontant(Double montant) {
        this.montant = montant;
    }

    public Integer getNombreEleves() {
        return nombreEleves;
    }

    public void setNombreEleves(Integer nombreEleves) {
        this.nombreEleves = nombreEleves;
    }

    public Integer getNombreProfs() {
        return nombreProfs;
    }

    public void setNombreProfs(Integer nombreProfs) {
        this.nombreProfs = nombreProfs;
    }

    public Integer getNombreMatieres() {
        return nombreMatieres;
    }

    public void setNombreMatieres(Integer nombreMatieres) {
        this.nombreMatieres = nombreMatieres;
    }

    public Classe(Integer classeId, String nom, Double montant, Integer nombreEleves, Integer nombreProfs, Integer nombreMatieres) {
        this.classeId = classeId;
        this.nom = nom;
        this.montant = montant;
        this.nombreEleves = nombreEleves;
        this.nombreProfs = nombreProfs;
        this.nombreMatieres = nombreMatieres;
    }

    public static Classe getById(Integer classeId){
        DB DB = new DB();
        if(DB.connect()){
            String rq = "select * from comptes where compteId = ?";
            try {
                DB.ps = DB.con.prepareStatement(rq);
                DB.ps.setInt(1, classeId);
                DB.rs = DB.ps.executeQuery();

                String nom = DB.rs.getString("nom");
                Double montant = DB.rs.getDouble("montant");
                Integer nombreEleves = DB.rs.getInt("nombreEleves");
                Integer nombreProfs = DB.rs.getInt("nombreProfs");
                Integer nombreMatieres = DB.rs.getInt("nombreMatieres");

                return new Classe(classeId,nom,montant,nombreEleves,nombreProfs,nombreMatieres);

            } catch (Exception e) {
                e.printStackTrace();
                com.zectschool.db.DB.error("get by id selection error");
            }
        }
        return  null;
    }
    public static ArrayList<Classe> getAll(){
        DB DB = new DB();
        ArrayList<Classe> classeList = new ArrayList<>();
        if(!DB.connect()){DB.error("error connexion ");};
        String rq = "select * from eleves";
        try {
            DB.ps = DB.con.prepareStatement(rq);
            DB.rs = DB.ps.executeQuery();
            while (DB.rs.next()) {
                Integer classeId = DB.rs.getInt("classeId");
                classeList.add(Classe.getById(classeId));
            }
        } catch (Exception e) {
            e.printStackTrace();
            DB.error("get all insertion error");
        }finally {
            DB.closeDB();
        }

        return  classeList;
    }

    public boolean delete(){
        DB DB = new DB();
        if (!DB.connect()) DB.error("delete connexion error");
        String rq = "delete from classe where classeId = ?";
        try {
            DB.ps = DB.con.prepareStatement(rq);
            DB.ps.setInt(1,this.classeId);
            return DB.ps.executeUpdate() > 0;

        } catch (Exception e) {
            e.printStackTrace();
            ShowAlert.show(Alert.AlertType.ERROR,null,"compte get error","get error");
        }finally {
            DB.closeDB();
        }

        return false;
    }

    public boolean save(){
        DB DB = new DB();
        if (!DB.connect()) DB.error("save connection error");

        String rq = "insert into classes (nom,montant,nombreEleves,nombreProfs,nombreMatieres) values(?,?,?,?,?)";
        try {
            DB.ps = DB.con.prepareStatement(rq);

            DB.ps.setString(1,this.nom);
            DB.ps.setDouble(2,this.montant);
            DB.ps.setInt(3,this.nombreEleves);
            DB.ps.setInt(4,this.nombreProfs);
            DB.ps.setInt(5,this.nombreMatieres);
            DB.ps.executeUpdate();
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            DB.error("save insertion error");
        }finally {
            DB.closeDB();
        }
        return false;
    }
}
