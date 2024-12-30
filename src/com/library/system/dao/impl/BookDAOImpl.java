package com.library.system.dao.impl;

import com.library.system.dao.BookDAO;
import com.library.system.model.Book;
import com.library.system.exception.bookDaoException.*;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class BookDAOImpl implements BookDAO {

    private List<Book> books;

    public BookDAOImpl() {
        this.books = new ArrayList<>();
    }

    @Override
    public void addBook(Book book) throws BookAddException {
        try {
            books.add(book);
        } catch (Exception e) {
            throw new BookAddException("Erreur lors de l'ajout du livre : " + book.getTitle(), e);
        }
    }

    @Override
    public List<Book> displayAvailableBooks() throws BookDisplayException {
        try {
            return books.stream()
                    .filter(Book::isAvailable)
                    .collect(Collectors.toList());
        } catch (Exception e) {
            throw new BookDisplayException("Erreur lors de l'affichage des livres disponibles.", e);
        }
    }

    @Override
    public void updateBook(Book book) throws BookUpdateException {
        try {
            Book existingBook = searchBookById(book.getBook_id());
            existingBook.setTitle(book.getTitle());
            existingBook.setAuthors(book.getAuthors());
            existingBook.setCategories(book.getCategories());  // Changer de setCategory à setCategories
        } catch (Exception e) {
            throw new BookUpdateException("Erreur lors de la mise à jour du livre avec l'ID : ");
        }
    }


    @Override
    public void removeBook(int bookId) throws BookRemoveException {
        try {
            Book bookToRemove = searchBookById(bookId);
            books.remove(bookToRemove);
        } catch (Exception e) {
            throw new BookRemoveException("Erreur lors de la suppression du livre avec l'ID : " + bookId, e);
        }
    }

    public boolean isAvailable(Book book) throws BookAvailabilityException {
        try {
            return books.contains(book) && book.isAvailable();
        } catch (Exception e) {
            throw new BookAvailabilityException("Erreur lors de la vérification de la disponibilité du livre.", e);
        }
    }

    @Override
    public List<Book> searchBookByTitle(String title) throws BookSearchByTitleException {
        try {
            return books.stream()
                    .filter(book -> book.getTitle().equalsIgnoreCase(title))
                    .collect(Collectors.toList());
        } catch (Exception e) {
            throw new BookSearchByTitleException("Erreur lors de la recherche du livre avec le titre : " + title, e);
        }
    }

    @Override
    public List<Book> searchBookByCategory(String category_name) throws BookSearchByCategoryException {
        try {
            return books.stream()
                    .filter(book -> book.getCategories().stream()
                            .anyMatch(category -> category.getCategory_name().equalsIgnoreCase(category_name)))  // Comparaison du nom de la catégorie
                    .collect(Collectors.toList());
        } catch (Exception e) {
            throw new BookSearchByCategoryException("Erreur lors de la recherche de livres dans la catégorie : " + category_name, e);
        }
    }


    @Override
    public Book searchBookById(int bookId) throws BookGetByIdException {
        return books.stream()
                .filter(book -> book.getBook_id() == bookId)
                .findFirst()
                .orElseThrow(() -> new BookGetByIdException("Livre avec l'ID " + bookId + " introuvable."));
    }

    @Override
    public List<Book> getAllBooks() throws BookDisplayException {
        try {
            return new ArrayList<>(books);
        } catch (Exception e) {
            throw new BookDisplayException("Erreur lors de l'affichage de tous les livres.", e);
        }
    }

    @Override
    public void markAsBorrowed(int bookId) throws BookUpdateException {
        try {
            Book book = searchBookById(bookId);
            if (!book.isAvailable()) {
                throw new BookUpdateException("Le livre avec l'ID " + bookId + " est déjà emprunté.");
            }
            book.setAvailable(false);
        } catch (Exception e) {
            throw new BookUpdateException("Erreur lors de la mise à jour de l'état du livre avec l'ID : " + bookId, e);
        }
    }

    @Override
    public void markAsReturned(int bookId) throws BookUpdateException {
        try {
            Book book = searchBookById(bookId);
            if (book.isAvailable()) {
                throw new BookUpdateException("Le livre avec l'ID " + bookId + " est déjà disponible.");
            }
            book.setAvailable(true);
        } catch (Exception e) {
            throw new BookUpdateException("Erreur lors de la mise à jour de l'état du livre avec l'ID : " + bookId, e);
        }
    }

    @Override
    public boolean isAvailable(int bookId) throws BookAvailabilityException {
        try {
            // Recherche le livre par son ID
            Book book = searchBookById(bookId);
            // Retourne l'état de disponibilité du livre
            return book.isAvailable();
        } catch (BookGetByIdException e) {
            throw new BookAvailabilityException("Erreur lors de la vérification de la disponibilité du livre avec l'ID : " + bookId, e);
        }
    }
}
