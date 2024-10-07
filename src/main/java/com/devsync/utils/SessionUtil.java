package com.devsync.utils;

import com.devsync.domain.entities.User;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
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
