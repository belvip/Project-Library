
package com.library.system.service.impl;

import com.library.system.model.Book;
import com.library.system.model.Member;
import com.library.system.exception.loanException.RegisterLoanException;
import com.library.system.repository.LoanRepository;
import com.library.system.service.LoanService;

import java.sql.SQLException;
import java.util.List;

public class LoanServiceImpl implements LoanService {

    private LoanRepository loanRepository;

    public LoanServiceImpl(LoanRepository loanRepository) {
        this.loanRepository = loanRepository;
    }

    @Override
    public void registerLoan(Member member, List<Book> books) throws RegisterLoanException {
        try {
            // Utiliser l'ID du membre (member.getMember_id()) et passer la liste de livres
            loanRepository.registerLoan(member.getMember_id(), books);
        } catch (Exception e) {
            throw new RegisterLoanException("Erreur lors de l'emprunt : " + e.getMessage());
        }
    }

    public void returnBook(int loanId) {
        try {
            loanRepository.returnBook(loanId);  // Appeler la méthode returnBook de LoanRepository
            System.out.println("📚 Le livre a été retourné avec succès.");
        } catch (SQLException e) {
            System.out.println("❌ Erreur lors du retour du livre : " + e.getMessage());
        }
    }

}
