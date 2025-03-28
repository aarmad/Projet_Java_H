public abstract class Employe {
    protected String nom;
    protected String role;

    public Employe(String nom, String role) {
        this.nom = nom;
        this.role = role;
    }

    public String getNom() { return nom; }
    public String getRole() { return role; }

    public abstract void effectuerTache();
}