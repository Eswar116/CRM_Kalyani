package com.crm.crmapp.order.activity;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.hardware.camera2.CameraCharacteristics;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.ParcelFileDescriptor;
import android.provider.MediaStore;
import android.provider.OpenableColumns;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;

import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.crm.crmapp.BuildConfig;
import com.crm.crmapp.R;
import com.crm.crmapp.order.model.DocDetailModel;
import com.crm.crmapp.order.util.ConstantVariable;
import com.crm.crmapp.test.CommonFunctions;
import com.google.android.material.snackbar.Snackbar;

import java.io.*;
import java.nio.channels.FileChannel;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class CameraActivity extends AppCompatActivity implements View.OnClickListener {

    private final String TAG = getClass().getSimpleName();

    private static final int PICK_CAMERA_IMAGE = 2;
    private static final int PICK_GALLERY_IMAGE = 1;
    private static final int PICK_GALLERY_file = 3;

    public static final String DATE_FORMAT = "yyyyMMdd_HHmmss";
    public static final String IMAGE_DIRECTORY = "ImageScalling";
    public static final String TAG_FROM = "from";

    private TextView btnGallery;
    private TextView btnCamera;
    private Button btnCompress;
    Context context;
    private Uri imageCaptureUri;
    private File file;
    private File sourceFile;
    private File destFile;
    private SimpleDateFormat dateFormatter;
    private String displayName = "";
    private DocDetailModel docDetailModel;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_camera);
        file = new File(Environment.getExternalStorageDirectory()
                + "/" + IMAGE_DIRECTORY);
        if (!file.exists()) {
            file.mkdirs();
        }
        context = this;
        dateFormatter = new SimpleDateFormat(
                DATE_FORMAT, Locale.US);
        initView();

    }

    public void initView() {
        btnGallery = (TextView) findViewById(R.id.activity_main_btn_load_from_gallery);
        if (getIntent() != null && getIntent().getStringExtra("AttendanceActivity") != null) {
            btnGallery.setVisibility(View.GONE);
        } else {
            btnGallery.setVisibility(View.VISIBLE);
        }
        btnCamera = (TextView) findViewById(R.id.activity_main_btn_load_from_camera);
        btnCompress = (Button) findViewById(R.id.activity_main_btn_compress);
        checkPermission();
    }


    private void checkPermission() {
        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.READ_EXTERNAL_STORAGE) + ContextCompat.checkSelfPermission(this,
                Manifest.permission.CAMERA) + ContextCompat
                .checkSelfPermission(this,
                        Manifest.permission.WRITE_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {

            if (ActivityCompat.shouldShowRequestPermissionRationale
                    (this, Manifest.permission.READ_EXTERNAL_STORAGE) ||

                    ActivityCompat.shouldShowRequestPermissionRationale
                            (this, Manifest.permission.CAMERA) || ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.WRITE_EXTERNAL_STORAGE)) {

                Snackbar.make(this.findViewById(android.R.id.content),
                        "Please Grant Permissions to upload profile photo",
                        Snackbar.LENGTH_INDEFINITE).setAction("ENABLE",
                        new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                                    requestPermissions(
                                            new String[]{Manifest.permission
                                                    .READ_EXTERNAL_STORAGE, Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE},
                                            100);
                                }
                            }
                        }).show();
            } else {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    requestPermissions(
                            new String[]{Manifest.permission
                                    .READ_EXTERNAL_STORAGE, Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE},
                            100);
                }
            }
        } else {

            btnGallery.setOnClickListener(this);
            btnCamera.setOnClickListener(this);
            // write your logic code if permission already granted
        }
    }


    @Override

    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {

        switch (requestCode) {
            case 100:
                if (grantResults.length > 0) {
                    boolean cameraPermission = grantResults[1] == PackageManager.PERMISSION_GRANTED;
                    boolean readExternalFile = grantResults[0] == PackageManager.PERMISSION_GRANTED;
                    if (cameraPermission && readExternalFile) {
                        btnGallery.setOnClickListener(this);
                        btnCamera.setOnClickListener(this);
                        // write your logic here
                    } else {
                        Snackbar.make(this.findViewById(android.R.id.content),
                                "Please Grant Permissions to upload profile photo",
                                Snackbar.LENGTH_INDEFINITE).setAction("ENABLE",
                                new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                                            requestPermissions(
                                                    new String[]{Manifest.permission
                                                            .READ_EXTERNAL_STORAGE, Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE},
                                                    100);
                                        }
                                    }
                                }).show();
                    }
                }
                break;
        }
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.activity_main_btn_load_from_gallery:
                Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
                intent.addCategory(Intent.CATEGORY_OPENABLE);
                intent.setType("*/*");
                Intent i = Intent.createChooser(intent, "File");
                startActivityForResult(i, PICK_GALLERY_file);
                break;
            case R.id.activity_main_btn_load_from_camera:
                destFile = new File(file, "img_"
                        + dateFormatter.format(new Date()).toString() + ".png");
                if (android.os.Build.VERSION.SDK_INT > 23) {
                    imageCaptureUri = FileProvider.getUriForFile(CameraActivity.this, BuildConfig.APPLICATION_ID + ".provider", destFile);

                } else {
                    imageCaptureUri = Uri.fromFile(destFile);
                }

               /* if (getIntent() != null && getIntent().getStringExtra("AttendanceActivity") != null) {
                    Intent intentCamera = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                    intentCamera.putExtra(MediaStore.EXTRA_OUTPUT, imageCaptureUri);
                    intentCamera.putExtra("android.intent.extras.CAMERA_FACING", android.hardware.Camera.CameraInfo.CAMERA_FACING_FRONT);
                    intentCamera.putExtra("android.intent.extras.LENS_FACING_FRONT", 1);
                    intentCamera.putExtra("android.intent.extra.USE_FRONT_CAMERA", true);
                    startActivityForResult(intentCamera, PICK_CAMERA_IMAGE);
                } else {
                    Intent intentCamera = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                    intentCamera.putExtra(MediaStore.EXTRA_OUTPUT, imageCaptureUri);
                    startActivityForResult(intentCamera, PICK_CAMERA_IMAGE);
                }*/

                if (getIntent() != null && getIntent().getStringExtra("AttendanceActivity") != null) {
                    CommonFunctions.startAnncaCameraActivity(context, destFile.getAbsolutePath(), PICK_CAMERA_IMAGE, true);
                } else {
                    CommonFunctions.startAnncaCameraActivity(context, destFile.getAbsolutePath(), PICK_CAMERA_IMAGE, false);
                }

                break;
        }

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK) {
            switch (requestCode) {
                case PICK_GALLERY_IMAGE:
                    Uri uriPhoto = data.getData();
                    Log.d(TAG + ".PICK_GALLERY_IMAGE", "Selected image uri path :" + uriPhoto.toString());

                    sourceFile = new File(getPathFromGooglePhotosUri(uriPhoto));
                    destFile = new File(file, "img_"
                            + dateFormatter.format(new Date()).toString() + ".png");
                    Log.d(TAG, "Source File Path :" + sourceFile);
                    try {
                        copyFile(sourceFile, destFile);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    //sendDataOrderActivity(uriPhoto ,myFile,PathHolder);

                    break;
                case PICK_CAMERA_IMAGE:
                    if (imageCaptureUri != null) {
                        sendDataOrderActivity(imageCaptureUri, new File(imageCaptureUri.toString()), imageCaptureUri.getPath(), destFile);
                    }
                    break;
                case PICK_GALLERY_file:
                    Uri uriFile = data.getData();
                    String PathHolder = data.getData().getPath();
                    Toast.makeText(CameraActivity.this, PathHolder, Toast.LENGTH_LONG).show();
                    File myFile = new File(uriFile.toString());
                    sourceFile = new File(getPathFromGooglePhotosUri(uriFile));
                    destFile = new File(file, "img_"
                            + dateFormatter.format(new Date()).toString() + ".png");
                    Log.d(TAG, "Source File Path :" + sourceFile);
                    try {
                        copyFile(sourceFile, destFile);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    if (data.getData().getPath() != null) {
                        sendDataOrderActivity(uriFile, myFile, PathHolder, destFile);
                    } else {
                        Toast.makeText(this, "Something went Wrong", Toast.LENGTH_SHORT).show();
                    }
                    break;
            }
        }
    }


    private void sendDataOrderActivity(Uri uriFile, File myFile, String pathHolder, File destFile) {

        Bitmap bmp = decodeFile(destFile);

       /* File sd = Environment.getExternalStorageDirectory();
        File image = new File(destFile, "");
        BitmapFactory.Options bmOptions = new BitmapFactory.Options();
        Bitmap bmp = BitmapFactory.decodeFile(image.getAbsolutePath(),bmOptions);
        try {
            bmp= TextExtractActivity.modifyOrientation(bmp,destFile.getAbsolutePath());
        } catch (IOException e) {
            e.printStackTrace();
        }*/
        docDetailModel = new DocDetailModel(BitMapToString(bmp), getFileName(uriFile.toString(), uriFile, myFile), getFileSize(myFile), destFile.getAbsolutePath());
        Intent intent = new Intent(this, NewOrderActivity.class);
        intent.putExtra(ConstantVariable.Companion.getDocDetailModel(), docDetailModel);
        setResult(RESULT_OK, intent);
        finish();
    }

    public String BitMapToString(Bitmap bitmap) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
        byte[] b = baos.toByteArray();
        String temp = Base64.encodeToString(b, Base64.DEFAULT);
        return temp;
    }

    private double getFileSize(File file) {

        long fileSizeInBytes = file.length();
        long fileSizeInKB = fileSizeInBytes / 1024;
        long fileSizeInMB = fileSizeInKB / 1024;
        return fileSizeInKB;

    }

    // TODO: 29-11-2018
    private String getFileName(String uriString, Uri uri, File myFile) {
        if (uriString.startsWith("content://")) {
            Cursor cursor = null;
            try {
                cursor = this.getContentResolver().query(uri, null, null, null, null);
                if (cursor != null && cursor.moveToFirst()) {
                    displayName = cursor.getString(cursor.getColumnIndex(OpenableColumns.DISPLAY_NAME));
                }
            } finally {
                cursor.close();
            }
        } else if (uriString.startsWith("file://")) {
            displayName = myFile.getName();
        }
        return displayName;
    }

    private Bitmap decodeFile(File f) {
        Bitmap b = null;
        //Decode image size
        BitmapFactory.Options o = new BitmapFactory.Options();
        o.inJustDecodeBounds = false;
        FileInputStream fis = null;
        try {
            fis = new FileInputStream(f);
            BitmapFactory.decodeStream(fis, null, o);
            fis.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();

        } catch (IOException e) {
            e.printStackTrace();
        }
        int IMAGE_MAX_SIZE = 1024;
        int scale = 1;
        if (o.outHeight > IMAGE_MAX_SIZE || o.outWidth > IMAGE_MAX_SIZE) {
            scale = (int) Math.pow(2, (int) Math.ceil(Math.log(IMAGE_MAX_SIZE /
                    (double) Math.max(o.outHeight, o.outWidth)) / Math.log(0.5)));
        }

        //Decode with inSampleSize
        BitmapFactory.Options o2 = new BitmapFactory.Options();
        o2.inSampleSize = scale;
        try {
            fis = new FileInputStream(f);
            b = BitmapFactory.decodeStream(fis, null, o2);
            Log.d(TAG, "Width :" + b.getWidth() + " Height :" + b.getHeight());
            fis.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        destFile = new File(file, "img_"
                + dateFormatter.format(new Date()).toString() + ".png");
        try {
            FileOutputStream out = new FileOutputStream(destFile);
            b.compress(Bitmap.CompressFormat.JPEG, 100, out);
            out.flush();
            out.close();

        } catch (Exception e) {
            e.printStackTrace();
        }
        return b;
    }


    /**
     * This is useful when an image is available in sdcard physically.
     *
     * @param uriPhoto
     * @return
     */
    /*public String getPathFromUri(Uri uriPhoto) {
        if (uriPhoto == null)
            return null;

        if (SCHEME_FILE.equals(uriPhoto.getScheme())) {
            return uriPhoto.getPath();
        } else if (SCHEME_CONTENT.equals(uriPhoto.getScheme())) {
            final String[] filePathColumn = {MediaStore.MediaColumns.DATA,
                    MediaStore.MediaColumns.DISPLAY_NAME};
            Cursor cursor = null;
            try {
                cursor = getContentResolver().query(uriPhoto, filePathColumn, null, null, null);
                if (cursor != null && cursor.moveToFirst()) {
                    final int columnIndex = (uriPhoto.toString()
                            .startsWith("content://com.google.android.gallery3d")) ? cursor
                            .getColumnIndex(MediaStore.MediaColumns.DISPLAY_NAME)
                            : cursor.getColumnIndex(MediaStore.MediaColumns.DATA);

                    // Picasa images on API 13+
                    if (columnIndex != -1) {
                        String filePath = cursor.getString(columnIndex);
                        if (!TextUtils.isEmpty(filePath)) {
                            return filePath;
                        }
                    }
                }
            } catch (IllegalArgumentException e) {
                // Nothing we can do
                Log.d(TAG, "IllegalArgumentException");
                e.printStackTrace();
            } catch (SecurityException ignored) {
                Log.d(TAG, "SecurityException");
                // Nothing we can do
                ignored.printStackTrace();
            } finally {
                if (cursor != null)
                    cursor.close();
            }
        }
        return null;
    }*/
    public String getPathFromGooglePhotosUri(Uri uriPhoto) {
        if (uriPhoto == null)
            return null;

        FileInputStream input = null;
        FileOutputStream output = null;
        try {
            ParcelFileDescriptor pfd = getContentResolver().openFileDescriptor(uriPhoto, "r");
            FileDescriptor fd = pfd.getFileDescriptor();
            input = new FileInputStream(fd);

            String tempFilename = getTempFilename(this);
            output = new FileOutputStream(tempFilename);

            int read;
            byte[] bytes = new byte[4096];
            while ((read = input.read(bytes)) != -1) {
                output.write(bytes, 0, read);
            }
            return tempFilename;
        } catch (IOException ignored) {
            // Nothing we can do
        } finally {
            closeSilently(input);
            closeSilently(output);
        }
        return null;
    }

    public static void closeSilently(Closeable c) {
        if (c == null)
            return;
        try {
            c.close();
        } catch (Throwable t) {
            // Do nothing
        }
    }

    private static String getTempFilename(Context context) throws IOException {
        File outputDir = context.getCacheDir();
        File outputFile = File.createTempFile("image", "tmp", outputDir);
        return outputFile.getAbsolutePath();
    }

    private void copyFile(File sourceFile, File destFile) throws IOException {
        if (!sourceFile.exists()) {
            return;
        }
        FileChannel source = null;
        FileChannel destination = null;
        source = new FileInputStream(sourceFile).getChannel();
        destination = new FileOutputStream(destFile).getChannel();
        if (destination != null && source != null) {
            destination.transferFrom(source, 0, source.size());
        }
        if (source != null) {
            source.close();
        }
        if (destination != null) {
            destination.close();
        }
    }


    // TODO: 28-11-2018

}


/**
 * This is useful when an image is not available in sdcard physically but it displays into photos application via google drive(Google Photos) and also for if image is available in sdcard physically.
 *
 * @param uriPhoto
 * @return
 */