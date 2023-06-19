package com.zectschool.models;

import com.zectschool.db.DB;
import com.zectschool.models.abstracts.Compte;
import com.zectschool.utils.ShowAlert;
import javafx.scene.control.Alert;

import java.util.ArrayList;

public class AnneeScolaire {

    private Integer anneescolaireId;
    private String annee;
    private Boolean active;

    // LE CONSTRUCTEUR

   public AnneeScolaire(Integer anneescolaireId, String annee, Boolean active) {
        this.anneescolaireId = anneescolaireId;
        this.annee = annee;
        this.active = active;
    }

    // LES SETTEURS ET LES GETTEURS

   public Integer getAnneescolaireId() {
        return anneescolaireId;
    }

    public void setAnneesolaireId(Integer anneesolaireId) {
        this.anneescolaireId = anneesolaireId;
    }

    public String getAnnee() {
        return annee;
    }

    public void setAnnee(String annee) {
        this.annee = annee;
    }

    public Boolean getActive() {
        return active;
    }

    public void setActive(Boolean active) {
        this.active = active;
    }

    //LA METHODE GETBYID
    public static AnneeScolaire getById(Integer anneescolaireId){
        DB DB = new DB();
        if(DB.connect()){
            String rq = "select * from anneescolaires where annescolaireId = ?";
            try {
                DB.ps = DB.con.prepareStatement(rq);
                DB.ps.setInt(1, anneescolaireId);
                DB.rs = DB.ps.executeQuery();

                String annee = DB.rs.getString("annee");
                Boolean active = DB.rs.getBoolean("active");

                return new AnneeScolaire(anneescolaireId, annee, active);

            } catch (Exception e) {
                e.printStackTrace();
                com.zectschool.db.DB.error("get by id selection error");
            }
        }
        return  null;
    }



    public static ArrayList<AnneeScolaire> getAll(){
        DB DB = new DB();
        ArrayList<AnneeScolaire> anneeList = new ArrayList<>();
        if(!DB.connect()){DB.error("error connexion ");};
        String rq = "select * from anneescolaires";
        try {
            DB.ps = DB.con.prepareStatement(rq);
            DB.rs = DB.ps.executeQuery();
            while (DB.rs.next()) {
                Integer anneescolaireId = DB.rs.getInt("anneescolaireId");
                String annee = DB.rs.getString("annee");
                Boolean active = DB.rs.getBoolean("active");
                System.out.println(anneescolaireId);
            }
        } catch (Exception e) {
            e.printStackTrace();
            DB.error("get all insertion error");
        }finally {
            DB.closeDB();
        }
        return anneeList;
   }


   public boolean insertIntoDB() {
       DB DB = new DB();
       if (DB.connect()) {
           DB.error("Save connection error");
       }

       String rq = "INSERT INTO annneescolaires(annee, active) VALUES(?, ?)";

       try {
           DB.ps = DB.con.prepareStatement(rq);

           DB.ps.setInt(1, this.anneescolaireId = anneescolaireId);
           DB.ps.setString(2, this.annee = annee);
           DB.ps.setBoolean(3, this.active = active);
           DB.ps.executeUpdate();
           return true;
       } catch (Exception e) {
           e.printStackTrace();
           DB.error("save insertion error");
       } finally {
           DB.closeDB();
       }
       return false;
   }

   /// LA METHODE INSERT

    public boolean delete(){
        DB DB = new DB();
        if (!DB.connect()) DB.error("delete connexion error");
        String rq = "delete from anneescolaires where anneescolaireid = ?";
        try {
            DB.ps = DB.con.prepareStatement(rq);
            DB.ps.setInt(1,this.anneescolaireId);
            DB.rs = DB.ps.executeQuery();

        } catch (Exception e) {
            e.printStackTrace();
            ShowAlert.show(Alert.AlertType.ERROR,null,"Annee get error","get error");
        }finally {
            DB.closeDB();
        }

        return false;
    }

}




