package medipass;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class DossierMedical {

    private static int compteurId = 0;
    private String idDossier;
    private Patient patient;
    private LocalDate dateCreation;
    private List<Antecedent> antecedents;
    private List<String> idsConsultations;

    
    public DossierMedical(Patient patient) {
        if (patient == null) {
            throw new IllegalArgumentException("Le patient est obligatoire");
        }

        compteurId++;
        this.idDossier = "DM-" + String.format("%05d", compteurId);
        this.patient = patient;
        this.dateCreation = LocalDate.now();
        this.antecedents = new ArrayList<>();
        this.idsConsultations = new ArrayList<>();
    }

    
    public Antecedent ajouterAntecedent(String type, String description,
                                        LocalDate date, String gravite) {
        Antecedent antecedent = new Antecedent(type, description, date, gravite);
        antecedents.add(antecedent);
        return antecedent;
    }

    public Antecedent ajouterAntecedent(String type, String description) {
        return ajouterAntecedent(type, description, null, "Modéré");
    }

    public Antecedent ajouterAllergie(String allergene, String gravite) {
        return ajouterAntecedent("Allergie", "Allergie à : " + allergene, null, gravite);
    }

    public Antecedent ajouterAllergie(String allergene) {
        return ajouterAllergie(allergene, "Modéré");
    }

    public List<Antecedent> getAntecedents() {
        return antecedents;
    }

    public List<Antecedent> getAllergies() {
        List<Antecedent> allergies = new ArrayList<>();
        for (Antecedent a : antecedents) {
            if (a.getType().equals("Allergie")) {
                allergies.add(a);
            }
        }
        return allergies;
    }

    public boolean aDesAllergies() {
        return !getAllergies().isEmpty();
    }

 
    
    public void ajouterConsultation(String idConsultation) {
        idsConsultations.add(idConsultation);
    }

    public List<String> getIdsConsultations() {
        return idsConsultations;
    }

    
    public String getIdDossier() {
        return idDossier;
    }

    public Patient getPatient() {
        return patient;
    }

    public LocalDate getDateCreation() {
        return dateCreation;
    }

    
    public String afficherResume() {
        StringBuilder sb = new StringBuilder();

        sb.append("========== DOSSIER MÉDICAL ==========\n");
        sb.append("ID Dossier: ").append(idDossier).append("\n");
        sb.append("Patient: ").append(patient.getNomComplet()).append("\n");
        sb.append("Âge: ").append(patient.getAge()).append(" ans\n");
        sb.append("Créé le: ").append(dateCreation).append("\n\n");

     
        List<Antecedent> allergies = getAllergies();
        if (!allergies.isEmpty()) {
            sb.append("⚠ ALLERGIES:\n");
            for (Antecedent a : allergies) {
                sb.append("  - ").append(a.getDescription());
                sb.append(" [").append(a.getGravite()).append("]\n");
            }
            sb.append("\n");
        }

        sb.append("ANTÉCÉDENTS (").append(antecedents.size()).append("):\n");
        if (antecedents.isEmpty()) {
            sb.append("  Aucun antécédent\n");
        } else {
            for (Antecedent a : antecedents) {
                sb.append("  - ").append(a.toString()).append("\n");
            }
        }

      
        
        return sb.toString();  
    }

   
    public String toString() {
        return "DossierMedical [" + idDossier + "] - Patient: " + patient.getNomComplet();
    }
}