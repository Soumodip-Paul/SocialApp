package com.sp.SPBrowser;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.speech.RecognizerIntent;
import android.view.Menu;
import android.view.MenuItem;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.sp.SPBrowser.textHandler.TextOutput;

import java.util.ArrayList;
import java.util.Locale;
import java.util.Objects;

public class MainActivity extends AppCompatActivity {
    WebView w;
    boolean loading;
    TextOutput textOutput;
    String string;
    String intentUrl;
    Intent intent;

    @SuppressLint("setJavaScriptEnabled")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        overridePendingTransition(android.R.anim.slide_in_left,android.R.anim.slide_out_right);
        setContentView(R.layout.activity_main);
        textOutput = new TextOutput(this,this);

        PermissionClass.onRequest(this, Manifest.permission.WRITE_EXTERNAL_STORAGE,"grant storage permission ","requires to read write files",1);
        PermissionClass.onRequest(this, Manifest.permission.READ_EXTERNAL_STORAGE,"grant storage permission ","requires to read write files",2);
        Objects.requireNonNull(getSupportActionBar()).setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowCustomEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayUseLogoEnabled(true);
        getSupportActionBar().setLogo(R.mipmap.ic_launcher_round);
        getSupportActionBar().setTitle(R.string.app_name);

        w = findViewById(R.id.webView);

      Toast.makeText(this,"hi i am successfully installed and opened",Toast.LENGTH_LONG).show();
      String action = getIntent().getAction();
      intent = getIntent();
      intentUrl = intent.getData().toString();
        Toast.makeText(this, intentUrl, Toast.LENGTH_SHORT).show();
      if (Objects.equals(action, "android.intent.action.VIEW")){
          if (intent.getScheme().equals("http")||intent.getScheme().equals("https")){w.loadUrl(intentUrl);}
         else if(intent.getScheme().equals("content")){w.loadUrl("file://"+getRealPath(getIntent().getData()));}
         else {
              w.loadUrl(intentUrl);
          }
      }
      else {w.loadUrl("file:///storage/emulated/0/file.html");}
//
        WebSettings ws = w.getSettings();
      ws.setJavaScriptCanOpenWindowsAutomatically(true);
      ws.setJavaScriptEnabled(true);
      ws.setAllowContentAccess(true);
      ws.setAllowFileAccess(true);
      ws.setBuiltInZoomControls(true);
      ws.setAllowFileAccessFromFileURLs(true);
      ws.setGeolocationEnabled(true);
      ws.setSupportZoom(true);
      w.setWebViewClient(new WebViewClient(){
          @Override
          public void onPageStarted(WebView view, String url, Bitmap favicon) {
              super.onPageStarted(view, url, favicon);
              loading = true;
          }

          @Override
          public void onPageFinished(WebView view, String url) {
              super.onPageFinished(view, url);
              loading =false;
          }
      });
      w.setWebChromeClient(new WebChromeClient(){
          @Override
          public void onProgressChanged(WebView view, int newProgress) {
              super.onProgressChanged(view, newProgress);
              loading = true;
          }

          @Override
          public void onReceivedTitle(WebView view, String title) {
              super.onReceivedTitle(view, title);
              Objects.requireNonNull(getSupportActionBar()).setTitle(title);
              getSupportActionBar().setSubtitle(w.getOriginalUrl());
          }

          @Override
          public void onReceivedIcon(WebView view, Bitmap icon) {
              super.onReceivedIcon(view, icon);
              Drawable drawable =new BitmapDrawable(getResources(),icon);
             Objects.requireNonNull(getSupportActionBar()).setLogo(drawable);
          }

      });
      w.addJavascriptInterface(new Android(this),"Android");

    }

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

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.toolbar_menu,menu);

        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()){
            case R.id.app_bar_search:

                break;
            case android.R.id.home:
//                textOutput.Speak("You are going back");
              String s = getVoiceInput();
                Toast.makeText(MainActivity.this,s,Toast.LENGTH_LONG).show();
//                onBackPressed();
                break;
            case  R.id.forward:
                if (w.canGoForward()){
                    textOutput.Speak("You are going forward");
                    w.goForward();
                }
            case R.id.end:
               if(loading){w.stopLoading();}
                break;

            default:
                throw new IllegalStateException("Unexpected value: " + item.getItemId());
        }
        return super.onOptionsItemSelected(item);
    }



    public String getVoiceInput(){
        Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL,RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, Locale.US);
        intent.putExtra(RecognizerIntent.EXTRA_PROMPT,"Please speak something");
        try{
            startActivityForResult(intent,1);
        }catch(Exception e){e.printStackTrace();}
        return string;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode==1&&resultCode==RESULT_OK&&null!=data){
            ArrayList<String> result = data.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
            string = Objects.requireNonNull(result).get(0);
        }
    }
    @Override
    public void onBackPressed() {

        if(w.canGoBack()){w.goBack();}
       else {textOutput.close();
            new Android(MainActivity.this).alert("EXIT", "Do you want to exit", (dialog, which) -> dialog.dismiss(), (dialog, which) -> {
                MainActivity.this.finish();
                overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
            }
            );

    }
}
}