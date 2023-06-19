package com.zectschool.models;

import com.zectschool.db.DB;
import com.zectschool.utils.ShowAlert;
import javafx.scene.control.Alert;

import java.sql.SQLException;
import java.util.ArrayList;

public class Absence {
    private  Integer absenceId;
    private  Eleve eleve;
    private  Boolean justifie;
    private  Cours cours;

    public Integer getAbsenceId() {
        return absenceId;
    }

    public void setAbsenceId(Integer absenceId) {
        this.absenceId = absenceId;
    }

    public Eleve getEleve() {
        return eleve;
    }

    public void setEleve(Eleve eleve) {
        this.eleve = eleve;
    }

    public Boolean getJustifie() {
        return justifie;
    }

    public void setJustifie(Boolean justifie) {
        this.justifie = justifie;
    }

    public Cours getCours() {
        return cours;
    }

    public void setCours(Cours cours) {
        this.cours = cours;
    }

    public Absence(Integer absenceId, Eleve eleve, Boolean justifie, Cours cours) {
        this.absenceId = absenceId;
        this.eleve = eleve;
        this.justifie = justifie;
        this.cours = cours;
    }

    public static Absence getById(Integer absenceId){
        DB DB = new DB();
        if (!DB.connect()) DB.closeDB();
        Absence absence = null;
        try{
            String rq = "select * from absences where absenceId = ?";

            DB.ps = DB.con.prepareStatement(rq);
            DB.rs = DB.ps.executeQuery();

            String pvEleve = DB.rs.getString("pvEleve");
            Boolean justifie = DB.rs.getBoolean("justifie");
            Integer coursId = DB.rs.getInt("macoursId");

            absence =  new Absence(absenceId,Eleve.getById(pvEleve),justifie,Cours.getById(coursId));

        }catch (Exception e){
            e.printStackTrace();
            DB.error("cours select error");
        }finally {DB.closeDB();}

        return absence;
    }

    public static ArrayList<Absence> getAll() throws SQLException {
        ArrayList<Absence> absencesList = new ArrayList<>();
        DB DB = new DB();

        if (DB.connect()) DB.closeDB();
        String rq = "SELECT * FROM absences";
        try {
            DB.ps = DB.con.prepareStatement(rq);
            DB.rs = DB.ps.executeQuery();
            while (DB.rs.next()) {
                Integer absenceId = DB.rs.getInt("absenceId");
                absencesList.add(Absence.getById(absenceId));
            }
        }catch (Exception e){
            e.printStackTrace();
            ShowAlert.show(Alert.AlertType.INFORMATION,null,"absence db","absence get all probleme");
        }
        finally {
            DB.closeDB();
        }
        return absencesList;
    }

    public boolean delete() {
        DB DB = new DB();
        if (!DB.connect()) DB.error("delete connexion error");
        String rq = "delete from absences where absenceId = ?";
        try {
            DB.ps = DB.con.prepareStatement(rq);
            DB.ps.setInt(1,this.absenceId);
            return DB.ps.executeUpdate() > 0;

        } catch (Exception e) {
            e.printStackTrace();
            DB.error("delete absence error db");
        }finally {
            DB.closeDB();
        }

        return false;
    }

    public boolean save() {
        DB DB = new DB();
        if (!DB.connect()) com.zectschool.db.DB.error("save connection error");

        String rq = "insert into absences(pvEleve,justifie,coursId) values (?,?,?)";
        try {
            DB.ps = DB.con.prepareStatement(rq);

            DB.ps.setString(1,this.eleve.getPvEleve());
            DB.ps.setBoolean(2,this.justifie);
            DB.ps.setInt(3,this.cours.getCoursId());
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
