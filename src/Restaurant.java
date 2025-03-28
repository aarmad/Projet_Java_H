import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class Restaurant {
    public static void main(String[] args) {
        Menu menu = new Menu();
        List<Plat> plats = menu.getPlats();
        Stock stock = new Stock();

        System.out.println("Menu du restaurant:");
        for (Plat plat : plats) {
            System.out.println("- " + plat);
        }

        Commande commande = new Commande();
        Plat platChoisi = menu.getPlatParNom("Margherita");
        if (platChoisi != null && stock.verifierStock(platChoisi.getId())) {
            commande.ajouterPlat(platChoisi);
            stock.mettreAJourStock(platChoisi.getId());
        } else {
            System.out.println("Stock insuffisant pour le plat choisi.");
        }

        commande.afficherCommande();
        commande.enregistrerCommande();

        Employe serveur = new Serveur("Marco");
        serveur.effectuerTache();

        Employe cuisinier = new Cuisinier("Giovanni");
        cuisinier.effectuerTache();

        Employe gerant = new Gerant("Luca");
        gerant.effectuerTache();
    }
}