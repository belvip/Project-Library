package com.library.system.model;

/**
 * Représente une catégorie dans le système de bibliothèque.
 */
public class Category {
    private int category_id;           // L'identifiant unique de la catégorie
    private String category_name;      // Le nom de la catégorie

    public Category(int category_id, String category_name) {
        this.category_id = category_id;
        this.category_name = category_name;
    }

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

    // toString pour afficher les informations sur la catégorie
    @Override
    public String toString() {
        return "Category{" +
                "id=" + category_id +
                ", name='" + category_name + '\'' +
                '}';
    }
}
