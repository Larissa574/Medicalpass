package medipass;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Scanner;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

public class Main {
    private static Scanner scanner = new Scanner(System.in);
    private static Utilisateur utilisateurConnecte = null;
    
    // Données globales partagées
    private static List<Utilisateur> utilisateurs;
    private static List<Patient> patients;
    private static List<Consultation> consultations;
    private static Map<Integer, List<String>> disponibilites;

    // Instances des classes pour gérer les fonctionnalités
    private static AideSoignant aideSoignant = new AideSoignant("", "", "", "");
    private static Medecin medecin = new Medecin("", "", "", "", "");

    // Formatters
    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("dd/MM/yyyy");
    private static final DateTimeFormatter TIME_FORMATTER = DateTimeFormatter.ofPattern("HH:mm");

    public static void main(String[] args) {
        System.out.println("╔════════════════════════════════════════════════════════╗");
        System.out.println("║    SYSTÈME DE GESTION MÉDICALE - HÔPITAL CENTRAL      ║");
        System.out.println("╚════════════════════════════════════════════════════════╝");
        
        initialiserDonnees();
        seConnecter(); // Directement demande username/password
        
        while (true) {
            if (utilisateurConnecte != null) {
                afficherMenuSousService();
            } else {
                seConnecter(); // Redemande connexion si déconnecté
            }
        }
    }

    private static void initialiserDonnees() {
        utilisateurs = HospitalSession.chargerUtilisateurs();
        
        List<ProfessionnelSante> professionnels = utilisateurs.stream()
            .filter(u -> u instanceof ProfessionnelSante)
            .map(u -> (ProfessionnelSante) u)
            .collect(Collectors.toList());
        
        patients = HospitalSession.chargerPatients(professionnels);
        disponibilites = HospitalSession.chargerDisponibilites();
        consultations = new ArrayList<>();
        
        HospitalSession.synchroniserDisponibilites(disponibilites, professionnels);
        HospitalSession.chargerConsultationsProgrammes(patients, professionnels, consultations);
        HospitalSession.reconstruireCollections(patients, consultations, new ArrayList<>(), new ArrayList<>());
        
        // Initialiser les instances avec les données
        aideSoignant.setData(patients, consultations, utilisateurs, disponibilites);
        medecin.setData(patients, consultations, utilisateurs, disponibilites);
    }

    private static void seConnecter() {
        System.out.println("\n--- CONNEXION ---");
        System.out.print("Nom d'utilisateur: ");
        String username = scanner.nextLine().trim();
        
        if (username.equals("exit")) {
            System.out.println("Au revoir !");
            System.exit(0);
        }
        
        System.out.print("Mot de passe: ");
        String password = scanner.nextLine().trim();
        
        for (Utilisateur u : utilisateurs) {
            if (u.getUsername().equals(username) && u.getPassword().equals(password)) {
                utilisateurConnecte = u;
                // Mettre à jour l'objet statique aideSoignant avec l'utilisateur connecté
                if (u instanceof AideSoignant) {
                    aideSoignant = (AideSoignant) u;
                    aideSoignant.setData(patients, consultations, utilisateurs, disponibilites);
                }
                System.out.println("✓ Connexion réussie. Bienvenue " + u.getPrenom() + " " + u.getNom() + " (" + u.getRole() + ")");
                return;
            }
        }
        
        System.out.println("✗ Identifiants incorrects.");
    }

    private static void afficherMenuSousService() {
        if (utilisateurConnecte instanceof Administrateur) {
            menuAdministrateur();
        } else if (utilisateurConnecte instanceof Medecin) {
            menuMedecin();
        } else if (utilisateurConnecte instanceof AideSoignant) {
            menuAideSoignant();
        } else {
            System.out.println("Rôle non reconnu.");
            utilisateurConnecte = null;
        }
    }

    private static void menuAdministrateur() {
        System.out.println("\n--- MENU ADMINISTRATEUR ---");
        System.out.println("1. Créer un utilisateur");
        System.out.println("2. Lister les utilisateurs");
        System.out.println("3. Supprimer un utilisateur");
        System.out.println("4. Modifier mon mot de passe");
        System.out.println("5. Voir les statistiques");
        System.out.println("6. Se déconnecter");
        System.out.print("Choix: ");
        
        String choix = scanner.nextLine().trim();
        
        switch (choix) {
            case "1":
                creerUtilisateur();
                break;
            case "2":
                listerUtilisateurs();
                break;
            case "3":
                supprimerUtilisateur();
                break;
            case "4":
                modifierMotDePasse();
                break;
            case "5":
                afficherStatistiques();
                break;
            case "6":
                utilisateurConnecte = null;
                System.out.println("✓ Déconnecté.");
                break;
            default:
                System.out.println("Choix invalide.");
        }
    }

    private static void menuMedecin() {
        while (true) {
            System.out.println("\n--- MENU MÉDECIN ---");
            System.out.println("1. Rechercher un patient");
            System.out.println("2. Consulter dossier médical");
            System.out.println("3. Ajouter un examen médical");
            System.out.println("4. Faire une prescription");
            System.out.println("5. Gérer mes disponibilités");
            System.out.println("6. Voir mes consultations");
            System.out.println("7. Faire une consultation");
            System.out.println("8. Modifier mon mot de passe");
            System.out.println("9. Se déconnecter");
            System.out.print("Choix: ");
            
            String choix = scanner.nextLine().trim();
            
            switch (choix) {
                case "1":
                    rechercherPatient();
                    break;
                case "2":
                    consulterDossierMedical();
                    break;
                case "3":
                    ajouterExamenMedical();
                    break;
                case "4":
                    prescrireTraitement();
                    break;
                case "5":
                    gererDisponibilites();
                    break;
                case "6":
                    voirMesConsultationsMedecin();
                    break;
                case "7":
                    faireConsultation();
                    break;
                case "8":
                    modifierMotDePasse();
                    break;
                case "9":
                    utilisateurConnecte = null;
                    System.out.println("✓ Déconnecté.");
                    return;
                default:
                    System.out.println("Choix invalide. Veuillez réessayer.");
            }
        }
    }

    private static void menuAideSoignant() {
        System.out.println("\n--- MENU AIDE-SOIGNANT ---");
        System.out.println("1. Créer un dossier patient");
        System.out.println("2. Rechercher un patient");
        System.out.println("3. Consulter dossier médical");
        System.out.println("4. Planifier une consultation");
        System.out.println("5. Annuler une consultation");
        System.out.println("6. Mettre à jour les constantes");
        System.out.println("7. Exporter les données");
        System.out.println("8. Importer des patients");
        System.out.println("9. Archiver un dossier médical");
        System.out.println("10. Désarchiver un dossier médical");
        System.out.println("11. Modifier mon mot de passe");
        System.out.println("12. Se déconnecter");
        System.out.print("Choix: ");
        
        String choix = scanner.nextLine().trim();
        
        switch (choix) {
            case "1":
                creerDossierPatient();
                break;
            case "2":
                rechercherPatient();
                break;
            case "3":
                consulterDossierMedical();
                break;
            case "4":
                planifierConsultation();
                break;
            case "5":
                annulerConsultation();
                break;
            case "6":
                mettreAJourConstantes();
                break;
            case "7":
                exporterDonnees();
                break;
            case "8":
                importerDonnees();
                break;
            case "9":
                archiverDossierMedical();
                break;
            case "10":
                desarchiverDossierMedical();
                break;
            case "11":
                modifierMotDePasse();
                break;
            case "12":
                utilisateurConnecte = null;
                System.out.println("✓ Déconnecté.");
                break;
            default:
                System.out.println("Choix invalide.");
        }
    }

    // Méthodes utilitaires
    private static String lireChaineObligatoire(String message) {
        while (true) {
            System.out.print(message);
            String saisie = scanner.nextLine().trim();
            if (!saisie.isEmpty()) {
                return saisie;
            }
            System.out.println("Ce champ est obligatoire.");
        }
    }

    private static String lireChaineAvecRetour(String message) {
        System.out.print(message + ": ");
        String saisie = scanner.nextLine().trim();
        return saisie.isEmpty() ? null : saisie;
    }

    private static boolean validerMotDePasse(String password) {
        if (password == null || password.length() < 8) {
            return false;
        }
        
        boolean aChiffre = false;
        boolean aSpecial = false;
        
        for (char c : password.toCharArray()) {
            if (Character.isDigit(c)) {
                aChiffre = true;
            } else if (!Character.isLetterOrDigit(c)) {
                aSpecial = true;
            }
        }
        
        return aChiffre && aSpecial;
    }

    private static boolean validerEmail(String email) {
        if (email == null || email.isEmpty()) return false;
        return email.matches("^[\\w.-]+@[\\w.-]+\\.[a-zA-Z]{2,}$");
    }

    private static boolean validerDate(String date) {
        if (date == null || date.isEmpty()) return false;
        try {
            LocalDate parsed = LocalDate.parse(date, DATE_FORMATTER);
            return !parsed.isBefore(LocalDate.of(1900, 1, 1)) && 
                   !parsed.isAfter(LocalDate.of(2100, 12, 31));
        } catch (DateTimeParseException e) {
            return false;
        }
    }

    private static boolean validerGroupeSanguin(String groupe) {
        if (groupe == null || groupe.isEmpty()) return false;
        String[] groupesValides = {"A+", "A-", "B+", "B-", "AB+", "AB-", "O+", "O-"};
        for (String valide : groupesValides) {
            if (valide.equals(groupe)) return true;
        }
        return false;
    }

    private static boolean validerEmailUnique(String email) {
        if (email == null || email.isEmpty()) return false;
        
        for (Patient patient : patients) {
            if (patient.getEmail() != null && patient.getEmail().equalsIgnoreCase(email)) {
                return false; // Email déjà pris
            }
        }
        return true; // Email unique
    }

    private static boolean validerUsernameUnique(String username) {
        if (username == null || username.isEmpty()) return false;
        
        for (Utilisateur utilisateur : utilisateurs) {
            if (utilisateur.getUsername().equalsIgnoreCase(username)) {
                return false; // Username déjà pris
            }
        }
        return true; // Username unique
    }

    private static Integer lireEntierAvecRetour(String message) {
        while (true) {
            System.out.print(message + ": ");
            String saisie = scanner.nextLine().trim();
            if (saisie.isEmpty()) {
                return null;
            }
            try {
                return Integer.parseInt(saisie);
            } catch (NumberFormatException e) {
                System.out.println("Veuillez entrer un nombre entier valide.");
            }
        }
    }

    private static String lireDateAvecFormat(String message, String formatExemple, String regex) {
        while (true) {
            System.out.print(message + " (" + formatExemple + "): ");
            String saisie = scanner.nextLine().trim();
            if (saisie.isEmpty()) {
                return null;
            }
            if (Pattern.matches(regex, saisie)) {
                if (validerDate(saisie)) {
                    return saisie;
                } else {
                    System.out.println("Date hors limite (doit être entre 01/01/1900 et 31/12/2100).");
                }
            } else {
                System.out.println("Format invalide. Utilisez le format " + formatExemple);
            }
        }
    }

    private static String lireHeureAvecFormat(String message, String regex) {
        while (true) {
            System.out.print(message + " (HH:MM): ");
            String saisie = scanner.nextLine().trim();
            if (saisie.isEmpty()) {
                return null;
            }
            if (Pattern.matches(regex, saisie)) {
                try {
                    LocalTime.parse(saisie, TIME_FORMATTER);
                    return saisie;
                } catch (DateTimeParseException e) {
                    System.out.println("Heure invalide.");
                }
            } else {
                System.out.println("Format invalide. Utilisez le format HH:MM");
            }
        }
    }

    // Implémentations des fonctionnalités
    private static void creerUtilisateur() {
        System.out.println("\n--- CRÉATION D'UTILISATEUR ---");
        System.out.println("Types disponibles: admin, medecin, aide-soignant");
        System.out.println("(Appuyez sur Entrée pour annuler)");
        
        String type;
        do {
            type = lireChaineAvecRetour("Type d'utilisateur");
            if (type == null) return;
            
            // Validation stricte des types autorisés
            if (!type.equalsIgnoreCase("admin") && 
                !type.equalsIgnoreCase("medecin") && 
                !type.equalsIgnoreCase("aide-soignant")) {
                System.out.println("❌ Type invalide ! Seuls 'admin', 'medecin', ou 'aide-soignant' sont autorisés.");
                System.out.println("Veuillez réessayer ou appuyez sur Entrée pour annuler.");
                type = null; // Forcer la boucle à continuer
            }
        } while (type == null);
        
        String nom = lireChaineAvecRetour("Nom");
        if (nom == null) return;
        
        String prenom = lireChaineAvecRetour("Prénom");
        if (prenom == null) return;
        
        String username;
        do {
            username = lireChaineAvecRetour("Nom d'utilisateur");
            if (username == null) return;
            
            if (!validerUsernameUnique(username)) {
                System.out.println("✗ Ce nom d'utilisateur est déjà pris. Veuillez en choisir un autre.");
            }
        } while (!validerUsernameUnique(username));
        
        String password;
        do {
            password = lireChaineAvecRetour("Mot de passe (min 8 caractères, 1 chiffre, 1 caractère spécial)");
            if (password == null) return;
            
            if (!validerMotDePasse(password)) {
                System.out.println("✗ Mot de passe invalide. Doit contenir au moins 8 caractères, 1 chiffre et 1 caractère spécial.");
            }
        } while (!validerMotDePasse(password));
        
        String specialite = "";
        if (type.equals("medecin")) {
            specialite = lireChaineObligatoire("Spécialité:");
        }
        
        Utilisateur nouveau = Administrateur.creerUtilisateur(type, nom, prenom, username, password, specialite, utilisateurs);
        if (nouveau != null) {
            System.out.println("✓ Utilisateur créé avec succès (ID: " + nouveau.getId() + ")");
        }
    }

    private static void listerUtilisateurs() {
        System.out.println("\n--- LISTE DES UTILISATEURS ---");
        List<Utilisateur> tous = Administrateur.getTousLesUtilisateurs(utilisateurs);
        for (Utilisateur u : tous) {
            String ligne = "ID: " + u.getId() + " | " + u.getNom() + " " + u.getPrenom() + 
                          " | " + u.getUsername() + " | " + u.getRole();
            
            // Ajouter la spécialité pour les médecins
            if (u instanceof Medecin) {
                Medecin medecin = (Medecin) u;
                ligne += " | " + medecin.getSpecialite();
            }
            
            System.out.println(ligne);
        }
    }

    private static void supprimerUtilisateur() {
        System.out.println("\n--- SUPPRESSION UTILISATEUR ---");
        System.out.println("(Appuyez sur Entrée pour annuler)");
        Integer id = lireEntierAvecRetour("ID de l'utilisateur à supprimer");
        if (id == null) return;
        
        if (Administrateur.supprimerUtilisateur(id, utilisateurs)) {
            System.out.println("✓ Utilisateur supprimé.");
        } else {
            System.out.println("✗ Impossible de supprimer cet utilisateur.");
        }
    }

    private static void modifierMotDePasse() {
        System.out.println("\n--- MODIFICATION MOT DE PASSE ---");
        System.out.println("(Appuyez sur Entrée pour annuler)");
        
        String nouveau;
        do {
            nouveau = lireChaineAvecRetour("Nouveau mot de passe (min 8 caractères, 1 chiffre, 1 caractère spécial)");
            if (nouveau == null) return;
            
            if (!validerMotDePasse(nouveau)) {
                System.out.println("✗ Mot de passe invalide. Doit contenir au moins 8 caractères, 1 chiffre et 1 caractère spécial.");
                System.out.println("Veuillez réessayer ou appuyez sur Entrée pour annuler.");
            }
        } while (!validerMotDePasse(nouveau));
        
        // Alerte et re-saisie avant confirmation
        System.out.println("\n⚠️  ATTENTION : Vous êtes sur le point de modifier votre mot de passe !");
        System.out.print("Voulez-vous continuer ? (O/N): ");
        String alerte = scanner.nextLine().trim();
        
        if (!alerte.equalsIgnoreCase("O")) {
            System.out.println("✗ Modification de mot de passe annulée.");
            return;
        }
        
        System.out.println("\nVeuillez saisir à nouveau votre nouveau mot de passe pour confirmation:");
        String resaisie;
        do {
            resaisie = lireChaineAvecRetour("Retaper le nouveau mot de passe");
            if (resaisie == null) return;
            
            if (!nouveau.equals(resaisie)) {
                System.out.println("✗ Les mots de passe ne correspondent pas.");
                System.out.println("Veuillez réessayer ou appuyez sur Entrée pour annuler.");
            }
        } while (!nouveau.equals(resaisie));
        
        utilisateurConnecte.setPassword(nouveau);
        BaseDeDonnees.sauvegarderUtilisateurs(utilisateurs);
        System.out.println("✓ Mot de passe modifié avec succès.");
    }

    private static void afficherStatistiques() {
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
                .collect(Collectors.toList());
        System.out.println("  - Professionnels de santé: " + professionnelsSante.size());
        
        // Statistiques par spécialité (uniquement pour les médecins)
        System.out.println("\nMédecins par spécialité:");
        utilisateurs.stream()
                .filter(u -> u instanceof Medecin)
                .map(u -> (Medecin) u)
                .filter(m -> m.getSpecialite() != null && !m.getSpecialite().trim().isEmpty())
                .collect(Collectors.groupingBy(Medecin::getSpecialite, Collectors.counting()))
                .forEach((specialite, count) -> 
                    System.out.println("  - " + specialite + ": " + count));
        
        System.out.println("\nNombre total de consultations: " + consultations.size());
        
        // Consultations par statut
        System.out.println("\nConsultations par statut:");
        consultations.stream()
                .collect(Collectors.groupingBy(Consultation::getStatut, Collectors.counting()))
                .forEach((statut, count) -> 
                    System.out.println("  - " + statut + ": " + count));
        
        System.out.println("==============================================\n");
    }

    private static void voirMesConsultationsMedecin() {
        System.out.println("\n--- MES CONSULTATIONS ---");
        if (consultations.isEmpty()) {
            System.out.println("Aucune consultation programmée.");
            return;
        }
        
        // Filtrer les consultations pour le médecin connecté uniquement et non terminées
        List<Consultation> mesConsultations = consultations.stream()
            .filter(c -> c.getProfessionnel().getId() == utilisateurConnecte.getId()
                    && !c.getStatut().equalsIgnoreCase("Terminée"))
            .collect(Collectors.toList());
        
        if (mesConsultations.isEmpty()) {
            System.out.println("Aucune consultation à venir pour vous.");
            return;
        }
        
        System.out.println("Vos consultations à venir:");
        for (Consultation consultation : mesConsultations) {
            System.out.println("ID: " + consultation.getId() + 
                             " | Patient: " + consultation.getPatient().getNom() + " " + consultation.getPatient().getPrenom() +
                             " | Date: " + consultation.getDateHeure() +
                             " | Statut: " + consultation.getStatut());
        }
    }

    private static void creerDossierPatient() {
        System.out.println("\n--- CRÉATION DOSSIER PATIENT ---");
        System.out.println("(Appuyez sur Entrée pour annuler)");
        
        String nom = lireChaineAvecRetour("Nom");
        if (nom == null) return;
        
        String prenom = lireChaineAvecRetour("Prénom");
        if (prenom == null) return;
        
        String dateNaissance;
        do {
            dateNaissance = lireDateAvecFormat("Date de naissance", "JJ/MM/AAAA", "\\d{2}/\\d{2}/\\d{4}");
            if (dateNaissance == null) return;
        } while (!validerDate(dateNaissance));
        
        String adresse = lireChaineAvecRetour("Adresse");
        if (adresse == null) return;
        
        String telephone = lireChaineAvecRetour("Téléphone");
        if (telephone == null) return;
        
        String email;
        do {
            email = lireChaineAvecRetour("Email");
            if (email == null) return;
            
            if (!validerEmail(email)) {
                System.out.println("✗ Format d'email invalide.");
            } else if (!validerEmailUnique(email)) {
                System.out.println("✗ Cet email est déjà utilisé par un autre patient. Veuillez en choisir un autre.");
            }
        } while (!validerEmail(email) || !validerEmailUnique(email));
        
        String groupeSanguin;
        do {
            groupeSanguin = lireChaineAvecRetour("Groupe sanguin");
            if (groupeSanguin == null) return;
            
            if (!validerGroupeSanguin(groupeSanguin)) {
                System.out.println("✗ Groupe sanguin invalide.");
            }
        } while (!validerGroupeSanguin(groupeSanguin));
        
        Patient patient = aideSoignant.creerDossierMedical(nom, prenom, dateNaissance, adresse, telephone, email, groupeSanguin);
        if (patient != null) {
            System.out.println("✓ Dossier créé (ID: " + patient.getId() + ")");
        }
    }

    private static void rechercherPatient() {
        System.out.println("\n--- RECHERCHE PATIENT ---");
        System.out.println("(Appuyez sur Entrée pour annuler)");
        System.out.println("1. Rechercher par ID");
        System.out.println("2. Rechercher par nom");
        System.out.print("Choix: ");
        
        String choix = scanner.nextLine().trim();
        if (choix.isEmpty()) return;
        
        switch (choix) {
            case "1":
                rechercherPatientParId();
                break;
            case "2":
                rechercherPatientParNom();
                break;
            default:
                System.out.println("Choix invalide.");
        }
    }
    
    private static void rechercherPatientParId() {
        Integer id = lireEntierAvecRetour("ID du patient");
        if (id == null) return;
        
        Patient patient = aideSoignant.rechercherPatientParId(id, patients);
        if (patient != null) {
            afficherPatient(patient);
        } else {
            System.out.println("Patient non trouvé.");
        }
    }
    
    private static void rechercherPatientParNom() {
        String nom = lireChaineAvecRetour("Nom du patient");
        if (nom == null) return;
        
        // Utiliser la méthode héritée de ProfessionnelSante
        List<Patient> trouves = aideSoignant.rechercherPatientParNom(nom, patients);
        
        if (trouves.isEmpty()) {
            System.out.println("Aucun patient trouvé avec ce nom.");
        } else if (trouves.size() == 1) {
            afficherPatient(trouves.get(0));
        } else {
            System.out.println("Plusieurs patients trouvés:");
            for (Patient p : trouves) {
                System.out.println("ID: " + p.getId() + " | " + p.getNom() + " " + p.getPrenom() + 
                                 " | " + p.getDateNaissance());
            }
        }
    }
    
    private static void afficherPatient(Patient patient) {
        System.out.println("Patient trouvé:");
        System.out.println("ID: " + patient.getId());
        System.out.println("Nom: " + patient.getNom() + " " + patient.getPrenom());
        System.out.println("Date de naissance: " + patient.getDateNaissance());
        System.out.println("Adresse: " + patient.getAdresse());
        System.out.println("Téléphone: " + patient.getTelephone());
        System.out.println("Email: " + patient.getEmail());
    }

    private static void planifierConsultation() {
        System.out.println("\n--- PLANIFICATION CONSULTATION ---");
        System.out.println("(Appuyez sur Entrée pour annuler)");
        
        Integer idPatient = lireEntierAvecRetour("ID du patient");
        if (idPatient == null) return;
        
        Patient patient = aideSoignant.rechercherPatientParId(idPatient, patients);
        if (patient == null) {
            System.out.println("Patient non trouvé.");
            return;
        }
        
        // Afficher les médecins avec leurs disponibilités
        List<ProfessionnelSante> medecins = utilisateurs.stream()
            .filter(u -> u instanceof Medecin)
            .map(u -> (Medecin) u)
            .collect(Collectors.toList());
        
        if (medecins.isEmpty()) {
            System.out.println("Aucun médecin disponible.");
            return;
        }
        
        System.out.println("\n--- MÉDECINS ET DISPONIBILITÉS ---");
        for (ProfessionnelSante medecin : medecins) {
            System.out.println("\nID: " + medecin.getId() + " | " + medecin.getNom() + " " + medecin.getPrenom() + 
                             " (" + ((Medecin)medecin).getSpecialite() + ")");
            List<String> disponibilites = ((Medecin)medecin).getDisponibilites();
            if (disponibilites.isEmpty()) {
                System.out.println("  (Aucune disponibilité enregistrée)");
            } else {
                System.out.println("  Disponibilités:");
                for (String creneau : disponibilites) {
                    System.out.println("    - " + creneau);
                }
            }
        }
        
        Integer idMedecin = lireEntierAvecRetour("ID du médecin");
        if (idMedecin == null) return;
        
        Medecin medecinChoisi = medecins.stream()
            .filter(m -> m instanceof Medecin)
            .map(m -> (Medecin)m)
            .filter(m -> m.getId() == idMedecin)
            .findFirst()
            .orElse(null);
        
        if (medecinChoisi == null) {
            System.out.println("Médecin non trouvé.");
            return;
        }
        
        // Afficher les consultations existantes du médecin
        System.out.println("\n--- CONSULTATIONS DÉJÀ PROGRAMMÉES ---");
        List<Consultation> consultationsMedecin = consultations.stream()
            .filter(c -> c.getProfessionnel().getId() == medecinChoisi.getId() 
                    && !c.getStatut().equalsIgnoreCase("Terminée"))
            .collect(Collectors.toList());
        
        if (consultationsMedecin.isEmpty()) {
            System.out.println("✓ Aucune consultation programmée pour ce médecin.");
        } else {
            System.out.println("⚠️  CRÉNEAUX DÉJÀ OCCUPÉS :");
            for (Consultation consultation : consultationsMedecin) {
                System.out.println("  📅 " + consultation.getDateHeure() + 
                                 " | Patient: " + consultation.getPatient().getNom() + " " + consultation.getPatient().getPrenom() +
                                 " | Statut: " + consultation.getStatut());
            }
            System.out.println("\n⚠️  VEUILLEZ CHOISIR UN CRÉNEAU DIFFÉRENT DE CEUX CI-DESSUS");
        }
        
        List<String> disponibilites = medecinChoisi.getDisponibilites();
        if (disponibilites.isEmpty()) {
            System.out.println("Le médecin n'a aucun créneau disponible.");
            return;
        }
        
        System.out.println("\nSélectionnez un créneau disponible:");
        for (int i = 0; i < disponibilites.size(); i++) {
            System.out.println("  " + (i + 1) + ". " + disponibilites.get(i));
        }
        
        Integer indexCreneau = lireEntierAvecRetour("Numéro du créneau");
        if (indexCreneau == null) return;
        
        if (indexCreneau < 1 || indexCreneau > disponibilites.size()) {
            System.out.println("Créneau invalide.");
            return;
        }
        
        String creneauChoisi = disponibilites.get(indexCreneau - 1);
        
        // Extraire les heures du créneau pour validation
        String[] parties = creneauChoisi.split(" ");
        if (parties.length < 2) {
            System.out.println("Format de créneau invalide.");
            return;
        }
        
        String[] heures = parties[parties.length - 1].split("-");
        if (heures.length != 2) {
            System.out.println("Format d'heures invalide.");
            return;
        }
        
        LocalTime heureDebut = LocalTime.parse(heures[0], TIME_FORMATTER);
        LocalTime heureFin = LocalTime.parse(heures[1], TIME_FORMATTER);
        
        String dateHeure = lireDateAvecFormat("Date de la consultation", "JJ/MM/AAAA", "\\d{2}/\\d{2}/\\d{4}");
        if (dateHeure == null) return;
        
        String heure;
        LocalTime heureConsultation;
        boolean heureValide = false;
        
        do {
            heure = lireHeureAvecFormat("Heure", "\\d{2}:\\d{2}");
            if (heure == null) return;
            
            // Valider que l'heure est dans le créneau
            heureConsultation = LocalTime.parse(heure, TIME_FORMATTER);
            if (heureConsultation.isBefore(heureDebut) || heureConsultation.isAfter(heureFin)) {
                System.out.println("✗ L'heure " + heure + " n'est pas dans le créneau " + heures[0] + "-" + heures[1]);
                System.out.println("Veuillez entrer une heure valide.");
            } else {
                heureValide = true;
            }
        } while (!heureValide);
        
        dateHeure += " " + heure;
        
        Consultation consultation = aideSoignant.planifierConsultation(patient, medecinChoisi, dateHeure, utilisateurConnecte.getNom() + " " + utilisateurConnecte.getPrenom(), "");
        if (consultation != null) {
            System.out.println("✓ Consultation planifiée par " + utilisateurConnecte.getNom() + " " + utilisateurConnecte.getPrenom() + " (ID: " + consultation.getId() + ")");
        }
    }

    private static void consulterDossierMedical() {
        System.out.println("\n--- CONSULTATION DOSSIER MÉDICAL ---");
        System.out.println("(Appuyez sur Entrée pour annuler)");
        
        System.out.println("1. Rechercher par ID");
        System.out.println("2. Rechercher par nom");
        System.out.print("Choix: ");
        
        String choix = scanner.nextLine().trim();
        if (choix.isEmpty()) return;
        
        Patient patient = null;
        
        switch (choix) {
            case "1":
                Integer id = lireEntierAvecRetour("ID du patient");
                if (id == null) return;
                patient = aideSoignant.rechercherPatientParId(id, patients);
                break;
            case "2":
                String nom = lireChaineAvecRetour("Nom du patient");
                if (nom == null) return;
                
                // Rechercher tous les patients avec ce nom
                List<Patient> trouves = new ArrayList<>();
                for (Patient p : patients) {
                    if (p.getNom().toLowerCase().equals(nom.toLowerCase()) ||
                        p.getPrenom().toLowerCase().equals(nom.toLowerCase())) {
                        trouves.add(p);
                    }
                }
                
                if (trouves.isEmpty()) {
                    System.out.println("Aucun patient trouvé avec ce nom.");
                    return;
                } else if (trouves.size() == 1) {
                    patient = trouves.get(0);
                } else {
                    System.out.println("Plusieurs patients trouvés:");
                    for (int i = 0; i < trouves.size(); i++) {
                        Patient p = trouves.get(i);
                        System.out.println("  " + (i + 1) + ". ID: " + p.getId() + " | " + p.getNom() + " " + p.getPrenom() + 
                                         " | " + p.getDateNaissance());
                    }
                    
                    Integer index = lireEntierAvecRetour("Numéro du patient");
                    if (index == null) return;
                    
                    if (index < 1 || index > trouves.size()) {
                        System.out.println("Choix invalide.");
                        return;
                    }
                    patient = trouves.get(index - 1);
                }
                break;
            default:
                System.out.println("Choix invalide.");
                return;
        }
        
        if (patient == null) {
            System.out.println("Patient non trouvé.");
            return;
        }
        
        afficherDossierMedicalComplet(patient);
    }
    
    private static void afficherDossierMedicalComplet(Patient patient) {
        System.out.println("\n--- DOSSIER MÉDICAL COMPLET ---");
        System.out.println("ID: " + patient.getId());
        System.out.println("Nom: " + patient.getNom() + " " + patient.getPrenom());
        System.out.println("Date de naissance: " + patient.getDateNaissance());
        System.out.println("Adresse: " + patient.getAdresse());
        System.out.println("Téléphone: " + patient.getTelephone());
        System.out.println("Email: " + patient.getEmail());
        
        // Afficher le dossier médical
        DossierMedical dossier = patient.getDossierMedical();
        if (dossier != null) {
            System.out.println("\n--- INFORMATIONS MÉDICALES ---");
            
            // Groupe sanguin
            System.out.println("Groupe sanguin: " + (dossier.getGroupeSanguin() != null ? dossier.getGroupeSanguin() : "Non renseigné"));
            
            // Antécédents
            if (!dossier.getAntecedents().isEmpty()) {
                System.out.println("\nAntécédents:");
                for (Antecedent antecedent : dossier.getAntecedents()) {
                    System.out.println("  - " + antecedent.getDescription() + " (" + antecedent.getDate() + ")");
                }
            } else {
                System.out.println("Antécédents: Aucun antécédent enregistré");
            }
            
            // Constantes
            if (!dossier.getConstantes().isEmpty()) {
                System.out.println("\nConstantes:");
                for (String constante : dossier.getConstantes()) {
                    System.out.println("  - " + constante);
                }
            }
            
            // Examens
            if (!dossier.getExamens().isEmpty()) {
                System.out.println("\nExamens médicaux:");
                for (ExamenMedical examen : dossier.getExamens()) {
                    System.out.println("  - " + examen.getDate() + " | " + examen.getType() + " | " + examen.getResultat());
                }
            }
            
            // Prescriptions
            if (!dossier.getPrescriptions().isEmpty()) {
                System.out.println("\nPrescriptions:");
                for (Prescription prescription : dossier.getPrescriptions()) {
                    System.out.println("  - " + prescription.getDate() + " | " + prescription.getMedicaments());
                }
            }
            
            // Consultations
            if (!dossier.getConsultations().isEmpty()) {
                System.out.println("\nConsultations:");
                for (Consultation consultation : dossier.getConsultations()) {
                    System.out.println("  - " + consultation.getDateHeure() + " | " + consultation.getProfessionnel().getNom() + 
                                     " " + consultation.getProfessionnel().getPrenom() + " | Motif: " + consultation.getMotif() + 
                                     " | Diagnostic: " + (consultation.getDiagnostic() != null ? consultation.getDiagnostic() : "Non défini") +
                                     " | Observations: " + (consultation.getObservations() != null ? consultation.getObservations() : "Aucune"));
                }
            }
        }
    }

    private static void annulerConsultation() {
        System.out.println("\n--- ANNULATION CONSULTATION ---");
        System.out.println("(Appuyez sur Entrée pour annuler)");
        
        // Filtrer les consultations non terminées
        List<Consultation> consultationsProgrammees = consultations.stream()
            .filter(c -> !c.getStatut().equalsIgnoreCase("Terminée"))
            .collect(Collectors.toList());
        
        if (consultationsProgrammees.isEmpty()) {
            System.out.println("Aucune consultation programmée à annuler.");
            return;
        }
        
        // Afficher la liste des consultations programmées
        System.out.println("\n--- CONSULTATIONS PROGRAMMÉES ---");
        for (int i = 0; i < consultationsProgrammees.size(); i++) {
            Consultation c = consultationsProgrammees.get(i);
            System.out.println((i + 1) + ". ID: " + c.getId() + 
                             " | Patient: " + c.getPatient().getNom() + " " + c.getPatient().getPrenom() +
                             " | Médecin: " + c.getProfessionnel().getNom() + " " + c.getProfessionnel().getPrenom() +
                             " | Date/Heure: " + c.getDateHeure());
        }
        
        System.out.print("\nEntrez le numéro de la consultation à annuler: ");
        String choix = scanner.nextLine().trim();
        
        if (choix.isEmpty()) return;
        
        try {
            int numero = Integer.parseInt(choix);
            if (numero < 1 || numero > consultationsProgrammees.size()) {
                System.out.println("Numéro invalide.");
                return;
            }
            
            Consultation consultationAnnulee = consultationsProgrammees.get(numero - 1);
            
            // Afficher les détails de la consultation
            System.out.println("\nConsultation à annuler:");
            System.out.println("ID: " + consultationAnnulee.getId());
            System.out.println("Patient: " + consultationAnnulee.getPatient().getNom() + " " + 
                             consultationAnnulee.getPatient().getPrenom());
            System.out.println("Médecin: " + consultationAnnulee.getProfessionnel().getNom() + " " + 
                             consultationAnnulee.getProfessionnel().getPrenom());
            System.out.println("Date/Heure: " + consultationAnnulee.getDateHeure());
            System.out.println("Motif: " + consultationAnnulee.getMotif());
            
            System.out.print("\nConfirmer l'annulation? (O/N): ");
            String confirmation = scanner.nextLine().trim();
            
            if (confirmation.equalsIgnoreCase("O")) {
                consultations.remove(consultationAnnulee);
                BaseDeDonnees.sauvegarderConsultationsProgrammes(consultations);
                System.out.println("✓ Consultation annulée avec succès.");
            } else {
                System.out.println("Annulation annulée.");
            }
            
        } catch (NumberFormatException e) {
            System.out.println("Numéro invalide.");
        }
    }

    private static void archiverDossierMedical() {
        System.out.println("\n--- ARCHIVAGE DOSSIER MÉDICAL ---");
        System.out.println("(Appuyez sur Entrée pour annuler)");
        
        Integer idPatient = lireEntierAvecRetour("ID du patient à archiver");
        if (idPatient == null) return;
        
        Patient patient = aideSoignant.rechercherPatientParId(idPatient, patients);
        if (patient == null) {
            System.out.println("Patient non trouvé.");
            return;
        }
        
        // Afficher les détails du patient
        System.out.println("\nPatient à archiver:");
        System.out.println("ID: " + patient.getId());
        System.out.println("Nom: " + patient.getNom() + " " + patient.getPrenom());
        System.out.println("Date de naissance: " + patient.getDateNaissance());
        
        System.out.print("\nConfirmer l'archivage? (O/N): ");
        String confirmation = scanner.nextLine().trim();
        
        if (confirmation.equalsIgnoreCase("O")) {
            patient.getDossierMedical().archiver();
            BaseDeDonnees.sauvegarderDossiers(patients);
            System.out.println("✓ Dossier médical archivé avec succès.");
        } else {
            System.out.println("Archivage annulé.");
        }
    }

    private static void desarchiverDossierMedical() {
        System.out.println("\n--- DÉSARCHIVAGE DOSSIER MÉDICAL ---");
        System.out.println("(Appuyez sur Entrée pour annuler)");
        
        List<Patient> archives = patients.stream()
                                         .filter(p -> p.getDossierMedical().isArchive())
                                         .collect(Collectors.toList());

        if (archives.isEmpty()) {
            System.out.println("Aucun dossier archivé trouvé.");
            return;
        }

        System.out.println("\n--- DOSSIERS ARCHIVÉS ---");
        for (int i = 0; i < archives.size(); i++) {
            Patient patient = archives.get(i);
            System.out.println("  " + (i + 1) + ". ID: " + patient.getId() + " | " + patient.getNom() + " " + 
                             patient.getPrenom() + " | " + patient.getDateNaissance());
        }

        Integer index = lireEntierAvecRetour("Numéro du dossier à désarchiver");
        if (index == null) return;

        if (index < 1 || index > archives.size()) {
            System.out.println("Choix invalide.");
            return;
        }

        Patient patient = archives.get(index - 1);
        
        System.out.println("\nDossier à désarchiver:");
        System.out.println("ID: " + patient.getId());
        System.out.println("Nom: " + patient.getNom() + " " + patient.getPrenom());
        System.out.println("Date de naissance: " + patient.getDateNaissance());
        
        System.out.print("\nConfirmer le désarchivage? (O/N): ");
        String confirmation = scanner.nextLine().trim();
        
        if (confirmation.equalsIgnoreCase("O")) {
            patient.getDossierMedical().desarchiver();
            BaseDeDonnees.sauvegarderDossiers(patients);
            System.out.println("✓ Dossier médical désarchivé avec succès.");
        } else {
            System.out.println("Désarchivage annulé.");
        }
    }

    private static void mettreAJourConstantes() {
        System.out.println("\n--- MISE À JOUR CONSTANTES ---");
        System.out.println("(Appuyez sur Entrée pour annuler)");
        System.out.println("(Laissez vide si inchangé)");
        
        Integer idPatient = lireEntierAvecRetour("ID du patient");
        if (idPatient == null) return;
        
        Patient patient = aideSoignant.rechercherPatientParId(idPatient, patients);
        if (patient == null) {
            System.out.println("Patient non trouvé.");
            return;
        }
        
        String poids = lireChaineAvecRetour("Poids (kg)");
        String tension = lireChaineAvecRetour("Tension (ex: 12/8)");
        String temperature = lireChaineAvecRetour("Température (°C)");
        
        List<String> constantes = new ArrayList<>();
        if (poids != null && !poids.isEmpty()) constantes.add("Poids: " + poids + " kg");
        if (tension != null && !tension.isEmpty()) constantes.add("Tension: " + tension);
        if (temperature != null && !temperature.isEmpty()) constantes.add("Température: " + temperature + " °C");
        
        if (!constantes.isEmpty()) {
            aideSoignant.mettreAJourConstantes(patient, constantes);
            System.out.println("✓ Constantes mises à jour.");
        } else {
            System.out.println("Aucune constante à mettre à jour.");
        }
    }

    private static void exporterDonnees() {
        // Utiliser la méthode complète de AideSoignant (TOUTES les tables)
        aideSoignant.exporterDonnees(scanner);
    }

    private static void importerDonnees() {
        while (true) {
            String fichier = lireChaineAvecRetour("Nom du fichier CSV à importer");
            if (fichier == null || fichier.trim().isEmpty()) {
                System.out.println("Retour au menu principal...");
                return;
            }
            
            // Utiliser la méthode complète de AideSoignant (TOUTES les tables)
            aideSoignant.importerDonnees(fichier);
            
            // Demander si on veut continuer
            System.out.print("\nVoulez-vous importer un autre fichier ? (O/N): ");
            String continuer = scanner.nextLine().trim();
            if (!continuer.equalsIgnoreCase("O")) {
                System.out.println("Retour au menu principal...");
                return;
            }
        }
    }
    
    
    private static void faireConsultation() {
        System.out.println("\n--- FAIRE UNE CONSULTATION ---");
        System.out.println("(Appuyez sur Entrée pour annuler)");
        
        // Afficher les consultations programmées pour le médecin
        System.out.println("\n--- VOS CONSULTATIONS PROGRAMMÉES ---");
        List<Consultation> mesConsultations = consultations.stream()
            .filter(c -> c.getProfessionnel().getId() == utilisateurConnecte.getId())
            .filter(c -> !c.getStatut().equalsIgnoreCase("Terminée"))
            .collect(Collectors.toList());
        
        if (mesConsultations.isEmpty()) {
            System.out.println("Aucune consultation programmée à réaliser.");
            return;
        }
        
        System.out.println("Consultations à réaliser:");
        for (int i = 0; i < mesConsultations.size(); i++) {
            Consultation consultation = mesConsultations.get(i);
            System.out.println("  " + (i + 1) + ". ID: " + consultation.getId() + 
                             " | " + consultation.getDateHeure() + 
                             " | " + consultation.getPatient().getNom() + " " + consultation.getPatient().getPrenom() +
                             " | Statut: " + consultation.getStatut());
        }
        
        Integer choix = lireEntierAvecRetour("Numéro de la consultation à réaliser");
        if (choix == null) return;
        
        if (choix < 1 || choix > mesConsultations.size()) {
            System.out.println("Choix invalide.");
            return;
        }
        
        Consultation consultation = mesConsultations.get(choix - 1);
        
        System.out.println("\n--- CONSULTATION EN COURS ---");
        System.out.println("Patient: " + consultation.getPatient().getNom() + " " + consultation.getPatient().getPrenom());
        System.out.println("Date: " + consultation.getDateHeure().split(" ")[0]);
        
        // Heure de la consultation (laisser le médecin la définir)
        String heure = lireHeureAvecFormat("Heure de la consultation", "\\d{2}:\\d{2}");
        if (heure == null) return;
        
        // Mettre à jour l'heure complète
        String dateHeureComplete = consultation.getDateHeure().split(" ")[0] + " " + heure;
        consultation.setDateHeure(dateHeureComplete);
        
        // Antécédents
        System.out.println("\nAntécédents (laissez vide si inchangé)");
        String antecedents = lireChaineAvecRetour("Antécédents");
        
        // Motif de la consultation
        System.out.println("\nMotif de la consultation");
        String motif = lireChaineAvecRetour("Motif");
        if (motif == null || motif.isEmpty()) {
            System.out.println("Le motif est obligatoire pour terminer la consultation.");
            return;
        }
        
        // Observations
        System.out.println("\nObservations");
        String observations = lireChaineAvecRetour("Observations");
        
        // Diagnostic
        System.out.println("\nDiagnostic");
        String diagnostic = lireChaineAvecRetour("Diagnostic");
        if (diagnostic == null || diagnostic.isEmpty()) {
            System.out.println("Le diagnostic est obligatoire pour terminer la consultation.");
            return;
        }
        
        // Mettre à jour la consultation
        consultation.setStatut("Terminée");
        consultation.setMotif(motif);
        consultation.setDiagnostic(diagnostic);
        if (observations != null && !observations.isEmpty()) {
            consultation.setObservations(observations);
        }
        
        // Ajouter les antécédents si fournis
        if (antecedents != null && !antecedents.isEmpty()) {
            Antecedent nouvelAntecedent = new Antecedent(antecedents, antecedents, dateHeureComplete.split(" ")[0]);
            consultation.getPatient().getDossierMedical().ajouterAntecedent(nouvelAntecedent);
        }
        
        // Sauvegarder
        BaseDeDonnees.sauvegarderConsultationsProgrammes(consultations);
        BaseDeDonnees.sauvegarderDossiers(patients);
        
        System.out.println("✓ Consultation terminée avec succès.");
    }

    private static void ajouterExamenMedical() {
        System.out.println("\n--- ENREGISTREMENT EXAMEN MÉDICAL ---");
        System.out.println("(Appuyez sur Entrée pour annuler)");
        
        Integer idPatient = lireEntierAvecRetour("ID du patient");
        if (idPatient == null) return;
        
        Patient patient = aideSoignant.rechercherPatientParId(idPatient, patients);
        if (patient == null) {
            System.out.println("Patient non trouvé.");
            return;
        }
        
        System.out.println("Types d'examen disponibles:");
        System.out.println("1. Imagerie");
        System.out.println("2. Laboratoire");
        System.out.print("Choix: ");
        
        String choixType = scanner.nextLine().trim();
        if (choixType.isEmpty()) return;
        
        String typeExamen;
        switch (choixType) {
            case "1":
                typeExamen = "Imagerie";
                break;
            case "2":
                typeExamen = "Laboratoire";
                break;
            default:
                System.out.println("Choix invalide.");
                return;
        }
        
        String nomExamen = lireChaineAvecRetour("Nom de l'examen");
        if (nomExamen == null) return;
        
        String resultat = lireChaineAvecRetour("Résultats de l'examen");
        if (resultat == null) return;
        
        String date = lireDateAvecFormat("Date", "JJ/MM/AAAA", "\\d{2}/\\d{2}/\\d{4}");
        if (date == null) return;
        
        ExamenMedical examen = new ExamenMedical(typeExamen + " - " + nomExamen, "", date, patient, (ProfessionnelSante)utilisateurConnecte);
        examen.setResultat(resultat);
        medecin.ajouterExamenMedical(examen);
        System.out.println("✓ Examen médical enregistré avec succès.");
    }

    private static void prescrireTraitement() {
        System.out.println("\n--- PRESCRIPTION TRAITEMENT ---");
        System.out.println("(Appuyez sur Entrée pour annuler)");
        
        Integer idPatient = lireEntierAvecRetour("ID du patient");
        if (idPatient == null) return;
        
        Patient patient = aideSoignant.rechercherPatientParId(idPatient, patients);
        if (patient == null) {
            System.out.println("Patient non trouvé.");
            return;
        }
        
        String date = lireDateAvecFormat("Date", "JJ/MM/AAAA", "\\d{2}/\\d{2}/\\d{4}");
        if (date == null) return;
        
        System.out.println("\n--- MÉDICAMENTS ---");
        System.out.println("Ajoutez les médicaments un par un (laissez vide le nom pour terminer)");
        
        Prescription prescription = new Prescription(date, patient, (ProfessionnelSante)utilisateurConnecte, "");
        
        while (true) {
            String medicament = lireChaineAvecRetour("Nom du médicament");
            if (medicament == null || medicament.isEmpty()) {
                break;
            }
            
            
            String posologie = lireChaineAvecRetour("Posologie");
            if (posologie == null || posologie.isEmpty()) {
                System.out.println("La posologie est obligatoire.");
                continue;
            }
            
            String dureeTraitement = lireChaineAvecRetour("Durée du traitement pour ce médicament");
            if (dureeTraitement == null || dureeTraitement.isEmpty()) {
                System.out.println("La durée de traitement est obligatoire.");
                continue;
            }
            
            prescription.ajouterMedicament(medicament, posologie, dureeTraitement);
            System.out.println("✓ Médicament ajouté.");
        }
        
        if (!prescription.getMedicaments().isEmpty()) {
            medecin.fairePrescription(prescription);
            System.out.println("✓ Prescription enregistrée avec " + prescription.getMedicaments().size() + " médicament(s).");
        } else {
            System.out.println("Aucun médicament ajouté. Prescription annulée.");
        }
    }

    private static void gererDisponibilites() {
        System.out.println("\n--- GESTION DISPONIBILITÉS ---");
        System.out.println("(Appuyez sur Entrée pour annuler)");
        System.out.println("1. Ajouter une disponibilité");
        System.out.println("2. Supprimer une disponibilité");
        System.out.println("3. Voir mes disponibilités");
        System.out.print("Choix: ");
        
        String choix = scanner.nextLine().trim();
        if (choix.isEmpty()) return;
        
        switch (choix) {
            case "1":
                ajouterDisponibilite();
                break;
            case "2":
                supprimerDisponibilite();
                break;
            case "3":
                voirDisponibilites();
                break;
            default:
                System.out.println("Choix invalide.");
        }
    }

    private static void ajouterDisponibilite() {
        System.out.println("\n--- AJOUT DISPONIBILITÉ ---");
        System.out.println("(Appuyez sur Entrée pour annuler)");
        String creneau = lireChaineAvecRetour("Créneau (ex: Lundi 09:00-12:00)");
        if (creneau == null) return;
        ((Medecin) utilisateurConnecte).ajouterDisponibilite(creneau);
        
        // Sauvegarder les disponibilités dans la base de données
        java.util.Map<Integer, List<String>> disponibilitesMap = new java.util.HashMap<>();
        for (Utilisateur u : utilisateurs) {
            if (u instanceof Medecin) {
                Medecin medecin = (Medecin) u;
                disponibilitesMap.put(medecin.getId(), medecin.getDisponibilites());
            }
        }
        BaseDeDonnees.sauvegarderDisponibilites(disponibilitesMap);
        
        System.out.println("✓ Disponibilité ajoutée et sauvegardée.");
    }

    private static void supprimerDisponibilite() {
        System.out.println("\n--- SUPPRESSION DISPONIBILITÉ ---");
        System.out.println("(Appuyez sur Entrée pour annuler)");
        
        Medecin pro = (Medecin) utilisateurConnecte;
        List<String> dispos = pro.getDisponibilites();
        
        if (dispos.isEmpty()) {
            System.out.println("Aucune disponibilité à supprimer.");
            return;
        }
        
        System.out.println("\nVos disponibilités actuelles:");
        for (int i = 0; i < dispos.size(); i++) {
            System.out.println("  " + (i + 1) + ". " + dispos.get(i));
        }
        
        Integer choix = lireEntierAvecRetour("Numéro de la disponibilité à supprimer");
        if (choix == null) return;
        
        if (choix < 1 || choix > dispos.size()) {
            System.out.println("Choix invalide.");
            return;
        }
        
        String creneauASupprimer = dispos.get(choix - 1);
        ((Medecin) pro).supprimerDisponibilite(creneauASupprimer);
        
        // Sauvegarder les disponibilités dans la base de données
        java.util.Map<Integer, List<String>> disponibilitesMap = new java.util.HashMap<>();
        for (Utilisateur u : utilisateurs) {
            if (u instanceof Medecin) {
                Medecin medecinInstance = (Medecin) u;
                disponibilitesMap.put(medecinInstance.getId(), medecinInstance.getDisponibilites());
            }
        }
        BaseDeDonnees.sauvegarderDisponibilites(disponibilitesMap);
        
        System.out.println("✓ Disponibilité \"" + creneauASupprimer + "\" supprimée et sauvegardée.");
    }

    private static void voirDisponibilites() {
        System.out.println("\n--- MES DISPONIBILITÉS ---");
        Medecin pro = (Medecin) utilisateurConnecte;
        List<String> dispos = pro.getDisponibilites();
        if (dispos.isEmpty()) {
            System.out.println("Aucune disponibilité enregistrée.");
        } else {
            for (String d : dispos) {
                System.out.println("- " + d);
            }
        }
    }
}
