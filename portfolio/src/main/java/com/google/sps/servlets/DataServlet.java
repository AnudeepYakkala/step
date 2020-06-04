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
import com.google.gson.Gson;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Optional;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/** Servlet that returns some example content. TODO: modify this file to handle comments data */
@WebServlet("/data")
public class DataServlet extends HttpServlet {
  private ArrayList<String> messages = new ArrayList<>();

  /**
   * Write to /data the messages ArrayList as a json string.
   */
  @Override
  public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
    String json = convertMessageToJson(messages);
    response.setContentType("text/html;");
    response.getWriter().println(json);
  }

  /**
   * Obtain the input from the comment form and add it to the messages ArrayList.
   */
  @Override
  public void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
    // Get the comment input from the form.
    String comment = getParameter(request, "text-input").orElse("");
    messages.add(comment);

    // Respond with the result.
    response.setContentType("text/html;");
    response.getWriter().println(convertMessageToJson(messages));
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
    if (value == null) {
      return Optional.empty();
    }
    return Optional.of(value);
  }
}
