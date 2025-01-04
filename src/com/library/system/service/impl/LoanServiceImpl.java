package com.library.system.service.impl;

import com.library.system.repository.LoanRepository;
import com.library.system.service.LoanService;
import com.library.system.exception.loanException.RegisterLoanException;
import com.library.system.model.Member;
import com.library.system.model.Book;
import java.util.List;

public class LoanServiceImpl implements LoanService {

    private LoanRepository loanRepository;  // Dépendance à LoanRepository

    // Constructeur pour initialiser LoanRepository
    public LoanServiceImpl(LoanRepository loanRepository) {
        this.loanRepository = loanRepository;  // Injection de LoanRepository
    }

    @Override
    public void registerLoan(Member member, List<Book> books) throws RegisterLoanException {
        // Appeler la méthode registerLoan de LoanRepository
        loanRepository.registerLoan(member, books);
    }
}
