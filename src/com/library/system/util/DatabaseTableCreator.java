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
            String dropConstraint = "ALTER TABLE Category DROP CONSTRAINT IF EXISTS unique_category_name;";
            statement.executeUpdate(dropConstraint);

            // Ajouter la contrainte d'unicité pour category_name
            String addUniqueConstraint = "ALTER TABLE Category " +
                    "ADD CONSTRAINT unique_category_name UNIQUE (category_name);";
            statement.executeUpdate(addUniqueConstraint);

            System.out.println("Tables créées et contraintes appliquées avec succès.");

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
