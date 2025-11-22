package com.miniprojet;

public class Formation
{
    private int id;
    private String titre;
    private String descreption;
    private Formateur formateur;
    private double prix;
    //getters et setters des attributs de la classe formation(l'encapsulation)
    String getTitre()
    {
        return titre;
    }
    void setTitre(String titre)
    {
        this.titre=titre;
    }
    public int getId() {
        return id;
    }
    public void setId(int id) {
        this.id = id;
    }
    public String getDescreption()
    {
        return descreption;
    }
    public void setDescreption(String descreption)
    {
        this.descreption = descreption;
    }
    Formateur getFormateur()
    {
        return formateur;
    }
    double getPrix()
    {
        return prix;
    }
    void setDescription(String description)
    {
        this.descreption=description;
    }
    void setFormateur(Formateur formateur)
    {
        this.formateur=formateur;
    }
    void setPrix(double prix)
    {
        this.prix=prix;
    }
    public Formation (int id,String titre , String description , Formateur formateur , double prix)
    {
        this.titre=titre;
        this.descreption=description;
        this.formateur=formateur;
        this.prix=prix;
        this.id = id;
    }
}
