// Importation des classes nécessaires pour gérer les connexions et les requêtes SQL
import java.sql.*;
// Importation non utilisée ici mais utile si tu veux gérer des collections d'objets
import java.util.ArrayList;
import java.util.List;

public class Stock {

    // Connexion à la base de données
    private Connection conn;

    // Constructeur : établit la connexion avec la base de données MySQL
    public Stock() {
        try {
            conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/restaurant", "root", "1234");
        } catch (SQLException e) {
            // Affiche les erreurs s’il y a un problème de connexion
            e.printStackTrace();
        }
    }

    // Vérifie si tous les ingrédients nécessaires pour un plat sont disponibles en quantité suffisante
    public boolean verifierStock(int platId) {
        try {
            // Prépare une requête pour obtenir les ingrédients nécessaires pour un plat
            PreparedStatement stmt = conn.prepareStatement("SELECT ingredient_id, quantite FROM recette WHERE plat_id = ?");
            stmt.setInt(1, platId);
            ResultSet rs = stmt.executeQuery();

            // Pour chaque ingrédient, vérifier s’il est disponible en stock
            while (rs.next()) {
                int ingredientId = rs.getInt("ingredient_id");
                int quantiteNecessaire = rs.getInt("quantite");

                if (!verifierIngredient(ingredientId, quantiteNecessaire)) {
                    // Si un seul ingrédient est insuffisant, on retourne false
                    return false;
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        // Tous les ingrédients sont disponibles
        return true;
    }

    // Vérifie si un ingrédient est disponible dans une quantité suffisante
    private boolean verifierIngredient(int ingredientId, int quantiteNecessaire) {
        try {
            // Requête pour obtenir la quantité actuelle de l’ingrédient en stock
            PreparedStatement stmt = conn.prepareStatement("SELECT quantite FROM stock WHERE id = ?");
            stmt.setInt(1, ingredientId);
            ResultSet rs = stmt.executeQuery();

            // Si on a une ligne et que la quantité est suffisante
            if (rs.next() && rs.getInt("quantite") >= quantiteNecessaire) {
                return true;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        // Quantité insuffisante ou erreur
        return false;
    }

    //Met à jour le stock en soustrayant les quantités utilisées pour un plat
    public void mettreAJourStock(int platId) {
        try {
            // Obtenir les ingrédients et les quantités nécessaires pour ce plat
            PreparedStatement stmt = conn.prepareStatement("SELECT ingredient_id, quantite FROM recette WHERE plat_id = ?");
            stmt.setInt(1, platId);
            ResultSet rs = stmt.executeQuery();

            // Pour chaque ingrédient, mettre à jour la quantité en stock
            while (rs.next()) {
                int ingredientId = rs.getInt("ingredient_id");
                int quantiteUtilisee = rs.getInt("quantite");

                // Mise à jour du stock : soustraction de la quantité utilisée
                PreparedStatement updateStmt = conn.prepareStatement("UPDATE stock SET quantite = quantite - ? WHERE id = ?");
                updateStmt.setInt(1, quantiteUtilisee);
                updateStmt.setInt(2, ingredientId);
                updateStmt.executeUpdate();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

}
