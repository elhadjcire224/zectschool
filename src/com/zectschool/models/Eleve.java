package com.zectschool.models;

import com.zectschool.db.DB;
import com.zectschool.models.abstracts.Compte;
import com.zectschool.utils.MD5Hash;

import java.sql.Date;
import java.sql.Timestamp;
import java.util.ArrayList;

public class Eleve {

    //******** VARIABLES **************//
    private String pvEleve;
    private String phoneParent;
    private Date createdAt;
    private Date updatedAat;
    private Double totalAPayer;
    private Double totalPaye;
    private Boolean isSolde;
    private AnneeScolaire anneescolaire;
    private Classe classe;
    private Compte compte;

    public String getPvEleve() {
        return pvEleve;
    }

    public void setPvEleve(String pvEleve) {
        this.pvEleve = pvEleve;
    }

    public Date getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Date createdAt) {
        this.createdAt = createdAt;
    }

    public String getPhoneParent() {
        return phoneParent;
    }

    public void setPhoneParent(String phoneParent) {
        this.phoneParent = phoneParent;
    }

    public Date getUpdatedAat() {
        return updatedAat;
    }

    public void setUpdatedAat(Date updatedAat) {
        this.updatedAat = updatedAat;
    }

    public Double getTotalAPayer() {
        return totalAPayer;
    }

    public void setTotalAPayer(Double totalAPayer) {
        this.totalAPayer = totalAPayer;
    }

    public Double getTotalPaye() {
        return totalPaye;
    }

    public void setTotalPaye(Double totalPaye) {
        this.totalPaye = totalPaye;
    }

    public Boolean getSolde() {
        return isSolde;
    }

    public void setSolde(Boolean solde) {
        isSolde = solde;
    }

    public AnneeScolaire getAnneescolaire() {
        return anneescolaire;
    }

    public void setAnneescolaire(AnneeScolaire anneescolaire) {
        this.anneescolaire = anneescolaire;
    }

    public Classe getClasse() {
        return classe;
    }

    public void setClasse(Classe classe) {
        this.classe = classe;
    }

    public Compte getCompte() {
        return compte;
    }

    public void setCompte(Compte compte) {
        this.compte = compte;
    }

    public Eleve(String pvEleve, String phoneParent, Date createdAt, Date updatedAat, Double totalAPayer, Double totalPaye, Boolean isSolde, AnneeScolaire anneescolaire, Classe classe, Compte compte) {
        this.pvEleve = pvEleve;
        this.phoneParent = phoneParent;
        this.createdAt = createdAt;
        this.updatedAat = updatedAat;
        this.totalAPayer = totalAPayer;
        this.totalPaye = totalPaye;
        this.isSolde = isSolde;
        this.anneescolaire = anneescolaire;
        this.classe = classe;
        this.compte = compte;
    }


    public static Eleve getById(String pvEleve) {
        DB DB = new DB();
        if (DB.connect()) {
            String rq = "select * from eleves where pvEleve = ?";
            try {
                DB.ps = DB.con.prepareStatement(rq);
                DB.ps.setString(1, pvEleve);
                DB.rs = DB.ps.executeQuery();

                String phoneParent = DB.rs.getString("phoneParent");
                Date updateAt = new Date(DB.rs.getTimestamp("updateAt").getTime());
                Date createdAt = new Date(DB.rs.getTimestamp("createdAt").getTime());
                Double totalApayer = DB.rs.getDouble("totalApayer");
                Double totalPaye = DB.rs.getDouble("totalPaye");
                Boolean isSolde = DB.rs.getBoolean("isSolde");
                Integer anneescolaireId = DB.rs.getInt("anneescolaireId");
                Integer compteId = DB.rs.getInt("compteId");
                Integer classeId = DB.rs.getInt("classeId");
                return new Eleve(pvEleve, phoneParent, createdAt, updateAt, totalApayer, totalPaye, isSolde, AnneeScolaire.getById(anneescolaireId), Classe.getById(classeId), Compte.getById(compteId));

            } catch (Exception e) {
                e.printStackTrace();
                com.zectschool.db.DB.error("get by id selection error");
            }
        }
        return null;
    }

    public static ArrayList<Eleve> getAll() {
        DB DB = new DB();
        ArrayList<Eleve> eleveList = new ArrayList<>();
        if (!DB.connect()) {
            com.zectschool.db.DB.error("error connexion ");
        }
        String rq = "select * from eleves";
        try {
            DB.ps = DB.con.prepareStatement(rq);
            DB.rs = DB.ps.executeQuery();
            while (DB.rs.next()) {
                String pvEleve = DB.rs.getString("pvEleve");
                eleveList.add(Eleve.getById(pvEleve));
            }
        } catch (Exception e) {
            e.printStackTrace();
            com.zectschool.db.DB.error("get all insertion error");
        } finally {
            DB.closeDB();
        }

        return eleveList;
    }

    public boolean delete() {
        return this.compte.delete();
    }

    public boolean save() {
        DB DB = new DB();
        if (!DB.connect()) com.zectschool.db.DB.error("save connection error");

        String rq = "insert into eleves values(?,?,?,?,?,?,?,?,?,?)";
        try {
            DB.ps = DB.con.prepareStatement(rq);

            DB.ps.setString(1, this.pvEleve);
            DB.ps.setString(2, this.phoneParent);
            DB.ps.setTimestamp(3, new Timestamp(this.createdAt.getTime()));
            DB.ps.setTimestamp(4, new Timestamp(this.updatedAat.getTime()));
            DB.ps.setDouble(5, this.totalAPayer);
            DB.ps.setDouble(6, this.totalPaye);
            DB.ps.setBoolean(7, this.isSolde);
            DB.ps.setInt(8, this.anneescolaire.getAnneescolaireId());
            DB.ps.setInt(9, this.classe.getClasseId());
            DB.ps.setInt(10, this.compte.getCompteId());
            DB.ps.executeUpdate();
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            com.zectschool.db.DB.error("save insertion error");
        } finally {
            DB.closeDB();
        }
        return false;
    }


    public boolean update() {
        DB DB = new DB();
        if (!DB.connect()) {
            com.zectschool.db.DB.error("update connection error");
            return false;
        }

        String rq = "update eleves set phoneParent=?, updatedAt=?, totalAPayer=?, totalPaye=?, isSolde=?, anneescolaireId=?, classeId=?, compteId=? where pvEleve=?";
        try {
            DB.ps = DB.con.prepareStatement(rq);
            DB.ps.setString(1, this.phoneParent);
            DB.ps.setTimestamp(2, new Timestamp(this.updatedAat.getTime()));
            DB.ps.setDouble(3, this.totalAPayer);
            DB.ps.setDouble(4, this.totalPaye);
            DB.ps.setBoolean(5, this.isSolde);
            DB.ps.setInt(6, this.anneescolaire.getAnneescolaireId());
            DB.ps.setInt(7, this.classe.getClasseId());
            DB.ps.setInt(8, this.compte.getCompteId());
            DB.ps.setString(9, this.pvEleve);

            int rowsAffected = DB.ps.executeUpdate();
            return rowsAffected > 0;
        } catch (Exception e) {
            e.printStackTrace();
            com.zectschool.db.DB.error("update error");
        } finally {
            DB.closeDB();
        }
        return false;
    }
    public static Eleve authenticateEleve(String id,String pwd){
        pwd = MD5Hash.hash(pwd);
        DB DB = new DB();
        Eleve eleve = null;
        if (!DB.connect()) DB.error("connect eleveAd error");
        try {
            String rq = "select * from eleves a JOIN comptes c ON a.compteId = c.compteId where pvEleve = ? and password = ?";
            DB.ps = DB.con.prepareStatement(rq);
            DB.ps.setString(1,id);
            DB.ps.setString(2,pwd);
            DB.rs = DB.ps.executeQuery();
            if (DB.rs.next())   eleve = Eleve.getById(DB.rs.getString("matriculeAdmin"));

        }catch (Exception e){e.printStackTrace();}
        finally {
            DB.closeDB();
        }
        return eleve;
    }



}

