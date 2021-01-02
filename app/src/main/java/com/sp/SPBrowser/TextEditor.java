package com.sp.SPBrowser;

import android.Manifest;
import android.annotation.SuppressLint;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.Menu;
import android.view.MenuItem;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStreamReader;
import java.util.Objects;

public class TextEditor extends AppCompatActivity {
    EditText editText;
    String textContent;
    Uri uri;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_text_editor);
        editText = findViewById(R.id.textView);
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
        PermissionClass.onRequest(this, Manifest.permission.WRITE_EXTERNAL_STORAGE,"PERMISSION","permission required to read your file",1);
        getSupportActionBar().setDisplayShowCustomEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayUseLogoEnabled(true);
        getSupportActionBar().setLogo(R.mipmap.ic_launcher_round);
        getSupportActionBar().setTitle(R.string.app_name);
        Animation animation = AnimationUtils.loadAnimation(getApplicationContext(),R.anim.slide);
        editText.startAnimation(animation);
        uri = getIntent().getData();
        textContent =readFile(getRealPath(uri));
        editText.setText(textContent);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.textview_menu,menu);
        return super.onCreateOptionsMenu(menu);
    }

    @SuppressLint("NonConstantResourceId")
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()){
            case R.id.textSave:
                Save();
                finish();
                break;
            case R.id.cancel:
                if (textContent.equals(editText.getText().toString())){
                    finish();
                }
                else{
                  new AlertDialog.Builder(TextEditor.this)
                          .setTitle("Do you want to Save")
                          .setMessage("This document has been edited.Do you want to save it")
                          .setPositiveButton("yes", (dialog, which) -> {
                              Save();
                              finish();
                          })
                          .setNegativeButton("no", (dialog, which) -> finish())
                          .create()
                  .show();
                }
                break;
            default:
                throw new IllegalStateException("Unexpected value: " + item.getItemId());
        }

        return super.onOptionsItemSelected(item);
    }
   @NonNull private String readFile(String path){ File file = new File(path);
   String text = "";
      try {
          FileInputStream fileInputStream = new FileInputStream(file);
          InputStreamReader inputStreamReader = new InputStreamReader(fileInputStream);
          BufferedReader bufferedReader= new BufferedReader(inputStreamReader);
          StringBuilder stringBuilder = new StringBuilder();
          String line;
          while ((line = bufferedReader.readLine())!=null){
              line = line+"\n";
              stringBuilder.append(line);
          }
          text = stringBuilder.toString();
          fileInputStream.close();
          inputStreamReader.close();

      }
      catch (Exception e){
          Toast.makeText(this,e.toString(),Toast.LENGTH_SHORT).show();
      }

       return text;}
    private String getRealPath(Uri uri) {
        String path = "";
        String[] project = {MediaStore.MediaColumns.DATA};
        Cursor cursor = getContentResolver().query(uri,project,null,null,null);
        assert cursor != null;
        if(cursor.moveToFirst()){
            int column_index = cursor.getColumnIndexOrThrow(MediaStore.MediaColumns.DATA);
            path = cursor.getString(column_index);
        }cursor.close();
        return  path;}
        private  void Save(){
        String content = editText.getText().toString();
        File file = new File(getRealPath(uri));
            try {
                FileOutputStream fileOutputStream = new FileOutputStream(file);
                fileOutputStream.write(content.getBytes());
                fileOutputStream.close();
                textContent = content;
            } catch (Exception e) {
                e.printStackTrace();
            }
            Toast.makeText(this, R.string.save_action,Toast.LENGTH_SHORT).show();
        }
}