package com.library.system.dao.impl;

import com.library.system.dao.CategoryDAO;
import com.library.system.model.Category;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class CategoryDAOImpl implements CategoryDAO {

    private final Connection connection;

    public CategoryDAOImpl(Connection connection) {
        this.connection = connection;
    }

    @Override
    public void addCategory(Category category) throws SQLException {
        String query = "INSERT INTO Category (category_name) VALUES (?)";
        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setString(1, category.getCategory_name());
            stmt.executeUpdate();
        }
    }

    @Override
    public void updateCategory(Category category) throws SQLException {
        // Vérification des valeurs avant mise à jour
        if (category.getCategory_name() == null || category.getCategory_name().isEmpty()) {
            System.out.println("Le nom de la catégorie ne peut pas être vide.");
            return;
        }

        if (!doesCategoryExist(category.getCategory_id())) {
            System.out.println("La catégorie avec l'ID " + category.getCategory_id() + " n'existe pas.");
            return;
        }

        String query = "UPDATE Category SET category_name = ? WHERE category_id = ?";
        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setString(1, category.getCategory_name());
            stmt.setInt(2, category.getCategory_id());

            int rowsAffected = stmt.executeUpdate();
            if (rowsAffected > 0) {
                //System.out.println("Catégorie mise à jour avec succès!");
            } else {
                System.out.println("La catégorie n'a pas été mise à jour.");
            }
        }
    }

    @Override
    public boolean doesCategoryExist(int categoryId) throws SQLException {
        String query = "SELECT COUNT(*) FROM Category WHERE category_id = ?";
        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setInt(1, categoryId);
            ResultSet rs = stmt.executeQuery();
            return rs.next() && rs.getInt(1) > 0;
        }
    }

    @Override
    public List<Category> findCategoryByKeyword(String keyword) throws SQLException {
        // Initialisation de la liste des catégories
        List<Category> categories = new ArrayList<>();

        // Requête SQL pour rechercher des catégories dont le nom contient le mot-clé
        String query = "SELECT * FROM category WHERE category_name ILIKE ?";

        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            // Utilisation de "%" pour effectuer une recherche de type "contient"
            stmt.setString(1, "%" + keyword + "%");

            // Exécution de la requête
            ResultSet rs = stmt.executeQuery();

            // Parcours des résultats et ajout des catégories à la liste
            while (rs.next()) {
                Category category = new Category();
                category.setCategory_id(rs.getInt("category_id"));
                category.setCategory_name(rs.getString("category_name"));
                categories.add(category);
            }
        }

        return categories; // Retourner la liste des catégories trouvées
    }


    @Override
    public List<Category> findAllCategories() throws SQLException {
        String query = "SELECT * FROM category ORDER BY category_id ASC";  // Tri croissant par ID
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
        String query = "DELETE FROM Category WHERE category_id = ?";
        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setInt(1, categoryId);
            stmt.executeUpdate();
        }
    }

    @Override
    public boolean doesCategoryExist(String categoryName) throws SQLException {
        String query = "SELECT COUNT(*) FROM Category WHERE category_name = ?";
        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setString(1, categoryName);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return rs.getInt(1) > 0; // Si le nombre de résultats est supérieur à 0, la catégorie existe
            }
            return false; // Si aucune catégorie n'est trouvée, retourner false
        }
    }

    @Override
    public Category findById(int categoryId) throws SQLException {
        String query = "SELECT * FROM Category WHERE category_id = ?";
        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setInt(1, categoryId);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                // Créez un objet Category et remplissez les données de la base de données
                Category category = new Category();
                category.setCategory_id(rs.getInt("category_id"));
                category.setCategory_name(rs.getString("category_name"));
                return category;
            }
            return null; // Retourne null si aucune catégorie n'est trouvée
        }
    }



}
