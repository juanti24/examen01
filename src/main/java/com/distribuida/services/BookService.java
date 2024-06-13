package com.distribuida.services;

import com.distribuida.db.Book;

import java.util.List;

public interface BookService {

    Book findById(Integer id);
    List<Book> findAll();
    void insert(Book b);
    void update(Book b);
    void delete(Integer id);

}
