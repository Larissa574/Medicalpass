package medipass;
import java.util.ArrayList;
import java.util.List;

public class Prescription {
    private static int compteurId = 1;
    private int id;
    private String date;
    private Patient patient;
    private ProfessionnelSante medecin;
    private List<Medicament> medicaments;
    private String duree;

    public Prescription(String date, Patient patient, ProfessionnelSante medecin, String duree) {
        this.id = compteurId++;
        this.date = date;
        this.patient = patient;
        this.medecin = medecin;
        this.duree = duree;
        this.medicaments = new ArrayList<>();
    }

    public int getId() {
        return id;
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

    public ProfessionnelSante getMedecin() {
        return medecin;
    }

    public void setMedecin(ProfessionnelSante medecin) {
        this.medecin = medecin;
    }

    public List<Medicament> getMedicaments() {
        return medicaments;
    }

    public void ajouterMedicament(String nom, String posologie, String dureeTraitement) {
        Medicament medicament = new Medicament(nom, posologie, dureeTraitement);
        this.medicaments.add(medicament);
    }

    public String getDuree() {
        return duree;
    }

    public void setDuree(String duree) {
        this.duree = duree;
    }

    @Override
    public String toString() {
        StringBuilder meds = new StringBuilder();
        for (Medicament med : medicaments) {
            meds.append(med.getNom()).append(" (")
                .append(med.getPosologie()).append(", Durée: ").append(med.getDureeTraitement()).append("); ");
        }
        String listeMeds = meds.toString().isBlank() ? "Aucun médicament" : meds.toString();
        return "Prescription [ID: " + id + ", Date: " + date + ", Patient: " + 
               patient.getNom() + " " + patient.getPrenom() + 
               ", Médecin: " + medecin.getNom() + " " + medecin.getPrenom() + 
               ", Médicaments: " + listeMeds + "]";
    }

    public void setIdPourPersistence(int id) {
        this.id = id;
        if (id >= compteurId) {
            compteurId = id + 1;
        }
    }
}

class Medicament {
    private String nom;
    private String posologie;
    private String dureeTraitement;

    public Medicament(String nom, String posologie, String dureeTraitement) {
        this.nom = nom;
        this.posologie = posologie;
        this.dureeTraitement = dureeTraitement;
    }

    public String getNom() {
        return nom;
    }

    public String getPosologie() {
        return posologie;
    }
    
    public String getDureeTraitement() {
        return dureeTraitement;
    }

    @Override
    public String toString() {
        return nom + " - " + posologie + " - " + dureeTraitement;
    }
}
