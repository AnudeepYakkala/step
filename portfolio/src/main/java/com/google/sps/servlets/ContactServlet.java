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
public class ContactServlet extends HttpServlet {
  private static final String NAME_VALUE = "name";
  private static final String MESSAGE_VALUE = "message";
  private static final String EMAIL_VALUE = "email";
  private static final String CONTACT_TIMESTAMP = "timestamp";
  private static final String CONTACT_KIND = "Contact";

  /**
   * Obtain the information from the contact form and store
   * it in Datastore. Then, redirect back to the homepage.
   */
  @Override
  public void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
    String message = getParameter(request, MESSAGE_VALUE).get();
    String name = getParameter(request, NAME_VALUE).get();
    String email = getParameter(request, EMAIL_VALUE).get();

    Entity contactEntity = new Entity(CONTACT_KIND);
    contactEntity.setProperty(NAME_VALUE, name);
    contactEntity.setProperty(EMAIL_VALUE, email);
    contactEntity.setProperty(MESSAGE_VALUE, message);
    contactEntity.setProperty(CONTACT_TIMESTAMP, System.currentTimeMillis());

    DatastoreService datastore = DatastoreServiceFactory.getDatastoreService();
    datastore.put(contactEntity);

    response.sendRedirect("index.html");
  }

  /**
   * @return the request parameter, or the default value if the parameter
   *         was not specified by the client.
   */
  private Optional<String> getParameter(HttpServletRequest request, String param) {
    return Optional.ofNullable(request.getParameter(param));
  }
}