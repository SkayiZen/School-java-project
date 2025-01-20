package fr.school.project.game;

import javax.swing.*;
import java.awt.*;

public class Plateau {
    public static void main(String[] args) {

        // Création de la fenêtre principale
        JFrame frame = new JFrame("Plateau");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(600, 600);

        // Création du panneau principal pour le plateau
        JPanel plateau = new JPanel();
        plateau.setLayout(new GridLayout(8, 8)); // 8x8 cases


        for (int row = 0; row < 8; row++) {
            for (int col = 0; col < 8; col++) {
                JPanel casePlateau = new JPanel();
                if ((row + col) % 2 == 0) {
                    casePlateau.setBackground(Color.WHITE); // cases blanches
                } else {
                    casePlateau.setBackground(Color.BLACK); // cases noires
                }
                plateau.add(casePlateau);
            }
        }

        // Ajout du plateau à la fenêtre
        frame.add(plateau);
        frame.setVisible(true);
    }
}