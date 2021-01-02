package com.sp.SPBrowser.textHandler;

import android.content.Context;
import android.speech.tts.TextToSpeech;

import androidx.appcompat.app.AppCompatActivity;

import com.sp.SPBrowser.Android;

import java.util.Locale;

public class TextOutput implements TextToSpeech.OnInitListener {
    private TextToSpeech tts;
    Context context;
    AppCompatActivity activity;
    Android a;
    String text;

    public void Speak(String text){
        a = new Android(activity);
        tts = new TextToSpeech(context, this);
        this.text= text;
        voiceOutput(text);

    }

    private void voiceOutput(String text) {
        tts.speak(text,TextToSpeech.QUEUE_FLUSH,null,"id1");

    }

    @Override
    public void onInit(int status) {
        if (status==TextToSpeech.SUCCESS){
            int result = tts.setLanguage(Locale.US);
           // float i = 50;
            if(result==TextToSpeech.LANG_MISSING_DATA||result==TextToSpeech.LANG_NOT_SUPPORTED){
               a.showToast("Language not Supported");
            }
            else {
                voiceOutput(text);
            }
        }
        else{
            a.showToast("Initialing failed");
        }
    }
    public  TextOutput (Context context, AppCompatActivity activity){
        this.context=context;
        this.activity= activity;

    }
    public void close(){
     if(tts!=null)  { tts.stop();
        tts.shutdown();}
    }
}
