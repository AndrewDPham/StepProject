package com.google.sps.servlets;

import com.google.appengine.api.blobstore.BlobstoreService;
import com.google.appengine.api.blobstore.BlobstoreServiceFactory;
import java.io.IOException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import com.google.appengine.api.datastore.DatastoreService;
import com.google.appengine.api.datastore.DatastoreServiceFactory;
import com.google.appengine.api.datastore.Entity;
import com.google.appengine.api.datastore.PreparedQuery;
import com.google.appengine.api.datastore.Query;
import com.google.appengine.api.datastore.Query.SortDirection;
import com.google.gson.Gson;
import com.google.sps.data.Comment;
import com.google.sps.data.MemePost;

/**
 * When the fetch() function requests the /blobstore-upload-url URL, the content of the response is
 * the URL that allows a user to upload a file to Blobstore. If this sounds confusing, try running a
 * dev server and navigating to /blobstore-upload-url to see the Blobstore URL.
 */
@WebServlet("/display-memepost")
public class DisplayMemePostServlet extends HttpServlet {

    @Override
    public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        ArrayList<MemePost> memePosts = new ArrayList<>();

        Query query = new Query("MemePost");
        DatastoreService datastore = DatastoreServiceFactory.getDatastoreService();
        PreparedQuery results = datastore.prepare(query);
        for (Entity entity : results.asIterable()) {
            String author = (String) entity.getProperty("author");
            String description = (String) entity.getProperty("description");
            String imageUrl = (String) entity.getProperty("imageUrl");
            // long likes = entity.getProperty("likes");
            // long dislikes =  entity.getProperty("dislikes");
            // Date date = (Date) entity.getProperty("date");
            memePosts.add(new MemePost(author, description, imageUrl));
        }

        Gson gson = new Gson();
        String json = gson.toJson(memePosts);
        response.setContentType("application/json");
        response.getWriter().println(json);
    }
}
