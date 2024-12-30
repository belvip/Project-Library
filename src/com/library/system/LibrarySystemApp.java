
package com.library.system;

import com.library.system.controller.AuthorController;
import com.library.system.controller.BookController;
import com.library.system.handler.AuthorHandler;
import com.library.system.handler.BookHandler;
import com.library.system.repository.BookRepository;
import com.library.system.repository.impl.BookRepositoryImpl;
import com.library.system.service.impl.AuthorServiceImpl;
import com.library.system.service.impl.BookServiceImpl;
import com.library.system.service.impl.CategoryServiceImpl;
import com.library.system.controller.CategoryController;
import com.library.system.handler.CategoryHandler;
import com.library.system.util.ConsoleHandler;
import com.library.system.util.DatabaseConnection;
import com.library.system.util.DatabaseTableCreator;

import java.sql.Connection;

public class LibrarySystemApp {

    // Codes ANSI pour les couleurs
    public static final String RESET = "\u001B[0m";
    public static final String GREEN = "\u001B[32m";
    public static final String YELLOW = "\u001B[33m";

    public static void main(String[] args) {
        // Message de bienvenue en couleur
        System.out.println(GREEN + "Bienvenue dans le système de gestion de bibliothèque !" + RESET);
        initialize();
    }

    private static void initialize() {
        try (Connection connection = DatabaseConnection.getConnection()) {
            if (connection != null) {
                System.out.println(YELLOW + "Connexion réussie à la base de données PostgreSQL !" + RESET);
                DatabaseTableCreator.createTables(connection);

                // Initialisation des services et contrôleurs pour les catégories
                CategoryServiceImpl categoryService = new CategoryServiceImpl(connection);
                CategoryHandler categoryHandler = new CategoryHandler(categoryService, new CategoryController(connection));

                // Initialisation des services et contrôleurs pour les auteurs
                AuthorServiceImpl authorService = new AuthorServiceImpl(connection);
                AuthorHandler authorHandler = new AuthorHandler(authorService, new AuthorController(connection));

                // Créer une instance de BookRepositoryImpl avec la connexion
                BookRepository bookRepository = new BookRepositoryImpl(connection);

                // Initialisation des services et contrôleurs pour les livres
                BookServiceImpl bookService = new BookServiceImpl(bookRepository); // Remplace avec ta propre implémentation de service
                BookHandler bookHandler = new BookHandler(bookService, new BookController(connection)); // Remplace avec ta propre implémentation de contrôleur

                // Initialisation de ConsoleHandler avec CategoryHandler et AuthorHandler
                ConsoleHandler consoleHandler = new ConsoleHandler(categoryHandler, authorHandler, bookHandler);

                // Lancer l'interaction avec l'utilisateur via ConsoleHandler
                consoleHandler.start();
            } else {
                System.out.println("Connexion échouée !");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
