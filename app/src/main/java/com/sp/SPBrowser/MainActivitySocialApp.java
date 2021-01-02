package com.sp.SPBrowser;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.Query;
import com.sp.SPBrowser.textHandler.dao.PostDao;
import com.sp.SPBrowser.textHandler.model.Post;

public class MainActivitySocialApp extends AppCompatActivity implements  IPostAdapter,IComment {

    PostDao postDao;
    PostAdapter adapter;
    RecyclerView recyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        overridePendingTransition(android.R.anim.slide_in_left,android.R.anim.slide_out_right);
        setContentView(R.layout.activity_main_social_app);
        recyclerView = findViewById(R.id.recyclerView);
        Log.d("TAG", "onCreate: called ");
        setUpRecyclerView();
        Log.d("TAG", "setUp: called ");
    }

    private void setUpRecyclerView() {
        postDao =new PostDao();Log.d("TAG", "PostDao created ");
        CollectionReference postsCollections = postDao.postCollections;
        Query query = postsCollections.orderBy("createdAt", Query.Direction.DESCENDING);Log.d("TAG", "query executed ");
        FirestoreRecyclerOptions<Post> recyclerViewOptions =new FirestoreRecyclerOptions.Builder<Post>().setQuery(query, Post.class).build();
        Log.d("TAG", "options ");
        adapter =new PostAdapter(recyclerViewOptions, this,this);
        Log.d("TAG", "adapter");
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        Log.d("TAG", "recyclerView set");
    }

    @Override
    protected void onStart() {
        super.onStart();
        adapter.startListening();
        Log.d("TAG", "adapter.startListening ");
    }

    @Override
    protected void onStop() {
        super.onStop();
        adapter.stopListening();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.post_menu,menu);


        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        if (item.getItemId() == R.id.postButton) {
           try {

                this.startActivity(new Intent(MainActivitySocialApp.this, CreatePostActivity.class));
            }catch (Exception e){
               Toast.makeText(MainActivitySocialApp.this, e.toString(), Toast.LENGTH_LONG).show();
           }
        }
        else if(item.getItemId()==R.id.userImage){
           FirebaseAuth auth = FirebaseAuth.getInstance();
            auth.signOut();
            GoogleSignIn.getClient(
                    this,
                    new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN).build()
            ).signOut();
            this.startActivity(new Intent(this,LogInActivity.class));
            finish();
            overridePendingTransition(android.R.anim.slide_out_right,android.R.anim.slide_in_left);
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onLikeClicked(String postId) {
        postDao.updateLikes(postId);
    }

    @Override
    public void onCommentButtonClicked(String postId) {
        Intent intent = new Intent(MainActivitySocialApp.this,CommentActivity.class);
        intent.putExtra("postId",postId);
        this.startActivity(intent);

    }
}