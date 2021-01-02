package com.sp.SPBrowser.textHandler.dao;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.sp.SPBrowser.textHandler.model.User;

public class UserDao {
    private final FirebaseFirestore db = FirebaseFirestore.getInstance();
    private final CollectionReference usersCollection = db.collection("users");

    public void addUser(@NonNull User user) {
        usersCollection.document(user.uId).set(user);
    }
    public Task<DocumentSnapshot> getUserById(String uId) {
        return usersCollection.document(uId).get();
    }
}
