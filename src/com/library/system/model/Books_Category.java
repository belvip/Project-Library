package com.library.system.model;

import com.library.system.exception.bookException.InvalidBookIdException;
import com.library.system.exception.bookException.InvalidCategoryIdException;

public class Books_Category {
    private int book_id;
    private String category_id;

    public Books_Category() {
    }

    public Books_Category(int book_id, String category_id) {
        setBook_id(book_id); // Validation incluse
        setCategory_id(category_id); // Validation incluse
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

    public String getCategory_id() {
        return category_id;
    }

    public void setCategory_id(String category_id) {
        if (category_id == null || category_id.isEmpty()) {
            throw new InvalidCategoryIdException("L'identifiant de la catégorie ne peut pas être vide.");
        }
        this.category_id = category_id;
    }

    @Override
    public String toString() {
        return "Books_Category{" +
                "bookId=" + book_id +
                ", categoryId='" + category_id + '\'' +
                '}';
    }
}
