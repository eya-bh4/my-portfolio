package com.miniprojet;

public class UtilisateurNonTrouveException extends Exception 
{
    @Override
    public String toString()
    {
        return ("utilisateur non trouvé");
    }
}
