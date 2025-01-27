
package com.library.system.controller;


import com.library.system.exception.bookDaoException.BookDisplayException;
import com.library.system.model.Book;
import com.library.system.model.Loan;
import com.library.system.model.Member;
import com.library.system.service.BookService;
import com.library.system.service.LoanService;
import com.library.system.service.MemberService;
import com.library.system.util.DatabaseConnection;
import com.library.system.util.Logger;  // Importation de la classe Logger


import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

public class LoanController {


    private LoanService loanService;
    private final BookService bookService;
    private final MemberService memberService;


    public LoanController(LoanService loanService, BookService bookService, MemberService memberService) {
        this.loanService = loanService;
        this.bookService = bookService;
        this.memberService = memberService;
    }


    public void registerLoan(Member member, List<Book> books) {
        try {
            loanService.registerLoan(member, books);
            Logger.logSuccess("Emprunt enregistré pour le membre " + member.getFirstName() + " " + member.getLastName());
        } catch (Exception e) {
            Logger.logError("l'enregistrement du prêt", e);
        }
    }


    public Book getBookById(int bookId) {
        try {
            return bookService.getBookById(bookId);  // Appel au service pour récupérer un livre par son ID
        } catch (BookDisplayException e) {
            Logger.logError("la récupération du livre avec ID " + bookId, e);
            return null;  // Ou retourner une valeur par défaut ou traiter l'erreur autrement
        }
    }


    public Member getMemberById(int memberId) {
        return memberService.findMemberById(memberId); // Utiliser le service pour obtenir le membre
    }


    public void returnBook(int loanId) throws SQLException {
        try {
            loanService.returnBook(loanId);  // Appel au service pour retourner le livre
            Logger.logSuccess("Livre retourné pour le prêt ID " + loanId);
        } catch (SQLException e) {
            Logger.logError("le retour du livre pour le prêt ID " + loanId, e);
        }
    }


    public void getAllLoans(List<Loan> loans) throws SQLException {
        try {
            loanService.getAllLoans(loans);
            if (loans.isEmpty()) {
                Logger.logError("Aucun emprunt trouvé.");
            } else {
                for (Loan loan : loans) {
                    System.out.println("Emprunt ID: " + loan.getLoanId() +
                            ", Membre: " + loan.getMember().getFirstName() + " " + loan.getMember().getLastName() +
                            ", Livres: " + loan.getBooks() +
                            ", Date d'emprunt: " + loan.getLoanDate() +
                            ", Date de retour prévue: " + loan.getDueDate() +
                            (loan.getReturnedDate() != null ? ", Date de retour: " + loan.getReturnedDate() : ""));
                }
            }
        } catch (SQLException e) {
            Logger.logError("l'affichage des emprunts", e);
            throw e;
        }
    }

    public void deleteLoan(int loanId) {
        try {
            loanService.deleteLoan(loanId);  // Appel du service pour supprimer le prêt
           Logger.logSuccess("Loan deleted successfully.");
        } catch (SQLException e) {
            //System.err.println("Error deleting loan: " + e.getMessage());
            Logger.logError("Error deleting loan: " , e);
            // Vous pouvez aussi loguer l'erreur si nécessaire
        }
    }

    public boolean hasMemberAlreadyLoanedBook(int memberId, int bookId) {
        // Requête SQL pour vérifier si le membre a déjà emprunté ce livre et que l'emprunt est toujours en cours (returnDate IS NULL)
        String sql = "SELECT l.member_id, bl.book_id " +
                "FROM Loan l " +
                "JOIN Book_Loan bl ON l.loan_id = bl.loan_id " +
                "WHERE l.member_id = ? AND bl.book_id = ? AND l.returnDate IS NULL";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) { // Correction ici

            stmt.setInt(1, memberId);
            stmt.setInt(2, bookId);

            ResultSet resultSet = stmt.executeQuery();
            return resultSet.next(); // Si une ligne est retournée, le membre a déjà emprunté ce livre

        } catch (SQLException e) {
            e.printStackTrace();
            return false; // En cas d'erreur, on considère que le membre peut emprunter ce livre
        }
    }





}
