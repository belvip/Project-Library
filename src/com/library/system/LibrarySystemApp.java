package com.library.system;


import com.library.system.controller.*;
import com.library.system.dao.BookDAO;
import com.library.system.dao.impl.BookDAOImpl;
import com.library.system.dao.impl.MemberDAOImpl;
import com.library.system.dao.impl.PenaltiesDAOImpl;
import com.library.system.exception.DatabaseNotFoundException;
import com.library.system.handler.*;
import com.library.system.repository.BookRepository;
import com.library.system.repository.impl.BookRepositoryImpl;
import com.library.system.repository.impl.LoanRepositoryImpl;
import com.library.system.repository.impl.MemberRepositoryImpl;
import com.library.system.repository.impl.PenaltiesRepositoryImpl;
import com.library.system.service.impl.*;
import com.library.system.util.ConsoleHandler;
import com.library.system.util.DatabaseConnection;
import com.library.system.util.DatabaseTableCreator;
import com.library.system.util.Logger;


import java.sql.*;


public class LibrarySystemApp {


    public static void main(String[] args) {
        // Utilisation de Logger pour afficher un message de bienvenue et l'enregistrer dans le fichier de log
        Logger.logInfo("BENVENUE DANS LE SYSTEME DE GESTION DE LA BIBLIOTHEQUE !");


        try {
            initialize();
        } catch (DatabaseNotFoundException e) {
            System.err.println("[❌] ERREUR : " + e.getMessage());
            System.exit(1); // Arrêter l'application proprement
        } catch (Exception e) {
            System.err.println("[❌] Une erreur inattendue est survenue : " + e.getMessage());
            e.printStackTrace();
            System.exit(1);
        }
    }


    private static void initialize() {
        try (Connection connection = DatabaseConnection.getConnection()) {


            if (connection != null) {
                Logger.logSuccess("Connexion réussie à la base de données PostgreSQL !");


                // Vérification de la base de données 'library_management'
                if (!isDatabaseAvailable(connection)) {
                    Logger.logError("La base de données 'library_management' n'existe pas.");
                    createDatabase(); // Créer la base si nécessaire
                    return; // Quitter après création pour éviter d'aller plus loin
                }


                // Créer les tables nécessaires si elles n'existent pas
                DatabaseTableCreator.createTables(connection);
                Logger.logInfo("Système de gestion de bibliothèque initialisé avec succès.");


                // Initialisation des services et contrôleurs pour les catégories
                CategoryServiceImpl categoryService = new CategoryServiceImpl(connection);
                CategoryHandler categoryHandler = new CategoryHandler(categoryService, new CategoryController(connection));


                // Initialisation des services et contrôleurs pour les auteurs
                AuthorServiceImpl authorService = new AuthorServiceImpl(connection);
                AuthorHandler authorHandler = new AuthorHandler(authorService, new AuthorController(connection));


                // Créer une instance de BookDAO et BookRepositoryImpl
                BookDAO bookDAO = new BookDAOImpl(connection);
                BookRepository bookRepository = new BookRepositoryImpl(connection, bookDAO);

                // Créez ou récupérez les instances nécessaires pour AuthorService et CategoryService5


                // Initialisation des services et contrôleurs pour les livres
                BookServiceImpl bookService = new BookServiceImpl(bookRepository);
                BookHandler bookHandler = new BookHandler(bookService, new BookController(connection), authorService, categoryService);
                //BookHandler bookHandler = new BookHandler(bookService, new BookController(connection));


                // Initialisation de MemberDAOImpl et MemberRepositoryImpl
                MemberDAOImpl memberDAO = new MemberDAOImpl(connection);
                MemberRepositoryImpl memberRepository = new MemberRepositoryImpl(connection, memberDAO);
                MemberServiceImpl memberService = new MemberServiceImpl(memberRepository);


                // Initialisation de MemberController avec le bon service
                MemberController memberController = new MemberController(memberService);


                // Création du MemberHandler avec le memberService et memberController
                MemberHandler memberHandler = new MemberHandler(memberService, memberController);


                // ✅ Initialisation du LoanRepositoryImpl avec uniquement la connection
                LoanRepositoryImpl loanRepository = new LoanRepositoryImpl(connection);


                // ✅ Initialisation de LoanService avec loanRepository
                LoanServiceImpl loanService = new LoanServiceImpl(loanRepository);


                // Création du LoanController avec les bons services : LoanService, BookService et MemberService
                LoanController loanController = new LoanController(loanService, bookService, memberService);
                LoanHandler loanHandler = new LoanHandler(loanService, loanController);


                // Initialisation du PenaltiesDAOImpl sans connexion (si nécessaire)
                PenaltiesDAOImpl penaltiesDAO = new PenaltiesDAOImpl();  // Aucun argument ici


                // Initialisation du PenaltiesRepository
                PenaltiesRepositoryImpl penaltiesRepository = new PenaltiesRepositoryImpl(penaltiesDAO);  // Passer PenaltiesDAO


                // Initialisation des services et contrôleurs pour les pénalités
                PenaltiesServiceImpl penaltiesService = new PenaltiesServiceImpl(penaltiesRepository);
                PenaltiesController penaltiesController = new PenaltiesController(penaltiesService);
                PenaltiesHandler penaltiesHandler = new PenaltiesHandler(penaltiesController);


                // Initialisation de ConsoleHandler avec tous les gestionnaires
                ConsoleHandler consoleHandler = new ConsoleHandler(
                        categoryHandler, authorHandler, bookHandler, memberHandler, loanHandler, penaltiesHandler
                );


                //Logger.logInfo("Système de gestion de bibliothèque initialisé avec succès.");


                // Lancer l'interaction avec l'utilisateur via ConsoleHandler
                consoleHandler.start();
            } else {
                Logger.logError("Connexion à la base de données", new Exception("Échec de la connexion"));
            }
        } catch (SQLException e) {
            Logger.logError("Erreur SQL lors de la connexion", e);
        } catch (Exception e) {
            Logger.logError("Initialisation du système de gestion de bibliothèque", e);
        }
    }


    // Méthode pour vérifier si la base de données est disponible
    private static boolean isDatabaseAvailable(Connection connection) {
        String query = "SELECT 1 FROM pg_database WHERE datname = 'library_system'";


        try (Statement stmt = connection.createStatement();
             ResultSet rs = stmt.executeQuery(query)) {
            return rs.next(); // Retourne true si la base existe
        } catch (SQLException e) {
            Logger.logError("Erreur lors de la vérification de la base de données", e);
            return false;
        }
    }


    // Méthode pour créer la base de données si elle n'existe pas
    private static void createDatabase() {
        String url = "jdbc:postgresql://localhost:5432/"; // Connexion sans base spécifique
        String createDbQuery = "CREATE DATABASE bibli";


        try (Connection connection = DriverManager.getConnection(url, "your_user", "your_password");
             Statement statement = connection.createStatement()) {
            statement.executeUpdate(createDbQuery);
            Logger.logInfo("Base de données 'bibli' créée avec succès.");
        } catch (SQLException e) {
            Logger.logError("Échec de la création de la base de données 'bibli'", e);
        }
    }
}


