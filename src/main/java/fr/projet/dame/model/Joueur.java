package fr.projet.dame.model;

public class Joueur {
    private final String nom; // Nom du joueur
    private final boolean estBlanc; // Indique si le joueur joue avec les pièces blanches

    // Constructeur initialisant le nom et la couleur du joueur.
    public Joueur(String nom, boolean estBlanc) {
        this.nom = nom;
        this.estBlanc = estBlanc;
    }

    // Retourne le nom du joueur.
    public String getNom() {
        return nom;
    }

    // Retourne vrai si le joueur joue avec les pièces blanches, faux sinon.
    public boolean estBlanc() {
        return estBlanc;
    }
}
