package com.sp.SPBrowser;

import android.content.DialogInterface;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;
import android.webkit.JavascriptInterface;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;


public class Android  {
    AppCompatActivity context;
   @JavascriptInterface
    public void showToast(String text){
       Toast.makeText(context,text,Toast.LENGTH_LONG).show();
    }
    public  Android(AppCompatActivity context){
        this.context=context;
    }
    @JavascriptInterface
    public void alert(String title, String message, DialogInterface.OnClickListener yesListener, DialogInterface.OnClickListener noListener){
        new AlertDialog.Builder(context)
                .setTitle(title)
                .setMessage(message)
                .setIcon(R.mipmap.ic_launcher_round)
                .setNegativeButton("no", noListener)
                .setPositiveButton("yes", yesListener)
                .create().show();
    }
    @JavascriptInterface
    public void close(){
       context.finish();
    }
    public static Bitmap getBitmapFromUrl(String stringUrl){
       try{
           URL url = new URL(stringUrl);
           HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
           urlConnection.setDoInput(true);
           urlConnection.connect();
           InputStream inputStream = urlConnection.getInputStream();
           return BitmapFactory.decodeStream(inputStream);
       }
       catch (Exception e){
           e.printStackTrace();
           Log.d("TAG", "getBitmapFromUrl: exception ");
           return  BitmapFactory.decodeResource(Resources.getSystem(),R.mipmap.ic_launcher);
       }
    }

}
