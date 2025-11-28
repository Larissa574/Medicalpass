public class Patient {
    private static int compteurId = 1;
    private int id;
    private String nom;
    private String prenom;
    private String dateNaissance;
    private String adresse;
    private String telephone;
    private String email;
    private DossierMedical dossierMedical;

    public Patient(String nom, String prenom, String dateNaissance, String adresse, String telephone, String email) {
        this.id = compteurId++;
        this.nom = nom;
        this.prenom = prenom;
        this.dateNaissance = dateNaissance;
        this.adresse = adresse;
        this.telephone = telephone;
        this.email = email;
        this.dossierMedical = new DossierMedical(this);
    }

    public int getId() {
        return id;
    }

    public String getNom() {
        return nom;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public String getPrenom() {
        return prenom;
    }

    public void setPrenom(String prenom) {
        this.prenom = prenom;
    }

    public String getDateNaissance() {
        return dateNaissance;
    }

    public void setDateNaissance(String dateNaissance) {
        this.dateNaissance = dateNaissance;
    }

    public String getAdresse() {
        return adresse;
    }

    public void setAdresse(String adresse) {
        this.adresse = adresse;
    }

    public String getTelephone() {
        return telephone;
    }

    public void setTelephone(String telephone) {
        this.telephone = telephone;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public DossierMedical getDossierMedical() {
        return dossierMedical;
    }

    public void setIdPourPersistence(int id) {
        this.id = id;
        if (id >= compteurId) {
            compteurId = id + 1;
        }
    }

    @Override
    public String toString() {
        return "Patient [ID: " + id + ", Nom: " + nom + " " + prenom + 
               ", Date de naissance: " + dateNaissance + ", Tél: " + telephone + "]";
    }

    public void afficherDetails() {
        System.out.println("=== Informations du Patient ===");
        System.out.println("ID: " + id);
        System.out.println("Nom: " + nom + " " + prenom);
        System.out.println("Date de naissance: " + dateNaissance);
        System.out.println("Adresse: " + adresse);
        System.out.println("Téléphone: " + telephone);
        System.out.println("Email: " + email);
    }
}
