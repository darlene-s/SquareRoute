package com.example.squareroute.BasicActivity;

/**
 * @author G.Christian | S.Darlène | T.Kenny
 * Classe User qui permet de faire le lien entre l'entité utilisateur de la base de donnée
 * et le code Java
 */
public class User {
    public String email, prenom;

    public User() {
    }

    /**
     * Constructeur de la classe User
     *
     * @param email  : String représentant l'email de l'utilisateur
     * @param prenom : String représentant le prénom de l'utilisateur
     */
    public User(String email, String prenom) {
        this.email = email;
        this.prenom = prenom;
    }
}
