package com.library.system.model;

public class Category {
    private int category_id;
    private String category_name;

    // Constructeur sans argument
    public Category() {
    }

    // Constructeur avec arguments
    public Category(String category_name) {
        //this.category_id = category_id;
        this.category_name = category_name;
    }


    // Getter et Setter
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
}
