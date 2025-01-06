package com.library.system.repository.impl;


import com.library.system.dao.BookDAO;
import com.library.system.dao.LoanDAO;
import com.library.system.dao.MemberDAO;
import com.library.system.dao.impl.LoanDAOImpl;
import com.library.system.dao.impl.MemberDAOImpl;
import com.library.system.dao.impl.BookDAOImpl; // Ajout de l'importation de BookDAOImpl
import com.library.system.exception.loanException.RegisterLoanException;
import com.library.system.model.Book;
import com.library.system.model.Member;
import com.library.system.repository.LoanRepository;


import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;


public class LoanRepositoryImpl implements LoanRepository {


    private LoanDAO loanDAO;
    private MemberDAO memberDAO;
    private BookDAO bookDAO;


    // Constructeur où tu passes la connexion à LoanDAOImpl, MemberDAOImpl et BookDAOImpl
    public LoanRepositoryImpl(Connection connection) {
        this.loanDAO = new LoanDAOImpl(connection);  // Initialise LoanDAOImpl avec la connexion
        this.memberDAO = new MemberDAOImpl(connection);  // Initialise MemberDAOImpl avec la même connexion
        this.bookDAO = new BookDAOImpl(connection);  // Initialise BookDAOImpl avec la même connexion
    }


    // Méthode pour enregistrer un prêt
    @Override
    public void registerLoan(int memberId, List<Book> books) throws RegisterLoanException {
        // 1️⃣ Récupérer le membre à partir de son ID
        Member member = memberDAO.findMemberById(memberId);
        if (member == null) {
            throw new RegisterLoanException("Le membre avec l'ID " + memberId + " n'a pas été trouvé.");
        }


        // Pour chaque livre, vérifiez sa disponibilité et enregistrez-le dans la base de données
        for (Book book : books) {
            int bookId = book.getBook_id(); // Récupérer l'ID du livre
            // Vérifiez si le livre existe
            Book existingBook = bookDAO.findBookById(bookId);
            if (existingBook == null) {
                throw new RegisterLoanException("Le livre avec l'ID " + bookId + " n'a pas été trouvé.");
            }


            // Enregistrer le prêt (ajoutez la logique de prêt ici si nécessaire)
            loanDAO.registerLoan(member, List.of(book));  // Passe une liste contenant un seul livre
        }
    }


    @Override
    public void returnBook(int loanId) throws SQLException {
        // Appeler la méthode de LoanDAOImpl pour mettre à jour la date de retour
        loanDAO.returnBook(loanId);
    }
}

