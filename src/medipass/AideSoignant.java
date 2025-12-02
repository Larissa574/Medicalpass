package medipass;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.HashMap;
import java.util.stream.Collectors;

public class AideSoignant extends ProfessionnelSante {

    private List<Patient> patients;
    private List<Consultation> consultations;
    private List<Utilisateur> utilisateurs;
    private Map<Integer, List<String>> disponibilites;

    public AideSoignant(String nom, String prenom, String username, String password) {
        super(nom, prenom, username, password, "");
        this.patients = null;
        this.consultations = null;
        this.utilisateurs = null;
        this.disponibilites = null;
    }

    public void setData(List<Patient> patients, List<Consultation> consultations, List<Utilisateur> utilisateurs, Map<Integer, List<String>> disponibilites) {
        this.patients = patients == null ? new ArrayList<>() : patients;
        this.consultations = consultations == null ? new ArrayList<>() : consultations;
        this.utilisateurs = utilisateurs == null ? new ArrayList<>() : utilisateurs;
        this.disponibilites = disponibilites == null ? new HashMap<>() : disponibilites;
    }

    //Gestion des patients
    public Patient creerDossierMedical(String nom, String prenom,
                                        String dateNaissance, String adresse, String telephone,
                                        String email, String groupeSanguin) {
        if (patients == null) {
            System.out.println("Erreur: liste des patients non initialisée.");
            return null;
        }
        Patient patient = new Patient(nom, prenom, dateNaissance, adresse, telephone, email);
        if (groupeSanguin != null && !groupeSanguin.isBlank()) {
            patient.getDossierMedical().setGroupeSanguin(groupeSanguin);
        }
        patients.add(patient);
        BaseDeDonnees.sauvegarderDossiers(patients);
        return patient;
    }

    public List<Patient> getPatients() {
        return patients == null ? new ArrayList<>() : new ArrayList<>(patients);
    }

    public List<Patient> getPatientsActifs() {
        if (patients == null) return new ArrayList<>();
        return patients.stream()
                       .filter(p -> !p.getDossierMedical().isArchive())
                       .collect(Collectors.toList());
    }

    public List<Patient> getPatientsArchives() {
        if (patients == null) return new ArrayList<>();
        return patients.stream()
                       .filter(p -> p.getDossierMedical().isArchive())
                       .collect(Collectors.toList());
    }

    public void archiverDossierMedical(Patient patient) {
        if (patient == null) {
            return;
        }
        DossierMedical dossier = patient.getDossierMedical();
        if (dossier == null) {
            return;
        }
        if (dossier.isArchive()) {
            return;
        }
        dossier.archiver();
        BaseDeDonnees.sauvegarderDossiers(patients);
    }

    public void desarchiverDossierMedical(Patient patient) {
        if (patient == null) {
            return;
        }
        DossierMedical dossier = patient.getDossierMedical();
        if (dossier == null || !dossier.isArchive()) {
            return;
        }
        dossier.desarchiver();
        BaseDeDonnees.sauvegarderDossiers(patients);
    }

    public void mettreAJourConstantes(Patient patient, List<String> constantes) {
        if (patient == null || constantes == null || constantes.isEmpty()) {
            return;
        }
        DossierMedical dossier = patient.getDossierMedical();
        if (dossier == null) {
            return;
        }
        for (String constante : constantes) {
            dossier.ajouterConstante(constante);
        }
        BaseDeDonnees.sauvegarderDossiers(patients);
    }

    // Gestion des consultations

    public Consultation planifierConsultation(Patient patient,
                                               Medecin medecin, String dateHeure,
                                               String motif, String observations) {
        if (consultations == null) {
            System.out.println("Erreur: liste des consultations non initialisée.");
            return null;
        }
        if (patient == null || medecin == null || dateHeure == null || dateHeure.isBlank()) {
            return null;
        }
        if (!medecin.estDisponible(dateHeure, consultations)) {
            System.out.println("Le médecin n'est pas disponible à " + dateHeure + ".");
            return null;
        }
        Consultation consultation = new Consultation(dateHeure, patient, medecin, motif, observations);
        consultation.setStatut("non faite");
        consultations.add(consultation);
        patient.getDossierMedical().ajouterConsultation(consultation);
        // Plus besoin de stocker dans Medecin - géré globalement
        BaseDeDonnees.sauvegarderDossiers(patients);
        BaseDeDonnees.sauvegarderConsultationsProgrammes(consultations);
        return consultation;
    }

    public boolean annulerConsultation(Consultation consultation) {
        if (consultations == null) return false;
        if (consultation == null || !consultations.contains(consultation)) {
            return false;
        }
        consultation.setStatut("Annulée");
        // Plus besoin de gérer les disponibilités ici - les disponibilités sont gérées séparément
        BaseDeDonnees.sauvegarderDossiers(patients);
        BaseDeDonnees.sauvegarderConsultationsProgrammes(consultations);
        return true;
    }

    
    public void importerDonnees(String fichier) {
        System.out.println("\n--- IMPORT DES DONNÉES ---");
        System.out.println("(Appuyez sur Entrée pour annuler)");
        System.out.println("\n💡 INSTRUCTIONS :");
        System.out.println("- Mettez simplement le nom du fichier CSV (ex: monfichier.csv)");
        System.out.println("- OU mettez le chemin complet (ex: C:\\Users\\Downloads\\monfichier.csv)");
        System.out.println("- Le système cherchera automatiquement dans plusieurs emplacements");
        
        String fichierTrouve = null;
        if (fichier.contains(":")) {
            // C'est un chemin absolu, l'utiliser directement
            if (new java.io.File(fichier).exists()) {
                fichierTrouve = fichier;
            }
        } else {
            // Chercher le fichier dans plusieurs emplacements
            String[] cheminsPossibles = {
                System.getProperty("user.home") + "/Desktop/" + fichier, // Bureau
                System.getProperty("user.home") + "/Downloads/" + fichier, // Downloads
                System.getProperty("user.home") + "/Documents/" + fichier // Documents
            };
            
            for (String chemin : cheminsPossibles) {
                if (new java.io.File(chemin).exists()) {
                    fichierTrouve = chemin;
                    break;
                }
            }
        }
        
        if (fichierTrouve == null) {
            System.out.println("✗ Fichier '" + fichier + "' non trouvé.");
            if (fichier.contains(":")) {
                System.out.println("Le chemin spécifié n'existe pas.");
            } else {
                System.out.println("Emplacements recherchés :");
                String[] cheminsPossibles = {
                    System.getProperty("user.home") + "/Desktop/" + fichier, // Bureau
                    System.getProperty("user.home") + "/Downloads/" + fichier, // Downloads
                    System.getProperty("user.home") + "/Documents/" + fichier // Documents
                };
                for (String chemin : cheminsPossibles) {
                    System.out.println("  - " + chemin);
                }
            }
            System.out.println("\n💡 CONSEILS :");
            System.out.println("- Vérifiez que le chemin complet est correct");
            System.out.println("- Utilisez un chemin comme C:\\Users\\Downloads\\monfichier.csv");
            return;
        }
        
        System.out.println("✓ Fichier trouvé : " + fichierTrouve);
        
        // Importer selon le type de fichier (seulement patients supportés actuellement)
        if (fichier.toLowerCase().contains("patient") || fichier.toLowerCase().contains("utilisateur") || fichier.toLowerCase().contains("consultation")) {
            BaseDeDonnees.importerPatientsCSV(fichierTrouve, patients);
            System.out.println("✓ Patients importés avec succès");
        } else {
            // Import générique pour n'importe quel fichier CSV
            BaseDeDonnees.importerCSVGenerique(fichierTrouve);
            System.out.println("✓ Import réussi: " + fichierTrouve);
        }
    }

    public void exporterDonnees(java.util.Scanner scanner) {
        System.out.println("\n--- EXPORT DES DONNÉES ---");
        System.out.println("(Appuyez sur Entrée pour annuler)");
        System.out.println("1. Exporter les patients");
        System.out.println("2. Exporter les utilisateurs");
        System.out.println("3. Exporter les consultations");
        System.out.println("4. Exporter les dossiers archivés");
        System.out.println("5. Exporter les disponibilités");
        System.out.println("6. Exporter TOUTES les bases de données");
        System.out.print("Choix: ");
        
        String choix = scanner.nextLine().trim();
        if (choix.isEmpty()) return;
        
        try {
            // Créer le répertoire export s'il n'existe pas
            new java.io.File("export").mkdirs();
            
            switch (choix) {
                case "1":
                    BaseDeDonnees.exporterPatientsCSV(patients, "export/patients_export.csv");
                    System.out.println("✓ Patients exportés dans export/patients_export.csv");
                    break;
                case "2":
                    BaseDeDonnees.exporterUtilisateursCSV(utilisateurs, "export/utilisateurs_export.csv");
                    System.out.println("✓ Utilisateurs exportés dans export/utilisateurs_export.csv");
                    break;
                case "3":
                    BaseDeDonnees.exporterConsultationsProgrammesCSV(consultations, "export/consultations_export.csv");
                    System.out.println("✓ Consultations exportées dans export/consultations_export.csv");
                    break;
                case "4":
                    BaseDeDonnees.exporterDossiersArchivesCSV(patients, "export/dossiers_archives_export.csv");
                    System.out.println("✓ Dossiers archivés exportés dans export/dossiers_archives_export.csv");
                    break;
                case "5":
                    BaseDeDonnees.exporterDisponibilitesCSV(disponibilites, "export/disponibilites_export.csv");
                    System.out.println("✓ Disponibilités exportées dans export/disponibilites_export.csv");
                    break;
                case "6":
                    // Exporter toutes les bases
                    BaseDeDonnees.exporterUtilisateursCSV(utilisateurs, "export/utilisateurs_export.csv");
                    BaseDeDonnees.exporterPatientsCSV(patients, "export/patients_export.csv");
                    BaseDeDonnees.exporterDossiersArchivesCSV(patients, "export/dossiers_archives_export.csv");
                    BaseDeDonnees.exporterConsultationsProgrammesCSV(consultations, "export/consultations_export.csv");
                    BaseDeDonnees.exporterDisponibilitesCSV(disponibilites, "export/disponibilites_export.csv");
                    
                    System.out.println("✓ TOUTES les bases de données ont été exportées au format CSV dans le dossier 'export/'");
                    break;
                default:
                    System.out.println("Choix invalide.");
            }
        } catch (Exception e) {
            System.out.println("Erreur lors de l'export : " + e.getMessage());
        }
    }

    @Override
    public String getRole() {
        return "Aide-soignant";
    }
}
