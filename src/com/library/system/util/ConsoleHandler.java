package com.library.system.util;


import com.library.system.exception.authorException.AuthorNotFoundException;
import com.library.system.handler.*;


import java.sql.SQLException;
import java.util.InputMismatchException;
import java.util.Scanner;


public class ConsoleHandler {
    private final Scanner scanner = new Scanner(System.in);
    private final CategoryHandler categoryHandler;
    private final AuthorHandler authorHandler;
    private final BookHandler bookHandler;
    private final MemberHandler memberHandler;
    private final LoanHandler loanHandler;
    private final PenaltiesHandler penaltiesHandler;


    // Modifie le constructeur pour accepter BookHandler en plus de CategoryHandler et AuthorHandler
    public ConsoleHandler(CategoryHandler categoryHandler, AuthorHandler authorHandler, BookHandler bookHandler, MemberHandler memberHandler, LoanHandler loanHandler, PenaltiesHandler penaltiesHandler) {
        this.categoryHandler = categoryHandler;
        this.authorHandler = authorHandler;
        this.bookHandler = bookHandler;
        this.memberHandler = memberHandler;


        this.loanHandler = loanHandler;
        this.penaltiesHandler = penaltiesHandler;
    }


    public void start() throws AuthorNotFoundException, SQLException {
        boolean running = true;
        while (running) {
            displayMenu();
            int choice = getChoiceInput();


            switch (choice) {
                case 1:
                    categoryHandler.handleCategoryOperations();
                    break;


                case 2:
                    authorHandler.handleAuthorOperations(); // Appelle les opérations sur les auteurs
                    break;


                case 3:
                    bookHandler.handleBookOperations(); // Appelle les opérations sur les livres
                    break;
                case 4:
                    memberHandler.handleMemberOperations();
                    break;
                case 5:
                    loanHandler.handleLoanOperations();
                    break;
                case 6:
                    penaltiesHandler.handlePenalitiesOperations();
                    break;
                case 7:
                    running = false;
                    System.out.println("Quitter le système...");
                    break;


                default:
                    System.out.println("\u001B[31m⛔ Choix invalide. Veuillez essayer de nouveau.\u001B[0m");

            }
        }
    }


    private int getChoiceInput() {
        try {
            return scanner.nextInt();
        } catch (InputMismatchException e) {
            System.out.println("\u001B[31m⚠️ Choix invalide. Veuillez entrer un numéro valide.\u001B[0m");

            scanner.nextLine(); // Clear buffer
            return -1;
        }
    }

    private void displayMenu() {
        System.out.println("\n\u001B[32m================ Library Management System ==================\u001B[0m");

        // Affichage du message en TEAL (Cyan)
        System.out.println("\u001B[36m⌨️ Veuillez choisir le numéro de l'opération et appuyer sur Entrer\u001B[0m");

        // Ligne supérieure du tableau
        System.out.println("+---------+------------------------------------------+");

        // En-tête avec couleurs (Cyan pour le titre et Blanc pour le texte)
        System.out.printf("| \u001B[36m%-7s\u001B[0m | \u001B[36m%-40s\u001B[0m |\n", "N°", "Opérations");

        // Ligne de séparation après l'en-tête
        System.out.println("+---------+------------------------------------------+");

        // Options du menu avec couleurs
        System.out.printf("| \u001B[34m%-7s\u001B[0m | %-40s |\n", "1", "Opérations sur les catégories");
        System.out.printf("| \u001B[35m%-7s\u001B[0m | %-40s |\n", "2", "Opérations sur les auteurs");
        System.out.printf("| \u001B[33m%-7s\u001B[0m | %-40s |\n", "3", "Opérations sur les livres");
        System.out.printf("| \u001B[33m%-7s\u001B[0m | %-40s |\n", "4", "Opérations sur les membres");
        System.out.printf("| \u001B[33m%-7s\u001B[0m | %-40s |\n", "5", "Opérations sur les emprunts");
        System.out.printf("| \u001B[33m%-7s\u001B[0m | %-40s |\n", "6", "Afficher la liste des pénalités");
        System.out.printf("| \u001B[31m%-7s\u001B[0m | \u001B[31m%-40s\u001B[0m |\n", "7", "Quitter le système");

        // Ligne inférieure du tableau
        System.out.println("+---------+------------------------------------------+");

        System.out.print("\u001B[37mEntrez votre choix: \u001B[0m");
    }



}



