package com.library.system.dao.impl;

import com.library.system.dao.BookDAO;
import com.library.system.model.Book;
import com.library.system.exception.bookDaoException.*;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class BookDAOImpl implements BookDAO {

    // Une simple liste en mémoire pour simuler une base de données (remplacer par un DAO avec JDBC ou JPA dans un cas réel)
    private List<Book> books = new ArrayList<>();

    @Override
    public void addBook(Book book) throws BookAddException {
        try {
            // Vérifier si le livre existe déjà avant de l'ajouter (par exemple, selon l'ID ou le titre)
            if (books.stream().anyMatch(b -> b.getTitle().equals(book.getTitle()))) {
                throw new BookAddException("Le livre avec ce titre existe déjà.");
            }
            books.add(book);
        } catch (Exception e) {
            throw new BookAddException("Erreur lors de l'ajout du livre : " + e.getMessage());
        }
    }

    @Override
    public List<Book> displayAvailableBooks() throws BookDisplayException {
        try {
            // Filtrer les livres qui sont disponibles (par exemple, en fonction d'un attribut isAvailable)
            return books.stream()
                    .filter(Book::isAvailable) // Suppose qu'un livre a un attribut isAvailable
                    .collect(Collectors.toList());
        } catch (Exception e) {
            throw new BookDisplayException("Erreur lors de l'affichage des livres disponibles : " + e.getMessage());
        }
    }

    @Override
    public void updateBook(Book book) throws BookUpdateException {
        try {
            // Vérifier si le livre existe déjà dans la liste
            Book existingBook = books.stream()
                    .filter(b -> b.getBook_id() == book.getBook_id())
                    .findFirst()
                    .orElseThrow(() -> new BookUpdateException("Le livre avec cet ID n'existe pas."));

            // Mettre à jour les informations du livre
            existingBook.setTitle(book.getTitle());
            existingBook.setNumber_Of_Copies(book.getNumber_Of_Copies());
        } catch (Exception e) {
            throw new BookUpdateException("Erreur lors de la mise à jour du livre : " + e.getMessage());
        }
    }

    @Override
    public void removeBook(int bookId) throws BookRemoveException {
        try {
            // Chercher le livre par son ID
            Book bookToRemove = books.stream()
                    .filter(b -> b.getBook_id() == bookId)
                    .findFirst()
                    .orElseThrow(() -> new BookRemoveException("Livre non trouvé pour la suppression."));

            // Supprimer le livre
            books.remove(bookToRemove);
        } catch (Exception e) {
            throw new BookRemoveException("Erreur lors de la suppression du livre : " + e.getMessage());
        }
    }

    @Override
    public boolean isAvailable(Book book) throws BookAvailabilityException {
        try {
            // Vérifier la disponibilité du livre (par exemple, en utilisant un attribut isAvailable)
            return book.getNumber_Of_Copies() > 0; // Si le nombre de copies est supérieur à 0, le livre est disponible
        } catch (Exception e) {
            throw new BookAvailabilityException("Erreur lors de la vérification de la disponibilité du livre : " + e.getMessage());
        }
    }

    @Override
    public List<Book> searchBookByTitle(String title) throws BookSearchByTitleException {
        try {
            // Rechercher les livres dont le titre correspond à la recherche
            return books.stream()
                    .filter(book -> book.getTitle().contains(title))
                    .collect(Collectors.toList());
        } catch (Exception e) {
            throw new BookSearchByTitleException("Erreur lors de la recherche du livre par titre : " + e.getMessage());
        }
    }

    @Override
    public List<Book> searchBookByCategory(String categoryName) throws BookSearchByCategoryException {
        try {
            // Rechercher les livres selon la catégorie (en supposant que la relation entre Book et Category est gérée)
            return books.stream()
                    .filter(book -> book.getCategories().stream()
                            .anyMatch(category -> category.getCategory_name().equals(categoryName)))
                    .collect(Collectors.toList());
        } catch (Exception e) {
            throw new BookSearchByCategoryException("Erreur lors de la recherche du livre par catégorie : " + e.getMessage());
        }
    }
}
