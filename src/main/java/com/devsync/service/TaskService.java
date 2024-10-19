package com.devsync.service;

import com.devsync.Repositories.TaskDao;
import com.devsync.Repositories.UserDao;
import com.devsync.domain.entities.Task;
import com.devsync.domain.entities.User;
import com.devsync.domain.entities.Tag;
import com.devsync.domain.enums.TaskStatus;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class TaskService {
    private TaskDao taskDao;
    private UserDao userDao;
    private UserService userService;
    private TagService tagService;

    public TaskService() {
        taskDao = new TaskDao();
        userService = new UserService();
        tagService = new TagService();
        userDao = new UserDao();

        updateTaskStatuses();
    }

    public List<Task> findAll() {
        return taskDao.findAll();
    }

    public List<User> findAllUsers() {
        return userDao.findAll();
    }

    public void save(String title, String description, TaskStatus status, LocalDate dateEnd, Long userId, Long createdByUserId, List<Long> tagIds) throws IllegalArgumentException {
        if (dateEnd.isBefore(LocalDate.now().plusDays(3))) {
            throw new IllegalArgumentException("End date must be at least 3 days from now");
        }

        User user = userService.findById(userId);
        User createdByUser = userService.findById(createdByUserId);

        Task task = new Task();
        task.setTitle(title);
        task.setDescription(description);
        task.setStatus(status);
        task.setDateCreated(LocalDate.now());
        task.setDateEnd(dateEnd);
        task.setUser(user);
        task.setCreatedByUser(createdByUser);

        List<Tag> selectedTags = new ArrayList<>();
        if (tagIds != null) {
            for (Long tagId : tagIds) {
                Tag tag = tagService.findById(tagId);
                selectedTags.add(tag);
            }
        }

        task.setTags(selectedTags);

        taskDao.save(task);
    }

    public Task findById(Long taskId) {
        return taskDao.findById(taskId);
    }

    public void update(Long taskId, String title, String description, TaskStatus status, LocalDate dateCreated, LocalDate dateEnd, Long userId) {
        Task task = new Task();
        task.setId(taskId);
        task.setTitle(title);
        task.setDescription(description);
        task.setStatus(status);
        task.setDateCreated(dateCreated);
        task.setDateEnd(dateEnd);

        taskDao.update(task);
    }

    public void delete(Long taskId, Long userId) throws IllegalStateException {
        User user = userDao.findById(userId);
        Task task = taskDao.findById(taskId);

        if (task == null) {
            throw new IllegalStateException("Task not found");
        }

        if (task.getCreatedByUser().getId().equals(userId)) {
            taskDao.delete(taskId);
        } else {
            if (user.getDeleteTokens() > 0) {
                user.setDeleteTokens(user.getDeleteTokens() - 1);
                userDao.update(user);
                taskDao.delete(taskId);
            } else {
                throw new IllegalStateException("You have no delete tokens left");
            }
        }
    }

    public List<User> getUserWhoHaveUserTypeUser() {
        return userService.getUserWhoHaveUserTypeUser();
    }

    public List<Tag> getAllTags() {
        return tagService.getAllTags();
    }

    public void updateStatus(Long taskId, TaskStatus newStatus) {
        Task task = taskDao.findById(taskId);
        if (task != null) {
            if (task.getDateEnd().isBefore(LocalDate.now()) && task.getStatus() != TaskStatus.DONE) {
                task.setStatus(TaskStatus.OVERDUE);
            } else {
                task.setStatus(newStatus);
            }
            taskDao.update(task);
        }
    }

    public void updateUser(Long taskId, Long userId) {
        Task task = taskDao.findById(taskId);
        User user = userDao.findById(userId);
        if (task != null && user != null) {
            task.setUser(user);
            taskDao.update(task);
        }
    }

    private void updateTaskStatuses() {
        List<Task> tasks = taskDao.findAll();
        LocalDate today = LocalDate.now();

        for (Task task : tasks) {
            LocalDate taskEndDate = task.getDateEnd();
            if (taskEndDate.isBefore(today) && task.getStatus() != TaskStatus.DONE) {
                task.setStatus(TaskStatus.OVERDUE);
            }
            taskDao.update(task);
        }
    }

    public void requestTask(int userId, int taskId) {
        if (userId == 0) {
            return;
        }
        Task task = taskDao.findById((long) taskId);
        task.setRequested(true);
        task.setDateRequested(LocalDateTime.now());
        taskDao.update(task);
    }


}