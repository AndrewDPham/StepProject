package com.google.sps.servlets;
 
import com.google.appengine.api.users.UserService;
import com.google.appengine.api.users.UserServiceFactory;
import java.io.IOException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import com.google.gson.Gson;

/** 
 * The AuthStatusServlet gives authentication to image-upload.html
 */ 
@WebServlet("/auth-status")
public class AuthStatusServlet extends HttpServlet {
 
  @Override
    public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        response.setContentType("text/html");
 
        UserService userService = UserServiceFactory.getUserService();
        if (!userService.isUserLoggedIn()) {
            response.sendRedirect("/login-page");
        } else {
            String userEmail = userService.getCurrentUser().getEmail();
            String urlToRedirectToAfterUserLogsOut = "/login-page";
            String logoutUrl = userService.createLogoutURL(urlToRedirectToAfterUserLogsOut);
 
            String json = "{\"logoutUrl\": \"" + logoutUrl + "\", \"userEmail\": \"" + userEmail + "\"}";
            System.out.println(json);
            response.setContentType("application/json");
            response.getWriter().println(json);
        }
    }
}
