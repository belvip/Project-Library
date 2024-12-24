package com.library.system.dao;

import com.library.system.model.Book;
import com.library.system.exception.bookDaoException.*;

import java.util.List;

public interface BookDAO {

    /**
     * Ajoute un livre dans la base de données.
     * @param book Le livre à ajouter.
     * @throws BookAddException Si l'ajout échoue.
     */
    void addBook(Book book) throws BookAddException;

    /**
     * Affiche tous les livres disponibles (non empruntés).
     * @return La liste des livres disponibles.
     * @throws BookDisplayException Si l'affichage échoue.
     */
    List<Book> displayAvailableBooks() throws BookDisplayException;

    /**
     * Met à jour les informations d'un livre existant.
     * @param book Le livre à mettre à jour.
     * @throws BookUpdateException Si la mise à jour échoue.
     */
    void updateBook(Book book) throws BookUpdateException;

    /**
     * Supprime un livre de la base de données.
     * @param bookId L'ID du livre à supprimer.
     * @throws BookRemoveException Si la suppression échoue.
     */
    void removeBook(int bookId) throws BookRemoveException;

    /**
     * Vérifie si un livre est disponible.
     * @param book Le livre à vérifier.
     * @return True si le livre est disponible, false sinon.
     * @throws BookAvailabilityException Si la vérification échoue.
     */
    boolean isAvailable(Book book) throws BookAvailabilityException;

    /**
     * Recherche un livre par son titre.
     * @param title Le titre du livre à rechercher.
     * @return La liste des livres correspondant au titre.
     * @throws BookSearchByTitleException Si la recherche échoue.
     */
    List<Book> searchBookByTitle(String title) throws BookSearchByTitleException;

    /**
     * Recherche un livre par sa catégorie.
     * @param categoryName Le nom de la catégorie du livre.
     * @return La liste des livres correspondant à la catégorie.
     * @throws BookSearchByCategoryException Si la recherche échoue.
     */
    List<Book> searchBookByCategory(String categoryName) throws BookSearchByCategoryException;

    boolean isAvailable(int bookId) throws BookAvailabilityException;
}
