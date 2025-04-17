import java.sql.*;
import java.util.Scanner;
import java.util.ArrayList;
import java.util.List;

public class Stock {

    private Connection conn;

    public Stock() {
        try {
            conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/restaurant", "root", "1234");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // Vérifie si tous les ingrédients nécessaires pour un plat sont disponibles en quantité suffisante
    public boolean verifierStock(int platId) {
        try {
            PreparedStatement stmt = conn.prepareStatement("SELECT ingredient_id, quantite FROM recette WHERE plat_id = ?");
            stmt.setInt(1, platId);
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                int ingredientId = rs.getInt("ingredient_id");
                int quantiteNecessaire = rs.getInt("quantite");

                if (!verifierIngredient(ingredientId, quantiteNecessaire)) {
                    return false;
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return true;
    }

    // Vérifie si un ingrédient est disponible dans une quantité suffisante
    private boolean verifierIngredient(int ingredientId, int quantiteNecessaire) {
        try {
            PreparedStatement stmt = conn.prepareStatement("SELECT quantite FROM stocks WHERE id = ?");
            stmt.setInt(1, ingredientId);
            ResultSet rs = stmt.executeQuery();

            if (rs.next() && rs.getInt("quantite") >= quantiteNecessaire) {
                return true;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    //Met à jour le stock en soustrayant les quantités utilisées pour un plat
    public void mettreAJourStock(int platId) {
        try {
            PreparedStatement stmt = conn.prepareStatement("SELECT ingredient_id, quantite FROM recette WHERE plat_id = ?");
            stmt.setInt(1, platId);
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                int ingredientId = rs.getInt("ingredient_id");
                int quantiteUtilisee = rs.getInt("quantite");

                PreparedStatement updateStmt = conn.prepareStatement("UPDATE stocks SET quantite = quantite - ? WHERE id = ?");
                updateStmt.setInt(1, quantiteUtilisee);
                updateStmt.setInt(2, ingredientId);
                updateStmt.executeUpdate();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void afficherEtModifierStock(Scanner scanner) {
        try {
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery("SELECT id, nom, quantite FROM stocks");

            System.out.println("\n⛃ État actuel du stock :");
            System.out.println("══════════════════════════════");
            while (rs.next()) {
                System.out.printf("ID: %d | %-15s | Quantité: %d\n",
                        rs.getInt("id"), rs.getString("nom"), rs.getInt("quantite"));
            }
            System.out.println("══════════════════════════════");

            System.out.print("\n◪ Voulez-vous modifier un stock ? (oui/non) : ");
            String reponse = scanner.nextLine().trim().toLowerCase();

            if (reponse.equals("oui")) {
                System.out.print("▶ ID de l'ingrédient à modifier : ");
                int id = scanner.nextInt();
                System.out.print("▶ Quantité à ajouter/retirer (ex : -2 ou 5) : ");
                int qte = scanner.nextInt();
                scanner.nextLine();

                PreparedStatement update = conn.prepareStatement(
                        "UPDATE stocks SET quantite = quantite + ? WHERE id = ?");
                update.setInt(1, qte);
                update.setInt(2, id);
                int rows = update.executeUpdate();

                if (rows > 0) {
                    System.out.println("√ Stock mis à jour !");
                } else {
                    System.out.println("✘ Aucun ingrédient trouvé avec cet ID.");
                }
            } else {
                System.out.println("❖ Affichage terminé sans modification.");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
