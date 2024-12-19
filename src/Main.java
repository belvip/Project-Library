

import com.library.system.util.DatabaseConnection;

import java.sql.Connection;

public class Main {
    public static void main(String[] args) {
        System.out.println("Bienvenue dans le système de gestion de bibliothèque !");

        try (Connection connection = DatabaseConnection.getConnection()) {
            if (connection != null) {
                System.out.println("Connexion réussie à la base de données PostgreSQL !");
            } else {
                System.out.println("Connexion échouée !");
                return; // Arrêter le programme si la connexion échoue
            }

            //DatabaseTableCreator.createTables(connection);



        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
