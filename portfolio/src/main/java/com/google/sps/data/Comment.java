package com.google.sps.data;

/**
 * The Comment class holds the user's comments
 */
public class Comment{
    
    private String name;
    private String content;
    
    public Comment(String name, String content){
        this.name = name;
        this.content = content;
    }

    public String getName(){
        return this.name;
    }

    public void setName(String newName){
        this.name = newName;
    }

    public String getContent(){
        return this.content;
    }

    public void setContent(String newContent){
        this.content = newContent;
    }

}
