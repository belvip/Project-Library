package com.library.system.handler;

import com.library.system.controller.BookController;
import com.library.system.exception.bookDaoException.BookDisplayException;
import com.library.system.exception.bookDaoException.BookUpdateException;
import com.library.system.model.Author;
import com.library.system.model.Book;
import com.library.system.model.Category;
import com.library.system.service.impl.BookServiceImpl;

import java.util.*;

public class BookHandler {

    private final Scanner scanner = new Scanner(System.in);
    private final BookServiceImpl bookService; // Service pour gérer les livres
    private final BookController bookController; // Contrôleur pour gérer les requêtes des livres

    // Constructeur qui accepte BookServiceImpl et BookController
    public BookHandler(BookServiceImpl bookService, BookController bookController) {
        this.bookService = bookService;  // Initialisation correcte
        this.bookController = bookController;
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
                    running = false;
                    break;
                default:
                    System.out.println("Choix invalide. Essayez encore.");
            }
        }
    }

    // Méthode pour afficher le menu des opérations
    private void displayBookMenu() {
        System.out.println("\n\u001B[34m======== Opérations sur les livres ========\u001B[0m");
        System.out.println("+--------------------------------------------+");
        System.out.printf("| %-2s | %-40s |\n", "1", "\u001B[32mAjouter un livre\u001B[0m");
        System.out.printf("| %-2s | %-40s |\n", "2", "\u001B[33mAfficher un livre\u001B[0m");
        System.out.printf("| %-2s | %-40s |\n", "3", "\u001B[36mAfficher tous les livres\u001B[0m");
        System.out.printf("| %-2s | %-40s |\n", "4", "\u001B[36mMettre a jour un livre\u001B[0m");
        System.out.printf("| %-2s | %-40s |\n", "5", "\u001B[31mQuitter\u001B[0m");
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
                    System.out.println("Le nombre d'exemplaires ne peut pas être négatif. Veuillez réessayer.");
                }
            } catch (InputMismatchException e) {
                // Si une exception se produit (par exemple, si ce n'est pas un nombre), on la gère
                System.out.println("Entrée invalide. Veuillez entrer un nombre entier.");
                scanner.nextLine(); // Nettoyer le buffer du scanner pour éviter une boucle infinie
            }
        }

        // Créer le set des auteurs
        Set<Author> authors = new HashSet<>();
        System.out.print("Entrez l'ID de l'auteur : ");
        int authorId = scanner.nextInt();
        Author author = new Author(); // Récupérer l'auteur via un service
        author.setAuthor_id(authorId);
        authors.add(author);

        // Créer le set des catégories
        Set<Category> categories = new HashSet<>();
        System.out.print("Entrez l'ID de la catégorie : ");
        int categoryId = scanner.nextInt();
        Category category = new Category(); // Récupérer la catégorie via un service
        category.setCategory_id(categoryId);
        categories.add(category);

        // Créer l'objet Book sans ID
        Book book = new Book();
        book.setTitle(title);
        book.setAuthors(authors);
        book.setCategories(categories);
        book.setNumber_Of_Copies(numberOfCopies);

        // Ajouter le livre via le contrôleur
        bookController.addBook(book);

        // Afficher l'ID généré
        System.out.println("Livre ajouté avec succès. L'ID du livre est : " + book.getBook_id());
    }

    // Méthode pour afficher un livre
    private void displayBookById() {
        System.out.print("Entrez l'ID du livre à afficher: ");
        int bookId = scanner.nextInt(); // Demander l'ID du livre à afficher

        try {
            // Appel du contrôleur pour obtenir le livre
            Book book = bookController.displayBookById(bookId);

            if (book != null) {
                System.out.println("\n\u001B[34m======== Détails du Livre ========\u001B[0m");
                System.out.println("+------------+-----------------------------------+---------------------+---------------------------+-----------------------------------------+");
                System.out.printf("| %-10s | %-30s | %-19s | %-25s | %-35s | %-35s |\n",
                        "ID", "Titre", "Nb Copies", "Catégorie", "Email Auteur", "Nom de l'Auteur");
                System.out.println("+------------+-----------------------------------+---------------------+---------------------------+-----------------------------------------+");

                // Récupérer les catégories du livre
                String categories = book.getCategories().stream()
                        .map(Category::getCategory_name)
                        .reduce((cat1, cat2) -> cat1 + ", " + cat2)
                        .orElse("Aucune catégorie");

                // Récupérer l'email et le nom de l'auteur
                String authorEmail = book.getAuthors().stream()
                        .findFirst() // On prend seulement le premier auteur
                        .map(Author::getAuthor_email)
                        .orElse("Email non disponible");

                // Récupérer le nom complet de l'auteur (prénom et nom)
                String authorName = book.getAuthors().stream()
                        .findFirst() // On prend seulement le premier auteur
                        .map(a -> a.getFirst_name() + " " + a.getLast_name())
                        .orElse("Auteur inconnu");

                // Afficher les informations du livre
                System.out.printf("| %-10d | %-30s | %-19d | %-25s | %-35s | %-35s |\n",
                        book.getBook_id(),
                        book.getTitle(),
                        book.getNumber_Of_Copies(),
                        categories,
                        authorEmail,
                        authorName);

                System.out.println("+------------+-----------------------------------+---------------------+---------------------------+-----------------------------------------+");
            } else {
                System.out.println("Livre non trouvé.");
            }
        } catch (BookDisplayException e) {
            System.err.println("Erreur: " + e.getMessage());
        }
    }

    // Méthode pour afficher tous les livres
    private void displayAllAvailableBooks() {
        try {
            // Appel du contrôleur pour récupérer tous les livres disponibles
            List<Book> books = bookController.displayAvailableBooks(); // Appel de la méthode sans argument

            // Vérification si la liste des livres est vide
            if (books.isEmpty()) {
                System.out.println("Aucun livre disponible.");
            } else {
                System.out.println("\n\u001B[34m======== Liste des Livres ========\u001B[0m");
                System.out.println("+------------+-----------------------------------+---------------------+---------------------------+-----------------------------------------+-----------------------+");
                System.out.printf("| %-10s | %-30s | %-19s | %-25s | %-35s | %-30s |\n",
                        "ID", "Titre", "Nb Copies", "Catégorie", "Email Auteur", "Nom de l'Auteur");
                System.out.println("+------------+-----------------------------------+---------------------+---------------------------+-----------------------------------------+-----------------------+");

                // Affichage des informations de chaque livre
                for (Book book : books) {
                    // Récupérer les catégories du livre
                    String categories = "Aucune catégorie";
                    if (book.getCategories() != null && !book.getCategories().isEmpty()) {
                        categories = book.getCategories().stream()
                                .map(Category::getCategory_name)
                                .reduce((cat1, cat2) -> cat1 + ", " + cat2)
                                .orElse("Aucune catégorie");
                    }

                    // Récupérer l'email de l'auteur
                    String authorEmail = "Email non disponible";
                    if (book.getAuthors() != null && !book.getAuthors().isEmpty()) {
                        authorEmail = book.getAuthors().stream()
                                .findFirst()
                                .map(Author::getAuthor_email)
                                .orElse("Email non disponible");
                    }

                    // Récupérer le nom complet de l'auteur
                    String authorFullName = "Auteur non disponible";
                    if (book.getAuthors() != null && !book.getAuthors().isEmpty()) {
                        authorFullName = book.getAuthors().stream()
                                .map(author -> author.getFirst_name() + " " + author.getLast_name())
                                .reduce((name1, name2) -> name1 + ", " + name2)
                                .orElse("Auteur inconnu");
                    }

                    // Affichage des informations du livre
                    System.out.printf("| %-10d | %-30s | %-19d | %-25s | %-35s | %-30s |\n",
                            book.getBook_id(),
                            book.getTitle(),
                            book.getNumber_Of_Copies(),
                            categories,
                            authorEmail,
                            authorFullName); // Affichage du nom complet de l'auteur
                }

                System.out.println("+------------+-----------------------------------+---------------------+---------------------------+-----------------------------------------+-----------------------+");
            }
        } catch (BookDisplayException e) {
            System.err.println("Erreur: " + e.getMessage());
        }
    }


    // Méthode pour mettre à jour un livre
    public void updateBook() {
        Scanner scanner = new Scanner(System.in);

        // Demander l'ID du livre à mettre à jour
        System.out.print("Entrez l'ID du livre à mettre à jour : ");
        int bookId = scanner.nextInt();
        scanner.nextLine();  // Consommer la ligne restante

        // Demander le nouveau titre
        System.out.print("Entrez le nouveau titre du livre : ");
        String title = scanner.nextLine();

        // Demander le nouveau nombre d'exemplaires
        System.out.print("Entrez le nouveau nombre d'exemplaires : ");
        int numberOfCopies = scanner.nextInt();

        // Demander les nouveaux IDs des auteurs
        Set<Author> authors = new HashSet<>();
        System.out.print("Entrez l'ID de l'auteur : ");
        int authorId = scanner.nextInt();
        Author author = new Author();
        author.setAuthor_id(authorId);  // Définir l'ID de l'auteur
        authors.add(author);  // Ajouter l'auteur au set

        // Demander les nouveaux IDs des catégories
        Set<Category> categories = new HashSet<>();
        System.out.print("Entrez l'ID de la catégorie : ");
        int categoryId = scanner.nextInt();
        Category category = new Category();
        category.setCategory_id(categoryId);  // Définir l'ID de la catégorie
        categories.add(category);  // Ajouter la catégorie au set

        // Créer un objet Book avec les nouvelles données
        Book bookToUpdate = new Book();
        bookToUpdate.setBook_id(bookId);  // Définir l'ID du livre
        bookToUpdate.setTitle(title);     // Définir le titre
        bookToUpdate.setNumber_Of_Copies(numberOfCopies);  // Définir le nombre d'exemplaires
        bookToUpdate.setAuthors(authors);  // Définir les auteurs
        bookToUpdate.setCategories(categories);  // Définir les catégories

        // Appeler la méthode de BookController pour mettre à jour le livre
        try {
            bookController.updateBook(bookToUpdate);
            System.out.println("Livre mis à jour avec succès.");
        } catch (BookUpdateException e) {
            // Gérer les erreurs si la mise à jour échoue
            System.err.println("Erreur lors de la mise à jour du livre : " + e.getMessage());
        }
    }




}
