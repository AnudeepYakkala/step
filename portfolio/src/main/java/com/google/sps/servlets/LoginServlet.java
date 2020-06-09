package com.google.sps.servlets;

import com.google.appengine.api.users.UserService;
import com.google.appengine.api.users.UserServiceFactory;
import com.google.gson.Gson;
import java.io.IOException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@WebServlet("/login")
public class LoginServlet extends HttpServlet {
  private static final String CONTENT_TYPE = "text/html;";
  private static final String LOGIN_REDIRECT = "/";
  private static final String LOGOUT_REDIRECT = "/";

  private class UserLogin {
    private boolean isLoggedIn;
    private String loginUrl;
    private String logoutUrl;

    public UserLogin(boolean isLoggedIn, String loginUrl, String logoutUrl) {
      this.isLoggedIn = isLoggedIn;
      this.loginUrl = loginUrl;
      this.logoutUrl = logoutUrl;
    }
  }

  /**
   * Check if a user is logged in. Return an object containg
   * the login status, login link, and logout link.
   */
  @Override
  public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
    UserService userService = UserServiceFactory.getUserService();
    String loginUrl = userService.createLoginURL(LOGIN_REDIRECT);
    String logoutUrl = userService.createLogoutURL(LOGOUT_REDIRECT);
    UserLogin userLogin;

    if (userService.isUserLoggedIn()) {
      userLogin = new UserLogin(true, loginUrl, logoutUrl);
    } else {
      userLogin = new UserLogin(false, loginUrl, logoutUrl);
    }
    Gson gson = new Gson();
    response.setContentType(CONTENT_TYPE);
    response.getWriter().println(gson.toJson(userLogin));
  }
}
