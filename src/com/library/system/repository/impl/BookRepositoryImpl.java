package com.library.system.repository.impl;

import com.library.system.dao.BookDAO;
import com.library.system.dao.impl.BookDAOImpl;
import com.library.system.model.Book;
import com.library.system.repository.BookRepository;

import java.util.List;

public class BookRepositoryImpl implements BookRepository {
    private BookDAO bookDAO; // Nom de la variable corrigé

    // Constructeur
    public BookRepositoryImpl() {
        this.bookDAO = new BookDAOImpl(); // Initialisation de l'instance DAO
    }

    @Override
    public void addBook(Book book) {
        bookDAO.addBook(book); // Utilisation de bookDAO
    }

    @Override
    public List<Book> displayAvailableBooks() {
        return bookDAO.displayAvailableBooks(); // Utilisation de bookDAO
    }

    @Override
    public void updateBook(Book book) {
        bookDAO.updateBook(book); // Utilisation de bookDAO
    }

    @Override
    public void removeBook(int bookId) {
        bookDAO.removeBook(bookId); // Utilisation de bookDAO
    }

    @Override
    public boolean isAvailable(int bookId) {
        return bookDAO.isAvailable(bookId); // Utilisation de bookDAO
    }

    @Override
    public List<Book> searchBookByTitle(String title) {
        return bookDAO.searchBookByTitle(title); // Utilisation de bookDAO
    }

    @Override
    public List<Book> searchBookByCategory(String category) {
        return bookDAO.searchBookByCategory(category); // Utilisation de bookDAO
    }
}
