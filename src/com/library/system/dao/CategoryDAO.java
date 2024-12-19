package com.library.system.dao;

import com.library.system.model.Category;
import java.sql.SQLException;
import java.util.List;

public interface CategoryDAO {
    // Créer ou récupérer une catégorie
    Category getOrCreateCategory(String categoryName) throws SQLException;

    // Trouver une catégorie par son ID
    Category findById(int categoryId) throws SQLException;

    // Mettre à jour une catégorie
    void updateCategory(Category category) throws SQLException;

    // Supprimer une catégorie
    void deleteCategory(int categoryId) throws SQLException;

    // Méthode pour rechercher des catégories par mot-clé
    List<Category> findCategoryByKeyword(String keyword) throws SQLException;
}
