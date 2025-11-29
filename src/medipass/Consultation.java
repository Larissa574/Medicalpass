package medipass;
public class Consultation {
    private static int compteurId = 1;
    private int id;
    private String dateHeure;
    private Patient patient;
    private ProfessionnelSante professionnel;
    private String motif;
    private String observations;
    private String diagnostic;
    private String statut; // En cours, Terminée, Annulée
    private String specialite;
    private String programmeurNom;
    private String programmeurPrenom;

    public Consultation(String dateHeure, Patient patient, ProfessionnelSante professionnel, 
                       String motif, String observations) {
        this.id = compteurId++;
        this.dateHeure = dateHeure;
        this.patient = patient;
        this.professionnel = professionnel;
        this.motif = motif;
        this.observations = observations;
        this.statut = "En cours";
        this.specialite = professionnel.getSpecialite();
    }

    public int getId() {
        return id;
    }

    public String getDateHeure() {
        return dateHeure;
    }

    public void setDateHeure(String dateHeure) {
        this.dateHeure = dateHeure;
    }

    public Patient getPatient() {
        return patient;
    }

    public void setPatient(Patient patient) {
        this.patient = patient;
    }

    public ProfessionnelSante getProfessionnel() {
        return professionnel;
    }

    public void setProfessionnel(ProfessionnelSante professionnel) {
        this.professionnel = professionnel;
    }

    public String getMotif() {
        return motif;
    }

    public void setMotif(String motif) {
        this.motif = motif;
    }

    public String getObservations() {
        return observations;
    }

    public void setObservations(String observations) {
        this.observations = observations;
    }

    public String getDiagnostic() {
        return diagnostic;
    }

    public void setDiagnostic(String diagnostic) {
        this.diagnostic = diagnostic;
    }

    public String getStatut() {
        return statut;
    }

    public void setStatut(String statut) {
        this.statut = statut;
    }

    public String getSpecialite() {
        return specialite;
    }

    public void setSpecialite(String specialite) {
        this.specialite = specialite;
    }

    public String getProgrammeurNom() {
        return programmeurNom;
    }

    public String getProgrammeurPrenom() {
        return programmeurPrenom;
    }

    public void setProgrammeur(String nom, String prenom) {
        this.programmeurNom = nom;
        this.programmeurPrenom = prenom;
    }

    public void setIdPourPersistence(int id) {
        this.id = id;
        if (id >= compteurId) {
            compteurId = id + 1;
        }
    }

    @Override
    public String toString() {
        return "Consultation [ID: " + id + ", Date: " + dateHeure + 
               ", Patient: " + patient.getNom() + " " + patient.getPrenom() +
               ", Médecin: " + professionnel.getNom() + " " + professionnel.getPrenom() +
               ", Motif: " + motif + ", Statut: " + statut + "]";
    }

    public void afficherDetails() {
        System.out.println("\n=== Détails de la Consultation ===");
        System.out.println("ID: " + id);
        System.out.println("Date et heure: " + dateHeure);
        System.out.println("Patient: " + patient.getNom() + " " + patient.getPrenom() + " (ID: " + patient.getId() + ")");
        System.out.println("Professionnel: " + professionnel.getNom() + " " + professionnel.getPrenom() + 
                          " (" + professionnel.getSpecialite() + ")");
        System.out.println("Spécialité: " + specialite);
        System.out.println("Motif: " + motif);
        System.out.println("Observations: " + observations);
        System.out.println("Diagnostic: " + (diagnostic != null ? diagnostic : "Non renseigné"));
        System.out.println("Statut: " + statut);
    }
}
