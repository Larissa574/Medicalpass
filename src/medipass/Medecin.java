package medipass;

public class Medecin extends ProfessionnelSante {
	
	public Medecin(int id, String nom, String prenom, String username, String password, String specialite) {
        super(id, nom, prenom, username, password, specialite);
    }

    //Méthode d'un medecin
    public void suivreConsultatin(){
        System.out.printf(">>Programmer la consultation<<");
    }

    public void mettreAjourDossierMedical(){
        System.out.printf(">>Ajouter ou modifier le dossier medical<<");
    }
    
}
