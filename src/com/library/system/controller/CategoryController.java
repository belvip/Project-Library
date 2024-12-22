package com.library.system.controller;

import com.library.system.dao.CategoryDAO;
import com.library.system.exception.categoryException.CategoryAlreadyExistsException;
import com.library.system.exception.categoryException.CategoryNotFoundException;
import com.library.system.model.Category;
import com.library.system.service.CategoryService;
import com.library.system.service.impl.CategoryServiceImpl;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

public class CategoryController {

    private final CategoryService categoryService;

    // Constructeur pour injecter le service
    public CategoryController(Connection connection) throws SQLException {
        this.categoryService = new CategoryServiceImpl(connection); // Appel au nouveau constructeur
    }

    // Méthode pour ajouter une catégorie avec validation
    public void addCategory(String categoryName) {
        try {
            if (categoryName == null || categoryName.trim().isEmpty()) {
                System.out.println("Le nom de la catégorie ne peut pas être vide.");
                return;
            }

            if (!categoryName.matches("^[a-zA-Z]+$")) {
                System.out.println("Le nom de la catégorie ne peut contenir que des lettres.");
                return;
            }

            Category category = new Category(categoryName);

            if (categoryService.doesCategoryExist(category.getCategory_name())) {
                System.out.println("La catégorie existe déjà : " + category.getCategory_name());
            } else {
                categoryService.addCategory(category);
                System.out.println("Catégorie ajoutée avec succès !");
            }
        } catch (SQLException e) {
            System.out.println("Erreur lors de l'ajout de la catégorie : " + e.getMessage());
        } catch (CategoryAlreadyExistsException e) {
            System.out.println("Erreur : " + e.getMessage()); // Gestion de l'exception CategoryAlreadyExistsException
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

    public void deleteCategory(int categoryId) {
        try {
            categoryService.deleteCategory(categoryId);
            System.out.println("Catégorie supprimée avec succès!");
        } catch (CategoryNotFoundException e) {
            System.out.println("Erreur: " + e.getMessage());
        } catch (SQLException e) {
            System.out.println("Erreur lors de la suppression de la catégorie: " + e.getMessage());
        }
    }


    // Lister toutes catégories
    public void listCategories() {
        try {
            List<Category> categories = categoryService.getAllCategories();

            if (categories == null || categories.isEmpty()) {
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

    // Méthode pour rechercher des catégories par mot-clé
    public void searchCategories(String keyword) {
        try {
            List<Category> categories = categoryService.findCategoryByKeyword(keyword);
            if (categories.isEmpty()) {
                System.out.println("Aucune catégorie trouvée avec le mot-clé : " + keyword);
            } else {
                for (Category category : categories) {
                    System.out.println("ID: " + category.getCategory_id() + " | Nom: " + category.getCategory_name());
                }
            }
        } catch (SQLException e) {
            System.out.println("Erreur lors de la recherche des catégories : " + e.getMessage());
        }
    }



}
