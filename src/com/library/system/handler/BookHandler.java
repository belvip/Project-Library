package com.library.system.handler;

import com.library.system.controller.BookController;
import com.library.system.exception.bookDaoException.BookDisplayException;
import com.library.system.exception.bookDaoException.BookRemoveException;
import com.library.system.exception.bookDaoException.BookUpdateException;
import com.library.system.model.Author;
import com.library.system.model.Book;
import com.library.system.model.Category;
import com.library.system.service.AuthorService;
import com.library.system.service.CategoryService;
import com.library.system.service.impl.BookServiceImpl;
import com.library.system.util.Logger;

import java.util.*;

public class BookHandler {
    // Codes ANSI pour les couleurs
    public static final String RESET = "\u001B[0m";   // Réinitialiser la couleur
    public static final String GREEN = "\u001B[32m";  // Vert pour le succès
    public static final String RED = "\u001B[31m";    // Rouge pour l'erreur


    private final Scanner scanner = new Scanner(System.in);
    private final BookController bookController; // Contrôleur pour gérer les requêtes des livres
    private final AuthorService authorService;
    private final  CategoryService categoryService;

    // Constructeur qui accepte BookServiceImpl et BookController
    public BookHandler(BookServiceImpl bookService, BookController bookController, AuthorService authorService, CategoryService categoryService) {
        // Service pour gérer les livres
        this.bookController = bookController;
        this.authorService = authorService;
        this.categoryService = categoryService;
    }

    // Méthode pour traiter les opérations sur les livres
    public void handleBookOperations() {
        boolean running = true;
        while (running) {
            displayBookMenu();
            int choice = getChoiceInput();
            switch (choice) {
                case 1:
                    addBook();
                    break;
                case 2:
                    displayBookById();  // Appeler la méthode pour afficher un livre
                    break;
                case 3: // Afficher tous les livres
                    displayAllAvailableBooks();  // Appeler la méthode pour afficher tous les livres
                    break;
                case 4:
                    updateBook();  // Méthode pour mettre à jour un livre
                    break;
                case 5:
                    searchBookByCategory();
                    break;
                case 6:
                    removeBook();
                    break;
                case 7:
                    running = false;
                    break;
                default:
                    System.out.println("Choix invalide. Essayez encore.");
            }
        }
    }

    // Méthode pour afficher le menu des opérations
    private void displayBookMenu() {
        //System.out.println("\n\u001B[34m======== Opérations sur les livres ========\u001B[0m");
        System.out.println("+--------------------------------------------+");
        System.out.printf("| %-2s | %-40s |\n", "1", "\u001B[32mAjouter un livre\u001B[0m");
        System.out.printf("| %-2s | %-40s |\n", "2", "\u001B[33mAfficher un livre\u001B[0m");
        System.out.printf("| %-2s | %-40s |\n", "3", "\u001B[36mAfficher tous les livres\u001B[0m");
        System.out.printf("| %-2s | %-40s |\n", "4", "\u001B[36mMettre a jour un livre\u001B[0m");
        System.out.printf("| %-2s | %-40s |\n", "5", "\u001B[36mRechercher un livre par categorie\u001B[0m");
        System.out.printf("| %-2s | %-40s |\n", "6", "\u001B[36mSupprimer un livre\u001B[0m");
        System.out.printf("| %-2s | %-40s |\n", "7", "\u001B[31mRetourner au menu principal\u001B[0m");
        System.out.println("+--------------------------------------------+");
        System.out.print("\u001B[33mEntrez votre choix: \u001B[0m");
    }

    // Méthode pour obtenir l'entrée de l'utilisateur
    private int getChoiceInput() {
        try {
            return scanner.nextInt();
        } catch (Exception e) {
            scanner.nextLine(); // Nettoie le buffer
            return -1;
        }
    }

    private void addBook() {
        Logger.logInfo("Ajouter un livre : ");
        scanner.nextLine(); // Nettoyer le buffer du scanner

        // Demander les informations du livre
        System.out.print("Entrez le titre du livre : ");
        String title = scanner.nextLine();

        int numberOfCopies = -1;  // Initialiser à une valeur invalide

        // Demander le nombre d'exemplaires jusqu'à ce qu'une valeur valide soit entrée
        while (numberOfCopies < 0) {
            System.out.print("Entrez le nombre d'exemplaires : ");
            try {
                numberOfCopies = scanner.nextInt();  // Essayer de lire un int
                if (numberOfCopies < 0) {
                    Logger.logWarn("Erreur.", " Le nombre d'exemplaires ne peut pas être négatif. Veuillez réessayer.");
                }
            } catch (InputMismatchException e) {
                Logger.logWarn("Entrée invalide.", " Veuillez entrer un nombre entier.");
                scanner.nextLine(); // Nettoyer le buffer du scanner pour éviter une boucle infinie
            }
        }

        // Créer le set des auteurs
        Set<Author> authors = new HashSet<>();
        int authorId = -1;

        // Demander l'ID de l'auteur et gérer les erreurs de saisie
        while (authorId < 0) {
            System.out.print("Entrez l'ID de l'auteur : ");
            try {
                authorId = scanner.nextInt();
                if (authorId < 0) {
                    Logger.logWarn("Entrée invalide.", " L'ID de l'auteur ne peut pas être négatif.");
                }
            } catch (InputMismatchException e) {
                Logger.logWarn("Entrée invalide.", " Veuillez entrer un nombre entier pour l'ID de l'auteur.");
                scanner.nextLine(); // Nettoyer le buffer
            }
        }

        // Vérifier si l'auteur existe dans la base de données
        Author author = authorService.getAuthorById(authorId);
        if (author == null) {
            Logger.logError("L'auteur avec l'ID " + authorId + " n'existe pas.");
            return; // Sortir de la méthode si l'auteur n'existe pas
        }
        authors.add(author);

        // Créer le set des catégories
        Set<Category> categories = new HashSet<>();
        int categoryId = -1;

        // Demander l'ID de la catégorie et gérer les erreurs de saisie
        while (categoryId < 0) {
            System.out.print("Entrez l'ID de la catégorie : ");
            try {
                categoryId = scanner.nextInt();
                if (categoryId < 0) {
                    Logger.logWarn("Entrée invalide.", " L'ID de la catégorie ne peut pas être négatif.");
                }
            } catch (InputMismatchException e) {
                Logger.logWarn("Entrée invalide.", " Veuillez entrer un nombre entier pour l'ID de la catégorie.");
                scanner.nextLine(); // Nettoyer le buffer
            }
        }

        // Vérifier si la catégorie existe dans la base de données
        Category category = categoryService.getCategoryById(categoryId);
        if (category == null) {
            Logger.logError("La catégorie avec l'ID " + categoryId + " n'existe pas.");
            return; // Sortir de la méthode si la catégorie n'existe pas
        }
        categories.add(category);

        // Créer l'objet Book sans ID
        Book book = new Book();
        book.setTitle(title);
        book.setAuthors(authors);
        book.setCategories(categories);
        book.setNumber_Of_Copies(numberOfCopies);

        // Ajouter le livre via le contrôleur
        try {
            bookController.addBook(book);
            Logger.logSuccess("✅ Livre ajouté avec succès. L'ID du livre est : " + book.getBook_id());
        } catch (Exception e) {
            Logger.logError("Erreur lors de l'ajout du livre : " + e.getMessage());
        }
    }



    // Méthode pour afficher un livre par ID
    private void displayBookById() {
        Logger.logInfo("Afficher un livre par ID");
        System.out.print("Entrez l'ID du livre à afficher: ");
        int bookId = scanner.nextInt(); // Demander l'ID du livre à afficher

        try {
            // Appel du contrôleur pour obtenir le livre
            Book book = bookController.displayBookById(bookId);

            if (book != null) {
                Logger.logInfo("--------------- Détails du Livre --------------- ");
                System.out.println("+------------+-----------------------------------+---------------------+---------------------------+-----------------------------+-----------------------------------------+");

                // Affichage des titres de colonnes
                System.out.printf("| %-10s | %-35s | %-15s | %-25s | %-25s | %-35s |\n",
                        "ID", "Titre", "Nb Copies", "Catégorie", "Email Auteur", "Nom de l'Auteur");
                System.out.println("+------------+-----------------------------------+---------------------+---------------------------+-----------------------------+-----------------------------------------+");

                // Récupérer les catégories du livre
                String categories = book.getCategories().stream()
                        .map(Category::getCategory_name)
                        .reduce((cat1, cat2) -> cat1 + ", " + cat2)
                        .orElse("Aucune catégorie");

                // Tronquer les catégories si elles sont trop longues
                if (categories.length() > 25) {
                    categories = categories.substring(0, 22) + "...";
                }

                // Récupérer l'email et le nom de l'auteur
                String authorEmail = book.getAuthors().stream()
                        .findFirst() // On prend seulement le premier auteur
                        .map(Author::getAuthor_email)
                        .orElse("Email non disponible");

                if (authorEmail.length() > 25) {
                    authorEmail = authorEmail.substring(0, 22) + "...";
                }

                // Récupérer le nom complet de l'auteur (prénom et nom)
                String authorName = book.getAuthors().stream()
                        .findFirst() // On prend seulement le premier auteur
                        .map(a -> a.getFirst_name() + " " + a.getLast_name())
                        .orElse("Auteur inconnu");

                if (authorName.length() > 35) {
                    authorName = authorName.substring(0, 32) + "...";
                }

                // Affichage des informations du livre avec des largeurs réduites
                System.out.printf("| %-10d | %-35s | %-15d | %-25s | %-25s | %-35s |\n",
                        book.getBook_id(),
                        book.getTitle().length() > 35 ? book.getTitle().substring(0, 32) + "..." : book.getTitle(),
                        book.getNumber_Of_Copies(),
                        categories,
                        authorEmail,
                        authorName);

                System.out.println("+------------+-----------------------------------+---------------------+---------------------------+-----------------------------+-----------------------------------------+");
            } else {
                Logger.logError("Livre non trouvé.");
            }
        } catch (BookDisplayException e) {
            System.err.println(RED + "Erreur: " + e.getMessage());
        }
    }

    // Méthode pour afficher tous les livres
    private void displayAllAvailableBooks() {
        try {
            // Appel du contrôleur pour récupérer tous les livres disponibles
            List<Book> books = bookController.displayAvailableBooks();


            // Vérification si la liste des livres est vide
            if (books.isEmpty()) {
                System.out.println("\n ❌ Aucun livre disponible.");
                return;
            }


            // Détermination des largeurs maximales pour chaque colonne
            int idWidth = "ID".length();
            int titleWidth = "Titre".length();
            int copiesWidth = "Nb Copies".length();
            int categoryWidth = "Catégorie".length();
            int authorEmailWidth = "Email Auteur".length();
            int authorNameWidth = "Nom de l'Auteur".length();


            for (Book book : books) {
                idWidth = Math.max(idWidth, String.valueOf(book.getBook_id()).length());
                titleWidth = Math.max(titleWidth, book.getTitle().length());
                copiesWidth = Math.max(copiesWidth, String.valueOf(book.getNumber_Of_Copies()).length());


                // Calculer la largeur pour les catégories
                String categories = book.getCategories().stream()
                        .map(Category::getCategory_name)
                        .reduce((cat1, cat2) -> cat1 + ", " + cat2)
                        .orElse("Aucune catégorie");
                categoryWidth = Math.max(categoryWidth, categories.length());


                // Calculer la largeur pour les emails des auteurs
                String authorEmail = book.getAuthors().stream()
                        .findFirst()
                        .map(Author::getAuthor_email)
                        .orElse("Email non disponible");
                authorEmailWidth = Math.max(authorEmailWidth, authorEmail.length());


                // Calculer la largeur pour les noms des auteurs
                String authorFullName = book.getAuthors().stream()
                        .map(author -> author.getFirst_name() + " " + author.getLast_name())
                        .reduce((name1, name2) -> name1 + ", " + name2)
                        .orElse("Auteur inconnu");
                authorNameWidth = Math.max(authorNameWidth, authorFullName.length());
            }


            // Couleurs ANSI pour les lignes et les en-têtes
            String CYAN = "\u001B[36m";
            String RESET = "\u001B[0m";


            // Ligne de séparation
            String horizontalLine = CYAN + "+-" + "-".repeat(idWidth) + "-+-" +
                    "-".repeat(titleWidth) + "-+-" +
                    "-".repeat(copiesWidth) + "-+-" +
                    "-".repeat(categoryWidth) + "-+-" +
                    "-".repeat(authorEmailWidth) + "-+-" +
                    "-".repeat(authorNameWidth) + "-+" + RESET;


            // Format d'affichage
            String format = "| %-" + idWidth + "s | %-" + titleWidth + "s | %-" + copiesWidth + "s | %-" +
                    categoryWidth + "s | %-" + authorEmailWidth + "s | %-" + authorNameWidth + "s |\n";


            // Affichage du tableau
            //System.out.println("\n\u001B[34m======== Liste des Livres ========\u001B[0m");
            Logger.logInfo("--------------------- Liste des Livres ---------------------");
            System.out.println(horizontalLine);
            System.out.printf(CYAN + format + RESET, "ID", "Titre", "Nb Copies", "Catégorie", "Email Auteur", "Nom de l'Auteur");
            System.out.println(horizontalLine);


            for (Book book : books) {
                String categories = book.getCategories().stream()
                        .map(Category::getCategory_name)
                        .reduce((cat1, cat2) -> cat1 + ", " + cat2)
                        .orElse("Aucune catégorie");


                String authorEmail = book.getAuthors().stream()
                        .findFirst()
                        .map(Author::getAuthor_email)
                        .orElse("Email non disponible");


                String authorFullName = book.getAuthors().stream()
                        .map(author -> author.getFirst_name() + " " + author.getLast_name())
                        .reduce((name1, name2) -> name1 + ", " + name2)
                        .orElse("Auteur inconnu");


                System.out.printf(format,
                        book.getBook_id(),
                        book.getTitle(),
                        book.getNumber_Of_Copies(),
                        categories,
                        authorEmail,
                        authorFullName);
            }


            System.out.println(horizontalLine);


        } catch (BookDisplayException e) {
            System.err.println("\u001B[31mErreur: " + e.getMessage() + "\u001B[0m");
        }
    }


    // Méthode pour mettre à jour un livre
    public void updateBook() {
        Logger.logInfo("Mettre à jour un livre : ");
        Scanner scanner = new Scanner(System.in);

        // 🔹 Demander l'ID du livre avec validation
        int bookId = getValidIntInput(scanner, "Entrez l'ID du livre à mettre à jour : ");

        // 🔹 Demander le nouveau titre
        System.out.print("Entrez le nouveau titre du livre : ");
        String title = scanner.nextLine().trim();  // Supprimer les espaces inutiles

        // 🔹 Demander le nouveau nombre d'exemplaires avec validation
        int numberOfCopies = getValidIntInput(scanner, "Entrez le nouveau nombre d'exemplaires : ");

        // 🔹 Demander les nouveaux IDs des auteurs avec validation
        Set<Author> authors = new HashSet<>();
        int authorId = getValidIntInput(scanner, "Entrez l'ID de l'auteur : ");
        Author author = new Author();
        author.setAuthor_id(authorId);
        authors.add(author);

        // 🔹 Demander les nouveaux IDs des catégories avec validation
        Set<Category> categories = new HashSet<>();
        int categoryId = getValidIntInput(scanner, "Entrez l'ID de la catégorie : ");
        Category category = new Category();
        category.setCategory_id(categoryId);
        categories.add(category);

        // 🔹 Créer un objet Book avec les nouvelles données
        Book bookToUpdate = new Book();
        bookToUpdate.setBook_id(bookId);
        bookToUpdate.setTitle(title);
        bookToUpdate.setNumber_Of_Copies(numberOfCopies);
        bookToUpdate.setAuthors(authors);
        bookToUpdate.setCategories(categories);

        // 🔹 Appeler la méthode de mise à jour dans BookController
        try {
            bookController.updateBook(bookToUpdate);
            Logger.logSuccess("📘 Livre mis à jour avec succès.");
        } catch (BookUpdateException e) {
            Logger.logError("❌ Erreur lors de la mise à jour du livre : ", e);
        }
    }

    private int getValidIntInput(Scanner scanner, String prompt) {
        int value;
        while (true) {
            System.out.print(prompt);
            if (scanner.hasNextInt()) {  // Vérifie si l'entrée est un nombre
                value = scanner.nextInt();
                scanner.nextLine();  // Consommer la ligne restante
                break;
            } else {
                System.out.println("⚠️ Entrée invalide ! Veuillez entrer un nombre valide.");
                scanner.next(); // Consommer l'entrée incorrecte pour éviter une boucle infinie
            }
        }
        return value;
    }


    // Methode pour supprimer un livre
    public void removeBook() {
        Logger.logInfo("Supprimer un livre : ");
        Scanner scanner = new Scanner(System.in);

        // Demander l'ID du livre à supprimer
        System.out.print("Entrez l'ID du livre à supprimer : ");
        int bookId = scanner.nextInt();
        scanner.nextLine();  // Consommer la ligne restante

        try {
            bookController.removeBook(bookId);  // Appel de la méthode pour supprimer le livre
        } catch (BookRemoveException e) {
            // Gestion des erreurs si le livre ne peut pas être supprimé
            Logger.logError(RED + "Erreur lors de la suppression du livre : " + e.getMessage() + RESET);
            return; // Ne pas afficher le message de succès en cas d'erreur
        }


        // Message de succès
        Logger.logSuccess(GREEN + "Le livre avec l'ID " + bookId + " a été supprimé avec succès." + RESET);
    }

    public void searchBookByCategory() {
        Logger.logInfo("Rechercher un livre par catégories : ");
        Scanner scanner = new Scanner(System.in);
        System.out.print("Entrez le nom de la catégorie : ");
        String categoryName = scanner.nextLine().trim().toLowerCase(); // Normalisation de l'entrée utilisateur

        List<Book> books = bookController.searchBookByCategory(categoryName);

        if (books.isEmpty()) {
            Logger.logWarn("Entrée invalide.",
                    " Aucun livre trouvé pour la catégorie : " + categoryName);
        } else {
            Logger.logSuccess("📚 Livres trouvés dans la catégorie : " + categoryName);
            System.out.println("+----+--------------------------------------+-------+---------------------+----------------------+-------------+");
            System.out.printf("| %-2s | %-36s | %-5s | %-19s | %-20s | %-11s |\n",
                    "ID", "Titre", "Nb", "Auteur", "Email", "Catégorie");
            System.out.println("+----+--------------------------------------+-------+---------------------+----------------------+-------------+");

            for (Book book : books) {
                String authorName = book.getAuthors().stream()
                        .findFirst()
                        .map(a -> a.getFirst_name() + " " + a.getLast_name())
                        .orElse("Inconnu");

                String authorEmail = book.getAuthors().stream()
                        .findFirst()
                        .map(Author::getAuthor_email)
                        .orElse("Non disponible");

                String categories = book.getCategories().stream()
                        .map(Category::getCategory_name)
                        .reduce((cat1, cat2) -> cat1 + ", " + cat2)
                        .orElse("Aucune");

                System.out.printf("| %-2d | %-36s | %-5d | %-19s | %-20s | %-11s |\n",
                        book.getBook_id(),
                        book.getTitle(),
                        book.getNumber_Of_Copies(),
                        authorName,
                        authorEmail,
                        categories);
            }

            System.out.println("+----+--------------------------------------+-------+---------------------+----------------------+-------------+");
        }
    }











}
