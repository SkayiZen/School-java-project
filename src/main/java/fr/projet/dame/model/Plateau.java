package fr.projet.dame.model;

public class Plateau {
    private final Case[][] cases; // Tableau représentant les cases du plateau

    // Constructeur initialisant le plateau et plaçant les pièces.
    public Plateau() {
        cases = new Case[8][8];
        initialiser();
    }

    // Initialise le plateau avec les pièces aux positions de départ.
    private void initialiser() {
        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                cases[i][j] = new Case(i, j);
                if ((i + j) % 2 != 0) {
                    if (i < 3) cases[i][j].setPiece(new Pion(true));
                    else if (i > 4) cases[i][j].setPiece(new Pion(false));
                }
            }
        }
    }

    // Déplace une pièce d'une case à une autre.
    public void deplacerPiece(Case depart, Case arrivee) {
        Piece piece = depart.getPiece();
        depart.setPiece(null);
        arrivee.setPiece(piece);

        // Vérifie si une pièce a été sautée (capture)
        if (Math.abs(arrivee.getX() - depart.getX()) == 2) {
            int xCapture = (depart.getX() + arrivee.getX()) / 2;
            int yCapture = (depart.getY() + arrivee.getY()) / 2;
            cases[xCapture][yCapture].setPiece(null);
        }

        // Promotion d'un pion en dame si atteint l'extrémité
        if (piece instanceof Pion && (arrivee.getX() == 0 || arrivee.getX() == 7)) {
            arrivee.setPiece(new Dame(piece.estBlanc()));
        }
    }

    // Retourne la case située aux coordonnées (x, y).
    public Case getCase(int x, int y) {
        if (x < 0 || x >= 8 || y < 0 || y >= 8) return null;
        return cases[x][y];
    }
}
