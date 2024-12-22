package com.library.system.util;

import com.library.system.controller.CategoryController;
import com.library.system.dao.CategoryDAO;
import com.library.system.model.Category;
import com.library.system.service.impl.CategoryServiceImpl;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.InputMismatchException;
import java.util.Scanner;

public class ConsoleHandler {
    private final Scanner scanner = new Scanner(System.in);
    private final CategoryServiceImpl categoryService;
    private final CategoryController categoryController;

    // Le constructeur prend CategoryServiceImpl, Connection, CategoryDAO comme paramètres
    public ConsoleHandler(CategoryServiceImpl categoryService, Connection connection, CategoryDAO categoryDAO) throws SQLException {
        this.categoryService = categoryService;

        // Créer une instance de CategoryController avec les paramètres nécessaires
        this.categoryController = new CategoryController(connection, categoryDAO);
    }

    // Méthode pour démarrer le menu principal
    public void start() {
        boolean running = true;
        while (running) {
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

    // Méthode pour lire le choix de l'utilisateur
    private int getChoiceInput() {
        try {
            return scanner.nextInt();
        } catch (InputMismatchException e) {
            System.out.println("Entrée invalide. Veuillez entrer un numéro valide.");
            scanner.nextLine(); // Clear buffer
            return -1; // Retourner une valeur invalide pour forcer le menu à réafficher
        }
    }

    // Codes ANSI pour les couleurs
    public static final String RESET = "\u001B[0m";
    public static final String RED = "\u001B[31m";
    public static final String GREEN = "\u001B[32m";
    public static final String YELLOW = "\u001B[33m";
    public static final String BLUE = "\u001B[34m";
    public static final String PURPLE = "\u001B[35m";
    public static final String CYAN = "\u001B[36m";
    public static final String WHITE = "\u001B[37m";

    // Afficher le menu principal
    private void displayMenu() {
        System.out.println("\n" + GREEN + "================ Library Management System ==================" + RESET);
        System.out.println(CYAN + "Choisissez une option en entrant le nombre correspondant:" + RESET);
        System.out.println("--------------------------------------------------------------");
        System.out.printf("%-5s %-40s\n", BLUE + "1" + RESET, "Opérations sur les catégories");
        System.out.printf("%-5s %-40s\n", YELLOW + "2" + RESET, "Quitter le système");
        System.out.println("--------------------------------------------------------------");
        System.out.print(WHITE + "Entrez votre choix: " + RESET);
    }

    // Sous-menu pour les catégories
    private void categoryOperation() {
        boolean running = true;
        while (running) {
            System.out.println("\n" + "\033[1;34m" + "======== Opérations sur les catégories ========" + "\033[0m");
            System.out.println("+--------------------------------------------+");
            System.out.printf("| %-2s | %-40s |\n", "N°", "Opération");
            System.out.println("+--------------------------------------------+");
            System.out.printf("| %-2s | %-40s |\n", "1", "\033[1;32mAjouter une catégorie\033[0m");
            System.out.printf("| %-2s | %-40s |\n", "2", "\033[1;32mModifier une catégorie\033[0m");
            System.out.printf("| %-2s | %-40s |\n", "3", "\033[1;32mSupprimer une catégorie\033[0m");
            System.out.printf("| %-2s | %-40s |\n", "4", "\033[1;32mLister toutes les catégories\033[0m");
            System.out.printf("| %-2s | %-40s |\n", "5", "\033[1;32mRechercher des catégories par mot-clé\033[0m");
            System.out.printf("| %-2s | %-40s |\n", "6", "\033[1;31mRetourner au menu principal\033[0m");
            System.out.println("+--------------------------------------------+");
            System.out.print("\033[1;33m" + "Entrez votre choix: " + "\033[0m");

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
                    searchCategoriesByKeyword();
                    break;
                case 6:
                    running = false;
                    break;
                default:
                    System.out.println("\033[1;31m" + "Choix invalide. Essayez encore." + "\033[0m");
            }
        }
    }

    // Ajouter une catégorie
    private void addCategory() {
        System.out.print("Entrez le nom de la catégorie: ");
        scanner.nextLine(); // Clear buffer
        String categoryName = scanner.nextLine();
        categoryController.addCategory(categoryName);
    }

    public void modifyCategory() {
        System.out.print("Entrez l'ID de la catégorie à modifier: ");
        int categoryId = getChoiceInput(); // Lire l'ID de la catégorie
        scanner.nextLine(); // Consommer le retour à la ligne après l'entrée de l'ID

        // Demander à l'utilisateur le nouveau nom de la catégorie sur une ligne séparée
        System.out.print("Entrez le nouveau nom de la catégorie: ");
        String newCategoryName = getChoiceInputString(); // Lire le nouveau nom de la catégorie

        // Validation du nom de la catégorie
        if (newCategoryName == null || newCategoryName.trim().isEmpty()) {
            System.out.println("Le nom de la catégorie ne peut pas être vide.");
            return;
        }

        // Vérifier si le nom de la catégorie contient uniquement des lettres
        if (!newCategoryName.matches("^[a-zA-Z]+$")) {
            System.out.println("Le nom de la catégorie ne peut contenir que des lettres.");
            return;
        }

        // Créer un objet Category pour mettre à jour la catégorie
        Category category = new Category();
        category.setCategory_id(categoryId);
        category.setCategory_name(newCategoryName);

        try {
            // Utilisation de categoryService pour mettre à jour la catégorie
            categoryService.updateCategory(category);
            System.out.println("Catégorie mise à jour avec succès!");
        } catch (SQLException e) {
            System.out.println("Erreur lors de la modification de la catégorie: " + e.getMessage());
        }
    }


    // Méthode pour lire une chaîne de caractères depuis l'entrée utilisateur
    private String getChoiceInputString() {
        return scanner.nextLine(); // Lire la ligne complète comme chaîne
    }

    // Supprimer une catégorie
    private void deleteCategory() {
        System.out.print("Entrez l'ID de la catégorie à supprimer: ");
        int categoryId = getChoiceInput();
        categoryController.deleteCategory(categoryId);
    }

    // Lister toutes les catégories
    private void listCategories() {
        categoryController.listCategories();
    }

    // Rechercher des catégories par mot-clé
    private void searchCategoriesByKeyword() {
        System.out.print("Entrez le mot-clé pour rechercher des catégories: ");
        scanner.nextLine(); // Clear buffer
        String keyword = scanner.nextLine();
        categoryController.searchCategories(keyword);
    }
}
