# Système de Gestion Hospitalière

## Vue d'ensemble

Système de gestion hospitalière complet développé en Java pour la gestion des patients, consultations, personnel médical et données. Le système offre une interface en ligne de commande intuitive avec des rôles utilisateurs différenciés et des fonctionnalités d'import/export CSV .

### Fonctionnalités principales

- **Gestion des patients** : Création, modification, archivage des dossiers médicaux
- **Prise de rendez-vous** : Programmation et annulation de consultations
- **Gestion du personnel** : Administration des utilisateurs avec rôles et permissions
- **Dossiers médicaux** : Suivi des constantes, prescriptions, antécédents, examens
- **Import/Export CSV** : Import de fichiers CSV et export des données
- **Multi-rôles** : Administrateur, Médecin, Aide-soignant avec permissions adaptées

---

## Installation et Configuration

### Prérequis

- **Java 8+** installé sur le système
- **Eclipse IDE** (recommandé pour le développement)
- **Windows** (système d'exploitation testé)

### Installation

1. **Cloner ou télécharger** le projet dans votre workspace Eclipse
2. **Importer le projet** dans Eclipse :
   - `File` → `Import` → `Existing Projects into Workspace`
   - Sélectionner le dossier
3. **Rafraîchir le projet** :
   - Clic droit sur le projet → `Refresh` (F5)
4. **Exécuter le programme** :
   - Clic droit sur `Main.java` → `Run As` → `Java Application`



---

## Rôles et Permissions

### Identifiants par défaut

| Rôle | Username | Mot de passe | Permissions |
|------|----------|--------------|-------------|
| Administrateur | `admin` | `admin123#` | Gestion des utilisateurs, statistiques système |
| Médecin | `azii` | `cder123@` | Gestion des consultations, prescriptions, examens médicaux , antécédents , consultation des dossiers patients, gestion des disponibilités |
| Aide-soignant | `QSI` | `cder1234#` | Création et gestion des dossiers patients, mise à jour des constantes, planification/annulation des consultations, import/export des données |



## Guide d'utilisation

### Menu principal

Au démarrage, le système affiche :
```
=== SYSTÈME DE GESTION HOSPITALIÈRE ===
1. Se connecter
2. Quitter
```

### Connexion

1. Choisir `1. Se connecter`
2. Entrer le **username** et **mot de passe**
3. Le système affiche le menu spécifique au rôle

---

# Menu Administrateur


```
--- MENU ADMINISTRATEUR ---
1. Créer un utilisateur 
2. Supprimer un utilisateur 
3. Lister les utilisateurs
4. Afficher statistiques 
5. Modifier mot de passe 
6. Se déconnecter
```

## 1. Gestion des Utilisateurs

### Créer un utilisateur
- **Objectif** : Ajouter un nouvel utilisateur au système
- **Rôles disponibles** :
  - Administrateur 
  - Médecin 
  - Aide-soignant 
- **Validation** : 
  - Username doit être unique dans le système
  - Email doit être unique dans le système

### Supprimer un utilisateur
- Retirer définitivement un utilisateur du système
- Suppression de toutes les données associées à l'utilisateur

### Lister les utilisateurs
-  Afficher tous les utilisateurs actifs du système

## 4. Statistiques du Système

### Indicateurs disponibles
- **Utilisateurs** : Nombre total par rôle et statut
- **Patients** : Total, actifs, archivés
- **Consultations** : Programmées, effectuées, annulées

## 5. Modification du mot de passe

-  Vérification de l'ancien mot de passe
-  Exigences de sécurité minimales
-  Double saisie pour validation

## 6. Déconnexion

- Fermeture automatique de la session


### Menu Médecin

```
# Menu Médecin - Fonctionnalités

## Menu Principal
```
--- MENU MÉDECIN ---
1. Rechercher un patient
2. Consulter dossier médical
3. Ajouter un examen médical
4. Faire une prescription
5. Gérer mes disponibilités
6. Voir mes consultations
7. Faire une consultation
8. Modifier mon mot de passe
9. Se déconnecter
```

## 1. Rechercher un patient
Recherche par ID ou nom pour accéder rapidement au dossier souhaité

## 2. Consulter dossier médical
Affichage complet du dossier patient 

## 3. Ajouter un examen médical
Saisie des résultats d'examens (biologie, imagerie, etc.) avec date et commentaires

## 4. Faire une prescription
- Saisie du médicament, posologie et durée de traitement
- Association automatique au patient concerné

## 5. Gérer mes disponibilités
- Ajouter/modifier ses créneaux disponibles

## 6. Voir mes consultations
Liste de tous les rendez-vous programmés avec statut

## 7. Faire une consultation
- Affiche les consultations programmées du jour
- Permet de compléter : antécédents, motif, observations, diagnostic
- Enregistre automatiquement la consultation comme "Terminée"

## 8. Modifier mon mot de passe
Changement du mot de passe avec confirmation

## 9. Se déconnecter
Fermeture de session et retour à l'écran de connexion
```

# Menu Aide-soignant 
```
1. Créer un dossier patient
2. Rechercher un patient
3. Consulter dossier médical
4. Planifier une consultation
5. Annuler une consultation
6. Mettre à jour les constantes
7. Exporter les données
8. Importer des patients
9. Archiver un dossier médical
10. Désarchiver un dossier médical
11. Modifier mon mot de passe
12. Se déconnecter
```

## 1. Créer un dossier patient
Saisie des informations démographiques et création du dossier médical initial

## 2. Rechercher un patient
Recherche par ID ou nom pour retrouver un patient existant

## 3. Consulter dossier médical
Affichage des informations patient et historique médical

## 4. Planifier une consultation
Prise de rendez-vous avec choix du médecin et du créneau horaire

## 5. Annuler une consultation
Suppression d'un rendez-vous programmé

## 6. Mettre à jour les constantes
Saisie des constantes vitales (tension, température, poids)

## 7. Exporter les données
Export des données au format CSV

## 8. Importer des patients
Import des données au format  CSV

## 9. Archiver un dossier médical
Archivage d'un dossier patient

## 10. Désarchiver un dossier médical
Restauration d'un dossier précédemment archivé

## 11. Modifier mon mot de passe
Changement du mot de passe avec confirmation

## 12. Se déconnecter
Fermeture de session et retour à l'écran de connexion
```

## Stockage des données

### Fichiers de données

| Fichier | Contenu | Format |
|---------|---------|--------|
| `data/dossiers.txt` | Patients et dossiers médicaux | `ID|Nom|Prenom|DateNaissance|...` |
| `data/dossiers_archives.txt` | Patients archivés | `ID|Nom|Prenom|DateNaissance|...` |
| `data/consultations_programme.txt` | Rendez-vous programmés | `ID|PatientID|MedecinID|DateHeure|...` |
| `data/disponibilite.txt` | Disponibilités du personnel | `ProfessionnelID|DateHeure` |
| `data/utilisateurs.txt` | Comptes utilisateurs | `ID|Nom|Prenom|Username|...` |

### Sauvegarde automatique

- **Sauvegarde immédiate** après chaque modification
- **Dossier import/** : Créé automatiquement lors du premier import, stocke toutes les données importées avec timestamp unique
- **Dossier export/** : Créé automatiquement lors du premier export, contient tous les fichiers d'export générés
- **Export manuel** disponible pour toutes les données à tout moment
```

## Dépannage

### Problèmes courants

#### Fichier non trouvé lors de l'import
```
Fichier 'monfichier.csv' non trouvé.
```
**Solutions** :
1. Vérifiez l'orthographe du nom de fichier
2. Placez le fichier dans le dossier du programme
3. Utilisez le chemin complet : `C:\Users\VotreNom\Downloads\fichier.csv`
4. Rafraîchissez Eclipse (F5) si les dossiers n'apparaissent pas

#### Dossiers import/export invisibles dans Eclipse
**Solution** :
- Clic droit sur le projet → `Refresh` (F5)
- Ou activer rafraîchissement automatique : `Window` → `Preferences` → `General` → `Workspace` → Cocher "Refresh using native hooks"

#### Mot de passe incorrect
**Solutions** :
- Utiliser les identifiants par défaut (voir tableau ci-dessus)
- Contacter l'administrateur pour réinitialiser
- Vérifier la casse (majuscules/minuscules)

#### Erreur de format de date
**Formats acceptés** :
- Dates : `dd/MM/yyyy` (ex: `12/12/2025`)
- Heures : `HH:mm` (ex: `14:30`)
- DateTime : `dd/MM/yyyy HH:mm` (ex: `12/12/2025 14:30`)

---
### Sécurité
- Les mots de passe sont validés (8 caractères minimum, 1 chiffre, 1 caractère spécial)
- Chaque utilisateur a des permissions adaptées à son rôle
- Les données sensibles sont stockées localement

---

*Merci d'utiliser notre système de gestion hospitalière !* 
