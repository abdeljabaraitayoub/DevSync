package com.devsync.servlets;

import com.devsync.service.UserService;
import com.devsync.utils.CheckAccess;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;

@WebServlet(name = "users", urlPatterns = {"/users"})
public class UserServlet extends HttpServlet {

    private UserService userService;


    @Override
    public void init() throws ServletException {
        userService = new UserService();
    }


    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        if (!CheckAccess.checkAccess(req, resp)) {
            return;
        }
        String action = req.getParameter("action");
        if ("create".equals(action)) {
            req.getRequestDispatcher("pages/users/create.jsp").forward(req, resp);
        } else {
            userService.findAll(req, resp);
        }
    }


    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        if (!CheckAccess.checkAccess(req, resp)) {
            return;
        }

        String method = req.getParameter("_method");

        switch (method) {
            case "DELETE":
                userService.delete(req, resp);
                break;
            case "UPDATE":
                userService.edit(req, resp);
                break;
            case "PUT":
                userService.update(req, resp);
                break;
            default:
                userService.save(req, resp);
                break;
        }

    }


}