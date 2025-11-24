package medipass;

public class Administrateur {
	
	public Administrateur(int id, String nom, String prenom, String username, String password) {
        super(id, nom, prenom, username, password);

    }

    //Méthodes propres à l'admin
    public void afficherStatistiques(){
        System.out.printf(">>Affichage des statistiques.<<");
        //ajouter des lignes de codes
    }

    public void creerCompteUtilisateur(){
        System.out.printf(">>Compte utilisateur<<");
    }

    public void supprimerCompteUtilisateur(){
        System.out.printf(">>Suppression d'un  compte utilisateur<<");
    }
    
}
