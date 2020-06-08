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
import com.google.common.collect.Iterables;
import com.google.gson.Gson;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Optional;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@WebServlet("/data")
public class DataServlet extends HttpServlet {
  private static final String COMMENT_KIND = "Comment";
  private static final String COMMENT_TIMESTAMP = "timestamp";
  private static final String CONTENT_TYPE = "text/html;";
  private static final String COMMENT_VALUE = "text";
  private static final String DEFAULT_MAX_COMMENTS = "20";

  /**
   * Get comments from Datastore and add them to an ArrayList. Convert
   * the ararylist to a json format and return it.
   */
  @Override
  public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
    Query query = new Query(COMMENT_KIND).addSort(COMMENT_TIMESTAMP, SortDirection.DESCENDING);
    DatastoreService datastore = DatastoreServiceFactory.getDatastoreService();
    PreparedQuery results = datastore.prepare(query);
    int maxComments =
        Integer.parseInt(getParameter(request, "max-comments").orElse(DEFAULT_MAX_COMMENTS));

    ArrayList<String> comments = new ArrayList<>();
    for (Entity entity : Iterables.limit(results.asIterable(), maxComments)) {
      String comment = (String) entity.getProperty(COMMENT_VALUE);
      comments.add(comment);
    }

    response.setContentType(CONTENT_TYPE);
    response.getWriter().println(convertMessageToJson(comments));
  }

  /**
   * Obtain the input from the comment form and add it to the messages ArrayList.
   * Create an entity with the comment text and store it in Datastore. Then,
   * redirect to the comments section of the home page.
   */
  @Override
  public void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
    Optional<String> comment = getParameter(request, "text-input");
    if (comment.isPresent()) {
      Entity commentEntity = new Entity(COMMENT_KIND);
      commentEntity.setProperty(COMMENT_VALUE, comment.get());
      commentEntity.setProperty(COMMENT_TIMESTAMP, System.currentTimeMillis());

      DatastoreService datastore = DatastoreServiceFactory.getDatastoreService();
      datastore.put(commentEntity);
    }

    response.sendRedirect("index.html#comments-container");
  }

  /**
   * @return the ArrayList paramater converted to a json format.
   */
  public String convertMessageToJson(ArrayList<String> messages) {
    Gson gson = new Gson();
    String json = gson.toJson(messages);
    return json;
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
