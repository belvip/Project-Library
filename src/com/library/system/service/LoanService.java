
package com.library.system.service;

import com.library.system.model.Member;
import com.library.system.model.Book;
import com.library.system.exception.loanException.RegisterLoanException;

import java.sql.SQLException;
import java.util.List;

public interface LoanService {
    void registerLoan(Member member, List<Book> books) throws RegisterLoanException;

    void returnBook(int loanId) throws SQLException;
}
