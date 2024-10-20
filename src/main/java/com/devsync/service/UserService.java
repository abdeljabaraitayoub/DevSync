package com.devsync.service;

import com.devsync.Repositories.UserDao;
import com.devsync.domain.entities.User;
import com.devsync.domain.enums.UserType;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.util.List;

public class UserService {

    private UserDao userDao;

    public UserService() {
        userDao = new UserDao();
    }

    public void findAll(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        List<User> users = userDao.findAll();
        req.setAttribute("users", users);
        req.getRequestDispatcher("/pages/users/list.jsp").forward(req, resp);
    }

    public User findById(Long userId) {
        return userDao.findById(userId);
    }

    public void save(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        req.setCharacterEncoding("UTF-8");
        String username = req.getParameter("username");
        String name = req.getParameter("name");
        String prenom = req.getParameter("prenom");
        String email = req.getParameter("email");
        String password = req.getParameter("password");
        String userType = req.getParameter("userType");

        User user = new User();
        user.setUsername(username);
        user.setName(name);
        user.setPrenom(prenom);
        user.setEmail(email);
        user.setPassword(password);
        user.setUserType(UserType.valueOf(userType.toUpperCase()));
        if (user.getUserType() == UserType.MANAGER) {
            user.setTokens(0);
        } else {
            user.setTokens(2);
            user.setDeleteTokens(1);
        }


        userDao.save(user);
        resp.sendRedirect(req.getContextPath() + "/users");
    }

    public void edit(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        Long userId = Long.parseLong(req.getParameter("id"));
        User user = userDao.findById(userId);
        if (user != null) {
            req.setAttribute("user", user);
            req.getRequestDispatcher("/pages/users/update.jsp").forward(req, resp);
        }
    }

    public void update(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        Long userId = Long.parseLong(req.getParameter("id"));
        String username = req.getParameter("username");
        String name = req.getParameter("name");
        String prenom = req.getParameter("prenom");
        String email = req.getParameter("email");
        String password = req.getParameter("password");
        String userType = req.getParameter("userType");

        User user = new User();
        user.setId(userId);
        user.setUsername(username);
        user.setName(name);
        user.setPrenom(prenom);
        user.setEmail(email);
        user.setPassword(password);
        user.setUserType(UserType.valueOf(userType.toUpperCase()));

        userDao.update(user);
        resp.sendRedirect(req.getContextPath() + "/users");
    }


    public void delete(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        Long userId = Long.parseLong(req.getParameter("id"));
        userDao.delete(userId);
        // req.setAttribute("successDeleteMessage", "User deleted successfully!");
        resp.sendRedirect(req.getContextPath() + "/users");
    }


    public List<User> getUserWhoHaveUserTypeUser() {
        return userDao.getUserWhoHaveUserTypeUser();
    }
}
