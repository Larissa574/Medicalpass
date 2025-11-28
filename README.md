# Système de Gestion Hospitalière

## Vue d'ensemble

Système de gestion hospitalière complet développé en Java pour la gestion des patients, consultations, personnel médical et données. Le système offre une interface en ligne de commande intuitive avec des rôles utilisateurs différenciés et des fonctionnalités d'import/export CSV universelles.

### Fonctionnalités principales

- **Gestion des patients** : Création, modification, archivage des dossiers médicaux
- **Prise de rendez-vous** : Programmation et annulation de consultations
- **Gestion du personnel** : Administration des utilisateurs avec rôles et permissions
- **Dossiers médicaux** : Suivi des constantes, prescriptions, antécédents
- **Import/Export CSV** : Import universel de fichiers CSV et export des données
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
   - Sélectionner le dossier `drarmandProject`
3. **Rafraîchir le projet** :
   - Clic droit sur le projet → `Refresh` (F5)
4. **Exécuter le programme** :
   - Clic droit sur `Main.java` → `Run As` → `Java Application`

### Structure des dossiers

```
drarmandProject/
├── src/                    # Code source Java
├── data/                   # Données système
│   ├── dossiers.txt
│   ├── consultations_programme.txt
│   └── disponibilite.txt
├── import/                 # Fichiers CSV importés
├── export/                 # Fichiers CSV exportés
└── README.md              # Documentation
```

---

## Rôles et Permissions

### Identifiants par défaut

| Rôle | Username | Mot de passe | Permissions |
|------|----------|--------------|-------------|
| Administrateur | `admin` | `admin123` | Accès total à toutes les fonctionnalités |
| Médecin | `medecin` | `med123` | Gestion des consultations, prescriptions, dossiers |
| Aide-soignant | `aide` | `aide123` | Gestion des patients, constantes, planning |

### Permissions détaillées

#### Administrateur
- Gestion complète des utilisateurs
- Import/Export de toutes les données
- Archivage/Restauration des dossiers
- Configuration du système

#### Médecin
- Réalisation des consultations
- Prescription de traitements
- Accès aux dossiers médicaux
- Gestion des disponibilités

#### Aide-soignant
- Gestion des patients
- Prise de constantes
- Programmation/Annulation de consultations
- Import de données CSV

---

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

### Menu Administrateur

```
--- MENU ADMINISTRATEUR ---
1. Gérer les utilisateurs
2. Importer des données
3. Exporter des données
4. Archiver un dossier médical
5. Désarchiver un dossier médical
6. Se déconnecter
```

#### 1. Gestion des utilisateurs
- Créer un utilisateur : Ajouter médecin, aide-soignant, ou administrateur
- Validation unique : Username et email doivent être uniques
- Rôles disponibles : Administrateur, Médecin, Aide-soignant

#### 2. Import des données
```
--- IMPORT DES DONNÉES ---
INSTRUCTIONS :
- Mettez simplement le nom du fichier CSV (ex: monfichier.csv)
- OU mettez le chemin complet (ex: C:\Users\Downloads\monfichier.csv)
- OU placez votre fichier dans le dossier du programme
- Le système cherchera automatiquement dans plusieurs emplacements

Nom du fichier CSV à importer: Base_Clients.csv
Fichier trouvé : data/Base_Clients.csv
Fichier importé avec succès !
```

**Emplacements recherchés automatiquement** :
- Dossier du programme
- `data/` (sous-dossier)
- `imports/` (sous-dossier)
- Dossier parent
- Bureau (`Desktop/`)
- Téléchargements (`Downloads/`)
- Documents (`Documents/`)

#### 3. Export des données
Options d'export disponibles :
- Utilisateurs
- Dossiers patients
- Dossiers archivés
- Consultations programmées
- Disponibilités
- **TOUTES les bases de données**

---

### Menu Médecin

```
--- MENU MÉDECIN ---
1. Faire une consultation
2. Prescrire un traitement
3. Voir mes disponibilités
4. Se déconnecter
```

#### 1. Faire une consultation
- Affiche les consultations programmées du jour
- Permet de compléter : motif, observations, diagnostic
- Enregistre automatiquement la consultation comme "Terminée"

#### 2. Prescrire un traitement
- Saisie du médicament, posologie, durée
- Association automatique au patient concerné

#### 3. Gestion des disponibilités
- Ajouter/modifier ses créneaux disponibles
- Format : `JJ/MM/AAAA HH:MM`

---

### Menu Aide-soignant

```
--- MENU AIDE-SOIGNANT ---
1. Gérer les patients
2. Mettre à jour les constantes
3. Programmer une consultation
4. Annuler une consultation
5. Importer des données
6. Se déconnecter
```

#### 1. Gestion des patients
- Créer un patient : Nom, prénom, date de naissance, email, téléphone
- Email unique : Validation automatique d'unicité
- Rechercher un patient : Par ID

#### 2. Mise à jour des constantes
```
--- MISE À JOUR CONSTANTES ---
ID du patient: 17
Date des constantes: 12/12/2025
Poids (kg): 64
Tension (ex: 12/8): 12/8
Température (°C): 38
Constantes enregistrées pour le 12/12/2025
```

**Format d'enregistrement** : `12/12/2025 ~ Poids: 64 kg; Tension: 12/8; Température: 38 °C`

#### 3. Programmation des consultations
- Sélection du patient et du professionnel de santé
- Choix de la date et heure
- Ajout automatique au planning

#### 4. Annulation des consultations
- Affichage numéroté des consultations programmées
- Sélection simple par numéro
- Confirmation avant annulation

---

## Import/Export CSV

### Import CSV Universel

Le système accepte **N'IMPORTE QUEL fichier CSV** sans restriction :

**Fonctionnalités** :
- Import brut : Stockage tel quel sans validation
- Recherche automatique dans plusieurs emplacements
- Timestamp unique pour éviter les conflits
- Boucle d'import : Importer plusieurs fichiers sans quitter
- Aperçu des données avant confirmation

**Format de stockage** :
```
import/import_20251127_234928_Base_Ventes.csv
```

**Exemples de fichiers acceptés** :
- `Base_Clients.csv` (données clients)
- `Base_Ventes.csv` (données ventes)
- `biens_imobiliers.csv` (immobilier)
- **N'importe quel fichier CSV**

### Export des données

**Formats d'export disponibles** :
- `utilisateurs_export.csv` : Personnel et identifiants
- `dossiers_export.csv` : Patients et dossiers médicaux
- `consultations_programmes_export.csv` : Planning des rendez-vous
- `disponibilites_export.csv` : Créneaux des professionnels

**Structure des fichiers exportés** :
- Format CSV standard avec séparateur `,`
- Encodage UTF-8
- En-têtes descriptives pour chaque colonne

---

## Stockage des données

### Fichiers de données

| Fichier | Contenu | Format |
|---------|---------|--------|
| `data/dossiers.txt` | Patients et dossiers médicaux | `ID|Nom|Prenom|DateNaissance|...` |
| `data/consultations_programme.txt` | Rendez-vous programmés | `ID|PatientID|MedecinID|DateHeure|...` |
| `data/disponibilite.txt` | Disponibilités du personnel | `ProfessionnelID|DateHeure` |
| `data/utilisateurs.txt` | Comptes utilisateurs | `ID|Nom|Prenom|Username|...` |

### Sauvegarde automatique

- Sauvegarde immédiate après chaque modification
- Backup automatique dans le dossier `import/` pour les imports
- Export manuel disponible pour toutes les données

---

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

### Support technique

Pour toute question ou problème :
1. Vérifier cette documentation
2. Consulter les messages d'erreur affichés
3. Contacter l'administrateur système

---

## Notes importantes

### Sécurité
- Les mots de passe sont validés (8 caractères minimum, 1 chiffre, 1 caractère spécial)
- Chaque utilisateur a des permissions adaptées à son rôle
- Les données sensibles sont stockées localement

### Bonnes pratiques
- Sauvegardez régulièrement vos données avec la fonction d'export
- Utilisez des noms de fichiers clairs pour les imports
- Archivez les dossiers médicaux anciens pour optimiser les performances
- Vérifiez les disponibilités avant de programmer des consultations

### Mises à jour
Le système est conçu pour être évolutif. Les fonctionnalités peuvent être étendues selon les besoins de l'établissement.

---

## Contact

**Développé par** : Équipe de développement hospitalière  
**Version** : 1.0  
**Dernière mise à jour** : Décembre 2025

---

*Merci d'utiliser notre système de gestion hospitalière !* 
