package com.sp.SPBrowser;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.pdf.PdfDocument;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Objects;

public class Img2PDF extends AppCompatActivity implements View.OnClickListener {
    public static final int GALLERY_PICTURE = 1;
    private static final int CAPTURE_PHOTO = 2;
    Button btn_select, btn_convert;
    ImageView iv_image;
   // boolean boolean_permission;
    boolean boolean_save;
    Bitmap bitmap;
    String action;
    Uri uri2 = null;
    static String path;

    /*private static final String IMAGE_CAPTURE_FOLDER = "Sp scanner";
    String _imageFileName;*/


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_img2_p_d_f);
        action = getIntent().getType();
        uri2 =getIntent().getData();
        boolean_save = false;
        PermissionClass.onRequest(this, Manifest.permission.WRITE_EXTERNAL_STORAGE,"grant storage permission ","requires to read write files",1);
        PermissionClass.onRequest(this, Manifest.permission.WRITE_EXTERNAL_STORAGE,"grant storage permission ","requires to read write files",2);
        init();
        listener();

    }

    private void init() {
        btn_select =  findViewById(R.id.btn_select);
        btn_convert = findViewById(R.id.btn_convert);
        iv_image =  findViewById(R.id.iv_image);
        if(action.equals("image/jpeg")||action.equals("image/png")){
            bitmap = BitmapFactory.decodeFile(getRealPathosUri(uri2));
            iv_image.setImageBitmap(bitmap);
            Toast.makeText(this, getRealPathosUri(uri2),Toast.LENGTH_LONG).show();
        }
    }

    private void listener() {
        btn_select.setOnClickListener(this);
        btn_convert.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_select:
                PopupMenu popupmenu = new PopupMenu(this,btn_select);
                popupmenu.getMenuInflater().inflate(R.menu.pmenu,popupmenu.getMenu());
                popupmenu.setOnMenuItemClickListener(item -> {
                    switch(item.getItemId()){
                        case R.id.gall:
                            Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                            startActivityForResult(intent, GALLERY_PICTURE);

                            break;
                        case R.id.cam:
                            PermissionClass.onRequest(this,Manifest.permission.CAMERA,"Access Camera","give Access to your Camera",3);
                            Intent i = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                            File dir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DCIM);

                            File output = new File(dir, "CameraContentDemo.jpeg");
                            i.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(output));

                            startActivityForResult(i,CAPTURE_PHOTO);
                            break;

                    }
                    return false;
                });
                popupmenu.show();

                break;

            case R.id.btn_convert:
                if (boolean_save){

                    Uri uri = Uri.fromFile(new File(path)) ;
                    Intent intent1=new Intent(Intent.ACTION_VIEW);
                    intent1.setData(uri);
                    Intent j = Intent.createChooser(intent1,"choose an app to open");
                    startActivity(j);

                }else {
                    createPdf();
                }
                break;


        }
    }

    private void createPdf() {
        PdfDocument document = new PdfDocument();

        PdfDocument.PageInfo pageInfo = new PdfDocument.PageInfo.Builder(bitmap.getWidth(), bitmap.getHeight(), 1).create();
        PdfDocument.Page page = document.startPage(pageInfo);

        Canvas canvas = page.getCanvas();


        Paint paint = new Paint();
        paint.setColor(Color.parseColor("#ffffff"));
        canvas.drawPaint(paint);
        bitmap = Bitmap.createScaledBitmap(bitmap, bitmap.getWidth(), bitmap.getHeight(), true);

        paint.setColor(Color.BLUE);
        canvas.drawBitmap(bitmap, 0, 0, null);
        document.finishPage(page);


        // write the document content
        fileName(document, this);
        boolean_save = true;
        // close the document
        document.close();
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == GALLERY_PICTURE && resultCode == RESULT_OK) {

            Uri selectedImage = data.getData();
            String[] filePathColumn = {MediaStore.Images.Media.DATA};


            Cursor cursor = getContentResolver().query(
                    Objects.requireNonNull(selectedImage), filePathColumn, null, null, null);
            Objects.requireNonNull(cursor).moveToFirst();

            int columnIndex = cursor.getColumnIndex(filePathColumn[0]);

            String  filePath = cursor.getString(columnIndex);
            cursor.close();


            bitmap = BitmapFactory.decodeFile(filePath);
            iv_image.setImageBitmap(bitmap);


            btn_convert.setClickable(true);
        }else if(requestCode == CAPTURE_PHOTO&&resultCode== Activity.RESULT_OK){
                bitmap =(Bitmap) Objects.requireNonNull(data.getExtras()).get("data");
                iv_image.setImageBitmap(bitmap);

                btn_convert.setClickable(true);

            }
        }


   /* private File getFile() {
        String filepath = Environment.getExternalStorageDirectory().getPath();
        File file = new File(filepath, IMAGE_CAPTURE_FOLDER);
        if (!file.exists()) {
            file.mkdirs();
        }

        return new File(file + File.separator + _imageFileName
                + ".jpg");
    }*/

    public void fileName(PdfDocument document, Context context){
        File file = new File(Environment.getExternalStorageDirectory().getPath(),"Pdf");
        try{if(!file.exists()) {file.mkdirs();}
        }catch(Exception e){e.printStackTrace();}
        for(int i =1;;i++){

            path = Environment.getExternalStorageDirectory().getAbsolutePath()+"/Pdf/image_pdf_"+i+".pdf";
            File file2 = new File(path);
            boolean isFileExists = file2.exists();
            if(!isFileExists){ try{
                document.writeTo(new FileOutputStream(file2));
                btn_convert.setText(R.string.chk);

            } catch (IOException e) {
                e.printStackTrace();
                Toast.makeText(context, "Something wrong: " + e.toString(), Toast.LENGTH_LONG).show();
            } break;}}
    }
    public  String getRealPathosUri(Uri uri){
        String _path="";
        String[] project ={MediaStore.MediaColumns.DATA};
        Cursor cursor = getContentResolver().query(uri,project,null,null,null);
        if(Objects.requireNonNull(cursor).moveToFirst()){
            int column_index = cursor.getColumnIndexOrThrow(MediaStore.MediaColumns.DATA);
            _path = cursor.getString(column_index);
        }cursor.close();
        return _path;
    }
}