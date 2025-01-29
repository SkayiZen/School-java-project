package fr.projet.dame.model;

public abstract class Piece {
    private final boolean estBlanc; // Indique si la pièce est blanche

    // Constructeur initialisant la couleur de la pièce.
    public Piece(boolean estBlanc) {
        this.estBlanc = estBlanc;
    }

    // Retourne vrai si la pièce est blanche, faux sinon.
    public boolean estBlanc() {
        return estBlanc;
    }

    // Méthode abstraite définissant le déplacement possible d'une pièce.
    public abstract boolean peutDeplacer(Case depart, Case arrivee, Plateau plateau);
}
