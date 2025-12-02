package medipass;
import java.util.ArrayList;
import java.util.List;

public class Administrateur extends Utilisateur {
    public Administrateur(String nom, String prenom, String username, String password) {
        super(nom, prenom, username, password);
    }

    // Gestion des utilisateurs - RÉSERVÉE AUX ADMINISTRATEURS
    public static Utilisateur creerUtilisateur(String type, String nom, String prenom, String username, 
                                       String password, String specialite, List<Utilisateur> utilisateurs) {
        if (utilisateurs == null) {
            System.out.println("Erreur: liste des utilisateurs non initialisée.");
            return null;
        }
        
        Utilisateur utilisateur;
        String typeNormalise = type != null ? type.trim().toLowerCase() : "";
        switch (typeNormalise) {
            case "admin" -> utilisateur = new Administrateur(nom, prenom, username, password);
            case "medecin" -> utilisateur = new Medecin(nom, prenom, username, password, specialite);
            case "aide-soignant", "aide_soignant" -> utilisateur = new AideSoignant(nom, prenom, username, password);
            default -> {
                System.out.println("Type d'utilisateur inconnu.");
                return null;
            }
        }

        utilisateurs.add(utilisateur);
        BaseDeDonnees.sauvegarderUtilisateurs(utilisateurs);
        return utilisateur;
    }

    public static Utilisateur rechercherUtilisateur(int id, List<Utilisateur> utilisateurs) {
        if (utilisateurs == null) return null;
        for (Utilisateur u : utilisateurs) {
            if (u.getId() == id) {
                return u;
            }
        }
        return null;
    }

    public static List<Utilisateur> getTousLesUtilisateurs(List<Utilisateur> utilisateurs) {
        return utilisateurs == null ? new ArrayList<>() : new ArrayList<>(utilisateurs);
    }

    public static boolean supprimerUtilisateur(int id, List<Utilisateur> utilisateurs) {
        if (utilisateurs == null) return false;
        Utilisateur cible = rechercherUtilisateur(id, utilisateurs);
        if (cible == null) {
            return false;
        }

        
        utilisateurs.remove(cible);
        BaseDeDonnees.sauvegarderUtilisateurs(utilisateurs);
        return true;
    }

    public static void listerTousLesUtilisateurs(List<Utilisateur> utilisateurs) {
        System.out.println("\n--- LISTE DES UTILISATEURS ---");
        List<Utilisateur> tous = getTousLesUtilisateurs(utilisateurs);
        for (Utilisateur u : tous) {
            String ligne = "ID: " + u.getId() + " | " + u.getNom() + " " + u.getPrenom() + 
                          " | " + u.getUsername() + " | " + u.getRole();
            
            
            if (u instanceof Medecin) {
                Medecin medecin = (Medecin) u;
                ligne += " | " + medecin.getSpecialite();
            }
            
            System.out.println(ligne);
        }
    }

    public static void afficherStatistiques(List<Utilisateur> utilisateurs, List<Patient> patients, List<Consultation> consultations) {
        System.out.println("\n========== STATISTIQUES DU SYSTÈME ==========");
        System.out.println("Nombre total de patients: " + patients.size());
        
        long patientsActifs = patients.stream()
                                     .filter(p -> !p.getDossierMedical().isArchive())
                                     .count();
        System.out.println("Patients actifs: " + patientsActifs);
        System.out.println("Patients archivés: " + (patients.size() - patientsActifs));
        System.out.println("\nNombre total d'utilisateurs: " + utilisateurs.size());
        
        long admins = utilisateurs.stream()
                .filter(u -> u instanceof Administrateur)
                .count();
        System.out.println("  - Administrateurs: " + admins);
        
        List<ProfessionnelSante> professionnelsSante = utilisateurs.stream()
                .filter(u -> u instanceof ProfessionnelSante)
                .map(u -> (ProfessionnelSante) u)
                .collect(java.util.stream.Collectors.toList());
        System.out.println("  - Professionnels de santé: " + professionnelsSante.size());
        
        // Statistiques par spécialité (uniquement pour les médecins)
        System.out.println("\nMédecins par spécialité:");
        utilisateurs.stream()
                .filter(u -> u instanceof Medecin)
                .map(u -> (Medecin) u)
                .filter(m -> m.getSpecialite() != null && !m.getSpecialite().trim().isEmpty())
                .collect(java.util.stream.Collectors.groupingBy(Medecin::getSpecialite, java.util.stream.Collectors.counting()))
                .forEach((specialite, count) -> 
                    System.out.println("  - " + specialite + ": " + count));
        
        System.out.println("\nNombre total de consultations: " + consultations.size());
        
        // Consultations par statut
        System.out.println("\nConsultations par statut:");
        consultations.stream()
                .collect(java.util.stream.Collectors.groupingBy(Consultation::getStatut, java.util.stream.Collectors.counting()))
                .forEach((statut, count) -> 
                    System.out.println("  - " + statut + ": " + count));
        
        System.out.println("==============================================\n");
    }
    @Override
    public String getRole() {
        return "Administrateur";
    }
}
