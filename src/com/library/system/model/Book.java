package com.library.system.model;

import com.library.system.exception.bookException.InvalidBookTitleException;
import com.library.system.exception.bookException.InvalidNumberOfCopiesException;

import java.util.HashSet;
import java.util.Set;

public class Book {
    private int book_id;
    private String title;
    private int number_Of_Copies;
    private String authorFullName;

    // Relations
    private Set<Author> authors = new HashSet<>();  // Un livre peut avoir plusieurs auteurs
    private Set<Category> categories = new HashSet<>(); // Un livre peut appartenir à plusieurs catégories

    // Constructeurs
    public Book() {
    }

    public Book(int book_id, String title, int number_Of_Copies) {
        this.book_id = book_id;
        setTitle(title); // Validation incluse
        setNumber_Of_Copies(number_Of_Copies); // Validation incluse
    }

    // Getter et setter pour le nom complet de l'auteur
    public String getAuthorFullName() {
        return authorFullName;
    }

    public void setAuthorFullName(String authorFullName) {
        this.authorFullName = authorFullName;
    }


    // Getters et Setters
    public int getBook_id() {
        return book_id;
    }

    public void setBook_id(int book_id) {
        this.book_id = book_id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        if (title == null || title.trim().isEmpty()) {
            throw new InvalidBookTitleException("Le titre du livre ne peut pas être vide ou null.");
        }
        this.title = title;
    }

    public int getNumber_Of_Copies() {
        return number_Of_Copies;
    }

    public void setNumber_Of_Copies(int number_Of_Copies) {
        if (number_Of_Copies < 0) {
            throw new InvalidNumberOfCopiesException("Le nombre d'exemplaires ne peut pas être négatif.");
        }
        this.number_Of_Copies = number_Of_Copies;
    }

    public Set<Author> getAuthors() {
        return authors;
    }

    public void addAuthor(Author author) {
        if (author != null) {
            authors.add(author);
        }
    }

    public void removeAuthor(Author author) {
        authors.remove(author);
    }

    public Set<Category> getCategories() {
        return categories;
    }

    public void addCategory(Category category) {
        if (category != null) {
            categories.add(category);
        }
    }

    public void removeCategory(Category category) {
        categories.remove(category);
    }

    @Override
    public String toString() {
        return "Book{" +
                "id=" + book_id +
                ", title='" + title + '\'' +
                ", numberOfCopies=" + number_Of_Copies +
                ", authors=" + authors +
                ", categories=" + categories +
                '}';
    }

    public boolean isAvailable() {
        return number_Of_Copies > 0;
    }

    public void setAuthors(Set<Author> authors) {
        this.authors = authors;
    }

    public void setCategories(Set<Category> categories) {
        this.categories = categories;  // Ajout de cette méthode
    }
    public void setAvailable(boolean available) {
        // Implémentez la logique de mise à jour de l'état de disponibilité du livre.
        if (available) {
            // Si vous souhaitez remettre le livre à disposition, vous pourriez augmenter le nombre de copies.
            // Vous pouvez ajouter votre logique ici si nécessaire.
            this.number_Of_Copies++;
        } else {
            // Si vous souhaitez marquer le livre comme emprunté, vous pourriez diminuer le nombre de copies.
            // Vous pouvez ajouter votre logique ici si nécessaire.
            if (this.number_Of_Copies > 0) {
                this.number_Of_Copies--;
            }
        }
    }

}
