package fr.projet.dame.view;

import fr.projet.dame.controller.Jeu;
import fr.projet.dame.model.Case;
import fr.projet.dame.model.Dame;
import fr.projet.dame.model.Pion;
import fr.projet.dame.model.Plateau;

import javax.swing.*;
import javax.swing.text.Style;
import javax.swing.text.StyleConstants;
import javax.swing.text.StyledDocument;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class InterfaceGraphique extends JFrame {
    private final Jeu jeu; // Instance du jeu en cours
    private final JPanel plateauPanel; // Panneau contenant le plateau
    private final JLabel messageLabel; // Label affichant les messages du jeu
    private Case caseSelectionnee; // Case sélectionnée par le joueur
    private final JTextPane historiqueTextPane; // Zone de texte affichant l'historique des coups
    private final JScrollPane historiqueScrollPane; // Scroll pane pour l'historique

    // Constructeur initialisant l'interface
    public InterfaceGraphique(Jeu jeu) {
        this.jeu = jeu;
        setTitle("Jeu de Dames - " + jeu.getJoueur1().getNom() + " vs " + jeu.getJoueur2().getNom());
        setSize(800, 800);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());
        getContentPane().setBackground(new Color(50, 50, 50));

        // Panneau de l'historique des coups avec une taille fixe
        JPanel historiquePanel = new JPanel(new BorderLayout());
        historiquePanel.setBorder(BorderFactory.createTitledBorder("Historique des coups"));
        historiqueTextPane = new JTextPane();
        historiqueTextPane.setEditable(false);
        historiqueTextPane.setBackground(new Color(30, 30, 30));
        historiqueTextPane.setForeground(Color.WHITE);
        historiqueTextPane.setPreferredSize(new Dimension(200, 600)); // Taille fixe
        historiqueScrollPane = new JScrollPane(historiqueTextPane);
        historiqueScrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
        historiquePanel.add(historiqueScrollPane, BorderLayout.CENTER);
        historiquePanel.setBackground(new Color(70, 70, 70));
        add(historiquePanel, BorderLayout.EAST);

        // Panneau principal contenant le plateau et les labels
        JPanel mainPanel = new JPanel(new BorderLayout());
        mainPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        mainPanel.setBackground(new Color(50, 50, 50));
        mainPanel.add(creerPanelColonnes(), BorderLayout.NORTH);
        mainPanel.add(creerPanelLignes(), BorderLayout.WEST);

        // Plateau de jeu
        plateauPanel = new JPanel(new GridLayout(8, 8));
        plateauPanel.setBackground(new Color(70, 70, 70));
        initialiserPlateau();
        mainPanel.add(plateauPanel, BorderLayout.CENTER);
        add(mainPanel, BorderLayout.CENTER);

        // Label d'affichage des messages
        messageLabel = new JLabel("", SwingConstants.CENTER);
        messageLabel.setFont(new Font("Arial", Font.BOLD, 18));
        messageLabel.setForeground(Color.WHITE);
        messageLabel.setBackground(new Color(70, 70, 70));
        messageLabel.setOpaque(true);
        mettreAJourMessage();
        add(messageLabel, BorderLayout.SOUTH);
    }

    // Crée le panneau affichant les colonnes (A-H)
    private JPanel creerPanelColonnes() {
        JPanel panel = new JPanel(new GridLayout(1, 8));
        for (char c = 'A'; c <= 'H'; c++) {
            JLabel label = new JLabel(String.valueOf(c), SwingConstants.CENTER);
            label.setFont(new Font("Arial", Font.BOLD, 14));
            panel.add(label);
        }
        return panel;
    }

    // Crée le panneau affichant les lignes (1-8)
    private JPanel creerPanelLignes() {
        JPanel panel = new JPanel(new GridLayout(8, 1));
        for (int i = 1; i <= 8; i++) {
            JLabel label = new JLabel(String.valueOf(i), SwingConstants.CENTER);
            label.setFont(new Font("Arial", Font.BOLD, 14));
            panel.add(label);
        }
        return panel;
    }

    private void initialiserPlateau() {
        plateauPanel.removeAll();
        Plateau plateau = jeu.getPlateau();

        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                Case caseCourante = plateau.getCase(i, j);
                JButton bouton = new JButton();
                bouton.setPreferredSize(new Dimension(80, 80));
                bouton.setBackground((i + j) % 2 == 0 ? new Color(245, 245, 245) : new Color(0, 0, 0));

                if (!caseCourante.estVide()) {
                    ImageIcon icon = getPieceIcon(caseCourante);
                    if (icon != null) bouton.setIcon(icon);
                }

                bouton.setBorder(BorderFactory.createLineBorder(Color.DARK_GRAY));
                bouton.addMouseListener(new MouseAdapter() {
                    public void mouseClicked(MouseEvent e) {
                        gererClicCase(caseCourante);
                    }
                });

                plateauPanel.add(bouton);
            }
        }
        plateauPanel.revalidate();
        plateauPanel.repaint();
    }

    // Récupère l'icône correspondant à la pièce avec redimensionnement
    private ImageIcon getPieceIcon(Case caseCourante) {
        String cheminImage = caseCourante.getPiece() instanceof Pion ?
                (caseCourante.getPiece().estBlanc() ? "/pionBlanc.png" : "/pionNoir.png") :
                (caseCourante.getPiece() instanceof Dame ?
                        (caseCourante.getPiece().estBlanc() ? "/dameBlanche.png" : "/dameNoire.png") : null);

        if (cheminImage != null) {
            ImageIcon icon = new ImageIcon(getClass().getResource(cheminImage));
            Image img = icon.getImage().getScaledInstance(80, 80, Image.SCALE_SMOOTH);
            return new ImageIcon(img);
        }
        return null;
    }
    // Met à jour le surlignage de la case sélectionnée
    private void highlightCaseSelectionnee() {
        Component[] components = plateauPanel.getComponents();
        for (Component comp : components) {
            if (comp instanceof JButton) {
                JButton bouton = (JButton) comp;
                bouton.setBorder(null);
            }
        }
        if (caseSelectionnee != null) {
            int index = caseSelectionnee.getX() * 8 + caseSelectionnee.getY();
            if (index >= 0 && index < components.length) {
                JButton bouton = (JButton) components[index];
                bouton.setBorder(BorderFactory.createLineBorder(Color.YELLOW, 3));
            }
        }
    }


    // Gère le clic sur une case du plateau avec mise en surbrillance et gestion des captures
    private void gererClicCase(Case caseCourante) {
        if (caseSelectionnee == null) {
            if (!caseCourante.estVide() && caseCourante.getPiece().estBlanc() == jeu.getJoueurActuel().estBlanc()) {
                caseSelectionnee = caseCourante;
                highlightCaseSelectionnee();
            }
        } else {
            Plateau plateau = jeu.getPlateau();
            if (caseSelectionnee.getPiece().peutDeplacer(caseSelectionnee, caseCourante, plateau)) {
                boolean capture = Math.abs(caseSelectionnee.getX() - caseCourante.getX()) == 2;
                Case caseCapturee = null;

                if (capture) {
                    int xCapture = (caseSelectionnee.getX() + caseCourante.getX()) / 2;
                    int yCapture = (caseSelectionnee.getY() + caseCourante.getY()) / 2;
                    caseCapturee = plateau.getCase(xCapture, yCapture);
                }

                jeu.enregistrerCoup(caseSelectionnee, caseCourante, caseCapturee);
                plateau.deplacerPiece(caseSelectionnee, caseCourante);
                jeu.changerTour();
                mettreAJourMessage();
                initialiserPlateau();
                mettreAJourHistorique(caseSelectionnee, caseCourante);
                verifierVictoire();
            } else {
                JOptionPane.showMessageDialog(this, "Déplacement invalide !", "Erreur", JOptionPane.ERROR_MESSAGE);
            }
            caseSelectionnee = null;
            highlightCaseSelectionnee();
        }
    }

    // Vérifie si le joueur actuel a perdu
    private void verifierVictoire() {
        if (jeu.joueurActuelAPerdu()) {
            JOptionPane.showMessageDialog(this, "Victoire de " + jeu.getJoueurAdverse().getNom() + " !", "Partie Terminée", JOptionPane.INFORMATION_MESSAGE);
            System.exit(0);
        }
    }

    // Met à jour l'historique des coups avec mise en surbrillance des prises
    private void mettreAJourHistorique(Case depart, Case arrivee) {
        StyledDocument doc = historiqueTextPane.getStyledDocument();
        Style normalStyle = historiqueTextPane.addStyle("Normal", null);
        Style highlightStyle = historiqueTextPane.addStyle("Highlight", null);
        StyleConstants.setForeground(highlightStyle, Color.YELLOW);

        try {
            String texte = "Tour " + jeu.getHistoriqueCoups().size() + " - " + jeu.getJoueurActuel().getNom() + " : " + jeu.convertirCoordonnees(depart) + " → " + jeu.convertirCoordonnees(arrivee);
            doc.insertString(doc.getLength(), texte + "\n", normalStyle);

            // Vérifie si une pièce a été capturée et met en évidence
            if (Math.abs(depart.getX() - arrivee.getX()) == 2) {
                int xCapture = (depart.getX() + arrivee.getX()) / 2;
                int yCapture = (depart.getY() + arrivee.getY()) / 2;
                String captureTexte = "   Prise : " + jeu.convertirCoordonnees(new Case(yCapture, xCapture));
                doc.insertString(doc.getLength(), captureTexte + "\n", highlightStyle);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // Met à jour le message affichant le tour du joueur
    private void mettreAJourMessage() {
        messageLabel.setText("Tour de " + jeu.getJoueurActuel().getNom() + " (" + (jeu.getJoueurActuel().estBlanc() ? "Blanc" : "Noir") + ")");
    }

    // Affiche l'interface graphique
    public void afficher() {
        setVisible(true);
    }
}
