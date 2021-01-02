package com.sp.SPBrowser.textHandler.model;

import java.util.ArrayList;

public class Post {
    public String text;
    public User createdBy ;
    public long createdAt;
    public ArrayList<String> likedBy = new ArrayList<String>(){};
    public ArrayList<PostComments> commentedBy =new ArrayList<>();
    public  Post(){}

    public Post(String text,User createdBy,long createdAt){
        this.text=text;
        this.createdBy=createdBy;
        this.createdAt = createdAt;

    }

}
