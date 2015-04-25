package com.example.wendy_guo.j4sp.view;

import android.app.ActionBar;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.Toast;

import com.example.wendy_guo.j4sp.Constants;
import com.example.wendy_guo.j4sp.R;
import com.example.wendy_guo.j4sp.adapter.DashBoardAdapter;
import com.example.wendy_guo.j4sp.sql.DBBuilder;

import java.io.File;


public class MainActivity extends Activity {
    DialogInterface.OnClickListener mDialogListener;
    DBBuilder mDBBuilder;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        final ActionBar actionBar = getActionBar();
        actionBar.hide();

        GridView gridview = (GridView) findViewById(R.id.gridview);
        gridview.setAdapter(new DashBoardAdapter(this));

        SharedPreferences sharedPref = this.getSharedPreferences(
                getString(R.string.preference_file_key), Context.MODE_PRIVATE);
        long ID = sharedPref.getInt(Constants.USER_ID, -1);
        Toast.makeText(this, ID +" get !", Toast.LENGTH_LONG).show();

        if (ID < 0) {
            Toast.makeText(MainActivity.this, R.string.expired,
                    Toast.LENGTH_LONG).show();
            navigateToLogin();
        }

            gridview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                public void onItemClick(AdapterView<?> parent, View v, int position, long id) {
                    switch (position) {
                        case 0:
                            /*String path = MainActivity.this.getFilesDir() + "/" + Constants.OFFLINE_CACHE;
                            File file = new File(path);
                            boolean test = file.delete();
                            Log.i(test + "", test + "");
                            Log.i(test+"",test+"");
                            Log.i(test+"",test+"");
                            Log.i(test+"",test+"");*/
                            Toast.makeText(MainActivity.this, "View Spending Trend", Toast.LENGTH_SHORT).show();
                            break;
                        case 1:
                            Toast.makeText(MainActivity.this, "View Local History", Toast.LENGTH_SHORT).show();
                            Intent intent = new Intent(MainActivity.this, FragListActivity.class);
                            startActivity(intent);
                            break;
                        case 2:
                            Toast.makeText(MainActivity.this, "Upload Gallery Photo", Toast.LENGTH_SHORT).show();
                            Intent pickPhotoIntent = new Intent(MainActivity.this, PickPhotoActivity.class);
                            startActivity(pickPhotoIntent);
                            break;
                        case 3:
                            Toast.makeText(MainActivity.this, "Take Photo", Toast.LENGTH_SHORT).show();
                            Intent takePhotoIntent = new Intent(MainActivity.this, TakePhotoActivity.class);
                            startActivity(takePhotoIntent);
                            break;
                        case 4:
                            Toast.makeText(MainActivity.this, "Search Record", Toast.LENGTH_SHORT).show();
                            //Intent searchIntent = new Intent(MainActivity.this, SearchActivity.class);
                            Intent searchIntent = new Intent(MainActivity.this, SearchPagerActivity.class);

                            startActivity(searchIntent);
                            break;
                        case 5:
                            Toast.makeText(MainActivity.this, "Share Bill" + position, Toast.LENGTH_SHORT).show();
                            AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
                            builder.setItems(R.array.share_choices, mDialogListener);
                            AlertDialog dialog = builder.create();
                            dialog.show();
                            break;
                        case 6:
                            Toast.makeText(MainActivity.this, "Visit Web Site" + position, Toast.LENGTH_SHORT).show();
                            Intent webViewIntent = new Intent(MainActivity.this, ViewWebPageActivity.class);
                            webViewIntent.putExtra(Constants.VIEW_URL,Constants.HOST+ Constants.PORT + "/index.html");
                            startActivity(webViewIntent);
                            break;
                        case 7:
                            Toast.makeText(MainActivity.this, "Setting" + position, Toast.LENGTH_SHORT).show();
                            Intent settingIntent = new Intent(MainActivity.this, SettingActivity.class);
                            startActivity(settingIntent);

                            break;

                    }
                }
            });

        mDialogListener =
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        switch(which) {
                            case 0:
                                Intent shareIntent = new Intent(MainActivity.this, TakePhotoActivity.class);
                                shareIntent.putExtra(Constants.TAKE_PHOTO_CODE,Constants.SHARE_BY_EMAIL_INT_EXTRA);
                                startActivity(shareIntent);
                                break;
                            case 1:
                                Intent sharePhotoIntent = new Intent(MainActivity.this, PickPhotoActivity.class);
                                sharePhotoIntent.putExtra(Constants.PICK_PHOTO_CODE,Constants.SHARE_BY_EMAIL_INT_EXTRA);
                                startActivity(sharePhotoIntent);
                                break;
                            case 2:
                                Intent smsIntent = new Intent(MainActivity.this, TakePhotoActivity.class);
                                smsIntent.putExtra(Constants.TAKE_PHOTO_CODE,Constants.SHARE_BY_SMS_INT_EXTRA);
                                startActivity(smsIntent);
                                break;
                            case 3:
                                Intent smsPhotoIntent = new Intent(MainActivity.this, PickPhotoActivity.class);
                                smsPhotoIntent.putExtra(Constants.PICK_PHOTO_CODE,Constants.SHARE_BY_SMS_INT_EXTRA);
                                startActivity(smsPhotoIntent);
                                break;
                            case 4:
                                Toast.makeText(MainActivity.this, "Will list file dir ", Toast.LENGTH_SHORT).show();

                                break;
                        }
                    }
                };

    }

    private void navigateToLogin() {
        Intent intent = new Intent(this, LoginActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
    }

    @Override
    protected void onResume() {
        super.onResume();
        //this.deleteDatabase("J4SP.db");
        //Log.i("DROP","    DB");
        mDBBuilder = new DBBuilder(this);
        //mDBBuilder.dropT(SQLiteHelper.TABLE_UPLOAD_HISTORY);
    }
}
