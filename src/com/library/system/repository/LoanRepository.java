
package com.library.system.repository;

import com.library.system.exception.loanException.RegisterLoanException;
import com.library.system.model.Book;
import com.library.system.model.Loan;

import java.sql.SQLException;
import java.util.List;

public interface LoanRepository {

    void registerLoan(int memberId, List<Book> bookId) throws RegisterLoanException;
    void returnBook(int loanId) throws SQLException;

    // Méthode pour récupérer tous les emprunts
    void getAllLoans(List<Loan> loans) throws SQLException;

    // Supprimer un emprunt
    void deleteLoan(int loanId) throws SQLException;
}
