package com.example.wendy_guo.j4sp.view;

import android.app.ActionBar;
import android.app.Activity;
import android.app.AlertDialog;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.net.ConnectivityManager;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.wendy_guo.j4sp.Constants;
import com.example.wendy_guo.j4sp.R;
import com.example.wendy_guo.j4sp.pojo.OnTaskCompleted;
import com.example.wendy_guo.j4sp.pojo.UploadRecord;
import com.example.wendy_guo.j4sp.sql.DBBuilder;
import com.example.wendy_guo.j4sp.util.FileHelper;

import org.apache.http.HttpResponse;
import org.apache.http.HttpVersion;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.mime.MultipartEntity;
import org.apache.http.entity.mime.content.ByteArrayBody;
import org.apache.http.entity.mime.content.ContentBody;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.params.CoreProtocolPNames;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.Locale;


public class UploadActivity extends Activity implements OnTaskCompleted {

    private Uri mUri;
    private String mFileType;
    private Button mButton;
    private EditText mName;
    private EditText mTotal;
    private static TextView mDate;
    private String name;
    private String date;
    private String total;
    private String comments = "NULL";
    private EditText mComments;
    private SimpleDateFormat sdfDisplay = new SimpleDateFormat(Constants.TIME_STAMP_FORMAT_DISPLAY, Locale.US);
    public boolean mImagePreference;
    public boolean mOfflineEnabled;
    protected float totalSize = 0;

    private String mLocation = "NULL";
    private ImageView mImageView;
    private Bitmap mBitmap;
    DialogInterface.OnClickListener mDialogListener;
    private LinkedHashMap<String, String> LoginMap;
    private File mImagePath;
    private String fileName;
    private SharedPreferences sharedPref;
    private byte[] fileBytes;
    private long rowID;
    private String filePath;


    private DBBuilder mDBBuilder;


//    String attachmentName = "bitmap";
//    String attachmentFileName = "bitmap.bmp";
//    String crlf = "\r\n";
//    String twoHyphens = "--";
//    String boundary =  "*****";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_upload);
        final ActionBar actionBar = getActionBar();
        actionBar.hide();
        mUri = getIntent().getData();
        filePath = getIntent().getStringExtra(Constants.PHOTO_PATH);
        mFileType = getIntent().getExtras().getString(Constants.KEY_TYPE);

        mName = (EditText) findViewById(R.id.fileName);
        mDate = (TextView) findViewById(R.id.date);
        mDate.setText(sdfDisplay.format(new Date()));
        mDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDatePickerDialog(v);
            }
        });
        mTotal = (EditText) findViewById(R.id.total);
        mComments = (EditText) findViewById(R.id.comment);

        mDBBuilder = new DBBuilder(this);
        mImageView = (ImageView) findViewById(R.id.test);
        sharedPref = UploadActivity.this.getSharedPreferences(
                getString(R.string.preference_file_key), Context.MODE_PRIVATE);
        mOfflineEnabled = sharedPref.getBoolean(Constants.PREF_KEY_UPLOAD_WIFI, false);

        try {
            mBitmap = MediaStore.Images.Media.getBitmap(
                    UploadActivity.this.getContentResolver(), mUri);
            mImageView.setImageBitmap(mBitmap);
        } catch (IOException e) {
            e.printStackTrace();
        }
        mButton = (Button) findViewById(R.id.sendButton);
        mButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setProgressBarIndeterminateVisibility(true);
                name = mName.getText().toString().trim();
                total = mTotal.getText().toString().trim();
                comments = mComments.getText().toString().trim();
                date = mDate.getText().toString().trim();

                if (name.isEmpty() || total.isEmpty() || comments.isEmpty()) {
                    AlertDialog.Builder builder = new AlertDialog.Builder(UploadActivity.this);
                    builder.setMessage(R.string.no_input)
                            .setTitle(R.string.error_title)
                            .setPositiveButton(android.R.string.ok, null);
                    AlertDialog dialog = builder.create();
                    dialog.show();
                    return;
                }

                ConnectivityManager connManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
                if (connManager != null && connManager.getActiveNetworkInfo() != null
                        && connManager.getActiveNetworkInfo().isConnected()) {
//                    NetworkInfo mWifi = connManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI);

                    Log.i("", "Connected: " + connManager.getActiveNetworkInfo() +
                            "  Enabled: " + mOfflineEnabled);

                    //get bytes
                    //fileBytes = FileHelper.getByteArrayFromFile(UploadActivity.this, mUri);
                    fileBytes = FileHelper.getB(UploadActivity.this, mUri);


                    mImagePreference = sharedPref.getBoolean(Constants.PREF_KEY_IMAGE_QUALITY, false);

                    if (mImagePreference) {
                        fileBytes = FileHelper.reduceImageForUpload(fileBytes);
                    }
                    Log.i("URI", mUri + "");

                    //get info
                    StringBuilder sb = new StringBuilder();
                    sb.append(sharedPref.getInt(Constants.USER_ID, -1));
                    sb.append("_");
                    sb.append(new SimpleDateFormat("yyyyMMdd_HHmmss", Locale.US).format(new Date()));
                    sb.append(".jpg");

                    fileName = sb.toString();
                    LoginMap = new LinkedHashMap<String, String>();
                    LoginMap.put("name", name);
                    LoginMap.put("total", total);
                    LoginMap.put("date", date);
                    LoginMap.put("review", comments);
                    LoginMap.put("location", mLocation);
                    LoginMap.put("file_path", fileName);
                    LoginMap.put("ID", sharedPref.getInt(Constants.USER_ID, -1) + "");


///
//                    attachmentName = fileName;
//                    attachmentFileName = attachmentName+".bmp";


                    Toast.makeText(UploadActivity.this, "Will Upload or abort", Toast.LENGTH_SHORT).show();

                    //UPLOAD WITH ASYNC TASK
                    String[] s = new String[]{Constants.HOST + Constants.PORT + "/ReceiptTracker/upload"};
                    new UploadFile(UploadActivity.this).execute(s);
//  String[] s = new String[]{Constants.HOST + Constants.PORT + "/photoOnly"};
//
//                    new POSTImage().execute(s);
//


                } else if (mOfflineEnabled) {
                    Toast.makeText(UploadActivity.this, "WILL WAITE",
                            Toast.LENGTH_SHORT).show();

                    Context mContext = getApplicationContext();

                    //mImagePath = FileHelper.saveImageToInternalStorage(mBitmap, mContext);
                    //name, date, total, comments, fileName, rowID
                    StringBuilder sb = new StringBuilder();
                    sb.append(name);
                    sb.append("##");
                    sb.append(total);
                    sb.append("##");
                    sb.append(date);
                    sb.append("##");
                    sb.append(comments);
                    sb.append("##");
                    sb.append(mLocation);
                    sb.append("##");

                    mImagePath = FileHelper.saveImageToInternalStorage(mBitmap, mContext);
                    sb.append(mImagePath);
                    sb.append("##");
                    sb.append(sharedPref.getInt(Constants.USER_ID, -1) + "");
                    sb.append("LineEnd");


                    try {
                        FileOutputStream fos = openFileOutput(Constants.OFFLINE_CACHE, Context.MODE_APPEND);
                        fos.write(sb.toString().getBytes());
                        fos.close();
                        Log.i("write to internal", "success");

                        SharedPreferences sharedPref = UploadActivity.this.getSharedPreferences(
                                getString(R.string.preference_file_key), Context.MODE_PRIVATE);
                        SharedPreferences.Editor editor = sharedPref.edit();
                        editor.putBoolean(Constants.PREF_KEY_CACHE_NOT_EMPTY, true);
                        editor.commit();
                        Log.i("___sb______", sb.toString());
                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                    finish();

                }
            }
        });

        Intent locationIntent = new Intent(UploadActivity.this, GetLocationActivity.class);
        startActivityForResult(locationIntent, 0);

        mDialogListener =
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        switch (which) {
                            case 0:
                                Intent shareIntent = new Intent(UploadActivity.this, ShareActivity.class);
                                shareIntent.setData(mUri);
                                startActivity(shareIntent);
                                finish();
                                break;
                            case 1:
                                Intent smsIntent = new Intent(UploadActivity.this, SMSActivity.class);
                                smsIntent.setData(mUri);
                                startActivity(smsIntent);
                                finish();
                                break;
                            case 2:
                                finish();
                                break;

                        }
                    }
                };
    }


//            mImagePreference = mPreferencesManager.getBooleanPreference(Constants.PREF_KEY_IMAGE_QUALITY);
//            //Toast.makeText(UploadActivity.this, mImagePreference+"", Toast.LENGTH_LONG).show();

    public void showDatePickerDialog(View v) {
        DialogFragment newFragment = new DatePickerFragment();
        newFragment.show(this.getFragmentManager(), "datePicker");
    }

    @Override
    protected void onResume() {//should store user input in bundle ????
        super.onResume();
    }

    @Override
    public void onTaskCompleted(String feedback) {

        if (feedback == null || feedback.length() == 0) {
            Toast.makeText(this, "Network Error", Toast.LENGTH_SHORT).show();
            return;
        }
        Log.i("Upload :", feedback);

        long result = -1;
        try {
            result = Long.parseLong(feedback);
        } catch (NumberFormatException e) {
            Toast.makeText(this, "upload Error", Toast.LENGTH_LONG).show();
        }
        if (result > 0) {
            rowID = result;
            UploadRecord record = new UploadRecord(name, date, total, comments, fileName, rowID);
            mDBBuilder.insertUploadRecord(record);
            AlertDialog.Builder builder = new AlertDialog.Builder(UploadActivity.this);
            builder.setItems(R.array.yN, mDialogListener);
            builder.setTitle("Continue to share will friends?");
            AlertDialog dialog = builder.create();
            dialog.show();


        } else {

            Toast.makeText(this, feedback, Toast.LENGTH_SHORT).show();
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            String error = "Can not get row ID";
            builder.setMessage(error)
                    .setTitle(R.string.error_title)
                    .setPositiveButton(android.R.string.ok, null);
            AlertDialog dialog = builder.create();
            dialog.show();

        }

    }

    public static class DatePickerFragment extends DialogFragment
            implements DatePickerDialog.OnDateSetListener {

        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {
            final Calendar c = Calendar.getInstance();
            int year = c.get(Calendar.YEAR);
            int month = c.get(Calendar.MONTH);
            int day = c.get(Calendar.DAY_OF_MONTH);

            return new DatePickerDialog(getActivity(), this, year, month, day);
        }

        public void onDateSet(DatePicker view, int year, int month, int day) {
            String date = year + "-";
            if (month < 9) {
                date = date + "0" + (month + 1) + "-";
            } else
                date = date + (month + 1) + "-";
            if (day < 10) {
                date = date + "0" + day;
            } else
                date = date + day;
            mDate.setText(date);
        }
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == RESULT_OK) {
            mLocation = data.getExtras().getString(Constants.KEY_LOCATION);

        } else {
            Toast.makeText(this, "get GEO canceled", Toast.LENGTH_SHORT).show();
        }
    }


    private class UploadFile extends AsyncTask<String, Integer, String> {

        HttpClient httpClient;
        HttpPost httpPost;
        OnTaskCompleted listener;

        public UploadFile(OnTaskCompleted listener) {
            this.listener = listener;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();


            httpClient = new DefaultHttpClient();
            httpClient.getParams().setParameter(CoreProtocolPNames.PROTOCOL_VERSION, HttpVersion.HTTP_1_1);

        }

        @Override
        protected String doInBackground(String... params) {

            Uri.Builder builder = Uri.parse(params[0]).buildUpon();
            for (String name : LoginMap.keySet()) {
                builder.appendQueryParameter(name, LoginMap.get(name).toString());
            }

            String url = builder.build().toString();
            httpPost = new HttpPost(url);
            MultipartEntity mpEntity = new MultipartEntity();
            Log.i("filename", fileName);
            ContentBody mimePart = new ByteArrayBody(fileBytes, fileName);

            mpEntity.addPart("userfile", mimePart);
//            FileBody cbFile = new FileBody(new File(filePath), "image/png");
//            cbFile.getMediaType();
//            mpEntity.addPart("userfile", cbFile);

            httpPost.setEntity(mpEntity);

            String result = "";
            HttpResponse httpResponse = null;

            try {
                httpResponse = httpClient.execute(httpPost);

                InputStream inputStream = httpResponse.getEntity().getContent();

                if (inputStream != null) {
                    BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
                    String line = "";
                    while ((line = bufferedReader.readLine()) != null)
                        result += line;
                    inputStream.close();
                    Log.i("test", result);
                }
            } catch (Exception e) {
                Log.d("InputStream", e.getLocalizedMessage());
            }

            return result;

        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            listener.onTaskCompleted(s);
        }

    }


/*
    private class POSTImage extends AsyncTask<String, Integer, String> {

        HttpURLConnection connection;
        private String result = "";

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected String doInBackground(String... params) {
            Uri.Builder builder = Uri.parse(params[0]).buildUpon();

            try {
                URL url = new URL(builder.build().toString());
                connection = (HttpURLConnection) url.openConnection();
                connection.setUseCaches(false);
                connection.setDoOutput(true);
                connection.setRequestMethod("POST");
                connection.setRequestProperty("Connection", "Keep-Alive");
                connection.setRequestProperty("Cache-Control", "no-cache");
                connection.setRequestProperty("Content-Type", "multipart/form-data;boundary=" + boundary);


                DataOutputStream request = new DataOutputStream(connection.getOutputStream());

                request.writeBytes(twoHyphens + boundary + crlf);
                request.writeBytes("Content-Disposition: form-data; name=\"" + attachmentName + "\";filename=\"" + attachmentFileName + "\"" + crlf);
                request.writeBytes(crlf);

                request.write(fileBytes);

                request.writeBytes(crlf);
                request.writeBytes(twoHyphens + boundary + twoHyphens + crlf);


                request.flush();
                request.close();

                InputStream inputStream = connection.getInputStream();
                if (inputStream != null) {
                    BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
                    String line = "";
                    while ((line = bufferedReader.readLine()) != null)
                        result += line;
                    inputStream.close();
                }
            } catch (Exception e) {
                Log.d("InputStream", e.getLocalizedMessage());
            }

            return result;
        }


        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            System.out.print(s);
            System.out.print(s);
            System.out.print(s);
            System.out.print(s);
        }


    }
*/

}


