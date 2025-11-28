import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class Medecin extends ProfessionnelSante {
    private List<ExamenMedical> examens;
    private List<Prescription> prescriptions;
    private List<Patient> patients;
    private Map<Integer, List<String>> disponibilitesParProfessionnel;
    private List<String> disponibilites;
    private List<Utilisateur> utilisateurs;
    private List<Consultation> consultations;

    public Medecin(String nom, String prenom, String username, String password,
                   String specialite) {
        super(nom, prenom, username, password, specialite);
        this.examens = null;
        this.prescriptions = null;
        this.patients = null;
        this.disponibilitesParProfessionnel = null;
        this.disponibilites = new ArrayList<>();
        this.utilisateurs = null;
        this.consultations = null;
    }

    public void setData(List<Patient> patients, List<Consultation> consultations, List<Utilisateur> utilisateurs,
                       Map<Integer, List<String>> disponibilitesParProfessionnel) {
        this.patients = patients == null ? new ArrayList<>() : patients;
        this.consultations = consultations == null ? new ArrayList<>() : consultations;
        this.utilisateurs = utilisateurs == null ? new ArrayList<>() : utilisateurs;
        this.disponibilitesParProfessionnel = disponibilitesParProfessionnel == null ? new java.util.HashMap<>() : disponibilitesParProfessionnel;
        
        // Critique: Peupler les disponibilités du médecin depuis la map globale
        this.disponibilites = this.disponibilitesParProfessionnel.get(this.getId());
        if (this.disponibilites == null) {
            this.disponibilites = new ArrayList<>();
        }
        
        this.examens = new ArrayList<>();
        this.prescriptions = new ArrayList<>();
        for (Patient p : this.patients) {
            if (p.getDossierMedical() != null) {
                this.examens.addAll(p.getDossierMedical().getExamens());
                this.prescriptions.addAll(p.getDossierMedical().getPrescriptions());
            }
        }
    }

    public void faireConsultation(Patient patient, String dateHeure, String motif, String observations, String diagnostic) {
        if (patient == null || dateHeure == null || dateHeure.isBlank()) {
            return;
        }
        
        // Chercher la consultation existante pour ce patient à cette date
        Consultation consultationExistante = null;
        if (consultations != null) {
            for (Consultation c : consultations) {
                if (c.getPatient().getId() == patient.getId() && 
                    c.getDateHeure().equals(dateHeure)) {
                    consultationExistante = c;
                    break;
                }
            }
        }
        
        if (consultationExistante != null) {
            // Mettre à jour la consultation existante en préservant les données du programmeur
            consultationExistante.setMotif(motif);
            consultationExistante.setObservations(observations);
            consultationExistante.setDiagnostic(diagnostic);
            consultationExistante.setStatut("Terminée");
        } else {
            // Créer une nouvelle consultation si aucune n'existe
            Consultation consultation = new Consultation(dateHeure, patient, this, motif, observations);
            consultation.setDiagnostic(diagnostic);
            consultation.setStatut("Terminée");
            
            if (consultations != null) {
                consultations.add(consultation);
                patient.getDossierMedical().ajouterConsultation(consultation);
            }
            }
        }
    
    // Plus besoin de stocker dans ProfessionnelSante - géré globalement
    private void sauvegarderConsultations() {
        BaseDeDonnees.sauvegarderDossiers(patients);
        BaseDeDonnees.sauvegarderConsultationsProgrammes(consultations);
    }

    public void voirMesConsultations() {
        if (consultations == null || consultations.isEmpty()) {
            System.out.println("Aucune consultation à suivre.");
            return;
        }
        System.out.println("=== Consultations du médecin " + getNom() + " " + getPrenom() + " ===");
        for (Consultation consultation : consultations) {
            if (consultation.getProfessionnel().getId() == this.getId()) {
                consultation.afficherDetails();
                System.out.println("---");
            }
        }
    }

    public List<ProfessionnelSante> getProfessionnelsSante() {
        if (utilisateurs == null) return new ArrayList<>();
        return utilisateurs.stream()
                          .filter(u -> u instanceof ProfessionnelSante)
                          .map(u -> u instanceof ProfessionnelSante ? (ProfessionnelSante) u : null)
                          .filter(java.util.Objects::nonNull)
                          .collect(Collectors.toList());
    }

    // Gestion des disponibilités du médecin
    public void gererDisponibilites(String action, String creneau) {
        if (action == null || creneau == null || creneau.isBlank()) {
            return;
        }
        if (action.equalsIgnoreCase("ajouter")) {
            this.ajouterDisponibilite(creneau);
        } else if (action.equalsIgnoreCase("supprimer")) {
            this.supprimerDisponibilite(creneau);
        }
    }

    // Les méthodes de gestion des disponibilités sont maintenant directes dans Medecin

    public void ajouterExamenMedical(ExamenMedical examen) {
        if (examens == null) {
            System.out.println("Erreur: liste des examens non initialisée.");
            return;
        }
        if (examen == null) {
            return;
        }
        examens.add(examen);
        if (examen.getPatient() != null) {
            examen.getPatient().getDossierMedical().ajouterExamen(examen);
        }
        BaseDeDonnees.sauvegarderDossiers(patients);
    }

    public void fairePrescription(Prescription prescription) {
        if (prescriptions == null) {
            System.out.println("Erreur: liste des prescriptions non initialisée.");
            return;
        }
        if (prescription == null) {
            return;
        }
        prescriptions.add(prescription);
        if (prescription.getPatient() != null) {
            prescription.getPatient().getDossierMedical().ajouterPrescription(prescription);
        }
        BaseDeDonnees.sauvegarderDossiers(patients);
    }
    // Méthodes de disponibilité spécifiques aux médecins
    public List<String> getDisponibilites() {
        return disponibilites;
    }

    public void ajouterDisponibilite(String creneau) {
        this.disponibilites.add(creneau);
    }

    public void supprimerDisponibilite(String creneau) {
        this.disponibilites.remove(creneau);
    }

    public boolean estDisponible(String dateHeure, List<Consultation> consultations) {
        if (dateHeure == null || dateHeure.isBlank()) {
            return false;
        }
        String[] parts = dateHeure.split(" ");
        if (parts.length < 2) {
            return false;
        }
        String date = parts[0];
        String heure = parts[1];
        
        // Vérifier les créneaux de disponibilité
        for (String creneau : disponibilites) {
            if (creneau == null) {
                continue;
            }
            String[] segments = creneau.split(" ", 3);
            
            // Gérer les créneaux généraux (sans date spécifique)
            if (segments.length == 1) {
                String[] limites = segments[0].split("-", 2);
                if (limites.length >= 2) {
                    String debut = limites[0];
                    String fin = limites[1];
                    
                    if (heure.compareTo(debut) >= 0 && heure.compareTo(fin) <= 0) {
                        // L'heure est dans le créneau, vérifier seulement les conflits de consultations non terminées
                        for (Consultation consultation : consultations) {
                            if (consultation.getDateHeure().equals(dateHeure) && 
                                !consultation.getStatut().equalsIgnoreCase("Terminée")) {
                                return false; // Conflit avec une consultation non terminée
                            }
                        }
                        return true; // Créneau valide et pas de conflit
                    }
                }
            } 
            // Gérer les créneaux récurrents et spécifiques
            else if (segments.length >= 2) {
                // Vérifier si c'est un créneau récurrent (tous les lundi, mardi, etc.)
                if (creneau.toLowerCase().startsWith("tous les")) {
                    String[] parties = creneau.split(" ", 4); // [tous, les, jour, horaire]
                    if (parties.length >= 4) {
                        String horaire = parties[3]; // "12:00-15:00"
                        String[] limites = horaire.split("-", 2);
                        if (limites.length >= 2) {
                            String debut = limites[0];
                            String fin = limites[1];
                            
                            if (heure.compareTo(debut) >= 0 && heure.compareTo(fin) <= 0) {
                                // L'heure est dans le créneau, vérifier seulement les conflits
                                for (Consultation consultation : consultations) {
                                    if (consultation.getDateHeure().equals(dateHeure) && 
                                        !consultation.getStatut().equalsIgnoreCase("Terminée")) {
                                        return false; // Conflit avec une consultation non terminée
                                    }
                                }
                                return true; // Créneau valide et pas de conflit
                            }
                        }
                    }
                }
                // Créneau spécifique à une date exacte
                else if (segments[0].equals(date)) {
                    String[] limites = segments[1].split("-", 2);
                    if (limites.length >= 2) {
                        String debut = limites[0];
                        String fin = limites[1];
                        
                        if (heure.compareTo(debut) >= 0 && heure.compareTo(fin) <= 0) {
                            // L'heure est dans le créneau spécifique, vérifier seulement les conflits
                            for (Consultation consultation : consultations) {
                                if (consultation.getDateHeure().equals(dateHeure) && 
                                    !consultation.getStatut().equalsIgnoreCase("Terminée")) {
                                    return false; // Conflit avec une consultation non terminée
                                }
                            }
                            return true; // Créneau valide et pas de conflit
                        }
                    }
                }
            }
        }
        
        return false; // Aucun créneau trouvé pour cette heure
    }

    @Override
    public String getRole() {
        return "Médecin";
    }
}
