package com.example.squareroute;

public class Universite {
    public String nom_univ,adresse_univ;
    public Double lat,lng;
    public Universite(){
    }
    public Universite (String nom, String adresse, Double lat, Double lng) {
        this.nom_univ = nom;
        this.adresse_univ = adresse;
        this.lat = lat;
        this.lng = lng;
    }
}
