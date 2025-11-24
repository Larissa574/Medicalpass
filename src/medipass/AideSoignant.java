package medipass;

public class AideSoignant extends ProfessionnelSante {

    private int id;
    private String nom;

    public AideSoignant(int id, String nom) {
        this.id = id;
        this.nom = nom;
    }

    public String getNom() {
        return nom;
    }

    // Vérifier si un médecin est disponible
    public boolean verifierDisponibiliteMedecin(Medecin medecin, String date, int heure) {
        if (medecin.estOccupe(date, heure)) {
            System.out.println("Le médecin " + medecin.getNom() + " n'est pas disponible à " + heure + "h le " + date + ".");
            return false;
        } else {
            System.out.println("Le médecin " + medecin.getNom() + " est disponible à " + heure + "h le " + date + ".");
            return true;
        }
    }

    // Planifier une consultation
    public void planifierConsultation(Consultation consultation) {
        System.out.println("Aide-soignant " + nom + " a planifié une consultation.");
        consultation.programmer();
    }

    // Annuler une consultation
    public void annulerConsultation(Consultation consultation) {
        System.out.println("Aide-soignant " + nom + " a annulé une consultation.");
        consultation.annuler();
    }

    // Prendre les signes vitaux
    public void enregistrerSignesVitaux(Patient p, String tension, float poids, float temperature) {
        System.out.println("Signes vitaux du patient " + p.getNom() + " :");
        System.out.println("- Tension : " + tension);
        System.out.println("- Poids : " + poids + " kg");
        System.out.println("- Température : " + temperature + "°C");
    }

    // Préparer la salle pour la consultation
    public void preparerSalleConsultation() {
        System.out.println("Salle de consultation préparée par l'aide-soignant " + nom+".");
}
}
