package com.zectschool.models;

public class EcoleConfigs {

    private String nomAbrevie;
    private String nomComplet ;
    private String adresse ;
    private String telephone ;
    private String email ;
    private String photo ;
    private Double inscriptionPrix  ;
    private Double reinscriptionPrix  ;

    public EcoleConfigs(String nomAbrevie, String nomComplet, String adresse, String telephone, String email, String photo, Double inscriptionPrix, Double reinscriptionPrix) {
        this.nomAbrevie = nomAbrevie;
        this.nomComplet = nomComplet;
        this.adresse = adresse;
        this.telephone = telephone;
        this.email = email;
        this.photo = photo;
        this.inscriptionPrix = inscriptionPrix;
        this.reinscriptionPrix = reinscriptionPrix;
    }

    public String getNomAbrevie() {
        return nomAbrevie;
    }

    public void setNomAbrevie(String nomAbrevie) {
        this.nomAbrevie = nomAbrevie;
    }

    public String getNomComplet() {
        return nomComplet;
    }

    public void setNomComplet(String nomComplet) {
        this.nomComplet = nomComplet;
    }

    public String getAdresse() {
        return adresse;
    }

    public void setAdresse(String adresse) {
        this.adresse = adresse;
    }

    public String getTelephone() {
        return telephone;
    }

    public void setTelephone(String telephone) {
        this.telephone = telephone;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhoto() {
        return photo;
    }

    public void setPhoto(String photo) {
        this.photo = photo;
    }

    public Double getInscriptionPrix() {
        return inscriptionPrix;
    }

    public void setInscriptionPrix(Double inscriptionPrix) {
        this.inscriptionPrix = inscriptionPrix;
    }

    public Double getReinscriptionPrix() {
        return reinscriptionPrix;
    }

    public void setReinscriptionPrix(Double reinscriptionPrix) {
        this.reinscriptionPrix = reinscriptionPrix;
    }




}
