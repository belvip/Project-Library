

import com.library.system.dao.CategoryDAO;
import com.library.system.dao.impl.CategoryDAOImpl;
import com.library.system.util.ConsoleHandler;
import com.library.system.util.DatabaseConnection;
import com.library.system.util.DatabaseTableCreator;

import java.sql.Connection;

public class LibrarySystemApp {
    public static void main(String[] args) {
        System.out.println("Bienvenue dans le système de gestion de bibliothèque !");
        initialize();

    }

    private static void initialize() {
        try (Connection connection = DatabaseConnection.getConnection()) {
            if (connection != null) {
                System.out.println("Connexion réussie à la base de données PostgreSQL !");
                DatabaseTableCreator.createTables(connection);

                // Créer une instance de CategoryDAO
                CategoryDAO categoryDAO = new CategoryDAOImpl(connection);

                // Initialisation de ConsoleHandler avec la connexion et CategoryDAO
                ConsoleHandler consoleHandler = new ConsoleHandler(connection, categoryDAO);

                // Démarrer l'interaction avec l'utilisateur via ConsoleHandler
                consoleHandler.start();
            } else {
                System.out.println("Connexion échouée !");
                return; // Arrêter le programme si la connexion échoue
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
