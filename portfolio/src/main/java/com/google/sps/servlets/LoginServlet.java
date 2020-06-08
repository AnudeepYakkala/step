package com.google.sps.servlets;

import com.google.appengine.api.users.UserService;
import com.google.appengine.api.users.UserServiceFactory;
import java.io.IOException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@WebServlet("/login")
public class LoginServlet extends HttpServlet {
  private static final String CONTENT_TYPE = "text/html;";
  private static final String LOGIN_REDIRECT = "/"

  @Override
  public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
    response.setContentType(CONTENT_TYPE);

    UserService userService = UserServiceFactory.getUserService();
    if (userService.isUserLoggedIn()) {
      response.getWriter().println("Logged In");
    } else {
      String loginUrl = userService.createLoginURL(LOGIN_REDIRECT);
      response.getWriter().println(loginUrl);
    }
  }
}