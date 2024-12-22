package com.library.system.service;

import com.library.system.exception.categoryException.CategoryAlreadyExistsException;
import com.library.system.exception.categoryException.CategoryNotFoundException;
import com.library.system.model.Category;

import java.sql.SQLException;
import java.util.List;

public interface CategoryService {

    // Méthode pour trouver une catégorie par mot-clé
    List<Category> findCategoryByKeyword(String keyword) throws SQLException;

    // Méthode pour récupérer toutes les catégories
    List<Category> getAllCategories() throws SQLException;

    // Méthode pour supprimer une catégorie par ID
    void deleteCategory(int categoryId) throws SQLException , CategoryNotFoundException;

    //void addCategory(Category category) throws SQLException;
    void addCategory(Category category) throws SQLException, CategoryAlreadyExistsException;

    void updateCategory(Category category) throws SQLException;
    boolean doesCategoryExist(String categoryName) throws SQLException;

    List<Category> findAllCategories() throws SQLException;


}
