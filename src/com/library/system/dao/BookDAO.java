package com.library.system.dao;

import com.library.system.model.Book;
import com.library.system.exception.bookDaoException.*;

import java.util.List;

/**
 * Interface représentant les opérations de gestion des livres
 * dans le système de gestion de bibliothèque.
 */
public interface BookDAO {

    /**
     * Ajoute un nouveau livre au système.
     *
     * @param book le livre à ajouter.
     * @throws BookAddException si une erreur survient lors de l'ajout.
     */
    void addBook(Book book) throws BookAddException;

    /**
     * Affiche la liste des livres disponibles pour emprunt.
     *
     * @return une liste de livres disponibles.
     * @throws BookDisplayException si une erreur survient lors de l'affichage.
     */
    List<Book> displayAvailableBooks() throws BookDisplayException;

    /**
     * Met à jour les informations d'un livre existant.
     *
     * @param book le livre avec ses nouvelles informations.
     * @throws BookUpdateException si une erreur survient lors de la mise à jour.
     */
    void updateBook(Book book) throws BookUpdateException;

    /**
     * Supprime un livre du système en fonction de son ID.
     *
     * @param bookId l'ID du livre à supprimer.
     * @throws BookRemoveException si une erreur survient lors de la suppression.
     */
    void removeBook(int bookId) throws BookRemoveException;

    /**
     * Vérifie si un livre est disponible pour emprunt.
     *
     * @param bookId l'ID du livre.
     * @return true si le livre est disponible, false sinon.
     * @throws BookAvailabilityException si une erreur survient lors de la vérification.
     */
    boolean isAvailable(int bookId) throws BookAvailabilityException;

    /**
     * Recherche des livres par titre.
     *
     * @param title le titre ou une partie du titre à rechercher.
     * @return une liste de livres correspondant au titre.
     * @throws BookSearchByTitleException si une erreur survient lors de la recherche.
     */
    List<Book> searchBookByTitle(String title) throws BookSearchByTitleException;

    /**
     * Recherche des livres par catégorie.
     *
     * @param categoryName le nom de la catégorie à rechercher.
     * @return une liste de livres correspondant à la catégorie.
     * @throws BookSearchByCategoryException si une erreur survient lors de la recherche.
     */
    List<Book> searchBookByCategory(String categoryName) throws BookSearchByCategoryException;

    /**
     * Recherche un livre par son ID unique.
     *
     * @param bookId l'ID du livre.
     * @return le livre correspondant à l'ID.
     * @throws BookGetByIdException si une erreur survient ou si le livre n'existe pas.
     */
    Book searchBookById(int bookId) throws BookGetByIdException;

    /**
     * Récupère tous les livres dans le système.
     *
     * @return une liste de tous les livres.
     * @throws BookDisplayException si une erreur survient lors de l'affichage.
     */
    List<Book> getAllBooks() throws BookDisplayException;

    /**
     * Marque un livre comme emprunté.
     *
     * @param bookId l'ID du livre à marquer comme emprunté.
     * @throws BookUpdateException si une erreur survient lors de la mise à jour.
     */
    void markAsBorrowed(int bookId) throws BookUpdateException;

    /**
     * Marque un livre comme retourné.
     *
     * @param bookId l'ID du livre à marquer comme retourné.
     * @throws BookUpdateException si une erreur survient lors de la mise à jour.
     */
    void markAsReturned(int bookId) throws BookUpdateException;
}
