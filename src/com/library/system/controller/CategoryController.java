package com.library.system.controller;

import com.library.system.dao.CategoryDAO;
import com.library.system.model.Category;
import com.library.system.service.CategoryService;
import com.library.system.service.impl.CategoryServiceImpl;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

public class CategoryController {

    private final CategoryService categoryService;

    // Constructeur pour injecter le service
    public CategoryController(Connection connection, CategoryDAO categoryDAO) throws SQLException {
        this.categoryService = new CategoryServiceImpl(connection, categoryDAO);
    }

    // Méthode pour ajouter une catégorie
    public void addCategory(String categoryName) {
        try {
            categoryService.addCategory(new Category(categoryName)); // Utilisation du service pour ajouter la catégorie
            System.out.println("Catégorie ajoutée avec succès !");
        } catch (SQLException e) {
            System.out.println("Erreur lors de l'ajout de la catégorie : " + e.getMessage());
        }
    }


    // Modifier une catégorie
    public void modifyCategory(int categoryId, String newCategoryName) {
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

    // Supprimer une catégorie
    public void deleteCategory(int categoryId) {
        try {
            categoryService.deleteCategory(categoryId);
            System.out.println("Catégorie supprimée avec succès!");
        } catch (SQLException e) {
            System.out.println("Erreur lors de la suppression de la catégorie: " + e.getMessage());
        }
    }

    // Lister toutes les catégories
    public void listCategories() {
        try {
            List<Category> categories = categoryService.getAllCategories();
            if (categories.isEmpty()) {
                System.out.println("Aucune catégorie disponible.");
            } else {
                System.out.println("\nListe des catégories:");
                for (Category category : categories) {
                    System.out.println("ID: " + category.getCategory_id() + " | Nom: " + category.getCategory_name());
                }
            }
        } catch (SQLException e) {
            System.out.println("Erreur lors de la récupération des catégories: " + e.getMessage());
        }
    }
}
