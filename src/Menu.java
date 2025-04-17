import java.sql.*;
import java.util.ArrayList;
import java.util.List;

class Menu {
    private Connection conn;

    public Menu() {
        this.conn = DatabaseConnection.getConnection();
        System.out.println("Connexion réussie !");
    }

    public List<Plat> getPlats() {
        List<Plat> plats = new ArrayList<>();
        try {
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery("SELECT id, nom, prix, type FROM plats");

            while (rs.next()) {
                plats.add(new Plat(rs.getInt("id"), rs.getString("nom"), rs.getDouble("prix"), rs.getString("type")));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return plats;
    }

    // Récupère un plat spécifique à partir de son nom
    public Plat getPlatParNom(String nom) {
        try {
            PreparedStatement stmt = conn.prepareStatement("SELECT id, nom, prix, type FROM plats WHERE nom = ?");
            stmt.setString(1, nom);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                return new Plat(rs.getInt("id"), rs.getString("nom"), rs.getDouble("prix"), rs.getString("type"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }
}
