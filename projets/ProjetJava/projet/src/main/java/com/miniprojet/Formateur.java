package com.miniprojet;

public class Formateur extends Utilisateur {
    private Formation[] formations;
    private int nbFormation;

    Formateur(int id, String nom, String email, String motDePasse) {
        super(id, nom, email, motDePasse);
        formations = new Formation[100];
        nbFormation = 0;
    }

    public Formation[] getFormations() {
        return formations;
    }

    public void setFormation(Formation[] formations) {
        this.formations = formations;
    }

    // nbFormation est calculé lors de la récupération des formations
    void ajouterFormation(Formation formation) {
        if (nbFormation < 100) {
            formations[nbFormation] = formation;
            System.out.println("votre formation a ete bien ajouté");
            nbFormation++;
        } else {
            System.out.println("capacité maximale atteinte");
        }
    }
}
