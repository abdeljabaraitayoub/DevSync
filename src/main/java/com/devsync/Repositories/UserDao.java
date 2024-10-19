package com.devsync.Repositories;


import com.devsync.domain.entities.User;
import com.devsync.domain.enums.UserType;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.TypedQuery;

import java.util.List;

public class UserDao {

    private static final EntityManagerFactory emf = Factory.getInstance();

    public static void main(String[] args) {
        UserDao userDao = new UserDao();
//        userDao.updateTokens();
        User user = new User();
        user.setUsername("user");
        user.setName("user");
        user.setPrenom("user");
        user.setEmail("user@gmail.com");
        user.setPassword("user123");
        user.setUserType(UserType.USER);
        user.setTokens(2);
        user.setDeleteTokens(1);


        User admin = new User();
        admin.setUsername("admin");
        admin.setName("admin");
        admin.setPrenom("admin");
        admin.setEmail("admin@gmail.com");
        admin.setPassword("admin123");
        admin.setUserType(UserType.MANAGER);
        admin.setTokens(2);
        admin.setDeleteTokens(1);

        userDao.save(user);
        userDao.save(admin);
    }


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


    public void delete(Long id) {
        EntityManager em = emf.createEntityManager();
        em.getTransaction().begin();

        User user = em.find(User.class, id);
        if (user != null) {
            em.remove(user);
        }

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

    public User findById(Long userId) {
        EntityManager em = emf.createEntityManager();
        User user = em.find(User.class, userId);
        em.close();
        return user;
    }

    public List<User> getUserWhoHaveUserTypeUser() {
        EntityManager em = emf.createEntityManager();
        TypedQuery<User> query = em.createQuery("SELECT u FROM User u WHERE u.userType = 'USER'", User.class);
        List<User> users = query.getResultList();
        em.close();
        return users;
    }

    public User findByEmail(String email) {
        EntityManager em = emf.createEntityManager();
        TypedQuery<User> query = em.createQuery("SELECT u FROM User u WHERE u.email = :email", User.class);
        query.setParameter("email", email);
        List<User> users = query.getResultList();
        em.close();
        return users.isEmpty() ? null : users.get(0);
    }

    public void refreshTokensEachDay() {
        EntityManager em = emf.createEntityManager();
        em.getTransaction().begin();
        em.createQuery("UPDATE User u SET u.tokens = 2").executeUpdate();
        em.getTransaction().commit();
    }

  
}