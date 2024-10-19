package com.devsync.servlets;

import com.devsync.service.TaskService;
import com.devsync.utils.SessionUtil;
import com.devsync.domain.enums.TaskStatus;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@WebServlet(name = "taskServlet", urlPatterns = {"/tasks"})
public class TaskServlet extends HttpServlet {
    private TaskService taskService;

    @Override
    public void init() throws ServletException {
        taskService = new TaskService();
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        if (!SessionUtil.isUserLoggedIn(req, resp)) {
            resp.sendRedirect(req.getContextPath() + "/login");
            return;
        }
        String action = req.getParameter("action");
        if ("create".equals(action)) {
            req.setAttribute("users", taskService.getUserWhoHaveUserTypeUser());
            req.setAttribute("tags", taskService.getAllTags());
            req.getRequestDispatcher("pages/tasks/create.jsp").forward(req, resp);
        } else {
            req.setAttribute("tasks", taskService.findAll());
            req.setAttribute("users", taskService.findAllUsers());
            req.getRequestDispatcher("/pages/tasks/list.jsp").forward(req, resp);
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String method = req.getParameter("_method");
        try {
            switch (method) {
                case "DELETE":
                    handleDelete(req, resp);
                    break;
                case "UPDATE":
                    handleEdit(req, resp);
                    break;
                case "PUT":
                    handleUpdate(req, resp);
                    break;
                case "UPDATE_STATUS":
                    handleUpdateStatus(req, resp);
                    break;
                case "UPDATE_USER":
                    handleUpdateUser(req, resp);
                    break;
                case "request":
                    handleRequestTask(req, resp);
                    break;
                default:
                    handleSave(req, resp);
                    break;
            }
        } catch (IllegalArgumentException | IllegalStateException e) {
            req.getSession().setAttribute("errorMessage", e.getMessage());
            resp.sendRedirect(req.getContextPath() + "/tasks");
        }
    }

    private void handleDelete(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        Long taskId = Long.parseLong(req.getParameter("id"));
        Long userId = Long.parseLong(req.getParameter("userId"));
        taskService.delete(taskId, userId);
        req.getSession().setAttribute("successMessage", "Task deleted successfully");
        resp.sendRedirect(req.getContextPath() + "/tasks");
    }

    private void handleEdit(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        Long taskId = Long.parseLong(req.getParameter("id"));
        req.setAttribute("task", taskService.findById(taskId));
        req.getRequestDispatcher("/pages/tasks/update.jsp").forward(req, resp);
    }

    private void handleUpdate(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        Long taskId = Long.parseLong(req.getParameter("id"));
        String title = req.getParameter("title");
        String description = req.getParameter("description");
        TaskStatus status = TaskStatus.valueOf(req.getParameter("status"));
        LocalDate dateCreated = LocalDate.parse(req.getParameter("dateCreated"));
        LocalDate dateEnd = LocalDate.parse(req.getParameter("dateEnd"));
        Long userId = Long.parseLong(req.getParameter("userId"));

        taskService.update(taskId, title, description, status, dateCreated, dateEnd, userId);
        resp.sendRedirect(req.getContextPath() + "/tasks");
    }

    private void handleUpdateStatus(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        Long taskId = Long.parseLong(req.getParameter("taskId"));
        TaskStatus newStatus = TaskStatus.valueOf(req.getParameter("newStatus"));
        taskService.updateStatus(taskId, newStatus);
        resp.sendRedirect(req.getContextPath() + "/tasks");
    }

    private void handleUpdateUser(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        Long taskId = Long.parseLong(req.getParameter("task_id"));
        Long userId = Long.parseLong(req.getParameter("user_id"));
        taskService.updateUser(taskId, userId);
        resp.sendRedirect(req.getContextPath() + "/tasks");
    }

    private void handleRequestTask(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        int userId = Integer.parseInt(req.getParameter("userId"));
        int taskId = Integer.parseInt(req.getParameter("id"));
        taskService.requestTask(userId, taskId);
        resp.sendRedirect(req.getContextPath() + "/tasks");
    }

    private void handleSave(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        String title = req.getParameter("title");
        String description = req.getParameter("description");
        TaskStatus status = TaskStatus.valueOf(req.getParameter("status"));
        LocalDate dateEnd = LocalDate.parse(req.getParameter("dateEnd"));
        Long userId = Long.parseLong(req.getParameter("user_id"));
        Long createdByUserId = Long.parseLong(req.getParameter("createdByUser"));

        String[] tagIds = req.getParameterValues("tags[]");
        List<Long> selectedTagIds = tagIds != null ? Arrays.stream(tagIds).map(Long::parseLong).collect(Collectors.toList()) : null;

        taskService.save(title, description, status, dateEnd, userId, createdByUserId, selectedTagIds);

        req.getSession().setAttribute("successMessage", "Task created successfully!");
        resp.sendRedirect(req.getContextPath() + "/tasks");
    }
}