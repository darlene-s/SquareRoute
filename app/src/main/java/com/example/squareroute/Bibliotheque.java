package com.example.squareroute;

public class Bibliotheque {
    public String nom_bibli,adresse_bibli;
    public Double lat,lng;
    public Bibliotheque(){
    }
    public Bibliotheque (String nom, String adresse, Double lat, Double lng) {
        this.nom_bibli = nom;
        this.adresse_bibli = adresse;
        this.lat = lat;
        this.lng = lng;
    }
}
