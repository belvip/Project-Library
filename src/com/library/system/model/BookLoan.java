package com.library.system.model;

import com.library.system.exception.bookException.InvalidBookIdException;
import com.library.system.exception.loanException.InvalidLoanIdException;

public class BookLoan {
    private int bookId;
    private int loanId;  // Lien avec un prêt, et non avec un membre

    // Constructeur avec paramètres
    public BookLoan(int bookId, int loanId) {
        setBookId(bookId);
        setLoanId(loanId);
    }

    // Constructeur par défaut
    public BookLoan() {
    }

    // Getter et Setter pour bookId
    public int getBookId() {
        return bookId;
    }

    public void setBookId(int bookId) {
        if (bookId <= 0) {
            throw new InvalidBookIdException("L'identifiant du livre doit être positif.");
        }
        this.bookId = bookId;
    }

    // Getter et Setter pour loanId
    public int getLoanId() {
        return loanId;
    }

    public void setLoanId(int loanId) {
        if (loanId <= 0) {
            throw new InvalidLoanIdException("L'identifiant du prêt doit être positif.");
        }
        this.loanId = loanId;
    }

    // Méthode pour ajouter un livre à l'objet BookLoan
    public void addBook(Book book) {
        if (book != null) {
            this.bookId = book.getBook_id();
        } else {
            throw new IllegalArgumentException("Le livre ne peut pas être nul.");
        }
    }

    // Affichage correct de la classe
    @Override
    public String toString() {
        return "BookLoan{" +
                "bookId=" + bookId +
                ", loanId=" + loanId +
                '}';
    }
}
