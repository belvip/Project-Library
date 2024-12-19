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

            System.out.println("Tables created successfully.");

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}