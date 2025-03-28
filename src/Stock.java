import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class Stock {

    private Connection conn;

    public Stock() {
        try {
            conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/restaurant", "root", "password");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

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

    private boolean verifierIngredient(int ingredientId, int quantiteNecessaire) {
        try {
            PreparedStatement stmt = conn.prepareStatement("SELECT quantite FROM stock WHERE id = ?");
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

    public void mettreAJourStock(int platId) {
        try {
            PreparedStatement stmt = conn.prepareStatement("SELECT ingredient_id, quantite FROM recette WHERE plat_id = ?");
            stmt.setInt(1, platId);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                int ingredientId = rs.getInt("ingredient_id");
                int quantiteUtilisee = rs.getInt("quantite");
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
