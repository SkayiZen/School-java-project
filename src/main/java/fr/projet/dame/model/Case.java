package fr.projet.dame.model;

public class Case {
    private final int x; // Position en abscisse sur le plateau
    private final int y; // Position en ordonnée sur le plateau
    private Piece piece; // Pièce actuellement sur cette case, ou null si vide

    // Constructeur de la case avec ses coordonnées.
    public Case(int x, int y) {
        this.x = x;
        this.y = y;
    }

    // Retourne la coordonnée x de la case.
    public int getX() {
        return x;
    }

    // Retourne la coordonnée y de la case.
    public int getY() {
        return y;
    }

    // Retourne la pièce située sur cette case.
    public Piece getPiece() {
        return piece;
    }

    // Place une pièce sur cette case.
    public void setPiece(Piece piece) {
        this.piece = piece;
    }

    // Vérifie si la case est vide (sans pièce).
    public boolean estVide() {
        return piece == null;
    }
}
