package com.google.sps.servlets;

import com.google.appengine.api.blobstore.BlobInfo;
import com.google.appengine.api.blobstore.BlobInfoFactory;
import com.google.appengine.api.blobstore.BlobKey;
import com.google.appengine.api.blobstore.BlobstoreService;
import com.google.appengine.api.blobstore.BlobstoreServiceFactory;
import com.google.appengine.api.images.ImagesService;
import com.google.appengine.api.images.ImagesServiceFactory;
import com.google.appengine.api.images.ServingUrlOptions;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;
import java.util.ArrayList;
import java.util.Map;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import com.google.sps.data.Comment;
import com.google.sps.data.MemePost;
import com.google.appengine.api.users.UserService;
import com.google.appengine.api.users.UserServiceFactory;

/** 
 * The FormHandlerServlet handles the memepost upload
 */ 
@WebServlet("/form-handler")
public class FormHandlerServlet extends HttpServlet {

    @Override
    public void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String description = request.getParameter("description-input");
        String imageUrl = getImageUrl(request, "image-input");

        UserService userService = UserServiceFactory.getUserService();
        String userEmail = userService.getCurrentUser().getEmail();
        MemePost memePost = new MemePost(userEmail, description, imageUrl);
        memePost.putInDatastore();
        response.sendRedirect("/image-upload.html");
    }

    /**
    * Returns a valid image url string  
    */
    public String getImageUrl(HttpServletRequest request, String formName) throws IOException{
        BlobstoreService blobstoreService = BlobstoreServiceFactory.getBlobstoreService();
        Map<String, List<BlobKey>> blobs = blobstoreService.getUploads(request);
        List<BlobKey> blobKeys = blobs.get("image-input");

        if (blobKeys == null || blobKeys.isEmpty()) {
            return null;
        }

        BlobKey blobKey = blobKeys.get(0);

        BlobInfo blobInfo = new BlobInfoFactory().loadBlobInfo(blobKey);
        if (blobInfo.getSize() == 0) {
            blobstoreService.delete(blobKey);
            return null;
        }

        ImagesService imagesService = ImagesServiceFactory.getImagesService();
        ServingUrlOptions options = ServingUrlOptions.Builder.withBlobKey(blobKey);

        try {
            URL url = new URL(imagesService.getServingUrl(options));
            return url.getPath();
        } catch (MalformedURLException e) {
            return imagesService.getServingUrl(options);
        }
    }
}
