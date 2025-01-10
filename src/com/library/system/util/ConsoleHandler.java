package com.library.system.util;


import com.library.system.exception.authorException.AuthorNotFoundException;
import com.library.system.handler.*;


import java.sql.SQLException;
import java.util.InputMismatchException;
import java.util.Scanner;


public class
ConsoleHandler {
    private final Scanner scanner = new Scanner(System.in);
    private final CategoryHandler categoryHandler;
    private final AuthorHandler authorHandler;
    private final BookHandler bookHandler;
    private final MemberHandler memberHandler;
    private final LoanHandler loanHandler;
    private final PenaltiesHandler penaltiesHandler;


    // Modifie le constructeur pour inclure tous les gestionnaires
    public ConsoleHandler(CategoryHandler categoryHandler, AuthorHandler authorHandler, BookHandler bookHandler, MemberHandler memberHandler, LoanHandler loanHandler, PenaltiesHandler penaltiesHandler) {
        this.categoryHandler = categoryHandler;
        this.authorHandler = authorHandler;
        this.bookHandler = bookHandler;
        this.memberHandler = memberHandler;
        this.loanHandler = loanHandler;
        this.penaltiesHandler = penaltiesHandler;
    }


    public void start() {
        boolean running = true;
        while (running) {
            try {
                displayMenu();
                int choice = getChoiceInput();


                switch (choice) {
                    case 1:
                        Logger.logInfo("Début de l'opération sur les catégories.");
                        categoryHandler.handleCategoryOperations();
                        Logger.logSuccess("Opérations sur les catégories");
                        break;


                    case 2:
                        Logger.logInfo("Début de l'opération sur les auteurs.");
                        authorHandler.handleAuthorOperations();
                        Logger.logSuccess("Opérations sur les auteurs");
                        break;


                    case 3:
                        Logger.logInfo("Début de l'opération sur les livres.");
                        bookHandler.handleBookOperations();
                        Logger.logSuccess("Opérations sur les livres");
                        break;


                    case 4:
                        Logger.logInfo("Début de l'opération sur les membres.");
                        memberHandler.handleMemberOperations();
                        Logger.logSuccess("Opérations sur les membres");
                        break;


                    case 5:
                        Logger.logInfo("Début de l'opération sur les emprunts.");
                        loanHandler.handleLoanOperations();
                        Logger.logSuccess("Opérations sur les emprunts");
                        break;


                    case 6:
                        Logger.logInfo("Début de l'opération sur les pénalités.");
                        penaltiesHandler.handlePenalitiesOperations();
                        Logger.logSuccess("Opérations sur les pénalités");
                        break;


                    case 7:
                        Logger.logInfo("L'utilisateur a choisi de quitter le système.");
                        running = false;
                        System.out.println("Quitter le système...");
                        break;


                    default:
                        Logger.logWarn("Choix invalide", "L'utilisateur a saisi un numéro hors des options disponibles.");
                        System.out.println("\u001B[31m⛔ Choix invalide. Veuillez essayer de nouveau.\u001B[0m");
                }
            } catch (AuthorNotFoundException e) {
                Logger.logError("Opérations sur les auteurs", e);
                System.out.println("\u001B[31m⛔ Auteur non trouvé : " + e.getMessage() + "\u001B[0m");
            } catch (Exception e) {
                Logger.logError("Erreur inconnue", e);
                System.out.println("\u001B[31m⛔ Une erreur inattendue est survenue : " + e.getMessage() + "\u001B[0m");
            }
        }
    }


    private int getChoiceInput() {
        try {
            return scanner.nextInt();
        } catch (InputMismatchException e) {
            Logger.logWarn("Saisie invalide", "L'utilisateur a entré une valeur non numérique.");
            System.out.println("\u001B[31m⚠️ Choix invalide. Veuillez entrer un numéro valide.\u001B[0m");
            scanner.nextLine(); // Clear buffer
            return -1;
        }
    }


    private void displayMenu() {
        System.out.println("\n\u001B[32m================ Library Management System ==================\u001B[0m");
        System.out.println("\u001B[36m⌨️ Veuillez choisir le numéro de l'opération et appuyer sur Entrer\u001B[0m");


        System.out.println("+---------+------------------------------------------+");
        System.out.printf("| \u001B[36m%-7s\u001B[0m | \u001B[36m%-40s\u001B[0m |\n", "N°", "Opérations");
        System.out.println("+---------+------------------------------------------+");
        System.out.printf("| \u001B[34m%-7s\u001B[0m | %-40s |\n", "1", "Opérations sur les catégories");
        System.out.printf("| \u001B[35m%-7s\u001B[0m | %-40s |\n", "2", "Opérations sur les auteurs");
        System.out.printf("| \u001B[33m%-7s\u001B[0m | %-40s |\n", "3", "Opérations sur les livres");
        System.out.printf("| \u001B[33m%-7s\u001B[0m | %-40s |\n", "4", "Opérations sur les membres");
        System.out.printf("| \u001B[33m%-7s\u001B[0m | %-40s |\n", "5", "Opérations sur les emprunts");
        System.out.printf("| \u001B[33m%-7s\u001B[0m | %-40s |\n", "6", "Afficher la liste des pénalités");
        System.out.printf("| \u001B[31m%-7s\u001B[0m | \u001B[31m%-40s\u001B[0m |\n", "7", "Quitter le système");
        System.out.println("+---------+------------------------------------------+");


        System.out.print("\u001B[37mEntrez votre choix: \u001B[0m");
    }
}

