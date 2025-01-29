package fr.projet.dame.model;

public class Pion extends Piece {
    // Constructeur initialisant la couleur du pion.
    public Pion(boolean estBlanc) {
        super(estBlanc);
    }

    // Vérifie si le pion peut se déplacer d'une case à une autre.
    @Override
    public boolean peutDeplacer(Case depart, Case arrivee, Plateau plateau) {
        int dx = arrivee.getX() - depart.getX();
        int dy = Math.abs(arrivee.getY() - depart.getY());
        int direction = estBlanc() ? 1 : -1;

        // Déplacement simple en diagonale
        if (dx == direction && dy == 1 && arrivee.estVide()) {
            return true;
        }

        // Déplacement avec capture
        if (dx == 2 * direction && dy == 2) {
            int xInter = depart.getX() + direction;
            int yInter = (depart.getY() + arrivee.getY()) / 2;
            Case caseInter = plateau.getCase(xInter, yInter);

            return caseInter != null
                    && !caseInter.estVide()
                    && caseInter.getPiece().estBlanc() != estBlanc()
                    && arrivee.estVide();
        }
        return false;
    }
}
