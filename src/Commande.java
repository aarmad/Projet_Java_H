import java.sql.Date;
import java.time.LocalDate;
import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

class Commande {
    private Map<Plat, Integer> plats;
    private double total;
    private Connection conn;

    public Commande() {
        this.plats = new HashMap<>();
        this.total = 0;
        try {
            conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/restaurant", "root", "1234");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // Commande un plat
    public void ajouterPlat(Plat plat, int quantite) {
        plats.put(plat, plats.getOrDefault(plat, 0) + quantite);
        total += plat.getPrix() * quantite;
    }

    // Affiche la commande
    public void afficherCommande() {
        System.out.println("Commande :");
        for (Map.Entry<Plat, Integer> entry : plats.entrySet()) {
            System.out.println("- " + entry.getKey().getNom() + " x" + entry.getValue());
        }
        System.out.println("Total : " + total + "€");
    }

    // Enregistre la commande dans la table commande_plat
    public void enregistrerCommande() {
        try {
            PreparedStatement stmt = conn.prepareStatement(
                    "INSERT INTO commandes (total) VALUES (?)", Statement.RETURN_GENERATED_KEYS);
            stmt.setDouble(1, total);
            stmt.executeUpdate();

            ResultSet rs = stmt.getGeneratedKeys();
            int commandeId = -1;
            if (rs.next()) {
                commandeId = rs.getInt(1);
            }

            for (Map.Entry<Plat, Integer> entry : plats.entrySet()) {
                Plat plat = entry.getKey();
                int quantite = entry.getValue();
                PreparedStatement platStmt = conn.prepareStatement(
                        "INSERT INTO commande_plat (id_commande, id_plat, quantite) VALUES (?, ?, ?) ON DUPLICATE KEY UPDATE quantite = quantite + VALUES(quantite)");
                platStmt.setInt(1, commandeId);
                platStmt.setInt(2, plat.getId());
                platStmt.setInt(3, quantite);
                platStmt.executeUpdate();

                enregistrerDansHistorique(plat.getId(), plat.getNom(), plat.getPrix() * quantite, quantite);
            }

            System.out.println("Enregistrement en cours...");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // Enregistre toutes les commandes dans l'historique
    public void enregistrerDansHistorique(int platId, String nomPlat, double total, int quantite) {
        try {
            Date date = Date.valueOf(LocalDate.now());

            PreparedStatement stmt = conn.prepareStatement(
                    "INSERT INTO historique_commandes (plat_id, nom_plat, quantite, prix_total, date_commande) VALUES (?, ?, ?, ?, ?)"
            );
            stmt.setInt(1, platId);
            stmt.setString(2, nomPlat);
            stmt.setInt(3, quantite);
            stmt.setDouble(4, total);
            stmt.setDate(5, date);

            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // Affiche l'historique des commandes
    public void afficherHistorique() {
        try {
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery("SELECT * FROM historique_commandes ORDER BY date_commande DESC");

            while (rs.next()) {
                System.out.printf("Plat ID: %d | Nom: %s | Quantité: %d | Total: %.2f€ | Date: %s\n",
                        rs.getInt("plat_id"),
                        rs.getString("nom_plat"),
                        rs.getInt("quantite"),
                        rs.getDouble("prix_total"),
                        rs.getString("date_commande")
                );
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}