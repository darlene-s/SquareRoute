package com.example.squareroute.Activity2;
/**
 * @author G.Christian | S.Darlène | T.Kenny
 * Classe Bibliothèque qui permet de faire le lien entre l'entité Bibliothèque de la base de donnée
 * et le code Java
 */
public class Bibliotheque {

    public String nom_bibli, adresse_bibli;
    public Double lat, lng;

    public Bibliotheque() {
    }

    /**
     * Constructeur de la classe Bibliotheque
     *
     * @param nom     : String pour le nom de la bibliothèque
     * @param adresse : String pour l'adresse de la bibliothèque
     * @param lat     : Double pour la position en latitude de la bibliothèque
     * @param lng     : Double pour la position en longitude de la bibliothèque
     */
    public Bibliotheque(String nom, String adresse, Double lat, Double lng) {
        this.nom_bibli = nom;
        this.adresse_bibli = adresse;
        this.lat = lat;
        this.lng = lng;
    }
}
