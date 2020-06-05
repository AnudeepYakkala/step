package com.google.sps.servlets;

import com.google.appengine.api.datastore.DatastoreService;
import com.google.appengine.api.datastore.DatastoreServiceFactory;
import com.google.appengine.api.datastore.Entity;
import java.io.IOException;
import java.util.Optional;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@WebServlet("/contact")
public class ContactServerlet extends HttpServlet {
  /**
   * Obtain the information from the contact form and store
   * it in Datastore. Then, redirect back to the homepage.
   */
  @Override
  public void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
    String message = getParameter(request, "message").get();
    String name = getParameter(request, "name").get();
    String email = getParameter(request, "email").get();

    Entity contactEntity = new Entity("Contact");
    contactEntity.setProperty("name", name);
    contactEntity.setProperty("email", email);
    contactEntity.setProperty("message", message);
    contactEntity.setProperty("timestamp", System.currentTimeMillis());

    DatastoreService datastore = DatastoreServiceFactory.getDatastoreService();
    datastore.put(contactEntity);

    response.sendRedirect("index.html");
  }

  /**
   * @return the request parameter, or the default value if the parameter
   *         was not specified by the client.
   */
  private Optional<String> getParameter(HttpServletRequest request, String param) {
    String value = request.getParameter(param);
    return Optional.ofNullable(value);
  }
}