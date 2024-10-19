package com.devsync;

import com.devsync.Repositories.Seeder;
import com.devsync.Repositories.UserDao;
import com.devsync.domain.entities.Task;
import com.devsync.domain.entities.User;
import com.devsync.domain.enums.TaskStatus;
import com.devsync.service.TaskService;
import org.junit.jupiter.api.*;

import java.time.LocalDate;
import java.util.List;

public class TaskTests {
    static TaskService taskService;
    static UserDao userDao;

    @BeforeEach
    void init() {
        new Seeder();
        taskService = new TaskService();
        userDao = new UserDao();
    }

    @Test
    public void getTasksTest() {
        List<Task> tasks = taskService.findAll();
        Assertions.assertEquals(2, tasks.size());
    }

    @Test
    public void createTaskTest() {
        User user = userDao.findById(1L);
        taskService.save("Task 3", "Description 3", TaskStatus.TODO, LocalDate.now().plusDays(3), user.getId(), user.getId(), null);
        List<Task> tasks = taskService.findAll();
        Assertions.assertEquals(3, tasks.size());
    }

    @Test
    public void createTaskWithTagsTest() {
        User user = userDao.findById(1L);
        List<Long> tagIds = List.of(1L, 2L);
        taskService.save("Task 3", "Description 3", TaskStatus.TODO, LocalDate.now().plusDays(3), user.getId(), user.getId(), tagIds);
        List<Task> tasks = taskService.findAll();
        Assertions.assertEquals(3, tasks.size());
        Task createdTask = tasks.get(2);
        Assertions.assertEquals(2, createdTask.getTags().size());
    }

    @Test
    public void createTaskWithInvalidEndDateTest() {
        User user = userDao.findById(1L);
        List<Long> tagIds = List.of(1L, 2L);
        Assertions.assertThrows(IllegalArgumentException.class, () -> {
            taskService.save("Task 3", "Description 3", TaskStatus.TODO, LocalDate.now().plusDays(1), user.getId(), user.getId(), tagIds);
        });
    }

    @Test
    public void updateTaskTest() {
        Task task = taskService.findById(1L);
        String newTitle = "Updated Task";
        taskService.update(task.getId(), newTitle, task.getDescription(), task.getStatus(), task.getDateCreated(), task.getDateEnd(), task.getUser().getId());
        Task updatedTask = taskService.findById(1L);
        Assertions.assertEquals(newTitle, updatedTask.getTitle());
    }

    @Test
    public void deleteTaskByCreatorTest() {
        User user = userDao.findById(1L);
        Task task = taskService.findById(1L);
        Assertions.assertDoesNotThrow(() -> taskService.delete(task.getId(), user.getId()));
        Assertions.assertEquals(1, taskService.findAll().size());
    }


    @Test
    public void updateTaskStatusTest() {
        Task task = taskService.findById(1L);
        TaskStatus newStatus = TaskStatus.IN_PROGRESS;
        taskService.updateStatus(task.getId(), newStatus);
        Task updatedTask = taskService.findById(1L);
        Assertions.assertEquals(newStatus, updatedTask.getStatus());
    }

    @Test
    public void updateTaskUserTest() {
        Task task = taskService.findById(1L);
        User newUser = userDao.findById(2L);
        taskService.updateUser(task.getId(), newUser.getId());
        Task updatedTask = taskService.findById(1L);
        Assertions.assertEquals(newUser.getId(), updatedTask.getUser().getId());
    }

    @Test
    public void requestTaskTest() {
        Task task = taskService.findById(1L);
        Assertions.assertFalse(task.isRequested());
        taskService.requestTask(2, 1);
        Task updatedTask = taskService.findById(1L);
        Assertions.assertTrue(updatedTask.isRequested());
    }

}