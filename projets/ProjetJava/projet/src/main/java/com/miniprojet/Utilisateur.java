package com.miniprojet;

public class Utilisateur
{
    private int id;
    private String nom;
    private String email;
    private String motDePasse;
    public int getId() {
        return id;
    }
    public void setId(int id) {
        this.id = id;
    }
    String getNom()
    {
        return nom;
    }
    String getEmail()
    {
        return email;
    }
    String getMotDePasse()
    {
        return motDePasse;
    }
    void setNom(String nom)
    {
        this.nom=nom;
    }
    void setEmail(String email)
    {
        this.email=email;
    }
    void setMotDePasse(String motDePasse)
    {
        this.motDePasse=motDePasse;
    }
    Utilisateur(int id,String nom , String email , String motDePasse)
    {
        this.id=id;
        this.nom=nom;
        this.email=email;
        this.motDePasse=motDePasse;
    }
}
