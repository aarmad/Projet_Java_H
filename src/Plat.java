import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class Plat {
    private int id;
    private String nom;
    private double prix;
    private String type;

    public Plat(int id, String nom, double prix, String type) {
        this.id = id;
        this.nom = nom;
        this.prix = prix;
        this.type = type;
    }

    public int getId() { return id; }
    public String getNom() { return nom; }
    public double getPrix() { return prix; }
    public String getType() { return type; }

    @Override
    public String toString() {
        return nom + " (" + type + ") - " + prix + "€";
    }
}
