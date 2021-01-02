package com.sp.SPBrowser.textHandler.model;

import com.google.firebase.auth.FirebaseAuth;
import com.sp.SPBrowser.textHandler.dao.PostDao;
import com.sp.SPBrowser.textHandler.dao.UserDao;

import java.util.Objects;

public class PostComments {
    public String text;
    public User user;
    public String postId;
    public long createdAt;
    public PostComments(){}

    public PostComments(String text, User user, String postId,long createdAt) {
        this.text = text;
        this.user = user;
        this.postId= postId;
        this.createdAt=createdAt;
    }
    public static void addComment(String comment,String postId){
        UserDao userDao = new UserDao();
        userDao.getUserById(Objects.requireNonNull(FirebaseAuth.getInstance().getCurrentUser()).getUid()).addOnSuccessListener(documentSnapshot -> {
            User user = documentSnapshot.toObject(User.class);
            PostComments postComments = new PostComments(comment,user,postId,System.currentTimeMillis());
            PostDao postDao = new PostDao();
            postDao.getPostById(postId).addOnSuccessListener(documentSnapshot1 -> {
                Post post = Objects.requireNonNull(documentSnapshot1.toObject(Post.class));
                post.commentedBy.add(postComments);
                postDao.postCollections.document(postId).set(post);
                });
        });
    }
}
