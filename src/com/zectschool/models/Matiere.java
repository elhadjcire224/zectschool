package com.zectschool.models;

import com.zectschool.db.DB;

public class Matiere {
    private Integer matiereId;
    private String nom;
    private Integer coefficient;
    private Double moyenne;

    public Integer getMatiereId() {
        return matiereId;
    }

    public void setMatiereId(Integer matiereId) {
        this.matiereId = matiereId;
    }

    public String getNom() {
        return nom;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public Integer getCoefficient() {
        return coefficient;
    }

    public void setCoefficient(Integer coefficient) {
        this.coefficient = coefficient;
    }

    public Double getMoyenne() {
        return moyenne;
    }

    public void setMoyenne(Double moyenne) {
        this.moyenne = moyenne;
    }

    public Matiere(Integer matiereId, String nom, Integer coefficient, Double moyenne) {
        this.matiereId = matiereId;
        this.nom = nom;
        this.coefficient = coefficient;
        this.moyenne = moyenne;
    }

    public static Matiere getById(Integer matiereId){
        DB DB = new DB();
        if (!DB.connect()) DB.closeDB();
        Matiere matiere = null;
        try{
            String rq = "select * from matieres where matiereId = ?";

            DB.ps = DB.con.prepareStatement(rq);
            DB.rs = DB.ps.executeQuery();


            String nom = DB.rs.getString("nom");
            Integer coefficient = DB.rs.getInt("coefficient");
            Double moyenne = DB.rs.getDouble("moyenne");

            matiere = new Matiere(matiereId,nom,coefficient,moyenne);

        }catch (Exception e){
            e.printStackTrace();
            DB.error("cours select error");
        }finally {DB.closeDB();}

        return matiere;
    }

}
