package com.example.wendy_guo.j4sp.view;

import android.app.ActionBar;
import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.wendy_guo.j4sp.Constants;
import com.example.wendy_guo.j4sp.pojo.OnTaskCompleted;
import com.example.wendy_guo.j4sp.R;
import com.example.wendy_guo.j4sp.pojo.UploadRecord;
import com.example.wendy_guo.j4sp.sql.DBBuilder;

import org.apache.commons.io.output.ByteArrayOutputStream;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.HttpVersion;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.CoreProtocolPNames;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.LinkedHashMap;

/*
 *this activity show user detailed info of record
 *and download image from backend()
 */
public class ViewSummaryActivity extends Activity implements OnTaskCompleted {

    private Button mSaveButton;
    private Button mDetailButton;
    private EditText mName;
    private EditText mTotal;
    private EditText mComments;
    private TextView mDate;
    private DBBuilder mDBBuilder;
    private ImageView mImageView;
    private LinkedHashMap<String, String> LoginMap;
    Bitmap bmp;
    private int code ;
    private UploadRecord record = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_summary);
        final ActionBar actionBar = getActionBar();
        actionBar.hide();
        Bundle data = getIntent().getExtras();
        record = data.getParcelable("record");
        code = getIntent().getIntExtra(Constants.SUMMARY_CODE, 0);
        mName = (EditText) findViewById(R.id.summaryFileName);
        mName.setText(record.getName());


        mTotal = (EditText) findViewById(R.id.summaryTotal);
        mTotal.setText(record.getTotal());

        mDate = (TextView) findViewById(R.id.summaryDate);
        mDate.setText(record.getDate());


        mComments = (EditText) findViewById(R.id.summaryComment);
        mComments.setText(record.getComments());
        mSaveButton = (Button) findViewById(R.id.saveButton);
        if(code == 1){
            mSaveButton.setText("Save to local");
            mName.setFocusable(false);
            mName.setClickable(false);
            mTotal.setFocusable(false);
            mTotal.setClickable(false);
            mComments.setFocusable(false);
            mComments.setClickable(false);
        }
        mDetailButton = (Button) findViewById(R.id.show);
        mImageView = (ImageView) findViewById(R.id.receipt);

        mDetailButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                LoginMap = new LinkedHashMap<String, String>();
                LoginMap.put("rID", "uploads/" + record.getPath());
                String[] s = new String[]{Constants.HOST+ Constants.PORT + "/ReceiptTracker/download_photo"};
                new GETPhoto(ViewSummaryActivity.this).execute(s);
            }
        });
        mSaveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(code == 1){
                    mDBBuilder = new DBBuilder(getApplication());
                    mDBBuilder.insertUploadRecord(record);
                    Toast.makeText(ViewSummaryActivity.this,"Record Saved !",Toast.LENGTH_SHORT).show();
                    return;
                }
                SharedPreferences sharedPref = ViewSummaryActivity.this.getSharedPreferences(
                        getString(R.string.preference_file_key), Context.MODE_PRIVATE);
                SharedPreferences.Editor editor = sharedPref.edit();
                editor.putBoolean(Constants.PREF_KEY_REFRESH, true);
                editor.commit();

                String name = mName.getText().toString();
                String total = mTotal.getText().toString();
                String comments = mComments.getText().toString();
                UploadRecord r = new UploadRecord(name, record.getDate(), total, comments, record.getPath(), record.getrID());
                LoginMap = new LinkedHashMap<String, String>();

                LoginMap.put("changes", "name = '" + name +
                        "', total = '" + total +
                        "', review = '" + comments + "'");
                LoginMap.put("condition", "rID = '" + record.getrID() + "'");


                mDBBuilder = new DBBuilder(ViewSummaryActivity.this);
                mDBBuilder.updateUploadRecordInfo(r);
                String[] s = new String[]{Constants.HOST+ Constants.PORT + "/ReceiptTracker/update"};
                new GET().execute(s);

                finish();


            }
        });


    }

    @Override
    public void onTaskCompleted(String feedback) {
        Dialog builder = new Dialog(this);
        builder.requestWindowFeature(Window.FEATURE_NO_TITLE);
        builder.getWindow().setBackgroundDrawable(
                new ColorDrawable(android.graphics.Color.TRANSPARENT));
        builder.setOnDismissListener(new DialogInterface.OnDismissListener() {
            @Override
            public void onDismiss(DialogInterface dialogInterface) {
            }
        });
        ImageView imageView = new ImageView(this);

        imageView.setImageBitmap(bmp);
        builder.addContentView(imageView, new RelativeLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT));
        builder.show();
    }

    private class GET extends AsyncTask<String, Integer, Void> {
        private HttpURLConnection connection;
        private String result = "";

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected Void doInBackground(String... params) {
            Uri.Builder builder = Uri.parse(params[0]).buildUpon();
            for (String name : LoginMap.keySet()) {
                builder.appendQueryParameter(name, LoginMap.get(name).toString());
            }


            try {
                URL url = new URL(builder.build().toString());
                connection = (HttpURLConnection) url.openConnection();
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

            return null;
        }


        @Override
        protected void onPostExecute(Void s) {
            super.onPostExecute(s);
        }


    }
    private class GETPhoto extends AsyncTask<String, Integer, Void> {
        HttpURLConnection connection;
        private OnTaskCompleted listener;
        private String result = "";


        public GETPhoto(OnTaskCompleted lis){
            listener = lis;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected Void doInBackground(String... params) {
            Uri.Builder builder = Uri.parse(params[0]).buildUpon();
            for (String name : LoginMap.keySet()) {
                builder.appendQueryParameter(name, LoginMap.get(name).toString());
            }


            try {
                URL url = new URL(builder.build().toString());
                connection = (HttpURLConnection) url.openConnection();
                connection.setDoOutput(false);

                InputStream inputStream = connection.getInputStream();
                if (connection.getResponseCode() == HttpURLConnection.HTTP_OK) {
                    downloadDisplay(inputStream);


                }
            } catch (ClientProtocolException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();

            }
            return null;
        }

        @Override
        protected void onPostExecute(Void v) {
            super.onPostExecute(v);
            listener.onTaskCompleted("");



        }

        public void downloadDisplay(InputStream inStream) {

            try {

                ByteArrayOutputStream stream = new ByteArrayOutputStream();
                int bufferSize = 1024;
                byte[] buffer = new byte[bufferSize];

                int len = 0;
                while ((len = inStream.read(buffer)) != -1) {
                    stream.write(buffer, 0, len);
                }
                byte[] b = stream.toByteArray();
                Log.i("Building photo....", b.length + "");

                bmp = BitmapFactory.decodeByteArray(b, 0, b.length);

                stream.close();

            } catch (IOException e) {
                e.printStackTrace();
            }
        }



    }
}