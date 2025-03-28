public class Cuisinier extends Employe {
    public Cuisinier(String nom) {
        super(nom, "Cuisinier");
    }

    @Override
    public void effectuerTache() {
        System.out.println(nom + " prépare un plat.");
    }
}