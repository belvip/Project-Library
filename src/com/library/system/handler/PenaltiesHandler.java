package com.library.system.handler;


import com.library.system.controller.PenaltiesController;
import com.library.system.exception.memberException.InvalidMemberEmailException;


import java.util.Scanner;


public class PenaltiesHandler {


    // Codes ANSI pour les couleurs
    public static final String RESET = "\u001B[0m";   // Réinitialiser la couleur
    public static final String GREEN = "\u001B[32m";  // Vert pour le succès
    public static final String RED = "\u001B[31m";    // Rouge pour l'erreur




    private final Scanner scanner = new Scanner(System.in);
    private final PenaltiesController penaltiesController;


    public PenaltiesHandler(PenaltiesController penaltiesController) {
        this.penaltiesController = penaltiesController;
    }


    // Méthode pour traiter les opérations sur les livres
    public void handlePenalitiesOperations() {
        boolean running = true;
        while (running) {
            displayBookMenu();
            int choice = getChoiceInput();
            switch (choice) {
                case 1:
                    displayDelayedLoans();
                    break;
                case 2:
                    running = false;
                    break;
                default:
                    System.out.println("Choix invalide. Essayez encore.");
            }
        }
    }


    // Appel de la méthode de PenaltiesController pour appliquer les pénalités
    private void applyPenaltiesToDelayedLoans() {
        try {
            penaltiesController.applyPenaltiesToDelayedLoans(); // Appel de la méthode dans le controller
        } catch (Exception e) {
            System.err.println("Erreur lors de l'application des pénalités : " + e.getMessage());
        }
    }


    // Méthode pour afficher le menu des opérations
    private void displayBookMenu() {
        System.out.println("\n\u001B[34m======== Opérations sur les livres ========\u001B[0m");
        System.out.println("+--------------------------------------------+");
        System.out.printf("| %-2s | %-40s |\n", "1", "\u001B[32mAfficher les pénalités\u001B[0m");
        System.out.printf("| %-2s | %-40s |\n", "2", "\u001B[31mRetourner au menu principal\u001B[0m");
        System.out.println("+--------------------------------------------+");
        System.out.print("\u001B[33mEntrez votre choix: \u001B[0m");
    }


    // Méthode pour obtenir l'entrée de l'utilisateur
    private int getChoiceInput() {
        try {
            return scanner.nextInt();
        } catch (Exception e) {
            scanner.nextLine(); // Nettoie le buffer
            return -1;
        }
    }


    public void displayDelayedLoans() {
        try {
            // Appel de la méthode dans le contrôleur pour afficher les prêts en retard
            penaltiesController.displayDelayedLoans();
        } catch (InvalidMemberEmailException e) {
            // Si l'email du membre est invalide, afficher un message d'erreur spécifique
            System.err.println("Erreur : L'email du membre est invalide. Veuillez vérifier l'adresse email.");
            e.printStackTrace();  // Afficher la trace de l'exception pour le débogage, si nécessaire
        } catch (Exception e) {
            // Capturer toute autre exception non anticipée
            System.err.println("Erreur inattendue lors de l'affichage des prêts en retard : " + e.getMessage());
            e.printStackTrace();  // Afficher la trace de l'exception pour le débogage, si nécessaire
        }
    }
}



