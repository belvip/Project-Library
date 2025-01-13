package com.library.system.util;

import java.sql.*;

public class DatabaseConnection {
    public static Connection getConnection() throws SQLException {
        String url = "jdbc:postgresql://localhost:5432/postgres"; // Connexion à la base par défaut
        String user = "postgres";
        String password = "belvi";

        // Connexion à la base 'postgres'
        Connection connection = DriverManager.getConnection(url, user, password);

        // Vérifier si la base 'bibli' existe
        if (!isDatabaseAvailable(connection)) {
            // Si la base n'existe pas, la créer
            createDatabase();  // ✅ Correction : appel sans paramètre
        }

        // ✅ Se reconnecter à la base 'bibli' après sa création
        return DriverManager.getConnection("jdbc:postgresql://localhost:5432/library_system", user, password);
    }

    private static boolean isDatabaseAvailable(Connection connection) {
        String query = "SELECT 1 FROM pg_database WHERE datname = 'library_system'";
        try (Statement stmt = connection.createStatement();
             ResultSet rs = stmt.executeQuery(query)) {
            return rs.next(); // Retourne true si la base existe
        } catch (SQLException e) {
            return false;
        }
    }

    private static void createDatabase() {  // ✅ Suppression du paramètre Connection
        String dbName = "library_system";
        String url = "jdbc:postgresql://localhost:5432/postgres"; // Connexion à la base par défaut
        String user = "postgres"; // ✅ Remplacez par votre nom d'utilisateur PostgreSQL
        String password = "belvi"; // ✅ Remplacez par votre mot de passe

        String checkDbQuery = "SELECT 1 FROM pg_database WHERE datname = ?";
        String createDbQuery = "CREATE DATABASE " + dbName;

        try (Connection connection = DriverManager.getConnection(url, user, password);
             PreparedStatement checkStmt = connection.prepareStatement(checkDbQuery)) {

            checkStmt.setString(1, dbName);
            ResultSet rs = checkStmt.executeQuery();

            if (rs.next()) {
                Logger.logSuccess("La base de données '" + dbName + "' existe déjà.");
            } else {
                try (Statement createStmt = connection.createStatement()) {
                    createStmt.executeUpdate(createDbQuery);
                    Logger.logSuccess("Base de données '" + dbName + "' créée avec succès.");
                }
            }

        } catch (SQLException e) {
            Logger.logError("Erreur lors de la création de la base de données '" + dbName + "'", e);
        }
    }
}
