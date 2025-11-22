package medipass;

public class Consultation {

    private Medecin medecin;
    private Patient patient;
    private String date;
    private int heure;
    private boolean estProgrammee;

    public Consultation(Medecin medecin, Patient patient, String date, int heure) {
        this.medecin = medecin;
        this.patient = patient;
        this.date = date;
        this.heure = heure;
        this.estProgrammee = false;
    }

    public void programmer() {
        estProgrammee = true;
        System.out.println("Consultation programmée pour " + patient.getNom() + " avec " + medecin.getNom() + " à " + heure + "h le " + date + ".");
    }

    public void annuler() {
        estProgrammee = false;
        System.out.println("Consultation annulée pour " + patient.getNom()+".");
}
}
