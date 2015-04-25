package com.example.wendy_guo.j4sp.view;

import android.app.ActionBar;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.example.wendy_guo.j4sp.Constants;
import com.example.wendy_guo.j4sp.R;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class TakePhotoActivity extends Activity {
    private Uri mUri;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_take_photo);
        final ActionBar actionBar = getActionBar();
        actionBar.hide();
        isDeviceSupportCamera();

        Intent takePhotoIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        mUri = getPath();
        if (mUri == null) {
            Toast.makeText(this, R.string.error_external_storage,
                    Toast.LENGTH_LONG).show();
        } else {
            if(getIntent().getIntExtra(Constants.TAKE_PHOTO_CODE,0) == Constants.SHARE_BY_EMAIL_INT_EXTRA){

                takePhotoIntent.putExtra(MediaStore.EXTRA_OUTPUT, mUri);
                startActivityForResult(takePhotoIntent, Constants.SHARE_BY_EMAIL);
            }
            else if(getIntent().getIntExtra(Constants.TAKE_PHOTO_CODE,0) == Constants.SHARE_BY_SMS_INT_EXTRA){
                takePhotoIntent.putExtra(MediaStore.EXTRA_OUTPUT, mUri);
                startActivityForResult(takePhotoIntent, Constants.SHARE_BY_SMS);
            }
            else {
                takePhotoIntent.putExtra(MediaStore.EXTRA_OUTPUT, mUri);
                startActivityForResult(takePhotoIntent, 0);
            }
            //OnActivityResult
        }
    }

    //for state restore
    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

        outState.putParcelable(Constants.PHOTO_URI, mUri);
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        mUri = savedInstanceState.getParcelable(Constants.PHOTO_URI);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == RESULT_OK) {

            if(requestCode == Constants.SHARE_BY_EMAIL){
                Log.i("WILL_MAIL","WILL_MAIL");
                Intent shareIntent = new Intent(this, ShareActivity.class);
                shareIntent.setData(mUri);
                startActivity(shareIntent);
            }
            else if(requestCode == Constants.SHARE_BY_SMS){
                Log.i("WILL_SEND","WILL_SEND");
                Intent smsIntent = new Intent(this, SMSActivity.class);
                smsIntent.setData(mUri);
                startActivity(smsIntent);
            }
            else {
                Log.i("WILL_UPLOAD","WILL_UPLOAD");
                Intent mediaScanIntent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
                mediaScanIntent.setData(mUri);
                sendBroadcast(mediaScanIntent);
                Log.i("IMAGE SAVED", "IMAGE SAVED");

                Intent uploadIntent = new Intent(this, UploadActivity.class);
                uploadIntent.setData(mUri);

                String type = Constants.IMAGE_TYPE;
                uploadIntent.putExtra(Constants.KEY_TYPE, type);
                Log.i("ABOUT to START UPLOAD", mUri+"");
                startActivity(uploadIntent);
            }
            finish();

        } else {
            Toast.makeText(this, "TAKE PHOTO CANCELED", Toast.LENGTH_LONG).show();
            finish();
        }
    }


    private Uri getPath() {

        String state = Environment.getExternalStorageState();

        if (state.equals(Environment.MEDIA_MOUNTED)) {// get the path
            File mediaStorageDir = new File(
                    Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES),
                    this.getString(R.string.app_name));
            // Create our own if not exist
            if (!mediaStorageDir.exists()) {
                if (!mediaStorageDir.mkdirs()) {
                    Log.e("File dir Error", "Failed to create directory.");
                    return null;
                }
            }
            //Create  file
            File file;
            Date now = new Date();
            String timestamp = new SimpleDateFormat(Constants.TIME_STAMP_FORMAT, Locale.US).format(now);

            String path = mediaStorageDir.getPath() + File.separator;

            file = new File(path + "IMG_" + timestamp + ".jpg");

            return Uri.fromFile(file);
        } else {
            return null;
        }
    }
    private void isDeviceSupportCamera() {
        if (getApplicationContext().getPackageManager().hasSystemFeature(
                PackageManager.FEATURE_CAMERA)) {
            return ;
        } else {
            finish();
        }
    }

}
