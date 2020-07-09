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
import com.google.appengine.api.datastore.DatastoreService;
import com.google.appengine.api.datastore.DatastoreServiceFactory;
import com.google.appengine.api.datastore.Entity;
import com.google.appengine.api.datastore.PreparedQuery;
import com.google.appengine.api.datastore.Query;
import com.google.appengine.api.datastore.Query.SortDirection;
import com.google.sps.data.Comment;
import com.google.sps.data.MemePost;

/** 
 * The DataServlet is the servlet which handles GET and POST requests to display 
 * and update comments.
 */
@WebServlet("/data")
public class DataServlet extends HttpServlet {

    private static int numberOfShowedComments = 3;    

    /**
     * Queries the entities in Datastore and sends the response in JSON format
     */
    @Override
    public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        ArrayList<Comment> comments = new ArrayList<>();
        int index = 0;

        Query query = new Query("Comment");
        DatastoreService datastore = DatastoreServiceFactory.getDatastoreService();
        PreparedQuery results = datastore.prepare(query);
        for (Entity entity : results.asIterable()) {
            if(index < numberOfShowedComments){
                index++;
            } else{
                break;
            }
            String name = (String) entity.getProperty("name");
            String content = (String) entity.getProperty("content");
            comments.add(new Comment(name, content));
        }

        // Gson gson = new Gson();
        // String json = gson.toJson(comments);
        response.setContentType("application/json");
        response.getWriter().println(convertToJsonUsingGson(comments));
    }

    /**
     * Obtain parameters of the HTML form and creates an entity in Datastore.
     */
    public void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
        ArrayList<Integer> errors = new ArrayList<>();
        String name = getParameter(request, "name-input", "");
        String content = getParameter(request, "text-input", "");
        boolean owo = getBooleanParameter(request, "owo", false);
        boolean uwu = getBooleanParameter(request, "uwu", false);
        DataServlet.numberOfShowedComments = getIntParameter(request, errors, "num-input", 3);
        
        String commentConcatenated = owoUwuTransformer(content, owo, uwu);

        if(errors.size() == 0){
            Entity commentEntity = new Entity("Comment");
            commentEntity.setProperty("name", name);
            commentEntity.setProperty("content", commentConcatenated);
            DatastoreService datastore = DatastoreServiceFactory.getDatastoreService();
            datastore.put(commentEntity);
            response.sendRedirect("/index.html");
        } else {
            response.sendError(errors.get(0));
        }
    }

    public String owoUwuTransformer(String content, boolean owo, boolean uwu){
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
        return commentConcatenated;
    }

    /**
     * Helper method to convert an ArrayList of Comments to JSON format
     * @param ArrayList<Comment> comments to be converted
     * @return JSON as a String
     */
    private String convertToJsonUsingGson(ArrayList<Comment> comments) {
        Gson gson = new Gson();
        String json = gson.toJson(comments);
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

    /**
     * @return the request parameter as an int, or the default value if the parameter
     *         was not specified by the client
     */
    private int getIntParameter(HttpServletRequest request, ArrayList<Integer> errors, String userValue, int defaultValue) throws IOException {
        String toParse = request.getParameter(userValue);
        if ("".equals(toParse) || toParse == null) {
            return defaultValue;
        }
        int value = defaultValue;
        try{ 
            value = Integer.parseInt(toParse);
        } catch (NumberFormatException nfe) {
            errors.add(400);
            return defaultValue;
        } catch (Exception e){
            errors.add(500);
            return defaultValue;
        }    
        if (value < 0) {
            return defaultValue;
        }
        return value;
    }

    /**
     * @return the request parameter as a boolean, or the default value if the parameter
     *         was not specified by the client
     */
    private Boolean getBooleanParameter(HttpServletRequest request, String userValue, boolean defaultValue) {
        String toParse = request.getParameter(userValue);
        if (toParse == null || !("true".equals(toParse.toLowerCase()))) {
            return defaultValue;
        }
        return Boolean.parseBoolean(toParse);
    }
}
