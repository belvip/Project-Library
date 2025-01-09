package com.library.system.service.impl;


import com.library.system.model.Book;
import com.library.system.model.Loan;
import com.library.system.model.Member;
import com.library.system.exception.loanException.RegisterLoanException;
import com.library.system.repository.LoanRepository;
import com.library.system.service.LoanService;
import com.library.system.util.Logger;


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
            // Log success
            Logger.logSuccess("Emprunt enregistré pour le membre " + member.getFirstName() + " " + member.getLastName());
        } catch (Exception e) {
            // Log error
            Logger.logError("l'enregistrement du prêt", e);
            throw new RegisterLoanException("Erreur lors de l'emprunt : " + e.getMessage());
        }
    }


    public void returnBook(int loanId) {
        try {
            loanRepository.returnBook(loanId);  // Appeler la méthode returnBook de LoanRepository
            // Log success
            Logger.logSuccess("Le livre avec l'ID " + loanId + " a été retourné avec succès.");
        } catch (SQLException e) {
            // Log error
            Logger.logError("le retour du livre", e);
            System.out.println("❌ Erreur lors du retour du livre : " + e.getMessage());  // Optionnel si vous voulez aussi afficher dans la console
        }
    }


    @Override
    public void getAllLoans(List<Loan> loans) throws SQLException {
        try {
            // Appel de la méthode du repository pour récupérer les prêts
            loanRepository.getAllLoans(loans);
            // Log success si des prêts sont récupérés
            if (loans.isEmpty()) {
                Logger.logInfo("Aucun emprunt trouvé.");
            } else {
                Logger.logInfo("Liste des emprunts récupérée avec succès.");
            }
        } catch (SQLException e) {
            // Log error
            Logger.logError("l'affichage des emprunts", e);
            throw e;  // Relancer l'exception après l'avoir loggée si vous souhaitez la propager
        }
    }
}


