
package com.library.system.repository.impl;

import com.library.system.model.Category;
import com.library.system.repository.CategoryRepository;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class CategoryRepositoryImpl implements CategoryRepository {

    private final Connection connection;

    public CategoryRepositoryImpl(Connection connection) {
        this.connection = connection;
    }

    @Override
    public void addCategory(Category category) throws SQLException {
        String query = "INSERT INTO category (category_name) VALUES (?)";
        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setString(1, category.getCategory_name());
            statement.executeUpdate();
        }
    }

    @Override
    public void updateCategory(Category category) throws SQLException {
        String query = "UPDATE category SET category_name = ? WHERE category_id = ?";
        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setString(1, category.getCategory_name());
            statement.setInt(2, category.getCategory_id());
            statement.executeUpdate();
        }
    }

    @Override
    public List<Category> findCategoryByKeyword(String keyword) throws SQLException {
        String query = "SELECT * FROM category WHERE category_name ILIKE ?";
        List<Category> categories = new ArrayList<>();
        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setString(1, "%" + keyword + "%");
            try (ResultSet resultSet = statement.executeQuery()) {
                while (resultSet.next()) {
                    categories.add(mapResultSetToCategory(resultSet));
                }
            }
        }
        return categories;
    }

    @Override
    public List<Category> findAllCategories() throws SQLException {
        String query = "SELECT * FROM category ORDER BY category_id ASC";
        List<Category> categories = new ArrayList<>();
        try (Statement statement = connection.createStatement();
             ResultSet resultSet = statement.executeQuery(query)) {
            while (resultSet.next()) {
                categories.add(mapResultSetToCategory(resultSet));
            }
        }
        return categories;
    }

    @Override
    public void deleteCategory(int categoryId) throws SQLException {
        String query = "DELETE FROM category WHERE category_id = ?";
        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setInt(1, categoryId);
            statement.executeUpdate();
        }
    }


    @Override
    public Category findById(int categoryId) throws SQLException {
        String query = "SELECT * FROM category WHERE category_id = ?";
        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setInt(1, categoryId);
            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    return mapResultSetToCategory(resultSet);
                }
            }
        }
        return null; // Retourne null si aucun résultat trouvé
    }

    @Override
    public boolean doesCategoryExist(String categoryName) throws SQLException {
        String query = "SELECT 1 FROM category WHERE category_name = ?";
        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setString(1, categoryName);
            try (ResultSet resultSet = statement.executeQuery()) {
                return resultSet.next();
            }
        }
    }

    @Override
    public boolean doesCategoryExistById(int categoryId) throws SQLException {
        String query = "SELECT COUNT(*) FROM category WHERE category_id = ?";
        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setInt(1, categoryId);
            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    return resultSet.getInt(1) > 0;  // Si le count est supérieur à 0, la catégorie existe
                }
            }
        }
        return false;  // Si aucune catégorie n'est trouvée, retourner false
    }

    private Category mapResultSetToCategory(ResultSet resultSet) throws SQLException {
        Category category = new Category();
        category.setCategory_id(resultSet.getInt("category_id"));
        category.setCategory_name(resultSet.getString("category_name"));
        return category;
    }
}
