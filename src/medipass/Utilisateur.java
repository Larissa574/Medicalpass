package medipass;
import java.util.List;

public abstract class Utilisateur {
    protected static int compteurId = 1000;
    protected int id;
    protected String nom;
    protected String prenom;
    protected String username;
    protected String password;
    private Utilisateur utilisateurConnecte;
    private List<Utilisateur> utilisateurs;

    public Utilisateur(String nom, String prenom, String username, String password) {
        this.id = compteurId++;
        this.nom = nom;
        this.prenom = prenom;
        this.username = username;
        this.password = password;
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

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setIdPourPersistence(int id) {
        this.id = id;
        if (id >= compteurId) {
            compteurId = id + 1;
        }
    }

    public abstract String getRole();

    protected String getSupplementalInfo() {
        return "";
    }


    public boolean seConnecter(String username, String password) {
        for (Utilisateur u : utilisateurs) {
            if (u.getUsername().equals(username) && u.getPassword().equals(password)) {
                utilisateurConnecte = u;
                return true;
            }
        }
        return false;
    }

    public void seDeconnecter() {
        utilisateurConnecte = null;
    }

    public Utilisateur getUtilisateurConnecte() {
        return utilisateurConnecte;
    }

    public boolean modifierMotDePasse(Utilisateur utilisateur, String motDePasse) {
        if (utilisateur == null || motDePasse == null || motDePasse.isBlank()) {
            return false;
        }
        utilisateur.setPassword(motDePasse);
        BaseDeDonnees.sauvegarderUtilisateurs(utilisateurs);
        return true;
    }
    @Override
    public String toString() {
        return "Utilisateur [ID: " + id + ", " + nom + " " + prenom + 
               ", Username: " + username + ", Role: " + getRole() + getSupplementalInfo() + "]";
    }
}
