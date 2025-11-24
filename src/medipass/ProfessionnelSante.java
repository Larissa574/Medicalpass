package medipass;

public class ProfessionnelSante extends Utilisateur {

	protected String specialite;

    public ProfessionnelSante(int id, String nom, String prenom, String username, String password, String specialite) {
        super(id, nom, prenom, username, password);
        this.specialite = specialite;
    }

    public String getSpecialite(){return specialite;}

    public void setSpecialite(String specialite){this.specialite = specialite;}

    //Méthode des professionnels de la santé
    public void creerDossierMedical (){
        System.out.printf(">>Créer dossier Medical<<");
    }
}
