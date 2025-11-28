import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public final class HospitalSession {
    private List<Utilisateur> utilisateurs;
    private List<Patient> patients;
    private List<Consultation> consultations;
    private List<ExamenMedical> examens;
    private List<Prescription> prescriptions;
    private Map<Integer, List<String>> disponibilitesParProfessionnel;
    private Utilisateur utilisateurConnecte;

    public HospitalSession() {
        this.utilisateurs = chargerUtilisateurs();
        List<ProfessionnelSante> professionnels = utilisateurs.stream()
                .filter(u -> u instanceof ProfessionnelSante)
                .map(u -> (ProfessionnelSante) u)
                .collect(Collectors.toList());
        this.patients = chargerPatients(professionnels);
        this.consultations = new ArrayList<>();
        this.examens = new ArrayList<>();
        this.prescriptions = new ArrayList<>();
        this.disponibilitesParProfessionnel = chargerDisponibilites();
        
        reconstruireCollections(patients, consultations, examens, prescriptions);
        chargerConsultationsProgrammes(patients, professionnels, consultations);
        synchroniserDisponibilites(disponibilitesParProfessionnel, professionnels);
    }

    public static List<Utilisateur> chargerUtilisateurs() {
        List<Utilisateur> utilisateurs = BaseDeDonnees.chargerUtilisateurs();
        if (utilisateurs == null) {
            utilisateurs = new ArrayList<>();
        }
        if (utilisateurs.stream().noneMatch(u -> u instanceof Administrateur)) {
            Administrateur admin = new Administrateur("Admin", "Système", "admin", "admin123");
            utilisateurs.add(admin);
            BaseDeDonnees.sauvegarderUtilisateurs(utilisateurs);
        }
        return utilisateurs;
    }

    public static List<Patient> chargerPatients(List<ProfessionnelSante> professionnels) {
        List<Patient> patients = BaseDeDonnees.chargerDossiers(professionnels);
        return patients == null ? new ArrayList<>() : patients;
    }

    public static void reconstruireCollections(List<Patient> patients, List<Consultation> consultations,
                                                 List<ExamenMedical> examens, List<Prescription> prescriptions) {
        consultations.clear();
        examens.clear();
        prescriptions.clear();
        for (Patient patient : patients) {
            DossierMedical dossier = patient.getDossierMedical();
            if (dossier == null) {
                continue;
            }
            for (Consultation consultation : dossier.getConsultations()) {
                if (!consultations.contains(consultation)) {
                    consultations.add(consultation);
                }
            }
            for (ExamenMedical examen : dossier.getExamens()) {
                if (!examens.contains(examen)) {
                    examens.add(examen);
                }
            }
            for (Prescription prescription : dossier.getPrescriptions()) {
                if (!prescriptions.contains(prescription)) {
                    prescriptions.add(prescription);
                }
            }
        }
    }

    public static void chargerConsultationsProgrammes(List<Patient> patients,
                                                        List<ProfessionnelSante> professionnels,
                                                        List<Consultation> consultations) {
        BaseDeDonnees.chargerConsultationsProgrammes(patients, professionnels, consultations);
    }

    public static Map<Integer, List<String>> chargerDisponibilites() {
        Map<Integer, List<String>> disponibilites = BaseDeDonnees.chargerDisponibilites();
        return disponibilites == null ? new HashMap<>() : disponibilites;
    }

    public static void synchroniserDisponibilites(Map<Integer, List<String>> disponibilites,
                                                   List<ProfessionnelSante> professionnels) {
        for (ProfessionnelSante professionnel : professionnels) {
            if (professionnel instanceof Medecin) {
                Medecin medecin = (Medecin) professionnel;
                List<String> creneaux = disponibilites.get(medecin.getId());
                medecin.getDisponibilites().clear();
                if (creneaux != null) {
                    medecin.getDisponibilites().addAll(creneaux);
                }
            }
        }
    }

    

    
}
