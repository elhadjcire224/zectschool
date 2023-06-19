package com.zectschool.models;

import com.zectschool.db.DB;
import com.zectschool.models.abstracts.Compte;
import com.zectschool.utils.ShowAlert;
import javafx.scene.control.Alert;

import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;

public class Cours {
    private Integer coursId;
    private Integer dureeCours;
    private Boolean annule;
    private Date jour;
    private Professeur  professeur;
    private Matiere matiere;

    public Professeur getProfesseur() {
        return professeur;
    }

    public void setProfesseur(Professeur professeur) {
        this.professeur = professeur;
    }

    public Matiere getMatiere() {
        return matiere;
    }

    public void setMatiere(Matiere matiere) {
        this.matiere = matiere;
    }

    public Integer getCoursId() {
        return coursId;
    }

    public void setCoursId(Integer coursId) {
        this.coursId = coursId;
    }

    public Integer getDureeCours() {
        return dureeCours;
    }

    public void setDureeCours(Integer dureeCours) {
        this.dureeCours = dureeCours;
    }

    public Boolean getAnnule() {
        return annule;
    }

    public void setAnnule(Boolean annule) {
        this.annule = annule;
    }

    public Date getJour() {
        return jour;
    }

    public void setJour(Date jour) {
        this.jour = jour;
    }


    public Cours(Integer coursId, Integer dureeCours, Boolean annule, Date jour, Professeur professeur, Matiere matiere) {
        this.coursId = coursId;
        this.dureeCours = dureeCours;
        this.annule = annule;
        this.jour = jour;
        this.professeur = professeur;
        this.matiere = matiere;
    }

    public static Cours getById(Integer coursId){
        DB DB = new DB();
        if (!DB.connect()) DB.closeDB();
        Cours cour = null;


        try{
            String rq = "select * from cours where coursId = ?";

            DB.ps = DB.con.prepareStatement(rq);
            DB.rs = DB.ps.executeQuery();

            if (DB.rs.next()){
                Integer heureCours = DB.rs.getInt("heureCours");
                Boolean annule = DB.rs.getBoolean("annule");
                Date jour = new Date(DB.rs.getTimestamp("jour").getTime());
                String matriculeProf = DB.rs.getString("matriculeProf");
                Integer matiereId = DB.rs.getInt("matriculeId");
                cour =  new Cours(coursId,heureCours,annule,jour,Professeur.getById(matriculeProf),Matiere.getById(matiereId));
            }


            



        }catch (Exception e){
            e.printStackTrace();
            DB.error("cours select error");
        }finally {DB.closeDB();}

        return null;
    }

    public static ArrayList<Cours> getAll() throws SQLException {
        ArrayList<Cours> coursList = new ArrayList<>();
        DB DB = new DB();

        if (DB.connect()) DB.closeDB();
        String rq = "SELECT * FROM cours";
        try {
            DB.ps = DB.con.prepareStatement(rq);
            DB.rs = DB.ps.executeQuery();
            while (DB.rs.next()) {
                Integer coursId = DB.rs.getInt("coursId");
                coursList.add(Cours.getById(coursId));
            }
        }catch (Exception e){
            e.printStackTrace();
            ShowAlert.show(Alert.AlertType.INFORMATION,null,"admin db","admin get all probleme");
        }
        finally {
            DB.closeDB();
        }
        return coursList;
    }

    public boolean delete() {
        DB DB = new DB();
        if (!DB.connect()) DB.error("delete connexion error");
        String rq = "delete from cours where coursId = ?";
        try {
            DB.ps = DB.con.prepareStatement(rq);
            DB.ps.setInt(1,this.coursId);
            return DB.ps.executeUpdate() > 0;

        } catch (Exception e) {
            e.printStackTrace();
            DB.error("delete cours error db");
        }finally {
            DB.closeDB();
        }

        return false;
    }

    public boolean save() {
        DB DB = new DB();
        if (!DB.connect()) com.zectschool.db.DB.error("save connection error");

        String rq = "insert into cours(dureeCours,dureeCours,annule,jour,matriculeProf,matiereId) values (?,?,?,?,?)";
        try {
            DB.ps = DB.con.prepareStatement(rq);

            DB.ps.setInt(1,this.dureeCours);
            DB.ps.setBoolean(2,this.annule);
            DB.ps.setTimestamp(3,new Timestamp(this.jour.getTime()));
            DB.ps.setString(4,this.professeur.getMatriculeProf());
            DB.ps.setInt(5,this.matiere.getMatiereId());
            return DB.ps.executeUpdate() > 0;

        } catch (Exception e) {
            e.printStackTrace();
            com.zectschool.db.DB.error("save insertion error");
        } finally {
            DB.closeDB();
        }
        return false;
    }

}
