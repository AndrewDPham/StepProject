package com.google.sps.servlets;

import java.io.IOException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import com.google.gson.Gson;
import java.util.Arrays;

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
        name = newName;
    }

    public String getContent(){
        return this.content;
    }

    public void setContent(String newContent){
        content = newContent;
    }

}
