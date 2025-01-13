package com.library.system.service;

import com.library.system.model.Author;
import com.library.system.exception.authorException.AuthorNotFoundException;

import java.util.List;

public interface AuthorService {
    Author createAuthor(Author author);
    Author findAuthorById(int author_id) throws AuthorNotFoundException;
    Author findAuthorByEmail(String author_email) throws AuthorNotFoundException;
    List<Author> displayAuthors();
    boolean deleteAuthor(int author_id) throws AuthorNotFoundException;

    Author getAuthorById(int authorId);
}
