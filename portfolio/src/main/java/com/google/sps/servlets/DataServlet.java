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

import java.io.IOException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import com.google.gson.Gson;
import java.util.Arrays;

/** Servlet that returns some example content. TODO: modify this file to handle comments data */
@WebServlet("/data")
public class DataServlet extends HttpServlet {

    private Comment userComment;
    private final ArrayList<String> comments = new ArrayList<>();

  @Override
  public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
    
    Gson gson = new Gson();
    String json = gson.toJson(comments);
    response.setContentType("application/json");
    response.getWriter().println(json);
    
    // Tutorial Steps Below:
    // ArrayList<String> messages = new ArrayList<String>();
    // messages.add("Alpha");
    // messages.add("Beta");
    // messages.add("Charlie");
    
    // String json = convertToJsonUsingGson(messages);

    // response.setContentType("application/json");
    // response.getWriter().println(json);

    //response.setContentType("text/html;");
    //response.getWriter().println("Hello Andrew");
  }

  public void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
    String name = getParameter(request, "name-input", "");
    String content = getParameter(request, "text-input", "");
    boolean owo = Boolean.parseBoolean(getParameter(request, "owo", "false"));
    boolean uwu = Boolean.parseBoolean(getParameter(request, "uwu", "false"));

    String[] commentWords = content.split(" ", 0);
    String commentConcatenated = "";
    for(String word : commentWords){
        if(owo){
            word += "owo";
        } else if (uwu){
            word += "uwu";
        } 
        commentConcatenated += word + " ";
    }

    userComment = new Comment(name, commentConcatenated);
    comments.add(userComment.getName() + " said " + userComment.getContent());
    response.sendRedirect("/index.html");

//    Tutorial Steps Below:
//    response.setContentType("text/html");
//    response.getWriter().println(Array.toString(commentWords));
  }


private String convertToJsonUsingGson(ArrayList<String> messages) {
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