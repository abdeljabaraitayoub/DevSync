package com.devsync.Repositories;

import com.devsync.domain.entities.Task;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Query;
import jakarta.persistence.TypedQuery;

import java.time.LocalDateTime;
import java.util.List;

public class TaskDao {

    private static final EntityManagerFactory emf = Factory.getInstance();

    public static void main(String[] args) {
        TaskDao taskDao = new TaskDao();
        taskDao.tokenSoldIfManagerNotAnswer();
    }

    public List<Task> findAll() {
        EntityManager em = emf.createEntityManager();
        TypedQuery<Task> query = em.createQuery("SELECT t FROM Task t", Task.class);
        List<Task> tasks = query.getResultList();
        em.close();
        return tasks;
    }

    public void save(Task task) {
        EntityManager em = emf.createEntityManager();
        em.getTransaction().begin();
        em.persist(task);
        em.getTransaction().commit();
        em.close();
    }

    public void delete(Long id) {
        EntityManager em = emf.createEntityManager();
        em.getTransaction().begin();

        Task task = em.find(Task.class, id);
        if (task != null) {
            em.remove(task);
        }

        em.getTransaction().commit();
        em.close();
    }

    public void update(Task task) {
        EntityManager em = emf.createEntityManager();
        em.getTransaction().begin();
        em.merge(task);
        em.getTransaction().commit();
        em.close();
    }

    public Task findById(Long taskId) {
        EntityManager em = emf.createEntityManager();
        Task task = em.find(Task.class, taskId);
        em.close();
        return task;
    }

    public void refresh(Task task) {
        EntityManager em = emf.createEntityManager();
        em.getTransaction().begin();
        em.refresh(task);
        em.getTransaction().commit();
        em.close();
    }

    public void tokenSoldIfManagerNotAnswer() {
        EntityManager em = emf.createEntityManager();
        try {
            em.getTransaction().begin();

            LocalDateTime twelveHoursAgo = LocalDateTime.now().minusHours(12);

            String jpql = "UPDATE User u " +
                    "SET u.tokens = u.tokens * 2 " +
                    "WHERE EXISTS (SELECT 1 FROM Task t " +
                    "              WHERE t.user = u " +
                    "              AND t.dateRequested < :twelveHoursAgo " +
                    "              AND t.isRequested = true)";

            Query query = em.createQuery(jpql);
            query.setParameter("twelveHoursAgo", twelveHoursAgo);

            int updatedCount = query.executeUpdate();

            em.getTransaction().commit();
        } catch (Exception e) {
            if (em.getTransaction().isActive()) {
                em.getTransaction().rollback();
            }
            e.printStackTrace();
        } finally {
            em.close();
        }
    }

}