package medipass;
public class ExamenMedical {
   private static int compteurId = 1;
   private int id;
   private String type;
   private String nom;
   private String date;
   private Patient patient;
   private ProfessionnelSante prescripteur;
   private String resultat;
   private String commentaire;

   public ExamenMedical(String var1, String var2, String var3, Patient var4, ProfessionnelSante var5) {
      this.id = compteurId++;
      this.type = var1;
      this.nom = var2;
      this.date = var3;
      this.patient = var4;
      this.prescripteur = var5;
   }

   public int getId() {
      return this.id;
   }

   public String getType() {
      return this.type;
   }

   public void setType(String var1) {
      this.type = var1;
   }

   public String getNom() {
      return this.nom;
   }

   public void setNom(String var1) {
      this.nom = var1;
   }

   public String getDate() {
      return this.date;
   }

   public void setDate(String var1) {
      this.date = var1;
   }

   public Patient getPatient() {
      return this.patient;
   }

   public void setPatient(Patient var1) {
      this.patient = var1;
   }

   public ProfessionnelSante getPrescripteur() {
      return this.prescripteur;
   }

   public void setPrescripteur(ProfessionnelSante var1) {
      this.prescripteur = var1;
   }

   public String getResultat() {
      return this.resultat;
   }

   public void setResultat(String var1) {
      this.resultat = var1;
   }

   public String getCommentaire() {
      return this.commentaire;
   }

   public void setCommentaire(String var1) {
      this.commentaire = var1;
   }

   public void setIdPourPersistence(int var1) {
      this.id = var1;
      if (var1 >= compteurId) {
         compteurId = var1 + 1;
      }

   }

   public String toString() {
      String var1 = this.resultat != null && !this.resultat.isBlank() ? this.resultat : "Non renseigné";
      String var2 = this.commentaire != null && !this.commentaire.isBlank() ? this.commentaire : "Non renseigné";
      int var10000 = this.id;
      return "Examen [ID: " + var10000 + ", Type: " + this.type + ", Nom: " + (this.nom != null ? this.nom : "Non renseigné") + ", Date: " + this.date + ", Patient: " + this.patient.getNom() + " " + this.patient.getPrenom() + ", Résultat: " + var1 + ", Commentaire: " + var2 + "]";
   }
}
