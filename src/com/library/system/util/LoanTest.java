package com.library.system.util;

import com.library.system.dao.BookDAO;
import com.library.system.dao.impl.BookDAOImpl;
import com.library.system.repository.LoanRepository;
import com.library.system.repository.impl.LoanRepositoryImpl;
import com.library.system.exception.bookDaoException.BookUpdateException;
import com.library.system.exception.loanException.RegisterLoanException;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/*public class LoanTest {

    public static void main(String[] args) {
        // ⚡ Connexion à la base de données
        String url = "jdbc:postgresql://localhost:5432/library_db";  // Remplace avec ton URL
        String user = "postgres";  // Remplace avec ton utilisateur PostgreSQL
        String password = "belvi";  // Remplace avec ton mot de passe

        try (Connection connection = DriverManager.getConnection(url, user, password)) {
            System.out.println("✅ Connexion réussie à la base de données !");

            // ✅ Création des DAO et Repository
            BookDAO bookDAO = new BookDAOImpl(connection);
            LoanRepository loanRepository = new LoanRepositoryImpl(connection);

            // 📌 1️⃣ Test de la mise à jour du nombre de copies après un emprunt
            int bookId = 2;  // Remplace avec un ID de livre existant
            try {
                bookDAO.borrowBook(bookId);
                System.out.println("📌 Nombre de copies du livre " + bookId + " mis à jour !");
            } catch (BookUpdateException e) {
                System.err.println("❌ Erreur borrowBook: " + e.getMessage());
            }

            // 📌 2️⃣ Test de l'enregistrement d'un prêt
            int memberId = 2;  // Remplace avec un ID de membre existant
            try {
                loanRepository.registerLoan(memberId, bookId);
                System.out.println("📌 Prêt enregistré pour le membre " + memberId + " et le livre " + bookId);
            } catch (RegisterLoanException e) {
                System.err.println("❌ Erreur registerLoan: " + e.getMessage());
            }

        } catch (SQLException e) {
            System.err.println("❌ Erreur de connexion à la base de données : " + e.getMessage());
        }
    }
} */
