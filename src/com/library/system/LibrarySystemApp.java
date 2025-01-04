package com.library.system;

import com.library.system.controller.*;
import com.library.system.dao.BookDAO;
import com.library.system.dao.impl.BookDAOImpl;
import com.library.system.dao.impl.MemberDAOImpl;
import com.library.system.handler.*;
import com.library.system.repository.BookRepository;
import com.library.system.repository.impl.BookRepositoryImpl;
import com.library.system.repository.impl.LoanRepositoryImpl;
import com.library.system.repository.impl.MemberRepositoryImpl;
import com.library.system.service.impl.*;
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

                BookDAO bookDAO = new BookDAOImpl(connection);

                // Créer une instance de BookRepositoryImpl avec la connexion
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

                // ✅ Initialisation du LoanRepository, LoanService et LoanController
                LoanRepositoryImpl loanRepository = new LoanRepositoryImpl(connection);
                LoanServiceImpl loanService = new LoanServiceImpl(loanRepository);
                LoanController loanController = new LoanController(loanService, memberService, bookService);  // ✅ Passer les 3 arguments
                LoanHandler loanHandler = new LoanHandler(loanService, loanController);


                // Initialisation de ConsoleHandler avec CategoryHandler, AuthorHandler, BookHandler, et MemberHandler
                ConsoleHandler consoleHandler = new ConsoleHandler(categoryHandler, authorHandler, bookHandler, memberHandler, loanHandler);

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
