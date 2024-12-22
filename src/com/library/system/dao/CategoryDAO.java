package com.library.system.dao;

import com.library.system.model.Category;

import java.sql.SQLException;
import java.util.List;

public interface CategoryDAO {

    void addCategory(Category category) throws SQLException;

    void updateCategory(Category category) throws SQLException;

    Category getOrCreateCategory(String categoryName) throws SQLException;

    List<Category> findCategoryByKeyword(String keyword) throws SQLException;

    List<Category> findAllCategories() throws SQLException;

    void deleteCategory(int categoryId) throws SQLException;

    boolean doesCategoryExist(int categoryId) throws SQLException;
    // Ajout de la méthode findById
    Category findById(int categoryId) throws SQLException; // Méthode pour rechercher une catégorie par ID
    boolean doesCategoryExist(String categoryName) throws SQLException; // Méthode pour vérifier si la catégorie existe
}
