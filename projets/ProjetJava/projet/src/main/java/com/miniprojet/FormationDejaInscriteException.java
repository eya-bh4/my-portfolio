package com.miniprojet;

public class FormationDejaInscriteException extends Exception 
{
    @Override
    public String toString()
    {
        return ("l'etudiant est déjà inscrit à cette formation");
    }
}
