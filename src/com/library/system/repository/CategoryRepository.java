package com.library.system.repository;

import com.library.system.model.Category;

import java.sql.SQLException;
import java.util.List;

public interface CategoryRepository {

    void addCategory(Category category) throws SQLException;

    void updateCategory(Category category) throws SQLException;

    List<Category> findCategoryByKeyword(String keyword) throws SQLException;

    List<Category> findAllCategories() throws SQLException;

    void deleteCategory(int categoryId) throws SQLException;

    Category findById(int categoryId) throws SQLException;

    boolean doesCategoryExist(String categoryName) throws SQLException;
    boolean doesCategoryExistById(int categoryId) throws SQLException;
}
