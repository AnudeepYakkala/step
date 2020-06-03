// Copyright 2019 Google LLC
//
// Licensed under the Apache License, Version 2.0 (the "License");
// you may not use this file except in compliance with the License.
// You may obtain a copy of the License at
//
//     https://www.apache.org/licenses/LICENSE-2.0
//
// Unless required by applicable law or agreed to in writing, software
// distributed under the License is distributed on an "AS IS" BASIS,
// WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
// See the License for the specific language governing permissions and
// limitations under the License.

package com.google.sps.servlets;

import com.google.appengine.api.datastore.DatastoreService;
import com.google.appengine.api.datastore.DatastoreServiceFactory;
import com.google.appengine.api.datastore.Entity;
import com.google.appengine.api.datastore.PreparedQuery;
import com.google.appengine.api.datastore.Query;
import com.google.appengine.api.datastore.Query.SortDirection;
import com.google.gson.Gson;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/** Servlet that returns some example content. TODO: modify this file to handle comments data */
@WebServlet("/data")
public class DataServlet extends HttpServlet {
  /**
   * Write to /data the messages ArrayList as a json string
   */
  @Override
  public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
    // Get comments data from Datastore
    Query query = new Query("Comment").addSort("timestamp", SortDirection.DESCENDING);
    DatastoreService datastore = DatastoreServiceFactory.getDatastoreService();
    PreparedQuery results = datastore.prepare(query);

    // Add the comments obtained from Datastore to the comments Arraylist
    ArrayList<String> comments = new ArrayList<>();
    for (Entity entity : results.asIterable()) {
      String comment = (String) entity.getProperty("text");
      comments.add(comment);
    }

    Gson gson = new Gson();

    // Convert comments to json format and return them
    response.setContentType("text/html;");
    response.getWriter().println(gson.toJson(comments));
  }

  /**
   * Obtain the input from the comment form and add it to the messages ArrayList
   */
  @Override
  public void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
    // Get the comment input from the form.
    String comment = getParameter(request, "text-input", /* default value= */ "");
    long timestamp = System.currentTimeMillis();

    // Create entity with comment information
    Entity commentEntity = new Entity("Comment");
    commentEntity.setProperty("text", comment);
    commentEntity.setProperty("timestamp", timestamp);

    // Store comment in Datastore
    DatastoreService datastore = DatastoreServiceFactory.getDatastoreService();
    datastore.put(commentEntity);

    // Redirect back to comments section of the home page
    response.sendRedirect("index.html#comments-container");
  }

  /**
   * @return the ArrayList paramater converted to a json format
   */
  public String convertToJson(ArrayList<String> messages) {
    Gson gson = new Gson();
    String json = gson.toJson(messages);
    return json;
  }

  /**
   * @return the request parameter, or the default value if the parameter
   *         was not specified by the client
   */
  private String getParameter(HttpServletRequest request, String name, String defaultValue) {
    String value = request.getParameter(name);
    if (value == null) {
      return defaultValue;
    }
    return value;
  }
}
