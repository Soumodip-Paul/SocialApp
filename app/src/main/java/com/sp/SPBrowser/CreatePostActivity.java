package com.sp.SPBrowser;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.sp.SPBrowser.textHandler.dao.PostDao;

public class CreatePostActivity extends AppCompatActivity {
    Button postButton;
    EditText editText;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        overridePendingTransition(android.R.anim.fade_in,android.R.anim.fade_out);
        setContentView(R.layout.activity_create_post);
        postButton = findViewById(R.id.button);
        editText = findViewById(R.id.editTextPost);
        PostDao  postDao = new PostDao();
        postButton.setOnClickListener(v -> {
            String input = editText.getText().toString();
            Toast.makeText(this,"",Toast.LENGTH_LONG).show();
            if(!input.isEmpty()){
                postDao.addPost(input);
                this.finish();
                overridePendingTransition(android.R.anim.fade_out,android.R.anim.fade_in);
            }
        });
    }
}