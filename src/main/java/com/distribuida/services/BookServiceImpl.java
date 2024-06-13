package com.distribuida.services;

import com.distribuida.db.Book;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;

import java.util.List;

@ApplicationScoped
public class BookServiceImpl implements BookService{

    @Inject
    EntityManager em;

    @Override
    public Book findById(Integer id) {
        return em.find(Book.class,id);
    }

    @Override
    public List<Book> findAll() {
        return em
                .createQuery("select b from Book b ", Book.class).getResultList();
    }

    @Override
    public void insert(Book b) {
        em.getTransaction().begin();
        em.persist(b);
        em.getTransaction().commit();
    }

    @Override
    public void update(Book b) {
        em.getTransaction().begin();
        em.merge(b);
        em.getTransaction().commit();
    }

    @Override
    public void delete(Integer id) {
        Book b = this.findById(id);
        em.getTransaction().begin();
        em.remove(b);
        em.getTransaction().commit();
    }

}
