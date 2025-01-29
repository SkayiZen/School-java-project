package fr.projet.dame.model;

public class Dame extends Piece {
    // Constructeur initialisant la couleur de la dame.
    public Dame(boolean estBlanc) {
        super(estBlanc);
    }

    // Vérifie si la dame peut se déplacer d'une case à une autre.
    @Override
    public boolean peutDeplacer(Case depart, Case arrivee, Plateau plateau) {
        int dx = arrivee.getX() - depart.getX();
        int dy = arrivee.getY() - depart.getY();

        // Vérification si le déplacement est bien diagonal
        if (Math.abs(dx) != Math.abs(dy)) return false;

        // Déterminer la direction du mouvement
        int stepX = Integer.compare(dx, 0);
        int stepY = Integer.compare(dy, 0);
        int x = depart.getX() + stepX;
        int y = depart.getY() + stepY;

        // Vérification du chemin jusqu'à la destination
        while (x != arrivee.getX() || y != arrivee.getY()) {
            Case caseInter = plateau.getCase(x, y);
            if (!caseInter.estVide()) {
                Piece pieceIntermediaire = caseInter.getPiece();
                if (pieceIntermediaire != null && pieceIntermediaire.estBlanc() != this.estBlanc()) {
                    int nextX = x + stepX;
                    int nextY = y + stepY;
                    Case caseSaut = plateau.getCase(nextX, nextY);

                    if (caseSaut.estVide()) {
                        x = nextX;
                        y = nextY;
                        continue;
                    } else {
                        return false;
                    }
                } else {
                    return false;
                }
            }
            x += stepX;
            y += stepY;
        }
        return arrivee.estVide();
    }
}
