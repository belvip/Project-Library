package com.library.system.model;

import java.util.HashSet;
import java.util.Set;

public class Category {
    private int category_id; // L'identifiant unique pour la catégorie
    private String category_name;

    // Relation avec les livres : une catégorie peut inclure plusieurs livres
    private Set<Book> books = new HashSet<>();

    // Constructeur sans argument
    public Category() {
    }

    // Constructeur avec arguments
    public Category(String category_name) {
        this.category_name = category_name;
    }

    // Getters et Setters
    public int getCategory_id() {
        return category_id;
    }

    public void setCategory_id(int category_id) {
        this.category_id = category_id;
    }

    public String getCategory_name() {
        return category_name;
    }

    public void setCategory_name(String category_name) {
        this.category_name = category_name;
    }

    public Set<Book> getBooks() {
        return books;
    }

    public void setBooks(Set<Book> books) {
        this.books = books;
    }

    // Ajouter un livre à la catégorie
    public void addBook(Book book) {
        this.books.add(book);
    }

    // Retirer un livre de la catégorie
    public void removeBook(Book book) {
        this.books.remove(book);
    }

    @Override
    public String toString() {
        return "Category{" +
                "category_id=" + category_id +
                ", category_name='" + category_name + '\'' +
                ", books=" + books +
                '}';
    }
}
