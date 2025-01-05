package com.library.system.repository;

import com.library.system.exception.loanException.RegisterLoanException;
import com.library.system.model.Book;

import java.util.List;

public interface LoanRepository {

    void registerLoan(int memberId, List<Book> bookId) throws RegisterLoanException;
}
