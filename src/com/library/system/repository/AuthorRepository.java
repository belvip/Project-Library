package com.library.system.repository;

import com.library.system.model.Author;
import com.library.system.exception.authorException.AuthorNotFoundException;

import java.util.List;

public interface AuthorRepository {
    Author createAuthor(Author author);
    Author findAuthorById(int author_id) throws AuthorNotFoundException;
    Author findAuthorByEmail(String author_email) throws AuthorNotFoundException;
    List<Author> displayAuthors();
    boolean deleteAuthor(int author_id) throws AuthorNotFoundException;
}
