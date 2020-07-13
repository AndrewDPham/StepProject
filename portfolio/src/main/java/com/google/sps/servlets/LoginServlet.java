package com.google.sps.servlets;
 
import com.google.appengine.api.users.UserService;
import com.google.appengine.api.users.UserServiceFactory;
import java.io.IOException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
 
/** 
 * The LoginServlet gives authorization to image-upload.html
 */  
@WebServlet("/login-page")
public class LoginServlet extends HttpServlet {
 
  @Override
    public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        response.setContentType("text/html");
 
        UserService userService = UserServiceFactory.getUserService();
        if (userService.isUserLoggedIn()) {
            response.sendRedirect("/image-upload.html");
        } else {
            String urlToRedirectToAfterUserLogsIn = "/image-upload.html";
            String urlBefore = "/index.html";
            String loginUrl = userService.createLoginURL(urlToRedirectToAfterUserLogsIn);
 
            response.getWriter().println("<p>Hello stranger. Login to see Memes!</p>");
            response.getWriter().println("<p>Login <a href=\"" + loginUrl + "\">here</a>.</p>");
            response.getWriter().println("<p>Click here to go back to the home page <a href=\"" +  urlBefore + "\">here</a>.</p>");
        }
    }
}
 
 

