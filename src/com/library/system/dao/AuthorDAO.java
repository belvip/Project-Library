package com.library.system.dao;

import com.library.system.model.Author;

import java.sql.SQLException;

public interface AuthorDAO {
    // Ajouter un auteur
    void createAuthor(Author author) throws SQLException;

}
