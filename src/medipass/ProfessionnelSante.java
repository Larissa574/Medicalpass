import java.util.ArrayList;
import java.util.List;

public class ProfessionnelSante extends Utilisateur {
    private String specialite;

    public ProfessionnelSante(String nom, String prenom, String username, String password,
                             String specialite) {
        super(nom, prenom, username, password);
        this.specialite = specialite;
    }

    public String getSpecialite() {
        return specialite != null ? specialite : "Professionnel";
    }

    public void setSpecialite(String specialite) {
        this.specialite = specialite;
    }

    // Les méthodes de disponibilité sont maintenant dans Medecin uniquement


    // Méthodes héritées par Medecin et AideSoignant
    public void consulterDossierMedical(Patient patient) {
        if (patient == null) {
            System.out.println("Patient non trouvé.");
            return;
        }
        DossierMedical dossier = patient.getDossierMedical();
        if (dossier == null) {
            System.out.println("Aucun dossier médical pour ce patient.");
            return;
        }
        dossier.afficher();
    }

    public Patient rechercherPatientParId(int id, List<Patient> patients) {
        if (patients == null) return null;
        for (Patient p : patients) {
            if (p.getId() == id) {
                return p;
            }
        }
        return null;
    }

    public List<Patient> rechercherPatientParNom(String nom, List<Patient> patients) {
        if (patients == null || nom == null) return new ArrayList<>();
        List<Patient> resultats = new ArrayList<>();
        for (Patient p : patients) {
            if (p.getNom().toLowerCase().equals(nom.toLowerCase()) ||
                p.getPrenom().toLowerCase().equals(nom.toLowerCase())) {
                resultats.add(p);
            }
        }
        return resultats;
    }

    @Override
    public String getRole() {
        return "Professionnel de Santé";
    }

    @Override
    protected String getSupplementalInfo() {
        return ", Spécialité: " + (specialite != null ? specialite : "Non renseignée");
    }
}
