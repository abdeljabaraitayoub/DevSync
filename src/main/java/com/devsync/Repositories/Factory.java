package com.devsync.Repositories;

import com.devsync.domain.entities.Tag;
import com.devsync.domain.entities.Task;
import com.devsync.domain.entities.User;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;
import jakarta.persistence.Query;

import java.util.Arrays;
import java.util.List;

public class Factory {
    private static EntityManagerFactory instance = null;
    private static final String PERSISTENCE_UNIT_NAME = "default";

    public static void main(String[] args) {
        Factory.refreshDb();
    }

    private Factory() {
    }

    public static EntityManagerFactory getInstance() {
        if (instance == null) {
            instance = Persistence.createEntityManagerFactory("default");
        }
        return instance;
    }

    public static void refreshDb() {
        EntityManagerFactory emf = getInstance();
        EntityManager em = emf.createEntityManager();
        try {
            em.getTransaction().begin();

            // Order matters here due to foreign key constraints
            List<String> tablesToClear = Arrays.asList(
                    "task_tags",
                    "tags",
                    "tasks",
                    "users"
            );

            for (String tableName : tablesToClear) {
                Query query = em.createNativeQuery("TRUNCATE TABLE " + tableName + " CASCADE");
                query.executeUpdate();
            }

            // Reset sequences
            List<String> sequencesToReset = Arrays.asList(
                    "tags_id_seq",
                    "tasks_id_seq",
                    "users_id_seq"
            );

            for (String sequenceName : sequencesToReset) {
                Query query = em.createNativeQuery("ALTER SEQUENCE " + sequenceName + " RESTART WITH 1");
                query.executeUpdate();
            }

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

    public static void close() {
        if (instance != null) {
            instance.close();
        }
    }
}
