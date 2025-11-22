package com.miniprojet;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
public class MiniProjet
{
    public static void main (String [] args)
    {
        //les tableaux utilisateurs et formateurs pour verifier 
        Utilisateur[] utilisateurs = new Utilisateur[100];
        Formation[] formations = new Formation[100];
            // URL de connexion à la base de données
        String url = "jdbc:mysql://localhost:3306/miniprojet";
        String user = "root"; 
        String password = "eyahiba2082008"; 
        Connection connection = null;
        int nbFormation = 0;
        try 
        {
            // Charger le driver MySQL 
            Class.forName("com.mysql.cj.jdbc.Driver");

            // Établir la connexion
            connection = DriverManager.getConnection(url, user, password);
            System.out.println("Connexion réussie !");
            //récupération des utilisateurs déjà insérés dans la base de donnée
            recupererLesUtilisateurs(utilisateurs,connection);
            Formateur formateur = new Formateur(10,"siwar", "siwar@gmail.com", "***");
            insererUtilisateur(utilisateurs, connection, formateur);
            Etudiant etudiant = new Etudiant(11,"samir", "samir@gmail.com", "***");
            insererUtilisateur(utilisateurs, connection, etudiant);
            //récupération de tous les utilisateurs insérés dans la base de donnée 
            recupererLesUtilisateurs(utilisateurs,connection);
            recupererFormation(formations,utilisateurs,connection,nbFormation);
            insererEtAfficherInscription(formations[0], etudiant, connection);
            supprimerInscription(connection, 200);
            supprimerFormation(connection, 101); 
            supprimerUtilisateur(connection, 10);
            recupererLesUtilisateurs(utilisateurs,connection);
            recupererFormation(formations,utilisateurs,connection,nbFormation);           
            try
            {
                etudiant.seConnecter(utilisateurs, "asma@gmail.com", "asmamdp");
                etudiant.seConnecter(utilisateurs, "email incorrecte", "****");
            } 
            catch (UtilisateurNonTrouveException e) 
            {
                System.out.println(e);
            }
        }
        catch (ClassNotFoundException e) 
        {
            System.out.println("Driver JDBC introuvable !");
        }
        catch (SQLException e)
        {
            System.out.println("Erreur SQL !");
        }
        finally
        {
            // Fermer les ressources
            try 
            {
                if (connection != null) connection.close();
            } 
            catch (SQLException e)
            {
                System.out.println(" exception sql");

            }
        }
    }
    public static void recupererLesUtilisateurs(Utilisateur[]utilisateurs,Connection connection) throws SQLException
    {   
        //utilisation de l'API JDBC pour créer un objet  de type Statment à partir d'une connexion à une BD 
        Statement statement = connection.createStatement();
        String selectUtilisateur = "SELECT * FROM utilisateurs"; 
        ResultSet resultSetUtilisateur = statement.executeQuery(selectUtilisateur);
        int i = 0;
        while (resultSetUtilisateur.next())
        {
            int id = resultSetUtilisateur.getInt("id");
            String nom = resultSetUtilisateur.getString("nom");
            String email = resultSetUtilisateur.getString("email");
            String motDePasse = resultSetUtilisateur.getString("mdp");
            System.out.println( "id "+ id +" Nom: " + nom + ", Email: " + email +" mot de passe : "+motDePasse);
            
            utilisateurs[i] = new Utilisateur(id,nom, email, motDePasse);
            i++;
        }
    }
    public static void insererUtilisateur(Utilisateur[]utilisateurs,Connection connection,Utilisateur utilisateur) throws SQLException{
        //vérification si l'utilisateur existe déjà ou pas       
        boolean utilisateurExist = false;
        int j=0;
        while( !utilisateurExist && utilisateurs[j]!=null){
            if(utilisateur.getEmail().equals(utilisateurs[j].getEmail()))
            {
                utilisateurExist = true;
            }
            j++;
        }
        if(!utilisateurExist)
        {
            System.out.println("nouveau utilisateur");
                //ecriture de la requete
                String ajouterEtudiant="insert into utilisateurs values(?,?,?,?)";
                //preparation de la requete
                PreparedStatement cn = connection.prepareStatement(ajouterEtudiant);
                cn.setInt(1,utilisateur.getId());
                cn.setString(2, utilisateur.getNom());
                cn.setString(3, utilisateur.getEmail());
                cn.setString(4, utilisateur.getMotDePasse());
                //execution de insert
                cn.executeUpdate();
                System.out.println( "Nom: " + utilisateur.getNom() + ", Email: " + utilisateur.getEmail() );
                utilisateurs[j] = new Utilisateur(utilisateur.getId(),utilisateur.getNom(), utilisateur.getEmail(), utilisateur.getMotDePasse());
                cn.close();
        }
        else
        {
            System.out.println("utilisateur deja existant");
        }
        
       }
    public static void insererEtAfficherInscription(Formation formation,Utilisateur etudiant,Connection connection) throws SQLException{
                //ecriture de la requete
                String ajouterInscription="insert into inscriptions values(?,?,?)";
                System.out.println("id formation :"+formation.getId()+" id etudiant "+etudiant.getId()+"");
                //preparation de la requete
                PreparedStatement cn = connection.prepareStatement(ajouterInscription);
                cn.setInt(1,200);
                cn.setInt(2, etudiant.getId());
                cn.setInt(3, formation.getId());
                //execution de insert
                cn.executeUpdate();
                System.out.println("nouvelle inscription");
                cn.close();
    }

    public static void recupererFormation(Formation[] formations, Utilisateur[]utilisateurs,Connection connection,int nbFormation) throws SQLException
    {
        Statement statementFormation = connection.createStatement();
        String selectFormation = "SELECT * FROM formations"; 
        //execution select
        ResultSet resultSetFormation = statementFormation.executeQuery(selectFormation);
        int i = 0;
        while(resultSetFormation.next())
        {
            int idFormation = resultSetFormation.getInt("id");
            String titreFormation = resultSetFormation.getString("titre");
            String descriptionFormation = resultSetFormation.getString("description");
            double prixFormation = resultSetFormation.getDouble("prix");
            int formateurId = resultSetFormation.getInt("formateur_id");
            System.out.println("Formation avec l'ID: " + idFormation + ", titre: " + titreFormation + ", description: " + descriptionFormation+", prix:"+prixFormation );
            Formateur formateur = trouverFormateur(utilisateurs,formateurId);
            formations[i]= new Formation(idFormation,titreFormation, descriptionFormation, formateur, prixFormation);
            System.out.println("Pour le formateur de cette formation : id "+formations[i].getId() );
            System.out.println("nom : "+utilisateurs[i].getNom());
            System.out.println("email :"+utilisateurs[i].getEmail()+" et mot de passe "+ utilisateurs[i].getMotDePasse());
            i++;
            nbFormation++;
        }
    }
    public static Formateur trouverFormateur(Utilisateur[] utilisateurs,int id)
    {
        int i = 0;
        while (utilisateurs[i]!=null) {
            if (utilisateurs[i].getId() == id){
                System.out.println("formateur trouvé");
                return new Formateur(utilisateurs[i].getId(),utilisateurs[i].getNom(), utilisateurs[i].getEmail(), utilisateurs[i].getMotDePasse());      
            }           
            i++;
        }
        return null;
    }
    //suppression d'un utilisateurs
    public static void supprimerUtilisateur(Connection connection ,int id) throws SQLException
    {
        String supprimerSQL= "DELETE FROM utilisateurs where id = ?";
        PreparedStatement supprimerStatement = connection.prepareStatement(supprimerSQL);
        supprimerStatement.setInt(1, id);
        supprimerStatement.executeUpdate();
         // Vérification du nombre de lignes affectées
        int rowsAffected = supprimerStatement.executeUpdate();
        if (rowsAffected >= 0)
        {
            System.out.println("L'utilisateur a été supprimée avec succès.");
        }
        else
        {
            System.out.println("Aucun utilisateur n'a été trouvée avec cet ID.");
        }
        supprimerStatement.close();
    }
    // suppression d'une formation
    public static void supprimerFormation(Connection connection ,int id) throws SQLException
    {
        String supprimerSQL= "DELETE FROM formations where id = ?";
        PreparedStatement supprimerStatement = connection.prepareStatement(supprimerSQL);
        supprimerStatement.setInt(1, id);
        supprimerStatement.executeUpdate();
        // Vérifier le nombre de lignes affectées
        //rows affected : combien de lignes ont ete modifiées
        int rowsAffected = supprimerStatement.executeUpdate();
        if (rowsAffected >= 0)
        {
            System.out.println("La formation a été supprimée avec succès");
        }
        else
        {
            System.out.println("Aucune formation n'a été trouvée avec cet id");
        }
        supprimerStatement.close();
    }
    //SUPPRIMER UNE INSCRIPTION
    public static void supprimerInscription(Connection connection ,int id) throws SQLException
    {
        String supprimerSQL= "DELETE FROM inscriptions where id = ?";
        PreparedStatement supprimerStatement = connection.prepareStatement(supprimerSQL);
        supprimerStatement.setInt(1, id);
        supprimerStatement.executeUpdate();
        int rowsAffected = supprimerStatement.executeUpdate();
        if (rowsAffected >= 0)
        {
            System.out.println("L'inscription a été supprimée avec succès.");
        } 
        else
        {
            System.out.println("l'inscription n'a été trouvée avec cet id.");
        }
        supprimerStatement.close();
    }    
}

