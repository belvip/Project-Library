package com.library.system.service.impl;

import com.library.system.dao.CategoryDAO;
import com.library.system.model.Category;
import com.library.system.service.CategoryService;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class CategoryServiceImpl implements CategoryService {

    private final CategoryDAO categoryDAO;
    private final Connection connection;  // Déclaration de la connexion

    // Constructeur avec injection de la connexion
    public CategoryServiceImpl(Connection connection, CategoryDAO categoryDAO) {
        this.connection = connection;  // Initialisation de la connexion
        this.categoryDAO = categoryDAO;
    }


    @Override
    public void addCategory(Category category) throws SQLException {
        // Ajouter une vérification pour l'existence de la catégorie
        if (categoryDAO.doesCategoryExist(category.getCategory_name())) {
            System.out.println("La catégorie existe déjà : " + category.getCategory_name());
        } else {
            categoryDAO.addCategory(category);
        }
    }

    @Override
    public void updateCategory(Category category) throws SQLException {
        categoryDAO.updateCategory(category);
    }

    @Override
    public boolean doesCategoryExist(String categoryName) throws SQLException {
        // Appeler la méthode correspondante dans le DAO pour vérifier l'existence de la catégorie
        return categoryDAO.doesCategoryExist(categoryName);
    }


    @Override
    public Category getOrCreateCategory(String categoryName) throws SQLException {
        return categoryDAO.getOrCreateCategory(categoryName);
    }

    @Override
    public List<Category> findCategoryByKeyword(String keyword) throws SQLException {
        return categoryDAO.findCategoryByKeyword(keyword);
    }

    @Override
    public List<Category> getAllCategories() throws SQLException {
        // Utiliser une requête SQL avec un tri croissant par category_id
        String query = "SELECT * FROM category ORDER BY category_id ASC";  // Tri par ID croissant
        List<Category> categories = new ArrayList<>();

        try (PreparedStatement stmt = connection.prepareStatement(query);
             ResultSet rs = stmt.executeQuery()) {
            while (rs.next()) {
                Category category = new Category();
                category.setCategory_id(rs.getInt("category_id"));
                category.setCategory_name(rs.getString("category_name"));
                categories.add(category);
            }
        }

        return categories;
    }


    @Override
    public void deleteCategory(int categoryId) throws SQLException {
        categoryDAO.deleteCategory(categoryId);
    }

    @Override
    public List<Category> findAllCategories() throws SQLException {
        // Utiliser le DAO pour exécuter la requête et obtenir les catégories
        List<Category> categories = categoryDAO.findAllCategories(); // Appel à la méthode du DAO pour obtenir les catégories
        if (categories == null) {
            return new ArrayList<>();  // Si le DAO retourne null, retourner une liste vide
        }
        return categories;
    }



}
