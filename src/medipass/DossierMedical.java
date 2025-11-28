import java.util.ArrayList;
import java.util.List;

public class DossierMedical {
    private Patient patient;
    private List<Antecedent> antecedents;
    private List<Consultation> consultations;
    private List<ExamenMedical> examens;
    private List<Prescription> prescriptions;
    private List<String> constantes;
    private String groupeSanguin;
    private boolean archive;

    public DossierMedical(Patient patient) {
        this.patient = patient;
        this.antecedents = new ArrayList<>();
        this.consultations = new ArrayList<>();
        this.examens = new ArrayList<>();
        this.prescriptions = new ArrayList<>();
        this.constantes = new ArrayList<>();
        this.archive = false;
    }

    public Patient getPatient() {
        return patient;
    }

    public List<Antecedent> getAntecedents() {
        return antecedents;
    }

    public void ajouterAntecedent(Antecedent antecedent) {
        this.antecedents.add(antecedent);
    }

    public List<Consultation> getConsultations() {
        return consultations;
    }

    public void ajouterConsultation(Consultation consultation) {
        this.consultations.add(consultation);
    }

    public List<ExamenMedical> getExamens() {
        return examens;
    }

    public void ajouterExamen(ExamenMedical examen) {
        this.examens.add(examen);
    }

    public List<Prescription> getPrescriptions() {
        return prescriptions;
    }

    public void ajouterPrescription(Prescription prescription) {
        this.prescriptions.add(prescription);
    }

    public List<String> getConstantes() {
        return constantes;
    }

    public void ajouterConstante(String constante) {
        if (constante != null && !constante.isBlank()) {
            constantes.add(constante);
        }
    }

    public String getGroupeSanguin() {
        return groupeSanguin;
    }

    public void setGroupeSanguin(String groupeSanguin) {
        this.groupeSanguin = groupeSanguin;
    }

    public boolean isArchive() {
        return archive;
    }

    public void archiver() {
        this.archive = true;
    }

    public void desarchiver() {
        this.archive = false;
    }

    public void afficher() {
        System.out.println("\n=== Dossier Médical ===");
        patient.afficherDetails();
        System.out.println("Groupe sanguin: " + (groupeSanguin != null ? groupeSanguin : "Non renseigné"));
        System.out.println("Statut: " + (archive ? "Archivé" : "Actif"));
        
        System.out.println("\n--- Antécédents (" + antecedents.size() + ") ---");
        for (Antecedent ant : antecedents) {
            System.out.println("  - " + ant);
        }
        
        System.out.println("\n--- Consultations (" + consultations.size() + ") ---");
        for (Consultation cons : consultations) {
            ProfessionnelSante professionnel = cons.getProfessionnel();
            String medecin = professionnel != null ? professionnel.getNom() + " " + professionnel.getPrenom() : "Inconnu";
            System.out.println("  ID: " + cons.getId());
            System.out.println("    Date et heure: " + cons.getDateHeure());
            System.out.println("    Médecin: " + medecin);
            System.out.println("    Motif: " + (cons.getMotif() != null ? cons.getMotif() : "Non renseigné"));
            System.out.println("    Observation: " + (cons.getObservations() != null ? cons.getObservations() : "Non renseigné"));
        }
        
        System.out.println("\n--- Examens (" + examens.size() + ") ---");
        for (ExamenMedical exam : examens) {
            System.out.println("  - " + exam);
        }
        
        System.out.println("\n--- Prescriptions (" + prescriptions.size() + ") ---");
        for (Prescription presc : prescriptions) {
            System.out.println("  - " + presc);
        }

        System.out.println("\n--- Constantes récentes (" + constantes.size() + ") ---");
        for (String constante : constantes) {
            System.out.println("  - " + constante);
        }
    }
}
