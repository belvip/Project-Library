

package com.library.system.handler;

import com.library.system.exception.loanException.RegisterLoanException;
import com.library.system.exception.memberException.FindMemberByIdException;
import com.library.system.model.*;
import com.library.system.service.impl.LoanServiceImpl;
import com.library.system.controller.LoanController;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class LoanHandler {

    private final Scanner scanner = new Scanner(System.in);
    private final LoanServiceImpl loanService;
    private final LoanController loanController;

    public LoanHandler(LoanServiceImpl loanService, LoanController loanController) {
        this.loanService = loanService;
        this.loanController = loanController;
    }

    // Méthode pour traiter les opérations sur les emprunts
    public void handleLoanOperations() {
        boolean running = true;
        while (running) {
            displayLoanMenu();
            int choice = getChoiceInput();

            scanner.nextLine();

            switch (choice) {
                case 1:
                    registerLoan();  // Appel de la méthode pour enregistrer un emprunt
                    break;
                case 2:
                    returnBook();  // Appel de la méthode pour retourner un livre
                    break;
                case 3:
                    getAllLoans
                            ();  // Appel de la méthode pour retourner un livre
                    break;
                case 4:
                    running = false;
                    break;
                default:
                    System.out.println("Choix invalide. Essayez encore.");
            }
        }
    }

    // Méthode pour obtenir l'entrée de l'utilisateur
    private int getChoiceInput() {
        try {
            return scanner.nextInt();
        } catch (Exception e) {
            scanner.nextLine();  // Nettoie le buffer
            return -1;
        }
    }

    // Méthode pour afficher le menu des opérations
    private void displayLoanMenu() {
        System.out.println("\n\u001B[34m======== Opérations sur les emprunts ========\u001B[0m");
        System.out.println("+--------------------------------------------+");
        System.out.printf("| %-2s | %-40s |\n", "1", "\u001B[32mEnregister un emprunt\u001B[0m");
        System.out.printf("| %-2s | %-40s |\n", "2", "\u001B[32mRetouner un emprunt\u001B[0m");
        System.out.printf("| %-2s | %-40s |\n", "3", "\u001B[32mLister tous les emprunts\u001B[0m");
        System.out.printf("| %-2s | %-40s |\n", "4", "\u001B[31mQuitter\u001B[0m");
        System.out.println("+--------------------------------------------+");
        System.out.print("\u001B[33mEntrez votre choix: \u001B[0m");
    }

    // Méthode pour enregistrer un emprunt via LoanController
    public void registerLoan() {
        // Étape 1 : Obtenir les informations du membre
        System.out.print("Entrez l'ID du membre qui souhaite emprunter des livres : ");
        int memberId = scanner.nextInt();
        scanner.nextLine(); // Consommer la nouvelle ligne après nextInt()

        Member member;
        try {
            member = loanController.getMemberById(memberId); // 🔍 Récupérer le membre par ID
        } catch (FindMemberByIdException e) {
            System.out.println("❌ Erreur : " + e.getMessage()); // Afficher un message clair
            return; // Arrêter le processus d'emprunt
        }

        if (member == null) {
            System.out.println("❌ Membre introuvable. Veuillez vérifier l'ID.");
            return;
        }

        // Étape 2 : Obtenir les livres à emprunter
        System.out.print("Entrez les IDs des livres à emprunter (séparés par des espaces) : ");
        String[] bookIds = scanner.nextLine().split(" ");

        List<Book> books = new ArrayList<>(); // Liste pour stocker les livres trouvés

        // Parcourir chaque ID et récupérer le livre correspondant
        for (String bookIdStr : bookIds) {
            try {
                int bookId = Integer.parseInt(bookIdStr); // Convertir String en int
                Book book = loanController.getBookById(bookId); // Récupérer le livre par ID

                if (book != null) {
                    books.add(book);
                } else {
                    System.out.println("⚠️ Livre avec ID " + bookId + " introuvable.");
                }
            } catch (NumberFormatException e) {
                System.out.println("⚠️ ID invalide : " + bookIdStr);
            }
        }

        // Vérifier si au moins un livre a été trouvé
        if (books.isEmpty()) {
            System.out.println("❌ Aucun livre valide n'a été trouvé avec les IDs fournis.");
            return;
        }

        // Étape 3 : Enregistrer l'emprunt via le LoanController
        loanController.registerLoan(member, books);  // Appeler la méthode de LoanController
    }

    public void returnBook() {
        System.out.print("Entrez l'ID du prêt à retourner : ");
        int loanId = scanner.nextInt();

        try {
            loanController.returnBook(loanId);  // Appel de la méthode returnBook dans LoanController
            System.out.println("✅ Le livre a été retourné avec succès !");
        } catch (SQLException e) {
            System.out.println("❌ Erreur lors du retour du livre : " + e.getMessage());
        }
    }

    public void getAllLoans() {
        try {
            List<Loan> loans = new ArrayList<>();
            loanController.getAllLoans(loans); // Passe la liste des emprunts à récupérer


            // Affichage de l'en-tête du tableau
            System.out.println("\n\u001B[34m========= Liste des Emprunts ========\u001B[0m");
            System.out.println("+----------+--------------------+------------------+-------------------+-------------------+-------------------+--------------------+--------------------+--------------------+--------------------+");
            System.out.printf("| %-8s | %-18s | %-16s | %-17s | %-17s | %-16s | %-18s | %-16s | %-16s | %-16s |\n",
                    "Emprunt ID", "Membre", "Date d'Emprunt", "Date d'Échéance", "Date de Retour", "Livre ID", "Titre", "Nb Copies", "Auteurs", "Catégories");
            System.out.println("+----------+--------------------+------------------+-------------------+-------------------+-------------------+--------------------+--------------------+--------------------+--------------------+");


            // Affichage des emprunts récupérés
            if (loans.isEmpty()) {
                // Affiche une ligne avec "Aucun emprunt trouvé"
                System.out.println("| Aucun emprunt trouvé                                                                            |");
            } else {
                for (Loan loan : loans) {
                    // Affichage des informations sur chaque emprunt avec les livres associés
                    for (Book book : loan.getBooks()) {
                        // Convertir Set<Author> en List<String> pour pouvoir utiliser String.join()
                        List<String> authorNames = new ArrayList<>();
                        for (Author author : book.getAuthors()) {
                            authorNames.add(author.getFirst_name() + " " + author.getLast_name());
                        }


                        // Convertir Set<Category> en List<String> pour pouvoir utiliser String.join()
                        List<String> categoryNames = new ArrayList<>();
                        for (Category category : book.getCategories()) {
                            categoryNames.add(category.getCategory_name());
                        }


                        // Affichage des détails du prêt et des livres associés
                        System.out.printf("| %-8d | %-18s | %-16s | %-17s | %-17s | %-16d | %-18s | %-16d | %-16s | %-16s |\n",
                                loan.getLoanId(),
                                loan.getMember().getFirstName() + " " + loan.getMember().getLastName(),
                                loan.getFormattedLoanDate(),          // Date d'emprunt formatée
                                loan.getFormattedDueDate(),           // Date d'échéance formatée
                                loan.getFormattedReturnedDate(),      // Date de retour formatée (ou "Pas encore retourné")
                                book.getBook_id(),
                                book.getTitle(),
                                book.getNumber_Of_Copies(),
                                String.join(", ", authorNames),       // Liste des auteurs
                                String.join(", ", categoryNames));    // Liste des catégories
                    }
                }
            }


            // Affichage de la ligne de fin du tableau
            System.out.println("+----------+--------------------+------------------+-------------------+-------------------+-------------------+--------------------+--------------------+--------------------+--------------------+");
        } catch (SQLException e) {
            // Affiche l'erreur en cas de problème avec la récupération des emprunts
            System.out.println("❌ Erreur lors de l'affichage des emprunts : " + e.getMessage());
        }
    }










}
