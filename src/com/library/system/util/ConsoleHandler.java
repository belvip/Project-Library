package com.library.system.util;

import com.library.system.controller.CategoryController;
import com.library.system.dao.CategoryDAO;
import com.library.system.dao.impl.CategoryDAOImpl;
import com.library.system.service.impl.CategoryServiceImpl;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.InputMismatchException;
import java.util.Scanner;

public class ConsoleHandler {
    private final Scanner scanner = new Scanner(System.in);
    //private final CategoryServiceImpl categoryService;
    private final CategoryController categoryController;

    public ConsoleHandler(Connection connection, CategoryDAO categoryDAO) throws SQLException {

        // Créer une instance de CategoryServiceImpl avec la connexion et le DAO
        //this.categoryService = new CategoryServiceImpl(connection, categoryDAO);
        this.categoryController = new CategoryController(connection, categoryDAO);

    }


    public void start() {
        // Boucle pour afficher le menu principal et traiter les choix
        boolean running = true;
        while (running) {
            // Affichez le menu principal
            displayMenu();
            int choice = getChoiceInput();

            switch (choice) {
                case 1:
                    categoryOperation();
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
            return -1; // Retourner une valeur invalide pour forcer le menu à réafficher
        }
    }

    /* ^^^^^^^^^^^^^^^^^^^^^^^ Menu d'affichage ^^^^^^^^^^^^^^^^^^^^^^^ */
    private void displayMenu() {
        System.out.println("\n================ Library Management System ==================");
        System.out.println("Choisissez une option en entrant le nombre correspondant:");
        System.out.println("--------------------------------------------------------------");
        System.out.printf("%-5s %-40s\n", "1", "Opérations sur les catégories");
        System.out.printf("%-5s %-40s\n", "2", "Quitter le système");
        System.out.println("--------------------------------------------------------------");
        System.out.print("Entrez votre choix: ");
    }

    /* ^^^^^^^^^^^^^^^^^^^^^^^ Sous-menu pour les catégories ^^^^^^^^^^^^^^^^^^^^^^^ */
    private void categoryOperation() {
        boolean running = true;
        while (running) {
            System.out.println("\n======== Opérations sur les catégories ========");
            System.out.println("1. Ajouter une catégorie");
            System.out.println("2. Modifier une catégorie");
            System.out.println("3. Supprimer une catégorie");
            System.out.println("4. Lister toutes les catégories");
            System.out.println("5. Retourner au menu principal");
            System.out.print("Entrez votre choix: ");

            int choice = getChoiceInput();
            switch (choice) {
                case 1:
                    addCategory();
                    break;
                case 2:
                    modifyCategory();
                    break;
                case 3:
                    deleteCategory();
                    break;
                case 4:
                    listCategories();
                    break;
                case 5:
                    running = false;
                    break;
                default:
                    System.out.println("Choix invalide. Essayez encore.");
            }
        }
    }

    private void addCategory() {
        System.out.print("Entrez le nom de la catégorie: ");
        scanner.nextLine(); // Clear buffer
        String categoryName = scanner.nextLine();
        categoryController.addCategory(categoryName);
    }

    private void modifyCategory() {
        System.out.print("Entrez l'ID de la catégorie à modifier: ");
        int categoryId = getChoiceInput();
        System.out.print("Entrez le nouveau nom de la catégorie: ");
        scanner.nextLine(); // Clear buffer
        String categoryName = scanner.nextLine();
        categoryController.modifyCategory(categoryId, categoryName);
    }

    private void deleteCategory() {
        System.out.print("Entrez l'ID de la catégorie à supprimer: ");
        int categoryId = getChoiceInput();
        categoryController.deleteCategory(categoryId);
    }

    private void listCategories() {
        categoryController.listCategories();
    }
}
