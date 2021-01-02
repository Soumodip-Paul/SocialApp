package com.sp.SPBrowser.textHandler.dao;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.sp.SPBrowser.textHandler.model.Post;
import com.sp.SPBrowser.textHandler.model.PostComments;
import com.sp.SPBrowser.textHandler.model.User;

import java.util.Objects;


public class PostDao {
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    public CollectionReference postCollections = db.collection("posts");
    FirebaseAuth  auth = FirebaseAuth.getInstance();


    public void addPost( String text) {
            String currentUserId = Objects.requireNonNull(auth.getCurrentUser()).getUid();
            UserDao userDao =new UserDao();
            userDao.getUserById(currentUserId)
            .addOnSuccessListener(documentSnapshot -> {
                    long currentTime = System.currentTimeMillis();
                    User user = documentSnapshot.toObject(User.class);
                    Post post =new Post(text, user,currentTime);
                    postCollections.document().set(post);
            });

    }

    public Task<DocumentSnapshot> getPostById(String postId) {
        return postCollections.document(postId).get();
    }

  public void updateLikes(String postId) {

            String currentUserId = Objects.requireNonNull(auth.getCurrentUser()).getUid();
            getPostById(postId).addOnSuccessListener(documentSnapshot -> {
             Post post = documentSnapshot.toObject(Post.class);
              assert post != null;
              boolean isLiked = post.likedBy.contains(currentUserId);

              if(isLiked) {
                  post.likedBy.remove(currentUserId);
              } else {
                  post.likedBy.add(currentUserId);
              }
              postCollections.document(postId).set(post);

          });

        }
    public void addComment(String text,String postId){
        String currentUserId = Objects.requireNonNull(auth.getCurrentUser()).getUid();
        UserDao userDao =new UserDao();
        userDao.getUserById(currentUserId)
                .addOnSuccessListener(documentSnapshot -> {
                    long currentTime = System.currentTimeMillis();
                    User user = documentSnapshot.toObject(User.class);
                     new PostDao().getPostById(postId).addOnSuccessListener(documentSnapshot1 -> {
                         Post post = documentSnapshot1.toObject(Post.class);
                         PostComments postComments = new PostComments(text,user,postId,currentTime);
                         post.commentedBy.add(postComments);
                         postCollections.document(postId).set(post);
                     });
                });
    }

    }


