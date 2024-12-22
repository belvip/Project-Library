import com.library.system.dao.CategoryDAO;
import com.library.system.dao.impl.CategoryDAOImpl;
import com.library.system.service.impl.CategoryServiceImpl;
import com.library.system.util.ConsoleHandler;
import com.library.system.util.DatabaseConnection;
import com.library.system.util.DatabaseTableCreator;

import java.sql.Connection;

public class LibrarySystemApp {

    // Codes ANSI pour les couleurs
    public static final String RESET = "\u001B[0m";
    public static final String GREEN = "\u001B[32m";
    public static final String YELLOW = "\u001B[33m";
    public static final String BLUE = "\u001B[34m";

    public static void main(String[] args) {
        // Message de bienvenue en couleur
        System.out.println(GREEN + "Bienvenue dans le système de gestion de bibliothèque !" + RESET);
        initialize();
    }

    private static void initialize() {
        try (Connection connection = DatabaseConnection.getConnection()) {
            if (connection != null) {
                System.out.println("Connexion réussie à la base de données PostgreSQL !");
                DatabaseTableCreator.createTables(connection);

                // Créer une instance de CategoryDAO
                CategoryDAO categoryDAO = new CategoryDAOImpl(connection);

                CategoryServiceImpl categoryService = new CategoryServiceImpl(connection);


                // Initialisation de ConsoleHandler avec CategoryServiceImpl, Connection et CategoryDAO
                ConsoleHandler consoleHandler = new ConsoleHandler(categoryService, connection);

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
