package com.library.system.dao.impl;

import com.library.system.dao.CategoryDAO;
import com.library.system.model.Category;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class CategoryDAOImpl implements CategoryDAO {
    private final Connection connection;

    // Constructeur pour injecter la connexion à la base de données
    public CategoryDAOImpl(Connection connection) {
        this.connection = connection;
    }


    public void addCategory(Category category) throws SQLException {
        String query = "INSERT INTO Category (category_name) VALUES (?)";
        try (PreparedStatement stmt = connection.prepareStatement(query, Statement.RETURN_GENERATED_KEYS)) {
            stmt.setString(1, category.getCategory_name());

            // Exécuter la requête d'insertion
            int affectedRows = stmt.executeUpdate();

            // Si la catégorie a été insérée avec succès, récupérer l'ID généré
            if (affectedRows > 0) {
                try (ResultSet generatedKeys = stmt.getGeneratedKeys()) {
                    if (generatedKeys.next()) {
                        // Assigner l'ID généré à l'objet Category
                        category.setCategory_id(generatedKeys.getInt(1));
                    }
                }
            } else {
                throw new SQLException("Aucune catégorie n'a été insérée.");
            }
        }
    }


    /**
     * Récupère une catégorie par son nom ou la crée si elle n'existe pas.
     *
     * @param categoryName Nom de la catégorie.
     * @return La catégorie récupérée ou créée.
     * @throws SQLException En cas de problème lors de la requête SQL.
     */
    @Override
    public Category getOrCreateCategory(String categoryName) throws SQLException {
        String selectQuery = "SELECT category_id, category_name FROM category WHERE category_name = ?";
        String insertQuery = "INSERT INTO category (category_name) VALUES (?) RETURNING category_id, category_name";

        // Rechercher si la catégorie existe déjà
        try (PreparedStatement selectStmt = connection.prepareStatement(selectQuery)) {
            selectStmt.setString(1, categoryName);
            ResultSet rs = selectStmt.executeQuery();

            if (rs.next()) { // Si la catégorie existe, la retourner
                return new Category(
                        rs.getString("category_name")
                );
            } else { // Sinon, créer la catégorie et la retourner
                try (PreparedStatement insertStmt = connection.prepareStatement(insertQuery)) {
                    insertStmt.setString(1, categoryName);
                    ResultSet insertRs = insertStmt.executeQuery();

                    if (insertRs.next()) {
                        return new Category(
                                insertRs.getString("category_name")
                        );
                    } else {
                        throw new SQLException("L'insertion de la catégorie a échoué.");
                    }
                }
            }
        }
    }

    /**
     * Trouve une catégorie par son ID.
     *
     * @param categoryId ID de la catégorie.
     * @return La catégorie si elle existe, sinon null.
     * @throws SQLException En cas de problème lors de la requête SQL.
     */
    @Override
    public Category findById(int categoryId) throws SQLException {
        String query = "SELECT category_id, category_name FROM category WHERE category_id = ?";
        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setInt(1, categoryId);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                return new Category(
                        rs.getString("category_name")
                );
            } else {
                return null;
            }
        }
    }

    /**
     * Met à jour le nom d'une catégorie existante.
     *
     * @param category Catégorie à mettre à jour.
     * @throws SQLException En cas de problème lors de la requête SQL.
     */
    @Override
    public void updateCategory(Category category) throws SQLException {
        String updateQuery = "UPDATE category SET category_name = ? WHERE category_id = ?";
        try (PreparedStatement stmt = connection.prepareStatement(updateQuery)) {
            stmt.setString(1, category.getCategory_name());
            stmt.setInt(2, category.getCategory_id());
            stmt.executeUpdate();
        }
    }

    /**
     * Supprime une catégorie par son ID.
     *
     * @param categoryId ID de la catégorie à supprimer.
     * @throws SQLException En cas de problème lors de la requête SQL.
     */
    @Override
    public void deleteCategory(int categoryId) throws SQLException {
        String deleteQuery = "DELETE FROM category WHERE category_id = ?";
        try (PreparedStatement stmt = connection.prepareStatement(deleteQuery)) {
            stmt.setInt(1, categoryId);
            stmt.executeUpdate();
        }
    }

    /**
     * Recherche des catégories dont le nom contient un mot-clé donné.
     *
     * @param keyword Mot-clé pour la recherche.
     * @return Liste des catégories correspondantes.
     * @throws SQLException En cas de problème lors de la requête SQL.
     */
    @Override
    public List<Category> findCategoryByKeyword(String keyword) throws SQLException {
        String query = "SELECT category_id, category_name FROM category WHERE category_name ILIKE ?";
        List<Category> categories = new ArrayList<>();

        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setString(1, "%" + keyword + "%");
            ResultSet rs = stmt.executeQuery();

            // Ajouter toutes les catégories correspondantes à la liste
            while (rs.next()) {
                categories.add(new Category(
                        rs.getString("category_name")
                ));
            }
        }

        return categories;
    }

    @Override
    public List<Category> findAllCategories() throws SQLException {
        // Crée une liste vide pour stocker les catégories récupérées depuis la base de données.
        List<Category> categories = new ArrayList<>();

        // La requête SQL pour sélectionner toutes les colonnes de la table "Category".
        String query = "SELECT * FROM Category";

        // Utilisation d'un bloc try-with-resources pour garantir la fermeture des ressources
        // après leur utilisation, évitant ainsi les fuites de mémoire.
        try (Statement stmt = connection.createStatement(); // Création d'un objet Statement pour exécuter la requête SQL.
             ResultSet rs = stmt.executeQuery(query)) {     // Exécution de la requête et récupération des résultats.

            // Parcourt chaque ligne du ResultSet pour récupérer les données.
            while (rs.next()) {
                // Création d'un objet Category à partir des colonnes du ResultSet.
                Category category = new Category(
                        // Récupère la valeur de la colonne "category_id" comme un entier.
                        rs.getString("category_name") // Récupère la valeur de la colonne "category_name" comme une chaîne.
                );

                // Ajoute l'objet Category à la liste des catégories.
                categories.add(category);
            }
        }
        // Retourne la liste des catégories trouvées dans la base de données.
        return categories;
    }
}
