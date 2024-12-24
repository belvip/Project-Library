package com.library.system.model;

import com.library.system.exception.authorException.InvalidAuthorIdException;
import com.library.system.exception.bookException.InvalidBookIdException;

public class Book_Author {
    private int book_id;
    private int author_id;

    // Constructeurs
    public Book_Author() {
    }

    public Book_Author(int book_id, int author_id) {
        setBook_id(book_id); // Validation incluse
        setAuthor_id(author_id); // Validation incluse
    }

    public int getBook_id() {
        return book_id;
    }

    public void setBook_id(int book_id) {
        if (book_id <= 0) {
            throw new InvalidBookIdException("L'identifiant du livre doit être positif.");
        }
        this.book_id = book_id;
    }

    public int getAuthor_id() {
        return author_id;
    }

    public void setAuthor_id(int author_id) {
        if (author_id <= 0) {
            throw new InvalidAuthorIdException("L'identifiant de l'auteur doit être positif.");
        }
        this.author_id = author_id;
    }

    @Override
    public String toString() {
        return "Book_Author{" +
                "bookId=" + book_id +
                ", authorId=" + author_id +
                '}';
    }
}
