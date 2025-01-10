package com.library.system;

import com.library.system.controller.*;
import com.library.system.dao.BookDAO;
import com.library.system.dao.impl.BookDAOImpl;
import com.library.system.dao.impl.MemberDAOImpl;
import com.library.system.dao.impl.PenaltiesDAOImpl;
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

import java.sql.Connection;

public class LibrarySystemApp {

    // Codes ANSI pour les couleurs
    public static final String RESET = "\u001B[0m";
    public static final String GREEN = "\u001B[32m";
    public static final String YELLOW = "\u001B[33m";

    public static void main(String[] args) {
        // Utilisation de Logger pour afficher un message de bienvenue et l'enregistrer dans le fichier de log
        Logger.logInfo("BENVENUE DANS LE SYSTEME DE GESTION DE LA BIBLIOTHEQUE !");

        initialize();
    }

    private static void initialize() {
        try (Connection connection = DatabaseConnection.getConnection()) {
            if (connection != null) {
                Logger.logSuccess("Connexion réussie à la base de données PostgreSQL !");
                DatabaseTableCreator.createTables(connection);

                // Initialisation des services et contrôleurs pour les catégories
                CategoryServiceImpl categoryService = new CategoryServiceImpl(connection);
                CategoryHandler categoryHandler = new CategoryHandler(categoryService, new CategoryController(connection));

                // Initialisation des services et contrôleurs pour les auteurs
                AuthorServiceImpl authorService = new AuthorServiceImpl(connection);
                AuthorHandler authorHandler = new AuthorHandler(authorService, new AuthorController(connection));

                // Créer une instance de BookDAO et BookRepositoryImpl
                BookDAO bookDAO = new BookDAOImpl(connection);
                BookRepository bookRepository = new BookRepositoryImpl(connection, bookDAO);

                // Initialisation des services et contrôleurs pour les livres
                BookServiceImpl bookService = new BookServiceImpl(bookRepository);
                BookHandler bookHandler = new BookHandler(bookService, new BookController(connection));

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

                Logger.logInfo("Système de gestion de bibliothèque initialisé avec succès.");

                // Lancer l'interaction avec l'utilisateur via ConsoleHandler
                consoleHandler.start();
            } else {
                Logger.logError("Connexion à la base de données", new Exception("Échec de la connexion"));
            }
        } catch (Exception e) {
            Logger.logError("Initialisation du système de gestion de bibliothèque", e);
        }
    }
}



