package medipass;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class BaseDeDonnees {
   private static final String DOSSIERS_FILE;
   private static final String DOSSIERS_ARCHIVES_FILE;
   private static final String UTILISATEURS_FILE;
   private static final String DISPONIBILITES_FILE;
   private static final String DISPONIBILITE_FILE;
   private static final String PATIENTS_LEGACY_FILE;
   private static final String PATIENTS_ARCHIVES_LEGACY_FILE;
   private static final String CONSULTATIONS_LEGACY_FILE;
   private static final String CONSULTATIONS_PROGRAMME_FILE;

   public BaseDeDonnees() {
   }

   private static void assurerRepertoire() {
      File var0 = new File("data");
      if (!var0.exists()) {
         var0.mkdirs();
      }

   }

   public static void exporterPatientsCSV(List<Patient> var0, String var1) {
      if (var0 != null && var1 != null && !var1.isBlank()) {
         try {
            PrintWriter var2 = new PrintWriter(new FileWriter(var1));

            try {
               // En-tête CSV complet avec toutes les données médicales
               var2.println("ID,Nom,Prenom,DateNaissance,Adresse,Telephone,Email,GroupeSanguin,Archive,Antecedents,Constantes,Consultations,Examens,Prescriptions");
               Iterator var3 = var0.iterator();

               while(var3.hasNext()) {
                  Patient var4 = (Patient)var3.next();
                  DossierMedical var5 = var4.getDossierMedical();
                  String var6 = var5.getGroupeSanguin() != null ? var5.getGroupeSanguin() : "";
                  String var7 = var5.isArchive() ? "true" : "false";
                  String var8 = encoderAntecedents(var5.getAntecedents());
                  String var9 = encoderConstantes(var5.getConstantes());
                  String var10 = encoderConsultations(var5.getConsultations());
                  String var11 = encoderExamens(var5.getExamens());
                  String var12 = encoderPrescriptions(var5.getPrescriptions());
                  
                  // Échapper les virgules dans les données complexes
                  var6 = var6.replace(",", ";");
                  var8 = var8.replace(",", ";");
                  var9 = var9.replace(",", ";");
                  var10 = var10.replace(",", ";");
                  var11 = var11.replace(",", ";");
                  var12 = var12.replace(",", ";");
                  
                  int var10001 = var4.getId();
                  var2.println("" + var10001 + "," + var4.getNom() + "," + var4.getPrenom() + "," + var4.getDateNaissance() + "," + var4.getAdresse() + "," + var4.getTelephone() + "," + var4.getEmail() + "," + var6 + "," + var7 + "," + var8 + "," + var9 + "," + var10 + "," + var11 + "," + var12);
               }
            } catch (Throwable var6) {
               try {
                  var2.close();
               } catch (Throwable var5) {
                  var6.addSuppressed(var5);
               }

               throw var6;
            }

            var2.close();
         } catch (IOException var7) {
            System.err.println("Erreur lors de l'export CSV patients : " + var7.getMessage());
         }

      }
   }

   public static void importerCSVGenerique(String cheminFichier) {
      if (cheminFichier != null && !cheminFichier.isBlank()) {
         // Copier le fichier dans le dossier import avec timestamp
         String nomFichier = new java.io.File(cheminFichier).getName();
         String timestamp = java.time.LocalDateTime.now().format(java.time.format.DateTimeFormatter.ofPattern("yyyyMMdd_HHmmss"));
         String nomFichierCopie = "import_" + timestamp + "_" + nomFichier;
         String cheminCopie = "import/" + nomFichierCopie;
         
         copierFichier(cheminFichier, cheminCopie);
         System.out.println("✓ Import réussi: " + cheminCopie);
      }
   }
   
   
   public static void importerPatientsCSV(String var0, List<Patient> var1) {
      if (var0 != null && !var0.isBlank() && var1 != null) {
         try {
            BufferedReader var2 = new BufferedReader(new FileReader(var0));

            try {
               String var3 = var2.readLine();

               while((var3 = var2.readLine()) != null) {
                  if (!var3.isBlank()) {
                     String[] var4 = var3.split(",");
                     if (var4.length >= 7) {
                        Patient var5 = new Patient(var4[1], var4[2], var4[3], var4[4], var4[5], var4[6]);
                        var1.add(var5);
                     }
                  }
               }
            } catch (Throwable var7) {
               try {
                  var2.close();
               } catch (Throwable var6) {
                  var7.addSuppressed(var6);
               }

               throw var7;
            }

            var2.close();
         } catch (IOException var8) {
            System.err.println("Erreur lors de l'import CSV patients : " + var8.getMessage());
         }

      }
   }

   public static List<Patient> chargerDossiers(List<ProfessionnelSante> var0) {
      assurerRepertoire();
      migrerAnciennesBases();
      Map var1 = (Map)var0.stream().collect(Collectors.toMap(Utilisateur::getId, (var0x) -> {
         return var0x;
      }));
      ArrayList var2 = new ArrayList();
      var2.addAll(chargerDossiersDepuisFichier(DOSSIERS_FILE, false, var1));
      var2.addAll(chargerDossiersDepuisFichier(DOSSIERS_ARCHIVES_FILE, true, var1));
      return var2;
   }

   private static List<Patient> chargerDossiersDepuisFichier(String var0, boolean var1, Map<Integer, ProfessionnelSante> var2) {
      ArrayList var3 = new ArrayList();
      File var4 = new File(var0);
      if (!var4.exists()) {
         return var3;
      } else {
         try {
            BufferedReader var5 = new BufferedReader(new FileReader(var4));

            try {
               label57:
               while(true) {
                  String[] var7;
                  do {
                     String var6;
                     do {
                        if ((var6 = var5.readLine()) == null) {
                           break label57;
                        }
                     } while(var6.trim().isEmpty());

                     var7 = var6.split("\\|", -1);
                  } while(var7.length < 14);

                  Patient var8 = new Patient(var7[1], var7[2], var7[3], var7[4], var7[5], var7[6]);
                  var8.setIdPourPersistence(parseEntier(var7[0]));
                  DossierMedical var9 = var8.getDossierMedical();
                  String var10 = var7[7];
                  if (!var10.isBlank()) {
                     var9.setGroupeSanguin(var10);
                  }

                  boolean var11 = var1 || "true".equalsIgnoreCase(var7[8]);
                  if (var11) {
                     var9.archiver();
                  }

                  reconstituerAntecedents(var7[9], var9);
                  reconstituerConstantes(var7[10], var9);
                  reconstituerConsultations(var7[11], var8, var2);
                  reconstituerExamens(var7[12], var8, var2);
                  reconstituerPrescriptions(var7[13], var8, var2);
                  var3.add(var8);
               }
            } catch (Throwable var13) {
               try {
                  var5.close();
               } catch (Throwable var12) {
                  var13.addSuppressed(var12);
               }

               throw var13;
            }

            var5.close();
         } catch (IOException var14) {
            System.err.println("Erreur lors du chargement des dossiers : " + var14.getMessage());
         }

         return var3;
      }
   }

   private static void migrerAnciennesBases() {
      renommerFichierAncien(PATIENTS_LEGACY_FILE, DOSSIERS_FILE);
      renommerFichierAncien(PATIENTS_ARCHIVES_LEGACY_FILE, DOSSIERS_ARCHIVES_FILE);
      renommerFichierAncien(DISPONIBILITES_FILE, DISPONIBILITE_FILE);
      renommerFichierAncien(CONSULTATIONS_LEGACY_FILE, CONSULTATIONS_PROGRAMME_FILE);
   }

   private static void renommerFichierAncien(String var0, String var1) {
      File var2 = new File(var0);
      File var3 = new File(var1);
      if (var2.exists() && !var3.exists()) {
         var2.renameTo(var3);
      }

   }

   private static void reconstituerAntecedents(String var0, DossierMedical var1) {
      if (var0 != null && !var0.isBlank()) {
         String[] var2 = var0.split(";", -1);
         int var3 = var2.length;

         for(int var4 = 0; var4 < var3; ++var4) {
            String var5 = var2[var4];
            if (!var5.isBlank()) {
               String[] var6 = var5.split("~", -1);
               if (var6.length >= 2) {
                  // Nouveau format : description|date (sans type)
                  var1.ajouterAntecedent(new Antecedent("", var6[0], var6[1]));
               } else if (var6.length >= 3) {
                  // Ancien format : type|description|date (compatibilité)
                  var1.ajouterAntecedent(new Antecedent(var6[0], var6[1], var6[2]));
               }
            }
         }

      }
   }

   private static void reconstituerConstantes(String var0, DossierMedical var1) {
      if (var0 != null && !var0.isBlank()) {
         String[] var2 = var0.split(";", -1);
         int var3 = var2.length;

         for(int var4 = 0; var4 < var3; ++var4) {
            String var5 = var2[var4];
            if (!var5.isBlank()) {
               var1.ajouterConstante(var5);
            }
         }

      }
   }

   private static void reconstituerConsultations(String var0, Patient var1, Map<Integer, ProfessionnelSante> var2) {
      if (var0 != null && !var0.isBlank()) {
         String[] var3 = var0.split(";", -1);
         int var4 = var3.length;

         for(int var5 = 0; var5 < var4; ++var5) {
            String var6 = var3[var5];
            if (!var6.isBlank()) {
               String[] var7 = var6.split("~", -1);
               if (var7.length >= 6) {
                  int var8 = parseEntier(var7[0]);
                  String var9 = var7[1];
                  int var10 = parseEntier(var7[2]);
                  ProfessionnelSante var11 = (ProfessionnelSante)var2.get(var10);
                  if (var11 != null) {
                     // Gérer l'ancien format (6 champs) et le nouveau format (7 champs avec diagnostic)
                     String motif = var7[3];
                     String observations = var7[4];
                     String diagnostic = "";
                     String statut = "";
                     
                     if (var7.length >= 7) {
                        // Nouveau format: ID~date~id~motif~observations~diagnostic~statut
                        diagnostic = var7[5];
                        statut = var7[6];
                     } else {
                        // Ancien format: ID~date~id~motif~observations~statut
                        statut = var7[5];
                     }
                     
                     Consultation var12 = new Consultation(var9, var1, var11, motif, observations);
                     var12.setDiagnostic(diagnostic);
                     var12.setStatut(statut);
                     var12.setIdPourPersistence(var8);
                     var1.getDossierMedical().ajouterConsultation(var12);
                     // Plus besoin de stocker dans ProfessionnelSante - géré globalement
                  }
               }
            }
         }

      }
   }

   private static void reconstituerExamens(String var0, Patient var1, Map<Integer, ProfessionnelSante> var2) {
      if (var0 != null && !var0.isBlank()) {
         String[] var3 = var0.split(";", -1);
         int var4 = var3.length;

         for(int var5 = 0; var5 < var4; ++var5) {
            String var6 = var3[var5];
            if (!var6.isBlank()) {
               String[] var7 = var6.split("~", -1);
               if (var7.length >= 6) {
                  int var8 = parseEntier(var7[0]);
                  String var9 = var7[1];
                  String var10;
                  String var11;
                  int var12;
                  String var13;
                  String var14;
                  if (var7.length >= 7) {
                     var10 = var7[2];
                     var11 = var7[3];
                     var12 = parseEntier(var7[4]);
                     var13 = var7[5];
                     var14 = var7[6];
                  } else {
                     var10 = "";
                     var11 = var7[2];
                     var12 = parseEntier(var7[3]);
                     var13 = var7.length > 4 ? var7[4] : "";
                     var14 = var7.length > 5 ? var7[5] : "";
                  }

                  ProfessionnelSante var15 = (ProfessionnelSante)var2.get(var12);
                  if (var15 != null) {
                     ExamenMedical var16 = new ExamenMedical(var9, var10, var11, var1, var15);
                     var16.setResultat(var13);
                     var16.setCommentaire(var14);
                     var16.setIdPourPersistence(var8);
                     var1.getDossierMedical().ajouterExamen(var16);
                  }
               }
            }
         }

      }
   }

   private static void reconstituerPrescriptions(String var0, Patient var1, Map<Integer, ProfessionnelSante> var2) {
      if (var0 != null && !var0.isBlank()) {
         String[] var3 = var0.split(";", -1);
         int var4 = var3.length;

         for(int var5 = 0; var5 < var4; ++var5) {
            String var6 = var3[var5];
            if (!var6.isBlank()) {
               String[] var7 = var6.split("~", -1);
               if (var7.length >= 5) {
                  int var8 = parseEntier(var7[0]);
                  String var9 = var7[1];
                  int var10 = parseEntier(var7[2]);
                  ProfessionnelSante var11 = (ProfessionnelSante)var2.get(var10);
                  if (var11 != null) {
                     Prescription var12 = new Prescription(var9, var1, var11, var7[3]);
                     var12.setIdPourPersistence(var8);
                     if (var7.length > 4 && !var7[4].isBlank()) {
                        String[] var13 = var7[4].split(",", -1);
                        int var14 = var13.length;

                        for(int var15 = 0; var15 < var14; ++var15) {
                           String var16 = var13[var15];
                           if (!var16.isBlank()) {
                              String[] var17 = var16.split("\\^", -1);
                              if (var17.length >= 3) {
                                 var12.ajouterMedicament(var17[0], var17[1], var17[2]);
                              }
                           }
                        }
                     }

                     var1.getDossierMedical().ajouterPrescription(var12);
                  }
               }
            }
         }

      }
   }

   private static int parseEntier(String var0) {
      if (var0 != null && !var0.isBlank()) {
         try {
            return Integer.parseInt(var0);
         } catch (NumberFormatException var2) {
            return -1;
         }
      } else {
         return -1;
      }
   }

   public static void sauvegarderDossiers(List<Patient> var0) {
      assurerRepertoire();
      sauvegarderListeDeDossiers((List)var0.stream().filter((var0x) -> {
         return !var0x.getDossierMedical().isArchive();
      }).collect(Collectors.toList()), DOSSIERS_FILE);
      sauvegarderListeDeDossiers((List)var0.stream().filter((var0x) -> {
         return var0x.getDossierMedical().isArchive();
      }).collect(Collectors.toList()), DOSSIERS_ARCHIVES_FILE);
   }

   private static void sauvegarderListeDeDossiers(List<Patient> var0, String var1) {
      try {
         PrintWriter var2 = new PrintWriter(new FileWriter(var1));

         try {
            Iterator var3 = var0.iterator();

            while(var3.hasNext()) {
               Patient var4 = (Patient)var3.next();
               DossierMedical var5 = var4.getDossierMedical();
               String var6 = var5.getGroupeSanguin() != null ? var5.getGroupeSanguin() : "";
               String var7 = var5.isArchive() ? "true" : "false";
               String var8 = var4.getId() + "|" + nettoyer(var4.getNom()) + "|" + nettoyer(var4.getPrenom()) + "|" + nettoyer(var4.getDateNaissance()) + "|" + nettoyer(var4.getAdresse()) + "|" + nettoyer(var4.getTelephone()) + "|" + nettoyer(var4.getEmail()) + "|" + nettoyer(var6) + "|" + var7 + "|" + encoderAntecedents(var5.getAntecedents()) + "|" + encoderConstantes(var5.getConstantes()) + "|" + encoderConsultations(var5.getConsultations()) + "|" + encoderExamens(var5.getExamens()) + "|" + encoderPrescriptions(var5.getPrescriptions());
               var2.println(var8);
            }
         } catch (Throwable var10) {
            try {
               var2.close();
            } catch (Throwable var9) {
               var10.addSuppressed(var9);
            }

            throw var10;
         }

         var2.close();
      } catch (IOException var11) {
         System.err.println("Erreur lors de la sauvegarde des dossiers : " + var11.getMessage());
      }

   }

   private static String encoderAntecedents(List<Antecedent> var0) {
      return var0 != null && !var0.isEmpty() ? (String)var0.stream().map((var0x) -> {
         return nettoyer(var0x.getDescription()) + "~" + nettoyer(var0x.getDate());
      }).collect(Collectors.joining(";")) : "";
   }

   private static String encoderConstantes(List<String> var0) {
      return var0 != null && !var0.isEmpty() ? (String)var0.stream().map(BaseDeDonnees::nettoyer).collect(Collectors.joining(";")) : "";
   }

   private static String encoderConsultations(List<Consultation> var0) {
      return var0 != null && !var0.isEmpty() ? (String)var0.stream().map((var0x) -> {
         int var10000 = var0x.getId();
         return "" + var10000 + "~" + nettoyer(var0x.getDateHeure()) + "~" + String.valueOf(var0x.getProfessionnel() != null ? var0x.getProfessionnel().getId() : "") + "~" + nettoyer(var0x.getMotif()) + "~" + nettoyer(var0x.getObservations()) + "~" + nettoyer(var0x.getDiagnostic()) + "~" + nettoyer(var0x.getStatut());
      }).collect(Collectors.joining(";")) : "";
   }

   private static String encoderExamens(List<ExamenMedical> var0) {
      return var0 != null && !var0.isEmpty() ? (String)var0.stream().map((var0x) -> {
         int var10000 = var0x.getId();
         return "" + var10000 + "~" + nettoyer(var0x.getType()) + "~" + nettoyer(var0x.getNom()) + "~" + nettoyer(var0x.getDate()) + "~" + String.valueOf(var0x.getPrescripteur() != null ? var0x.getPrescripteur().getId() : "") + "~" + nettoyer(var0x.getResultat()) + "~" + nettoyer(var0x.getCommentaire());
      }).collect(Collectors.joining(";")) : "";
   }

   private static String encoderPrescriptions(List<Prescription> var0) {
      return var0 != null && !var0.isEmpty() ? (String)var0.stream().map((var0x) -> {
         int var10000 = var0x.getId();
         return "" + var10000 + "~" + nettoyer(var0x.getDate()) + "~" + String.valueOf(var0x.getMedecin() != null ? var0x.getMedecin().getId() : "") + "~" + nettoyer(var0x.getDuree()) + "~" + encoderMedicaments(var0x.getMedicaments());
      }).collect(Collectors.joining(";")) : "";
   }

   private static String encoderMedicaments(List<Medicament> var0) {
      return var0 != null && !var0.isEmpty() ? (String)var0.stream().map((var0x) -> {
         String var10000 = nettoyer(var0x.getNom());
         return var10000 + "^" + nettoyer(var0x.getPosologie()) + "^" + nettoyer(var0x.getDureeTraitement());
      }).collect(Collectors.joining(",")) : "";
   }

   public static List<Utilisateur> chargerUtilisateurs() {
      ArrayList var0 = new ArrayList();
      File var1 = new File(UTILISATEURS_FILE);
      if (!var1.exists()) {
         return var0;
      } else {
         try {
            BufferedReader var2 = new BufferedReader(new FileReader(var1));

            try {
               label80:
               while(true) {
                  int var6;
                  Object var12;
                  label78:
                  while(true) {
                     String[] var4;
                     do {
                        String var3;
                        do {
                           if ((var3 = var2.readLine()) == null) {
                              break label80;
                           }
                        } while(var3.trim().isEmpty());

                        var4 = var3.split("\\|", -1);
                     } while(var4.length < 7);

                     String var5 = var4[0].toUpperCase();
                     var6 = Integer.parseInt(var4[1]);
                     String var7 = var4[2];
                     String var8 = var4[3];
                     String var9 = var4[4];
                     String var10 = var4[5];
                     String var11 = var4.length > 6 ? var4[6] : "";
                     switch (var5) {
                        case "ADMINISTRATEUR":
                        case "ADMIN":
                           var12 = new Administrateur(var7, var8, var9, var10);
                           break label78;
                        case "MEDECIN":
                           var12 = new Medecin(var7, var8, var9, var10, var11);
                           break label78;
                        case "AIDE-SOIGNANT":
                        case "AIDE_SOIGNANT":
                           var12 = new AideSoignant(var7, var8, var9, var10);
                           break label78;
                     }
                  }

                  ((Utilisateur)var12).setIdPourPersistence(var6);
                  var0.add(var12);
               }
            } catch (Throwable var16) {
               try {
                  var2.close();
               } catch (Throwable var15) {
                  var16.addSuppressed(var15);
               }

               throw var16;
            }

            var2.close();
         } catch (IOException var17) {
            System.err.println("Erreur lors du chargement des utilisateurs : " + var17.getMessage());
         }

         return var0;
      }
   }

   public static void sauvegarderUtilisateurs(List<Utilisateur> var0) {
      assurerRepertoire();

      try {
         PrintWriter var1 = new PrintWriter(new FileWriter(UTILISATEURS_FILE));

         Utilisateur var3;
         String var4;
         String var5;
         try {
            for(Iterator<Utilisateur> var2 = var0.iterator(); var2.hasNext(); var1.println(var4 + "|" + var3.getId() + "|" + var3.getNom() + "|" + var3.getPrenom() + "|" + var3.getUsername() + "|" + var3.getPassword() + "|" + var5)) {
               var3 = (Utilisateur)var2.next();
               if (var3 instanceof Administrateur) {
                  var4 = "ADMIN";
               } else if (var3 instanceof Medecin) {
                  var4 = "MEDECIN";
               } else if (var3 instanceof AideSoignant) {
                  var4 = "AIDE_SOIGNANT";
               } else {
                  var4 = "PROFESSIONNEL";
               }

               var5 = "";
               if (var3 instanceof ProfessionnelSante var6) {
                  var5 = var6.getSpecialite() != null ? var6.getSpecialite() : "";
               }
            }
         } catch (Throwable var8) {
            try {
               var1.close();
            } catch (Throwable var7) {
               var8.addSuppressed(var7);
            }

            throw var8;
         }

         var1.close();
      } catch (IOException var9) {
         System.err.println("Erreur lors de la sauvegarde des utilisateurs : " + var9.getMessage());
      }

   }

   public static Map<Integer, List<String>> chargerDisponibilites() {
      HashMap var0 = new HashMap();
      File var1 = new File(DISPONIBILITE_FILE);
      if (!var1.exists()) {
         return var0;
      } else {
         try {
            BufferedReader var2 = new BufferedReader(new FileReader(var1));

            String var3;
            try {
               while((var3 = var2.readLine()) != null) {
                  if (!var3.trim().isEmpty()) {
                     String[] var4 = var3.split("\\|", -1);
                     if (var4.length >= 4) {
                        int var5 = Integer.parseInt(var4[0]);
                        String var6 = var4[1];
                        String var7 = var4[2];
                        String var8 = var4[3];
                        String var9 = var6 + " " + var7 + "-" + var8;
                        ((List)var0.computeIfAbsent(var5, (var0x) -> {
                           return new ArrayList();
                        })).add(var9);
                     }
                  }
               }
            } catch (Throwable var11) {
               try {
                  var2.close();
               } catch (Throwable var10) {
                  var11.addSuppressed(var10);
               }

               throw var11;
            }

            var2.close();
         } catch (IOException var12) {
            System.err.println("Erreur lors du chargement des disponibilités : " + var12.getMessage());
         }

         return var0;
      }
   }

   public static void sauvegarderDisponibilites(Map<Integer, List<String>> var0) {
      assurerRepertoire();

      try {
         PrintWriter var1 = new PrintWriter(new FileWriter(DISPONIBILITE_FILE));

         try {
            Iterator var2 = var0.entrySet().iterator();

            while(var2.hasNext()) {
               Map.Entry var3 = (Map.Entry)var2.next();
               int var4 = (Integer)var3.getKey();
               Iterator var5 = ((List)var3.getValue()).iterator();

               while(var5.hasNext()) {
                  String var6 = (String)var5.next();
                  if (var6 != null && !var6.isBlank()) {
                     int var7 = var6.lastIndexOf(32);
                     if (var7 > 0) {
                        String var8 = var6.substring(0, var7);
                        String var9 = var6.substring(var7 + 1);
                        String[] var10 = var9.split("-", 2);
                        if (var10.length >= 2) {
                           var1.println("" + var4 + "|" + var8 + "|" + var10[0] + "|" + var10[1]);
                        }
                     }
                  }
               }
            }
         } catch (Throwable var12) {
            try {
               var1.close();
            } catch (Throwable var11) {
               var12.addSuppressed(var11);
            }

            throw var12;
         }

         var1.close();
      } catch (IOException var13) {
         System.err.println("Erreur lors de la sauvegarde des disponibilités : " + var13.getMessage());
      }

   }

   public static void exporterConsultationsProgrammesCSV(List<Consultation> consultations, String fichier) {
      try {
         PrintWriter writer = new PrintWriter(new FileWriter(fichier));
         
         // En-tête CSV
         writer.println("ID,NomPatient,PrenomPatient,IDPatient,IDMedecin,NomAideSoignant,PrenomAideSoignant,Statut");
         
         // Données
         for (Consultation consultation : consultations) {
            if (consultation != null && consultation.getStatut() != null) {
               int idConsultation = consultation.getId();
               String nomPatient = consultation.getPatient().getNom();
               String prenomPatient = consultation.getPatient().getPrenom();
               int idPatient = consultation.getPatient().getId();
               int idMedecin = consultation.getProfessionnel().getId();
               
               // Utiliser les champs du programmeur au lieu du motif
               String nomAideSoignant = consultation.getProgrammeurNom() != null ? consultation.getProgrammeurNom() : "";
               String prenomAideSoignant = consultation.getProgrammeurPrenom() != null ? consultation.getProgrammeurPrenom() : "";
               
               String statut = consultation.getStatut();
               
               writer.println(idConsultation + "," + nomPatient + "," + prenomPatient + "," + 
                             idPatient + "," + idMedecin + "," + nomAideSoignant + "," + 
                             prenomAideSoignant + "," + statut);
            }
         }
         
         writer.close();
      } catch (Exception e) {
         System.err.println("Erreur lors de l'export CSV des consultations programmées : " + e.getMessage());
      }
   }

   public static void exporterDossiersArchivesCSV(List<Patient> patients, String fichier) {
      try {
         PrintWriter writer = new PrintWriter(new FileWriter(fichier));
         
         // En-tête CSV
         writer.println("ID,Nom,Prenom,DateNaissance,Adresse,Telephone,Email,GroupeSanguin,Archive,Antecedents,Constantes,Consultations,Examens,Prescriptions,Traitements");
         
         // Données
         for (Patient patient : patients) {
            DossierMedical dossier = patient.getDossierMedical();
            if (dossier != null && dossier.isArchive()) {
               String groupeSanguin = dossier.getGroupeSanguin() != null ? dossier.getGroupeSanguin() : "";
               String antecedents = encoderAntecedents(dossier.getAntecedents());
               String constantes = encoderConstantes(dossier.getConstantes());
               String consultations = encoderConsultations(dossier.getConsultations());
               String examens = encoderExamens(dossier.getExamens());
               String prescriptions = encoderPrescriptions(dossier.getPrescriptions());
               
               // Échapper les virgules dans les données complexes
               groupeSanguin = groupeSanguin.replace(",", ";");
               antecedents = antecedents.replace(",", ";");
               constantes = constantes.replace(",", ";");
               consultations = consultations.replace(",", ";");
               examens = examens.replace(",", ";");
               prescriptions = prescriptions.replace(",", ";");
               
               String ligne = patient.getId() + "," + patient.getNom() + "," + patient.getPrenom() + "," +
                             patient.getDateNaissance() + "," + patient.getAdresse() + "," + 
                             patient.getTelephone() + "," + patient.getEmail() + "," +
                             groupeSanguin + "," +
                             "true" + "," +
                             antecedents + "," +
                             constantes + "," +
                             consultations + "," +
                             examens + "," +
                             prescriptions + "," +
                             "";
               writer.println(ligne);
            }
         }
         
         writer.close();
      } catch (Exception e) {
         System.err.println("Erreur lors de l'export CSV des dossiers archivés : " + e.getMessage());
      }
   }

   public static void exporterDisponibilitesCSV(Map<Integer, List<String>> disponibilites, String fichier) {
      try {
         PrintWriter writer = new PrintWriter(new FileWriter(fichier));
         
         // En-tête CSV
         writer.println("IDProfessionnel,Jour,HeureDebut,HeureFin");
         
         // Données
         for (Map.Entry<Integer, List<String>> entry : disponibilites.entrySet()) {
            int idProfessionnel = entry.getKey();
            for (String creneau : entry.getValue()) {
               if (creneau != null && !creneau.isBlank()) {
                  int lastSpace = creneau.lastIndexOf(" ");
                  if (lastSpace > 0) {
                     String jour = creneau.substring(0, lastSpace);
                     String heures = creneau.substring(lastSpace + 1);
                     String[] heureParts = heures.split("-", 2);
                     if (heureParts.length >= 2) {
                        writer.println(idProfessionnel + "," + jour + "," + heureParts[0] + "," + heureParts[1]);
                     }
                  }
               }
            }
         }
         
         writer.close();
      } catch (Exception e) {
         System.err.println("Erreur lors de l'export CSV des disponibilités : " + e.getMessage());
      }
   }

   public static void copierFichier(String source, String destination) {
      try {
         java.io.File srcFile = new java.io.File(source);
         java.io.File destFile = new java.io.File(destination);
         
         // Créer le répertoire de destination si nécessaire
         destFile.getParentFile().mkdirs();
         
         java.io.FileInputStream fis = new java.io.FileInputStream(srcFile);
         java.io.FileOutputStream fos = new java.io.FileOutputStream(destFile);
         
         byte[] buffer = new byte[1024];
         int length;
         while ((length = fis.read(buffer)) > 0) {
            fos.write(buffer, 0, length);
         }
         
         fis.close();
         fos.close();
      } catch (Exception e) {
         System.err.println("Erreur lors de la copie du fichier : " + e.getMessage());
      }
   }

   public static void exporterUtilisateursCSV(List<Utilisateur> utilisateurs, String fichier) {
      try {
         PrintWriter writer = new PrintWriter(new FileWriter(fichier));
         
         // En-tête CSV avec spécialité
         writer.println("ID,Nom,Prenom,Username,Password,Role,Specialite");
         
         // Données
         for (Utilisateur utilisateur : utilisateurs) {
            String specialite = "";
            // Si c'est un médecin, récupérer sa spécialité
            if (utilisateur instanceof ProfessionnelSante) {
               ProfessionnelSante medecin = (ProfessionnelSante) utilisateur;
               specialite = medecin.getSpecialite() != null ? medecin.getSpecialite() : "";
            }
            
            String ligne = utilisateur.getId() + "," + 
                          utilisateur.getNom() + "," + 
                          utilisateur.getPrenom() + "," + 
                          utilisateur.getUsername() + "," + 
                          utilisateur.getPassword() + "," + 
                          utilisateur.getRole() + "," +
                          specialite;
            writer.println(ligne);
         }
         
         writer.close();
      } catch (Exception e) {
         System.err.println("Erreur lors de l'export CSV des utilisateurs : " + e.getMessage());
      }
   }

   public static void exporterUtilisateurs(List<Utilisateur> utilisateurs, String fichier) {
      try {
         PrintWriter writer = new PrintWriter(new FileWriter(fichier));
         
         for (Utilisateur utilisateur : utilisateurs) {
            String ligne = utilisateur.getId() + "|" + 
                          utilisateur.getNom() + "|" + 
                          utilisateur.getPrenom() + "|" + 
                          utilisateur.getUsername() + "|" + 
                          utilisateur.getPassword() + "|" + 
                          utilisateur.getRole();
            writer.println(ligne);
         }
         
         writer.close();
      } catch (Exception e) {
         System.err.println("Erreur lors de l'export des utilisateurs : " + e.getMessage());
      }
   }

   public static void sauvegarderConsultationsProgrammes(List<Consultation> var0) {
      assurerRepertoire();

      try {
         PrintWriter var1 = new PrintWriter(new FileWriter(CONSULTATIONS_PROGRAMME_FILE));

         try {
            Iterator<Consultation> var2 = var0.iterator();

            while(var2.hasNext()) {
               Consultation var3 = (Consultation)var2.next();
               if (var3 != null && var3.getStatut() != null) {
                  // Format simplifié : ID|nom_patient|prénom_patient|ID_patient|ID_médecin|statut
                  int idConsultation = var3.getId();
                  String nomPatient = var3.getPatient().getNom();
                  String prenomPatient = var3.getPatient().getPrenom();
                  int idPatient = var3.getPatient().getId();
                  int idMedecin = var3.getProfessionnel().getId();
                  
                  String statut = var3.getStatut();
                  
                  // Format simplifié : ID|nom_patient|prénom_patient|ID_patient|ID_médecin|statut
                  var1.println("" + idConsultation + "|" + nomPatient + "|" + prenomPatient + "|" + idPatient + "|" + idMedecin + "|" + statut);
               }
            }
         } catch (Throwable var7) {
            try {
               var1.close();
            } catch (Throwable var6) {
               var7.addSuppressed(var6);
            }

            throw var7;
         }

         var1.close();
      } catch (IOException var8) {
         System.err.println("Erreur lors de la sauvegarde des consultations programmées : " + var8.getMessage());
      }

   }

   public static void chargerConsultationsProgrammes(List<Patient> var0, List<ProfessionnelSante> var1, List<Consultation> var2) {
      if (var0 != null && var1 != null && var2 != null) {
         File var3 = new File(CONSULTATIONS_PROGRAMME_FILE);
         if (var3.exists()) {
            Map var4 = (Map)var1.stream().collect(Collectors.toMap(Utilisateur::getId, (var0x) -> {
               return var0x;
            }));

            try {
               BufferedReader var5 = new BufferedReader(new FileReader(var3));

               try {
                  String ligne;
                  while ((ligne = var5.readLine()) != null) {
                     if (ligne.trim().isEmpty()) {
                        continue;
                     }

                     String[] var7 = ligne.split("\\|", -1);
                     if (var7.length < 6) { // Format simplifié avec 6 champs
                        continue;
                     }

                     int var8 = parseEntier(var7[0]);
                     if (var8 < 0) {
                        continue;
                     }

                     if (consultationDejaChargee(var2, var8)) {
                        // Mettre à jour le statut pour la consultation existante
                        for (Consultation existingConsult : var2) {
                           if (existingConsult.getId() == var8) {
                              existingConsult.setStatut(var7[5]);
                              break;
                           }
                        }
                        continue;
                     }

                     // Format simplifié : ID|nom_patient|prénom_patient|ID_patient|ID_médecin|statut
                     // On doit reconstruire la date/heure depuis le dossier patient
                     Patient var10 = trouverPatientParId(var0, parseEntier(var7[3]));
                     if (var10 == null) {
                        System.err.println("Patient ID " + var7[3] + " non trouvé pour la consultation " + var8 + ", ligne ignorée.");
                        continue; // Passer à la ligne suivante
                     }

                     ProfessionnelSante var11 = (ProfessionnelSante)var4.get(parseEntier(var7[4]));
                     if (var11 == null) {
                        System.err.println("Médecin ID " + var7[4] + " non trouvé pour la consultation " + var8 + ", ligne ignorée.");
                        continue; // Passer à la ligne suivante
                     }

                     String statut = var7[5];

                     // Chercher la consultation existante dans le dossier du patient pour préserver ses données médicales
                     Consultation consultationExistante = null;
                     for (Consultation existingConsult : var10.getDossierMedical().getConsultations()) {
                        if (existingConsult.getId() == var8) {
                           consultationExistante = existingConsult;
                           break;
                        }
                     }

                     if (consultationExistante != null) {
                        // Mettre à jour seulement le statut de la consultation existante
                        consultationExistante.setStatut(statut != null && !statut.isBlank() ? statut : "non faite");
                        ajouterConsultationSiUnique(var10, var11, var2, consultationExistante);
                     } else {
                        // Créer une nouvelle consultation si aucune n'existe
                        Consultation var15 = new Consultation("01/01/2000 00:00", var10, var11, "", "");
                        var15.setIdPourPersistence(var8);
                        var15.setStatut(statut != null && !statut.isBlank() ? statut : "non faite");
                        ajouterConsultationSiUnique(var10, var11, var2, var15);
                     }
                  }
               } catch (Throwable var17) {
                  try {
                     var5.close();
                  } catch (Throwable var16) {
                     var17.addSuppressed(var16);
                  }

                  throw var17;
               }

               var5.close();
            } catch (IOException var18) {
               System.err.println("Erreur lors du chargement des consultations programmées : " + var18.getMessage());
            }

         }
      }
   }

   private static void ajouterConsultationSiUnique(Patient var0, ProfessionnelSante var1, List<Consultation> var2, Consultation var3) {
      if (!var2.contains(var3) && !consultationDejaChargee(var2, var3.getId())) {
         if (!var0.getDossierMedical().getConsultations().stream().anyMatch((var1x) -> {
            return var1x.getId() == var3.getId();
         })) {
            var0.getDossierMedical().ajouterConsultation(var3);
         }

         // Plus besoin de vérifier les consultations par professionnel - géré globalement

         var2.add(var3);
      }

   }

   private static boolean consultationDejaChargee(List<Consultation> var0, int var1) {
      return var0.stream().anyMatch((var1x) -> {
         return var1x.getId() == var1;
      });
   }

   private static Patient trouverPatientParId(List<Patient> var0, int var1) {
      return (Patient)var0.stream().filter((var1x) -> {
         return var1x.getId() == var1;
      }).findFirst().orElse(null);
   }

   private static String nettoyer(String var0) {
      return var0 == null ? "" : var0.replace("|", "/");
   }

   static {
      DOSSIERS_FILE = "data" + File.separator + "dossiers.txt";
      DOSSIERS_ARCHIVES_FILE = "data" + File.separator + "dossiers_archives.txt";
      UTILISATEURS_FILE = "data" + File.separator + "utilisateurs.txt";
      DISPONIBILITES_FILE = "data" + File.separator + "disponibilites.txt";
      DISPONIBILITE_FILE = "data" + File.separator + "disponibilite.txt";
      PATIENTS_LEGACY_FILE = "data" + File.separator + "patients.txt";
      PATIENTS_ARCHIVES_LEGACY_FILE = "data" + File.separator + "patients_archives.txt";
      CONSULTATIONS_LEGACY_FILE = "data" + File.separator + "consultations.txt";
      CONSULTATIONS_PROGRAMME_FILE = "data" + File.separator + "consultations_programme.txt";
   }
}
