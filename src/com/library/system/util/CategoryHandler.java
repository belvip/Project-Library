package com.library.system.handler;

import com.library.system.controller.CategoryController;
import com.library.system.service.impl.CategoryServiceImpl;
import com.library.system.model.Category;

import java.sql.SQLException;
import java.util.InputMismatchException;
import java.util.Scanner;

public class CategoryHandler {
    private final Scanner scanner = new Scanner(System.in);
    private final CategoryServiceImpl categoryService;
    private final CategoryController categoryController;

    public CategoryHandler(CategoryServiceImpl categoryService, CategoryController categoryController) {
        this.categoryService = categoryService;
        this.categoryController = categoryController;
    }

    public void handleCategoryOperations() {
        boolean running = true;
        while (running) {
            displayCategoryMenu();
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
                    System.out.println("\u001B[31mChoix invalide. Essayez encore.\u001B[0m");
            }
        }
    }

    private void displayCategoryMenu() {
        System.out.println("\n\u001B[34m======== Opérations sur les catégories ========\u001B[0m");
        System.out.println("+--------------------------------------------+");
        System.out.printf("| %-2s | %-40s |\n", "1", "\u001B[32mAjouter une catégorie\u001B[0m");
        System.out.printf("| %-2s | %-40s |\n", "2", "\u001B[32mModifier une catégorie\u001B[0m");
        System.out.printf("| %-2s | %-40s |\n", "3", "\u001B[32mSupprimer une catégorie\u001B[0m");
        System.out.printf("| %-2s | %-40s |\n", "4", "\u001B[32mLister toutes les catégories\u001B[0m");
        System.out.printf("| %-2s | %-40s |\n", "5", "\u001B[32mRechercher des catégories par mot-clé\u001B[0m");
        System.out.printf("| %-2s | %-40s |\n", "6", "\u001B[31mRetourner au menu principal\u001B[0m");
        System.out.println("+--------------------------------------------+");
        System.out.print("\u001B[33mEntrez votre choix: \u001B[0m");
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

    private void addCategory() {
        System.out.print("Entrez le nom de la catégorie: ");
        scanner.nextLine(); // Clear buffer
        String categoryName = scanner.nextLine();
        categoryController.addCategory(categoryName);
    }

    public void modifyCategory() {
        System.out.print("Entrez l'ID de la catégorie à modifier: ");
        int categoryId = getChoiceInput();
        scanner.nextLine(); // Consommer le retour à la ligne après l'entrée de l'ID

        System.out.print("Entrez le nouveau nom de la catégorie: ");
        String newCategoryName = scanner.nextLine();

        if (newCategoryName == null || newCategoryName.trim().isEmpty()) {
            System.out.println("Le nom de la catégorie ne peut pas être vide.");
            return;
        }

        if (!newCategoryName.matches("^[a-zA-Z]+$")) {
            System.out.println("Le nom de la catégorie ne peut contenir que des lettres.");
            return;
        }

        Category category = new Category();
        category.setCategory_id(categoryId);
        category.setCategory_name(newCategoryName);

        try {
            categoryService.updateCategory(category);
            System.out.println("Catégorie mise à jour avec succès!");
        } catch (SQLException e) {
            System.out.println("Erreur lors de la modification de la catégorie: " + e.getMessage());
        }
    }

    private void deleteCategory() {
        System.out.print("Entrez l'ID de la catégorie à supprimer: ");
        int categoryId = getChoiceInput();
        categoryController.deleteCategory(categoryId);
    }

    private void listCategories() {
        categoryController.listCategories();
    }

    private void searchCategoriesByKeyword() {
        System.out.print("Entrez le mot-clé pour rechercher des catégories: ");
        scanner.nextLine(); // Clear buffer
        String keyword = scanner.nextLine();
        categoryController.searchCategories(keyword);
    }
}
