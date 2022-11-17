package org.example;

public class Videojuego {
    private int id;
    private String nom;
    private double precio;

    public Videojuego(int id, String nom, double precio) {
        this.id = id;
        this.nom = nom;
        this.precio = precio;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNom() {
        return nom;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public double getPrecio() {
        return precio;
    }

    public void setPrecio(double precio) {
        this.precio = precio;
    }

    @Override
    public String toString() {
        return "Videojuego{" +
                "id=" + id +
                ", nom='" + nom + '\'' +
                ", precio=" + precio +
                '}';
    }
}