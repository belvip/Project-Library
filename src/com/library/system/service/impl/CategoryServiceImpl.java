package com.library.system.service.impl;

import com.library.system.dao.CategoryDAO;
import com.library.system.model.Category;
import com.library.system.service.CategoryService;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

public class CategoryServiceImpl implements CategoryService {

    private final CategoryDAO categoryDAO;

    public CategoryServiceImpl(Connection connection, CategoryDAO categoryDAO) {
        this.categoryDAO = categoryDAO;
    }

    @Override
    public void addCategory(Category category) throws SQLException {
        categoryDAO.addCategory(category);
    }

    @Override
    public void updateCategory(Category category) throws SQLException {
        categoryDAO.updateCategory(category);  // Appel de la méthode updateCategory dans CategoryDAO
    }


    @Override
    public Category getOrCreateCategory(String categoryName) throws SQLException {
        return categoryDAO.getOrCreateCategory(categoryName);
    }

    @Override
    public List<Category> findCategoryByKeyword(String keyword) throws SQLException {
        return categoryDAO.findCategoryByKeyword(keyword);
    }

    @Override
    public List<Category> getAllCategories() throws SQLException {
        return categoryDAO.findAllCategories();
    }

    @Override
    public void deleteCategory(int categoryId) throws SQLException {
        categoryDAO.deleteCategory(categoryId);
    }


}
