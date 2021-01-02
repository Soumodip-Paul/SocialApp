package com.sp.SPBrowser;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.os.Build;

import androidx.annotation.NonNull;

public class PermissionClass {
    public static void onRequest(@NonNull final Activity a , final String permission, String title, String message, final int REQUEST_CODE_ASK_PERMISSIONS){

        int hasWriteStoragePermission = a.checkSelfPermission(permission);
        if (hasWriteStoragePermission != PackageManager.PERMISSION_GRANTED) {

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                if (!a.shouldShowRequestPermissionRationale(permission)) {
                    showMessage1(a,title,message,
                            (dialog, which) -> a.requestPermissions(new String[]{permission},
                                    REQUEST_CODE_ASK_PERMISSIONS));

                    return;
                }

                a.requestPermissions(new String[]{permission},
                        REQUEST_CODE_ASK_PERMISSIONS);
            }

        }
    }


    private static void showMessage1(final Activity a,final String title,final String message,final DialogInterface.OnClickListener okListener) {
        a.setTheme(android.R.style.Theme);
        new AlertDialog.Builder(a)
                .setTitle(title)
                .setMessage(message)
                .setPositiveButton("OK", okListener)
                .setOnCancelListener(p1 -> showMessage1(a,title,message,okListener))
                //.setNegativeButton("Cancel", null)
                .create()
                .show();

        a.setTheme(R.style.AppTheme);
    }

}
