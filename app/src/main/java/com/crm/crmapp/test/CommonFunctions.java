package com.crm.crmapp.test;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;

import androidx.core.app.ActivityCompat;

import io.github.memfis19.annca.Annca;
import io.github.memfis19.annca.internal.configuration.AnncaConfiguration;

public class CommonFunctions {

    public static void startAnncaCameraActivity(Context context, final String path, int requestcode, boolean openFrontCamera) {
        final AnncaConfiguration.Builder dialogDemo = new AnncaConfiguration.Builder((Activity) context, requestcode);
        dialogDemo.setMediaAction(AnncaConfiguration.MEDIA_ACTION_PHOTO);
        dialogDemo.setMediaResultBehaviour(AnncaConfiguration.PREVIEW);
        if (openFrontCamera) {
            dialogDemo.setCameraFace(AnncaConfiguration.CAMERA_FACE_FRONT);
        }
        if (ActivityCompat.checkSelfPermission(context, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        new Annca(dialogDemo.build()).launchCamera(path);
    }

}
