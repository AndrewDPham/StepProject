package com.google.sps.data;

import com.google.appengine.api.datastore.Entity;
import java.util.Date;

/**
 * The MemePost class holds data for the user's MemePost
 */
public class MemePost{
    
    private String author;
    private String description;
    private String imageUrl;
    private long likes;
    private long dislikes;
    
    public MemePost(String author, String description, String imageUrl){
        this.author = author;
        this.description = description;
        this.imageUrl = imageUrl;
        this.likes = 0;
        this.dislikes = 0;
    }

    public MemePost(Entity entity){
        this.author = (String) entity.getProperty("author");
        this.description = (String) entity.getProperty("description");
        this.imageUrl = (String) entity.getProperty("imageUrl");
        this.likes = 0;
        this.dislikes = 0;
    }

    public MemePost(String author, String description, String imageUrl, long likes, long dislikes){
        this.author = author;
        this.description = description;
        this.imageUrl = imageUrl;
        this.likes = likes;
        this.dislikes = dislikes;
    }

    public String getAuthor(){
        return this.author;
    }

    public void setAuthor(String author){
        this.author = author;
    }

    public String getDescription(){
        return this.description;
    }

    public void setDescription(String description){
        this.description = description;
    }

    public void increaseLikes() {
        this.likes++;
    }

    public void decreaseLikes() {
        this.likes--;
    }

    public void increaseDislikes() {
        this.dislikes++;
    }

    public void decreaseDislikes() {
        this.dislikes--;
    }

    public Entity toDatastoreEntity(){
        Entity memePostEntity = new Entity("MemePost");
        memePostEntity.setProperty("author", this.author);
        memePostEntity.setProperty("description", this.description);
        memePostEntity.setProperty("imageUrl", this.imageUrl);
        memePostEntity.setProperty("likes", this.likes);
        memePostEntity.setProperty("dislikes", this.dislikes);
        return memePostEntity;
    }

}
