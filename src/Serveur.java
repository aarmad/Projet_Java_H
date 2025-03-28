public class Serveur extends Employe {
    public Serveur(String nom) {
        super(nom, "Serveur");
    }

    @Override
    public void effectuerTache() {
        System.out.println(nom + " prend une commande.");
    }
}