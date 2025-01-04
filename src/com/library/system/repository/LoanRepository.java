package com.library.system.repository;

import com.library.system.exception.loanException.RegisterLoanException;
import com.library.system.model.Book;
import com.library.system.model.Member;

import java.util.List;

public interface LoanRepository {
    void registerLoan(Member member, List<Book> books) throws RegisterLoanException;
}
