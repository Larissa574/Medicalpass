package medipass;

public abstract class Utilisateur {
	
	 	protected int id;
	    protected String nom;
	    protected String prenom;
	    protected String username;
	    protected String password;



	    public Utilisateur (int id, String nom, String prenom, String username, String password){
	        this.id = id;
	        this.nom = nom;
	        this.prenom = prenom;
	        this.username = username;
	        this.password = password;
	    }

	    public boolean seConnecter (String username, String password){
	        return this.username.equals(username) && this.password.equals(password);
	    }

	    //------ GETTERS & SETTERS -----
	    public int getId() {return id;}
	    public String getNom() {return nom;}
	    public String getPrenom() {return prenom;}
	    public String getUsername() {return username;}
	    public String getPassword() {return password;}

	    public void setNom(String nom) {this.nom = nom;}
	    public void setPrenom(String prenom) {this.prenom = prenom;}
	    public void setUsername(String username) {this.username = username;}
	    public void setPassword(String password) {this.password = password;}

}
