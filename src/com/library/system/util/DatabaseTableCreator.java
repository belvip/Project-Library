package com.library.system.util;

import java.sql.Connection;
import java.sql.Statement;
import java.sql.SQLException;

public class DatabaseTableCreator {

    public static void createTables(Connection connection) {
        try (Statement statement = connection.createStatement()) {

            // Création de la table Category
            String createCategoryTable = "CREATE TABLE IF NOT EXISTS Category (" +
                    "category_id SERIAL PRIMARY KEY, " +
                    "category_name VARCHAR(100) NOT NULL" +
                    ");";
            statement.executeUpdate(createCategoryTable);

            // Supprimer la contrainte existante (si elle existe)
            String dropCategoryConstraint = "ALTER TABLE Category DROP CONSTRAINT IF EXISTS unique_category_name;";
            statement.executeUpdate(dropCategoryConstraint);

            // Ajouter la contrainte d'unicité pour category_name
            String addCategoryUniqueConstraint = "ALTER TABLE Category " +
                    "ADD CONSTRAINT unique_category_name UNIQUE (category_name);";
            statement.executeUpdate(addCategoryUniqueConstraint);

            // Création de la table Author
            String createAuthorTable = "CREATE TABLE IF NOT EXISTS Author (" +
                    "author_id SERIAL PRIMARY KEY, " +
                    "first_name VARCHAR(50) NOT NULL, " +
                    "last_name VARCHAR(50) NOT NULL, " +
                    "author_email VARCHAR(100) NOT NULL" +
                    ");";
            statement.executeUpdate(createAuthorTable);

            // Supprimer la contrainte existante (si elle existe)
            String dropAuthorEmailConstraint = "ALTER TABLE Author DROP CONSTRAINT IF EXISTS unique_author_email;";
            statement.executeUpdate(dropAuthorEmailConstraint);

            // Ajouter la contrainte d'unicité pour author_email
            String addAuthorUniqueConstraint = "ALTER TABLE Author " +
                    "ADD CONSTRAINT unique_author_email UNIQUE (author_email);";
            statement.executeUpdate(addAuthorUniqueConstraint);

            System.out.println("Tables Category et Author créées avec succès, et contraintes appliquées.");

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
