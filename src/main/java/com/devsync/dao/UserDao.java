package com.devsync.dao;


import com.devsync.domain.entities.User;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;
import jakarta.persistence.TypedQuery;

import java.util.List;

public class UserDao {

    private static final EntityManagerFactory emf = Persistence.createEntityManagerFactory("default");




    public List<User> findAll() {
        EntityManager em = emf.createEntityManager();
        TypedQuery<User> query = em.createQuery("SELECT u FROM User u", User.class);
        List<User> users = query.getResultList();
        em.close();
        return users;

    }

    public void save(User user) {
        EntityManager em = emf.createEntityManager();
        em.getTransaction().begin();
        em.persist(user);
        em.getTransaction().commit();
        em.close();
    }


    public void delete(User user) {
        EntityManager em = emf.createEntityManager();
        em.getTransaction().begin();
        em.remove(user);
        em.getTransaction().commit();
        em.close();

    }


    public void update(User user) {
        EntityManager em = emf.createEntityManager();
        em.getTransaction().begin();
        em.merge(user);
        em.getTransaction().commit();
        em.close();
    }
}