// Importation des classes nécessaires pour interagir avec la base de données
import java.sql.*;
// Importation des structures de données pour stocker les plats
import java.util.ArrayList;
import java.util.List;

class Menu {
    // Attribut pour gérer la connexion à la base de données
    private Connection conn;

    // Constructeur : initialise la connexion via la classe DatabaseConnection
    public Menu() {
        this.conn = DatabaseConnection.getConnection(); // Connexion avec root et mot de passe
        System.out.println("Connexion réussie !");
    }

    // Récupère tous les plats présents dans la table "plats"
    public List<Plat> getPlats() {
        List<Plat> plats = new ArrayList<>();
        try {
            // Création de la requête SQL
            Statement stmt = conn.createStatement();
            // Exécution de la requête
            ResultSet rs = stmt.executeQuery("SELECT id, nom, prix, type FROM plats");

            // Parcours des résultats pour créer des objets Plat
            while (rs.next()) {
                plats.add(new Plat(rs.getInt("id"), rs.getString("nom"), rs.getDouble("prix"), rs.getString("type")));
            }
        } catch (SQLException e) {
            // Affichage de l'erreur en cas d'exception SQL
            e.printStackTrace();
        }
        // Retourne la liste des plats
        return plats;
    }

    // Récupère un plat spécifique à partir de son nom
    public Plat getPlatParNom(String nom) {
        try {
            // Préparation de la requête paramétrée
            PreparedStatement stmt = conn.prepareStatement("SELECT id, nom, prix, type FROM plats WHERE nom = ?");
            // Remplacement du paramètre dans la requête
            stmt.setString(1, nom);
            // Exécution de la requête
            ResultSet rs = stmt.executeQuery();

            // Si un résultat est trouvé, on crée et retourne un Plat
            if (rs.next()) {
                return new Plat(rs.getInt("id"), rs.getString("nom"), rs.getDouble("prix"), rs.getString("type"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        // Si aucun plat trouvé, on retourne null
        return null;
    }
}
