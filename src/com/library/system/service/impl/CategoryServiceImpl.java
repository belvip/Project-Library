package com.library.system.service.impl;

import com.library.system.exception.categoryException.CategoryAlreadyExistsException;
import com.library.system.exception.categoryException.CategoryNotFoundException;
import com.library.system.model.Category;
import com.library.system.repository.CategoryRepository;
import com.library.system.repository.impl.CategoryRepositoryImpl;
import com.library.system.service.CategoryService;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class CategoryServiceImpl implements CategoryService {

    private final CategoryRepository categoryRepository;

    // Constructeur avec injection de la connexion
    public CategoryServiceImpl(Connection connection) {
        this.categoryRepository = new CategoryRepositoryImpl(connection);
    }

    @Override
    public void addCategory(Category category) throws SQLException, CategoryAlreadyExistsException {
        // Vérification si la catégorie existe déjà
        if (categoryRepository.doesCategoryExist(category.getCategory_name())) {
            throw new CategoryAlreadyExistsException("La catégorie existe déjà : " + category.getCategory_name());
        } else {
            categoryRepository.addCategory(category);
        }
    }

    @Override
    public void updateCategory(Category category) throws SQLException {
        categoryRepository.updateCategory(category);
    }

    @Override
    public boolean doesCategoryExist(String categoryName) throws SQLException {
        // Normaliser la casse du nom de la catégorie en minuscules
        String normalizedCategoryName = categoryName.toLowerCase();
        // Appeler la méthode correspondante dans le Repository pour vérifier l'existence de la catégorie
        return categoryRepository.doesCategoryExist(normalizedCategoryName);
    }

    @Override
    public List<Category> findCategoryByKeyword(String keyword) throws SQLException {
        return categoryRepository.findCategoryByKeyword(keyword);
    }

    @Override
    public List<Category> getAllCategories() throws SQLException {
        return categoryRepository.findAllCategories();
    }

    @Override
    public void deleteCategory(int categoryId) throws SQLException, CategoryNotFoundException {
        // Vérifier si la catégorie existe avant de tenter de la supprimer
        if (!categoryRepository.doesCategoryExistById(categoryId)) {
            throw new CategoryNotFoundException("La catégorie avec l'ID " + categoryId + " n'a pas été trouvée.");
        }
        categoryRepository.deleteCategory(categoryId);
    }



    @Override
    public List<Category> findAllCategories() throws SQLException {
        // Utiliser le Repository pour exécuter la requête et obtenir les catégories
        List<Category> categories = categoryRepository.findAllCategories();
        if (categories == null) {
            return new ArrayList<>(); // Si le Repository retourne null, retourner une liste vide
        }
        return categories;
    }
}
