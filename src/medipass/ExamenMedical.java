public class ExamenMedical {
    private static int compteurId = 1;
    private int id;
    private String type;
    private String nom;
    private String date;
    private Patient patient;
    private ProfessionnelSante prescripteur;
    private String resultat;
    private String commentaire;

    public ExamenMedical(String type, String nom, String date, Patient patient, ProfessionnelSante prescripteur) {
        this.id = compteurId++;
        this.type = type;
        this.nom = nom;
        this.date = date;
        this.patient = patient;
        this.prescripteur = prescripteur;
    }

    public int getId() {
        return id;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getNom() {
        return nom;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public Patient getPatient() {
        return patient;
    }

    public void setPatient(Patient patient) {
        this.patient = patient;
    }

    public ProfessionnelSante getPrescripteur() {
        return prescripteur;
    }

    public void setPrescripteur(ProfessionnelSante prescripteur) {
        this.prescripteur = prescripteur;
    }

    public String getResultat() {
        return resultat;
    }

    public void setResultat(String resultat) {
        this.resultat = resultat;
    }

    public String getCommentaire() {
        return commentaire;
    }

    public void setCommentaire(String commentaire) {
        this.commentaire = commentaire;
    }

    public void setIdPourPersistence(int id) {
        this.id = id;
        if (id >= compteurId) {
            compteurId = id + 1;
        }
    }

    @Override
    public String toString() {
        String resultatAffiche = resultat != null && !resultat.isBlank() ? resultat : "Non renseigné";
        String commentaireAffiche = commentaire != null && !commentaire.isBlank() ? commentaire : "Non renseigné";
        return "Examen [ID: " + id + ", Type: " + type + ", Nom: " + (nom != null ? nom : "Non renseigné") + ", Date: " + date + ", Patient: " + patient.getNom() + " " + patient.getPrenom() + ", Résultat: " + resultatAffiche + ", Commentaire: " + commentaireAffiche + "]";
    }
}
