package com.devsync.utils;

import com.devsync.domain.entities.User;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import java.io.IOException;

public class SessionUtil {

    public static boolean isUserLoggedIn(HttpServletRequest request, HttpServletResponse response) throws IOException {
        HttpSession session = request.getSession(false);
        return session != null && session.getAttribute("user") != null;
    }

    public static boolean isManager(HttpServletRequest request, HttpServletResponse response) throws IOException {
        HttpSession session = request.getSession(false);
       // if (session != null) {
            User user = (User) session.getAttribute("user");
        //    if (user != null) {
               return  "MANAGER".equals(user.getUserType().name());
         //   }
       // }
       // return false;
    }





}
