package com.library.system.exception;

import com.library.system.exception.categoryException.CategoryAlreadyExistsException;
import com.library.system.exception.categoryException.CategoryNotFoundException;
import com.library.system.exception.categoryException.InvalidCategoryNameException;

public class GlobalException {

    // Méthode pour gérer les exceptions de type Category
    public static void handleCategoryException(Exception e) {
        if (e instanceof CategoryAlreadyExistsException) {
            System.out.println("Erreur: La catégorie existe déjà.");
        } else if (e instanceof InvalidCategoryNameException) {
            System.out.println("Erreur: Le nom de la catégorie est invalide.");
        } else if (e instanceof CategoryNotFoundException) {
            System.out.println("Erreur: Catégorie non trouvée.");
        } else {
            System.out.println("Une erreur inattendue est survenue avec la catégorie.");
        }
    }

    // Méthode générale pour gérer toutes les exceptions
    public static void handleGeneralException(Exception e) {
        System.out.println("Une erreur est survenue: " + e.getMessage());
    }
}
