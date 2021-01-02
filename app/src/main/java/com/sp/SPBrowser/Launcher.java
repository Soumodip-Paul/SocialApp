package com.sp.SPBrowser;

import androidx.appcompat.app.AppCompatActivity;

import android.Manifest;
import android.content.Intent;
import android.os.Bundle;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;

import java.util.Objects;

public class Launcher extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_launcher);
        Objects.requireNonNull(getSupportActionBar()).hide();
        PermissionClass.onRequest(this, Manifest.permission.WRITE_EXTERNAL_STORAGE,"Storage","permission required to access storage",1);
        PermissionClass.onRequest(this, Manifest.permission.READ_EXTERNAL_STORAGE,"Storage","permission required to access storage",2);
        PermissionClass.onRequest(this, Manifest.permission.GET_ACCOUNTS,"Storage","permission required to access your accounts",3);
        Thread splash = new Thread(){
            @Override
            public void run() {
                try {
                    ImageView imageView = findViewById(R.id.imageView);
                    Animation animation= AnimationUtils.loadAnimation(getApplicationContext(),R.anim.zoom_in);
                    imageView.startAnimation(animation);
                    sleep(2*1000);
                    Intent intent = new Intent(Launcher.this,LogInActivity.class);
                    startActivity(intent);
                    finish();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        };
        splash.start();
    }

    @Override
    public void onBackPressed() {

    }
}