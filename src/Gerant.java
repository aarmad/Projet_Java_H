public class Gerant extends Employe {
    public Gerant(String nom) {
        super(nom, "Gérant");
    }

    @Override
    public void effectuerTache() {
        System.out.println(nom + " vérifie l'état du stock.");
    }
}