package medipass;
import java.time.LocalDate;
import java.time.Period;

public class Patient {
	
	    private static int compteurId = 0;
	    private String idPatient;
	    private String nom;
	    private String prenom;
	    private LocalDate dateNaissance;
	    private String sexe;
	    private String adresse;
	    private String telephone;
	    private String email;
	    private DossierMedical dossierMedical;
	    
	    
	    public Patient(String nom, String prenom, LocalDate dateNaissance, 
	                   String sexe, String adresse, String telephone, String email) {
	        
	        if (nom == null || nom.trim().isEmpty()) {
	            throw new IllegalArgumentException("Le nom est obligatoire");
	        }
	        if (prenom == null || prenom.trim().isEmpty()) {
	            throw new IllegalArgumentException("Le prénom est obligatoire");
	        }
	        if (dateNaissance == null) {
	            throw new IllegalArgumentException("La date de naissance est obligatoire");
	        }
	        
	        compteurId++;
	        this.idPatient = "PAT-" + String.format("%05d", compteurId);
	        this.nom = nom.toUpperCase();
	        this.prenom = prenom;
	        this.dateNaissance = dateNaissance;
	        this.sexe = sexe;
	        this.adresse = adresse;
	        this.telephone = telephone;
	        this.email = email;
	        this.dossierMedical = new DossierMedical(this);

	    }
	    
	    public Patient(String nom, String prenom, LocalDate dateNaissance) {
	        this(nom, prenom, dateNaissance, null, null, null, null);
	    }
	    public int getAge() {
	        return Period.between(dateNaissance, LocalDate.now()).getYears();
	    }
	    
	    public String getNomComplet() {
	        return prenom + " " + nom;
	    }
	    public String getIdPatient() {
	        return idPatient;
	    }
	    
	    public String getNom() {
	        return nom;
	    }
	    
	    public String getPrenom() {
	        return prenom;
	    }
	    
	    public LocalDate getDateNaissance() {
	        return dateNaissance;
	    }
	    
	    public String getSexe() {
	        return sexe;
	    }
	    
	    public String getAdresse() {
	        return adresse;
	    }
	    
	    public String getTelephone() {
	        return telephone;
	    }
	    
	    public String getEmail() {
	        return email;
	    }
	    
	    public DossierMedical getDossierMedical() {
	        return dossierMedical;
	    }
	    public void setNom(String nom) {
	        this.nom = nom.toUpperCase();
	    }
	    
	    public void setPrenom(String prenom) {
	        this.prenom = prenom;
	    }
	    
	    public void setDateNaissance(LocalDate dateNaissance) {
	        this.dateNaissance = dateNaissance;
	    }
	    
	    public void setSexe(String sexe) {
	        this.sexe = sexe;
	    }
	    
	    public void setAdresse(String adresse) {
	        this.adresse = adresse;
	    }
	    
	    public void setTelephone(String telephone) {
	        this.telephone = telephone;
	    }
	    
	    public void setEmail(String email) {
	        this.email = email;
	    }
	    public String toString() {
	        return "Patient [" + idPatient + "]\n" +
	               "  Nom: " + getNomComplet() + "\n" +
	               "  Date naissance: " + dateNaissance + " (" + getAge() + " ans)\n" +
	               "  Sexe: " + (sexe != null ? sexe : "Non renseigné") + "\n" +
	               "  Adresse: " + (adresse != null ? adresse : "Non renseignée") + "\n" +
	               "  Téléphone: " + (telephone != null ? telephone : "Non renseigné") + "\n" +
	               "  Email: " + (email != null ? email : "Non renseigné");
	    }
	    
	    
	    
	     }
	    
	    
	    



