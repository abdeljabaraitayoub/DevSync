package com.devsync.Repositories;

import com.devsync.domain.entities.Tag;
import com.devsync.domain.entities.Task;
import com.devsync.domain.entities.User;
import com.devsync.domain.enums.TaskStatus;
import com.devsync.domain.enums.UserType;

import java.util.List;

public class Seeder {

    public static void main(String[] args) {
        new Seeder();
    }

    public Seeder() {
        Factory.refreshDb();
        UserDao userDao = new UserDao();
        TaskDao taskDao = new TaskDao();
        TagDao tagDao = new TagDao();

        User user1 = new User();
        user1.setUsername("user1");
        user1.setName("User1");
        user1.setPrenom("User1");
        user1.setEmail("user@gmail.com");
        user1.setPassword("user@gmail.com");
        user1.setUserType(UserType.USER);
        user1.setTokens(2);
        user1.setDeleteTokens(1);
        userDao.save(user1);

        User user2 = new User();
        user2.setUsername("user2");
        user2.setName("User2");
        user2.setPrenom("User2");
        user2.setEmail("admin@gmail.com");
        user2.setPassword("admin@gmail.com");
        user2.setUserType(UserType.MANAGER);
        user2.setTokens(0);
        user2.setDeleteTokens(0);
        userDao.save(user2);


        Tag tag1 = new Tag();
        tag1.setName("Tag1");
        tagDao.save(tag1);

        Tag tag2 = new Tag();
        tag2.setName("Tag2");
        tagDao.save(tag2);

        Task task1 = new Task();
        task1.setTitle("Task1");
        task1.setDescription("Task1");
        task1.setStatus(TaskStatus.IN_PROGRESS);
        task1.setDateCreated(java.time.LocalDate.now());
        task1.setDateEnd(java.time.LocalDate.now().plusDays(5));
        task1.setUser(user1);
        task1.setTags(List.of(tag1, tag2));
        task1.setCreatedByUser(user2);
        taskDao.save(task1);

        Task task2 = new Task();
        task2.setTitle("Task2");
        task2.setDescription("Task2");
        task2.setStatus(TaskStatus.IN_PROGRESS);
        task2.setDateCreated(java.time.LocalDate.now());
        task2.setDateEnd(java.time.LocalDate.now().plusDays(5));
        task1.setTags(List.of(tag1, tag2));
        task2.setUser(user1);
        task2.setCreatedByUser(user2);
        taskDao.save(task2);


    }


}
