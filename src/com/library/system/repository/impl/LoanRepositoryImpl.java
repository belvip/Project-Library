package com.library.system.repository.impl;

import com.library.system.dao.impl.LoanDAOImpl;
import com.library.system.exception.loanException.RegisterLoanException;
import com.library.system.model.Member;
import com.library.system.model.Book;
import com.library.system.repository.LoanRepository;

import java.util.List;
import java.sql.Connection;

public class LoanRepositoryImpl implements LoanRepository {

    private LoanDAOImpl loanDAO;  // Une instance de LoanDAOImpl

    // Constructeur où tu passes la connexion à LoanDAOImpl
    public LoanRepositoryImpl(Connection connection) {
        this.loanDAO = new LoanDAOImpl(connection);  // Initialise LoanDAOImpl avec la connexion
    }

    @Override
    public void registerLoan(Member member, List<Book> books) throws RegisterLoanException {
        // Appeler la méthode registerLoan de LoanDAOImpl
        loanDAO.registerLoan(member, books);
    }
}
