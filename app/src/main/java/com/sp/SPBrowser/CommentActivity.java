package com.sp.SPBrowser;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.sp.SPBrowser.textHandler.model.Post;
import com.sp.SPBrowser.textHandler.model.PostComments;

import java.util.ArrayList;
import java.util.Objects;

public class CommentActivity extends AppCompatActivity implements View.OnClickListener {
    String postId ;
    ArrayList<PostComments>  postComments;
    RecyclerView recyclerView;
    CommentAdapter adapter;
    EditText editText;
    TextView userText,postText;
    ImageView userImage,uploaderImage,imageButton;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_comment);
        postId = getIntent().getStringExtra("postId");
        recyclerView = findViewById(R.id.commentRecyclerView);
        editText = findViewById(R.id.Comment);
        imageButton = findViewById(R.id.imageButton);
        userImage = findViewById(R.id.imageView2);
        userText = findViewById(R.id.textView2);
        postText = findViewById(R.id.textView3);
        uploaderImage = findViewById(R.id.postUploaderImage);

        Glide.with(userImage.getContext()).load(Objects.requireNonNull(Objects.requireNonNull(FirebaseAuth.getInstance().getCurrentUser()).getPhotoUrl()).toString()).into(userImage);

         FirebaseFirestore.getInstance().collection("posts").document(postId).addSnapshotListener((value, error) -> {
             if (error!=null){
                 Toast.makeText(CommentActivity.this, error.toString(), Toast.LENGTH_SHORT).show();
                 return;
             }
             assert value != null;
             Post post = value.toObject(Post.class);
             assert post != null;
             postText.setText(post.text);
             userText.setText(post.createdBy.UserName);
             Glide.with(uploaderImage.getContext()).load(post.createdBy.imageUri).into(uploaderImage);
             postComments = post.commentedBy;
             setUpRecyclerView();

         });
imageButton.setOnClickListener(this);

    }

    private void setUpRecyclerView() {
        adapter= new CommentAdapter(postComments);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(adapter);

    }

    @Override
    public void onClick(View v) {if (editText.getText().toString().isEmpty()){
        editText.setError("please comment something");
    }
    else{
       PostComments.addComment(editText.getText().toString(),postId);
    }

    }
}