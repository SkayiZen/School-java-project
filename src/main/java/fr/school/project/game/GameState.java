package fr.school.project.game;

public enum GameState {
    EN_COURS,    // Le jeu est en cours
    JOUEUR_1_GAGNE, // Le joueur 1 a gagné
    JOUEUR_2_GAGNE, // Le joueur 2 a gagné
    EGALITE;     // Le jeu se termine par une égalité

    @Override
    public String toString() {
        switch (this) {
            case EN_COURS:
                return "Le jeu est en cours.";
            case JOUEUR_1_GAGNE:
                return "Joueur 1 a gagné !";
            case JOUEUR_2_GAGNE:
                return "Joueur 2 a gagné !";
            case EGALITE:
                return "Le jeu se termine par une égalité.";
            default:
                return "État inconnu.";
        }
    }
}

