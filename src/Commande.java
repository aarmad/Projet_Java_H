import java.sql.*;
import java.util.ArrayList;
import java.util.List;

class Commande {
    private List<Plat> plats;
    private double total;
    private Connection conn;

    public Commande() {
        this.plats = new ArrayList<>();
        this.total = 0;
        try {
            // Etablie une connexion avec la BDD
            conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/restaurant", "root", "1234");
        } catch (SQLException e) {
            // Message d'errreur au cas où
            e.printStackTrace();
        }
    }

    // Ajoute un plat à la liste des plats
    public void ajouterPlat(Plat plat) {
        plats.add(plat);
        total += plat.getPrix();
    }

    // Affiche les commandes
    public void afficherCommande() {
        System.out.println("Commande :");
        for (Plat plat : plats) {
            System.out.println("- " + plat);
        }
        System.out.println("Total : " + total + "€");
    }

    // Enregistrer une commande
    public void enregistrerCommande() {
        try {
            // Insérer la commande et récupérer l'ID généré
            PreparedStatement stmt = conn.prepareStatement(
                    "INSERT INTO commandes (total) VALUES (?)", Statement.RETURN_GENERATED_KEYS);
            stmt.setDouble(1, total);
            stmt.executeUpdate();

            // Récupérer l'ID de la commande nouvellement insérée
            ResultSet rs = stmt.getGeneratedKeys();
            int commandeId = -1;
            if (rs.next()) {
                commandeId = rs.getInt(1);
            }

            // Associer les plats à cette commande dans la table commandes_plats
            for (Plat plat : plats) {
                PreparedStatement platStmt = conn.prepareStatement(
                        "INSERT INTO commandes_plats (commande_id, plat_id) VALUES (?, ?)");
                platStmt.setInt(1, commandeId);
                platStmt.setInt(2, plat.getId());
                platStmt.executeUpdate();
            }

            System.out.println("Commande enregistrée avec succès !");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
