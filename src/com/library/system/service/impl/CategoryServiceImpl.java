package com.library.system.service.impl;

import com.library.system.model.Category;
import com.library.system.repository.CategoryRepository;
import com.library.system.repository.impl.CategoryRepositoryImpl;
import com.library.system.service.CategoryService;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class CategoryServiceImpl implements CategoryService {

    private final CategoryRepository categoryRepository;

    // Constructeur avec injection de la connexion
    public CategoryServiceImpl(Connection connection) {
        this.categoryRepository = new CategoryRepositoryImpl(connection);
    }

    @Override
    public void addCategory(Category category) throws SQLException {
        // Ajouter une vérification pour l'existence de la catégorie
        if (categoryRepository.doesCategoryExist(category.getCategory_name())) {
            System.out.println("La catégorie existe déjà : " + category.getCategory_name());
        } else {
            categoryRepository.addCategory(category);
        }
    }

    @Override
    public void updateCategory(Category category) throws SQLException {
        categoryRepository.updateCategory(category);
    }

    @Override
    public boolean doesCategoryExist(String categoryName) throws SQLException {
        // Appeler la méthode correspondante dans le Repository pour vérifier l'existence de la catégorie
        return categoryRepository.doesCategoryExist(categoryName);
    }

    @Override
    public List<Category> findCategoryByKeyword(String keyword) throws SQLException {
        return categoryRepository.findCategoryByKeyword(keyword);
    }

    @Override
    public List<Category> getAllCategories() throws SQLException {
        return categoryRepository.findAllCategories();
    }

    @Override
    public void deleteCategory(int categoryId) throws SQLException {
        categoryRepository.deleteCategory(categoryId);
    }

    @Override
    public List<Category> findAllCategories() throws SQLException {
        // Utiliser le Repository pour exécuter la requête et obtenir les catégories
        List<Category> categories = categoryRepository.findAllCategories();
        if (categories == null) {
            return new ArrayList<>(); // Si le Repository retourne null, retourner une liste vide
        }
        return categories;
    }
}
