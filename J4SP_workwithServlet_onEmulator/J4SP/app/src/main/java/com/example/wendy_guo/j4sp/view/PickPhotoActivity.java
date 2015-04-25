package com.example.wendy_guo.j4sp.view;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.widget.Toast;

import com.example.wendy_guo.j4sp.Constants;
import com.example.wendy_guo.j4sp.R;


public class PickPhotoActivity extends Activity {
    Uri mUri;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pick_photo);

        if (getIntent().getIntExtra(Constants.PICK_PHOTO_CODE, 0) == Constants.SHARE_BY_EMAIL_INT_EXTRA) {
            Intent pickPhoto = new Intent(Intent.ACTION_PICK,
                    android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
            startActivityForResult(pickPhoto, Constants.SHARE_BY_EMAIL);
        } else if (getIntent().getIntExtra(Constants.PICK_PHOTO_CODE, 0) == Constants.SHARE_BY_SMS_INT_EXTRA) {
            Intent pickPhoto = new Intent(Intent.ACTION_PICK,
                    android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
            startActivityForResult(pickPhoto, Constants.SHARE_BY_SMS);
        } else {

            Intent pickPhoto = new Intent(Intent.ACTION_PICK,
                    android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
            startActivityForResult(pickPhoto, 1);
        }
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == RESULT_OK) {
            if (data == null) {
                Toast.makeText(this, getString(R.string.error_title), Toast.LENGTH_LONG).show();
            } else {
                mUri = data.getData();
                Log.i("mUri", mUri+"");
            }

            if (requestCode == Constants.SHARE_BY_EMAIL) {
                Log.i("WILL_MAIL", "WILL_MAIL");
                Intent shareIntent = new Intent(this, ShareActivity.class);
                shareIntent.setData(mUri);
                startActivity(shareIntent);
            }
            else if (requestCode == Constants.SHARE_BY_SMS) {
                Log.i("WILL_SEND", "WILL_SEND");
                Intent smsIntent = new Intent(this, SMSActivity.class);
                smsIntent.setData(mUri);
                startActivity(smsIntent);
            }
            else {
                Log.i("WILL_UPLOAD", "WILL_UPLOAD");

                Intent uploadIntent = new Intent(this, UploadActivity.class);
                uploadIntent.setData(mUri);
                String[] filePathColumn = {MediaStore.Images.Media.DATA};
                Cursor cursor = getContentResolver().query(
                        mUri, filePathColumn, null, null, null);
                cursor.moveToFirst();

                int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
                String filePath = cursor.getString(columnIndex);
                Log.i("PATH: ", filePath);

                cursor.close();
                uploadIntent.putExtra(Constants.PHOTO_PATH, filePath);
                String type = Constants.IMAGE_TYPE;
                uploadIntent.putExtra(Constants.KEY_TYPE, type);
                Log.i("ABOUT to START UPLOAD", "ABOUT to START UPLOAD");
                startActivity(uploadIntent);
            }
            finish();

        } else {
            Toast.makeText(this, "PICK PHOTO CANCELED", Toast.LENGTH_LONG).show();
            finish();
        }
    }

}
