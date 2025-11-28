public class Antecedent {
    private String type; // pathologie, chirurgie, allergie, traitement
    private String description;
    private String date;

    public Antecedent(String type, String description, String date) {
        this.description = description;
        this.date = date;
    }

    

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    @Override
    public String toString() {
        return type + ": " + description + " (" + date + ")";
    }
}
