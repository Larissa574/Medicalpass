import java.util.ArrayList;
import java.util.List;

public class Administrateur extends Utilisateur {
    // L'administrateur ne peut PAS accéder aux données médicales
    // selon le cahier de charges: "L'administrateur ne peut pas accéder 
    // aux données médicales sans habilitation spécifique."
    // Son rôle: création de comptes, gestion des rôles et droits d'accès uniquement

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

        long admins = utilisateurs.stream().filter(u -> u instanceof Administrateur).count();
        if (cible instanceof Administrateur && admins <= 1) {
            System.out.println("Impossible de supprimer le dernier administrateur.");
            return false;
        }

        utilisateurs.remove(cible);
        BaseDeDonnees.sauvegarderUtilisateurs(utilisateurs);
        return true;
    }
    @Override
    public String getRole() {
        return "Administrateur";
    }
}
