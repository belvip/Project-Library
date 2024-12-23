
package com.library.system.util;

import com.library.system.handler.AuthorHandler;
import com.library.system.handler.CategoryHandler;

import java.util.InputMismatchException;
import java.util.Scanner;

public class ConsoleHandler {
    private final Scanner scanner = new Scanner(System.in);
    private final CategoryHandler categoryHandler;

    public ConsoleHandler(CategoryHandler categoryHandler) {
        this.categoryHandler = categoryHandler;

    }

    public void start() {
        boolean running = true;
        while (running) {
            displayMenu();
            int choice = getChoiceInput();

            switch (choice) {
                case 1:
                    categoryHandler.handleCategoryOperations();
                    break;

                case 2:
                    running = false;
                    System.out.println("Quitter le système...");
                    break;
                default:
                    System.out.println("Choix invalide. Veuillez essayer de nouveau.");
            }
        }
    }

    private int getChoiceInput() {
        try {
            return scanner.nextInt();
        } catch (InputMismatchException e) {
            System.out.println("Entrée invalide. Veuillez entrer un numéro valide.");
            scanner.nextLine(); // Clear buffer
            return -1;
        }
    }

    private void displayMenu() {
        System.out.println("\n\u001B[32m================ Library Management System ==================\u001B[0m");
        System.out.println("\u001B[36mChoisissez une option en entrant le nombre correspondant:\u001B[0m");
        System.out.println("--------------------------------------------------------------");
        System.out.printf("%-5s %-40s\n", "\u001B[34m1\u001B[0m", "Opérations sur les catégories");
        System.out.printf("%-5s %-40s\n", "\u001B[33m2\u001B[0m", "Quitter le système");
        System.out.println("--------------------------------------------------------------");
        System.out.print("\u001B[37mEntrez votre choix: \u001B[0m");
    }
}
