package com.sp.SPBrowser;

import androidx.appcompat.app.AppCompatActivity;

import android.Manifest;
import android.database.Cursor;
import android.graphics.pdf.PdfRenderer;
import android.net.Uri;
import android.os.Bundle;
import android.os.ParcelFileDescriptor;
import android.provider.MediaStore;
import android.widget.TextView;

import java.io.File;
import java.io.IOException;

public class pdfReader extends AppCompatActivity {
    TextView textView;
Uri uri;
String path;
int pageIndex;
PdfRenderer pdfRenderer;
PdfRenderer.Page currentPage;
ParcelFileDescriptor parcelFileDescriptor;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pdf_reader);
        textView =findViewById(R.id.textView);
        PermissionClass.onRequest(this, Manifest.permission.READ_EXTERNAL_STORAGE,"Grant Storage","Permission required to access files",1);
        uri = getIntent().getData();
        path = getRealPath(uri);

    }

    @Override
    protected void onStart() {
        super.onStart();
        try {
            openRenderer();
            showPage(pageIndex);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void showPage(int pageIndex) {
        if(pdfRenderer.getPageCount()<=pageIndex){
            return;
        }
        if(null!=currentPage){
            currentPage.close();
        }
        currentPage = pdfRenderer.openPage(pageIndex);
        textView.setText(currentPage.toString());
    }

    public  void openRenderer() throws IOException {
        File file = new File(path);
        parcelFileDescriptor = ParcelFileDescriptor.open(file,ParcelFileDescriptor.MODE_READ_ONLY);
      if(parcelFileDescriptor!=null) { pdfRenderer = new PdfRenderer(parcelFileDescriptor);}
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
}