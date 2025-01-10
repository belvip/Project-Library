package com.library.system.controller;


import com.library.system.dao.CategoryDAO;
import com.library.system.exception.categoryException.CategoryAlreadyExistsException;
import com.library.system.exception.categoryException.CategoryNotFoundException;
import com.library.system.model.Category;
import com.library.system.service.CategoryService;
import com.library.system.service.impl.CategoryServiceImpl;
import com.library.system.util.Logger;


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
                Logger.logWarn("Ajout de la catégorie", "Le nom de la catégorie ne peut pas être vide.");
                return;
            }


            if (!categoryName.matches("^[a-zA-Z]+$")) {
                Logger.logWarn("Ajout de la catégorie", "Le nom de la catégorie ne peut contenir que des lettres.");
                return;
            }


            Category category = new Category(categoryName);


            if (categoryService.doesCategoryExist(category.getCategory_name())) {
                Logger.logWarn("Ajout de la catégorie", "La catégorie existe déjà : " + category.getCategory_name());
            } else {
                categoryService.addCategory(category);
                Logger.logSuccess("Ajout de la catégorie");
            }
        } catch (SQLException e) {
            Logger.logError("Ajout de la catégorie", e);
        } catch (CategoryAlreadyExistsException e) {
            Logger.logError("Ajout de la catégorie", e);
        }
    }


    // Modifier une catégorie
    public void modifyCategory(int categoryId, String newCategoryName) {
        Category category = new Category();
        category.setCategory_id(categoryId);
        category.setCategory_name(newCategoryName);
        try {
            categoryService.updateCategory(category);
            Logger.logSuccess("Modification de la catégorie");
        } catch (SQLException e) {
            Logger.logError("Modification de la catégorie", e);
        }
    }


    // Supprimer une catégorie
    public void deleteCategory(int categoryId) {
        try {
            categoryService.deleteCategory(categoryId);
            Logger.logSuccess("Suppression de la catégorie");
        } catch (CategoryNotFoundException e) {
            Logger.logError("Suppression de la catégorie", e);
        } catch (SQLException e) {
            Logger.logError("Suppression de la catégorie", e);
        }
    }


    // Lister toutes les catégories sous forme de tableau
    public void listCategories() {
        try {
            List<Category> categories = categoryService.getAllCategories();


            if (categories == null || categories.isEmpty()) {
                // Fusionner les messages dans une seule chaîne
                Logger.logInfo("Liste des catégories : Aucune catégorie disponible.");
                return;
            }


            // Définir les largeurs des colonnes
            int idWidth = "Catégorie ID".length();
            int nameWidth = "Nom de la catégorie".length();


            for (Category category : categories) {
                idWidth = Math.max(idWidth, String.valueOf(category.getCategory_id()).length());
                nameWidth = Math.max(nameWidth, category.getCategory_name().length());
            }


            // Couleurs ANSI pour la mise en forme
            String CYAN = "\u001B[36m";
            String RESET = "\u001B[0m";


            // Ligne de séparation
            String horizontalLine = CYAN + "+-" + "-".repeat(idWidth) + "-+-" + "-".repeat(nameWidth) + "-+" + RESET;


            // Format d'affichage
            String format = "| %-" + idWidth + "s | %-" + nameWidth + "s |\n";


            // Affichage du tableau
            Logger.logInfo("Liste des catégories : ");
            System.out.println(horizontalLine);
            System.out.printf(CYAN + format + RESET, "Catégorie ID", "Nom de la catégorie");
            System.out.println(horizontalLine);


            for (Category category : categories) {
                System.out.printf(format, category.getCategory_id(), category.getCategory_name());
            }


            System.out.println(horizontalLine);
        } catch (SQLException e) {
            Logger.logError("Récupération des catégories", e);
        }
    }


    // Méthode pour rechercher des catégories par mot-clé
    public void searchCategories(String keyword) {
        try {
            List<Category> categories = categoryService.findCategoryByKeyword(keyword);
            if (categories.isEmpty()) {
                // Fusion des messages dans un seul String
                Logger.logInfo("Recherche de catégories : Aucune catégorie trouvée avec le mot-clé : " + keyword);
            } else {
                for (Category category : categories) {
                    // Fusion des messages dans un seul String
                    Logger.logInfo("Recherche de catégories : ID: " + category.getCategory_id() + " | Nom: " + category.getCategory_name());
                }
            }
        } catch (SQLException e) {
            Logger.logError("Recherche des catégories", e);
        }
    }




}


