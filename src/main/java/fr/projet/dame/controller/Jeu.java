package fr.projet.dame.controller;

import fr.projet.dame.model.*;
import fr.projet.dame.view.InterfaceGraphique;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class Jeu {
    private final Plateau plateau;
    private final Joueur joueur1;
    private final Joueur joueur2;
    private Joueur joueurActuel;
    private final List<String> historiqueCoups;

    // Constructeur du jeu : initialise les joueurs, le plateau et détermine qui commence
    public Jeu(String nomJoueur1, String nomJoueur2) {
        this.plateau = new Plateau();
        this.joueur1 = new Joueur(nomJoueur1, true);
        this.joueur2 = new Joueur(nomJoueur2, false);
        this.joueurActuel = new Random().nextBoolean() ? joueur1 : joueur2;
        this.historiqueCoups = new ArrayList<>();
    }

    // Démarre l'interface graphique et lance la partie
    public void commencer() {
        new InterfaceGraphique(this).afficher();
    }

    // Retourne une copie de l'historique des coups joués
    public List<String> getHistoriqueCoups() {
        return new ArrayList<>(historiqueCoups);
    }

    // Enregistre un coup joué et ajoute l'information sur la prise si nécessaire
    public void enregistrerCoup(Case depart, Case arrivee, Case caseCapturee) {
        String coup = joueurActuel.getNom() + " (" + (joueurActuel.estBlanc() ? "Blanc" : "Noir") + ") : "
                + convertirCoordonnees(depart) + " → " + convertirCoordonnees(arrivee);

        if (caseCapturee != null) {
            coup += " (Prise: " + convertirCoordonnees(caseCapturee) + ")";
        }

        historiqueCoups.add(coup);
    }

    // Convertit les coordonnées en notation standard (ex: 1A, 3C)
    public String convertirCoordonnees(Case c) {
        return String.valueOf((char) ('A' + c.getY())) + (c.getX() + 1);
    }

    // Retourne le plateau de jeu
    public Plateau getPlateau() {
        return plateau;
    }

    // Retourne l'adversaire du joueur actuel
    public Joueur getJoueurAdverse() {
        return (joueurActuel == joueur1) ? joueur2 : joueur1;
    }

    // Retourne les joueurs
    public Joueur getJoueur1() {
        return joueur1;
    }

    public Joueur getJoueur2() {
        return joueur2;
    }

    public Joueur getJoueurActuel() {
        return joueurActuel;
    }

    // Change le tour après un coup
    public void changerTour() {
        joueurActuel = (joueurActuel == joueur1) ? joueur2 : joueur1;
    }

    // Vérifie si un joueur peut encore jouer
    private boolean joueurPeutJouer(Joueur joueur) {
        for (int x = 0; x < 8; x++) {
            for (int y = 0; y < 8; y++) {
                Case caseDepart = plateau.getCase(x, y);
                if (!caseDepart.estVide() && caseDepart.getPiece().estBlanc() == joueur.estBlanc()) {
                    for (int dx = -2; dx <= 2; dx++) {
                        for (int dy = -2; dy <= 2; dy++) {
                            if (dx == 0 || dy == 0) continue;
                            int newX = x + dx;
                            int newY = y + dy;
                            Case caseArrivee = plateau.getCase(newX, newY);
                            if (caseArrivee != null && caseDepart.getPiece().peutDeplacer(caseDepart, caseArrivee, plateau)) {
                                return true;
                            }
                        }
                    }
                }
            }
        }
        return false;
    }

    // Vérifie si le joueur actuel a perdu
    public boolean joueurActuelAPerdu() {
        return !joueurPeutJouer(joueurActuel);
    }

    // ----- MÉTHODES STATIQUES POUR LA CONFIGURATION DU JEU -----

    // Demande le nom des joueurs via une interface graphique améliorée
    public static String[] demanderNomsJoueurs() {
        JPanel panel = new JPanel(new GridLayout(3, 2, 10, 10));
        panel.add(new JLabel("Nom du Joueur 1 (Blanc) :"));
        JTextField nomJoueur1 = new JTextField();
        panel.add(nomJoueur1);
        panel.add(new JLabel("Nom du Joueur 2 (Noir) :"));
        JTextField nomJoueur2 = new JTextField();
        panel.add(nomJoueur2);

        int result = JOptionPane.showConfirmDialog(null, panel, "Configuration des joueurs", JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);
        if (result == JOptionPane.OK_OPTION && !nomJoueur1.getText().trim().isEmpty() && !nomJoueur2.getText().trim().isEmpty()) {
            return new String[]{nomJoueur1.getText().trim(), nomJoueur2.getText().trim()};
        }
        return null;
    }

    // Affiche une boîte de dialogue avec les informations de configuration des joueurs
    private static void afficherConfiguration(String nomJoueur1, String nomJoueur2, String premierJoueur) {
        JOptionPane.showMessageDialog(null,
                "Configuration :\n" +
                        "- " + nomJoueur1 + " (Blanc)\n" +
                        "- " + nomJoueur2 + " (Noir)\n" +
                        "Premier à jouer : " + premierJoueur + "\n" +
                        "Cliquez sur OK pour commencer la partie.",
                "Configuration des joueurs",
                JOptionPane.INFORMATION_MESSAGE);
    }

    // Point d'entrée du programme : configure et lance le jeu
    public static void main(String[] args) {
        String[] nomsJoueurs = demanderNomsJoueurs();
        if (nomsJoueurs != null) {
            Jeu jeu = new Jeu(nomsJoueurs[0], nomsJoueurs[1]);
            afficherConfiguration(nomsJoueurs[0], nomsJoueurs[1], jeu.getJoueurActuel().getNom());
            jeu.commencer();
        } else {
            JOptionPane.showMessageDialog(null, "Les noms des joueurs ne peuvent pas être vides.", "Erreur", JOptionPane.ERROR_MESSAGE);
        }
    }
}
