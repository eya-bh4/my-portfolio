package com.miniprojet;

public class Etudiant extends Utilisateur
{
    private Formation [] inscriptions;
    Etudiant(int id,String nom , String email , String motDePasse)
    {
        super(id,nom,email,motDePasse);
        inscriptions=new Formation [100];
    }  
    public Formation [] getInscriptions()
    {
        return inscriptions;
    }
    public void setInscriptions (Formation [] inscriptions)
    {
        this.inscriptions=inscriptions;
    }
    //méthode pour vérifier si une formation est déjà existante ou pas
    boolean formationExistante(Formation f , Formation [] inscriptions)
    {
        boolean test=false;
        int i=0;
        while(test==false && i<inscriptions.length)
        {
            if(f.getTitre().equals(inscriptions[i].getTitre()))
            {test=true;}
            i=i+1;
        }
        return test;
    }
    //méthode pour calculer le nombre des cases non vides dans le tableau inscriptions
    int nbInscriptionsEnregistres()
    {
        int nb=0;
        int i=0;
        while(i<inscriptions.length && inscriptions[i]!=null)
        {
            nb=nb+1;
            i=i+1;
        }
        return nb;
    }
    void sinscrireFormation(Formation formation)
    {
               
        if (nbInscriptionsEnregistres()<100)
        {
            try
            {
                int nb = nbInscriptionsEnregistres();
                if(nb == 0)
                {
                System.out.println("la premiere formation a ete ajoutée");
                inscriptions[0]=formation;
                }
                else if (formationExistante(formation, inscriptions)) throw new FormationDejaInscriteException();
                else
                {
                    inscriptions[nb]=formation;
                    System.out.println("formation bien ajoutée");
                }
            }
            catch(FormationDejaInscriteException e)
            {
                System.out.println(e);
            }
        }
    }
    void seConnecter(Utilisateur [] utilisateurs,String email,String mdp) throws UtilisateurNonTrouveException
    {
        int i=0;
        boolean trouvé = false;
        while(utilisateurs[i]!=null && !trouvé && i<utilisateurs.length)
        {
            if (utilisateurs[i].getEmail().equals(email) && utilisateurs[i].getMotDePasse().equals(mdp))
            {
                trouvé = true;
                System.out.println("données de connexion valides ");
            }
        i++;
        }
        if(!trouvé){
            throw new UtilisateurNonTrouveException();
        }
    }
}
