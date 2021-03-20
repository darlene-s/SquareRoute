package com.example.squareroute.Activity1;

/**
 * @author G.Christian | S.Darlène | T.Kenny
 * Classe Universite qui permet de faire le lien entre l'entité Université de la base de donnée
 * et le code Java
 */
public class Universite {
    public String nom_univ, adresse_univ;
    public Double lat, lng;

    public Universite() {
    }

    /**
     * Constructeur de la classe Université
     *
     * @param nom     : String représentant le nom de l'université
     * @param adresse : String représentant l'adresse de l'université
     * @param lat     : Double représentant la position en latitude de l'université
     * @param lng     : Double représentant la position en longitude de l'université
     */
    public Universite(String nom, String adresse, Double lat, Double lng) {
        this.nom_univ = nom;
        this.adresse_univ = adresse;
        this.lat = lat;
        this.lng = lng;
    }
}
