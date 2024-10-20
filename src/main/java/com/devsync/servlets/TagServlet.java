package com.devsync.servlets;

import com.devsync.domain.entities.Tag;
import com.devsync.service.TagService;
import com.devsync.utils.CheckAccess;
import com.devsync.utils.SessionUtil;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

@WebServlet(name = "tagServlet", urlPatterns = {"/tags"})
public class TagServlet extends HttpServlet {

    private TagService tagService;

    @Override
    public void init() throws ServletException {
        tagService = new TagService();
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        if (!CheckAccess.checkAccess(req, resp)) {
            return;
        }
        String action = req.getParameter("action");
        if ("create".equals(action)) {
            req.getRequestDispatcher("pages/tags/create.jsp").forward(req, resp);
            } else {
                tagService.findAll(req, resp);
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
                tagService.delete(req, resp);
                break;
            case "UPDATE":
                tagService.edit(req, resp);
                break;
            case "PUT":
                tagService.update(req, resp);
                break;
            default:
                tagService.save(req, resp);
                break;
        }
    }
}