package com.library.system.service;

import com.library.system.model.Category;

import java.sql.SQLException;
import java.util.List;

public interface CategoryService {

    // Méthode pour ajouter ou récupérer une catégorie
    Category getOrCreateCategory(String categoryName) throws SQLException;

    // Méthode pour trouver une catégorie par mot-clé
    List<Category> findCategoryByKeyword(String keyword) throws SQLException;

    // Méthode pour récupérer toutes les catégories
    List<Category> getAllCategories() throws SQLException;

    // Méthode pour supprimer une catégorie par ID
    void deleteCategory(int categoryId) throws SQLException;

    void addCategory(Category category) throws SQLException;

    void updateCategory(Category category) throws SQLException;
    boolean doesCategoryExist(String categoryName) throws SQLException;

    List<Category> findAllCategories() throws SQLException;


}
