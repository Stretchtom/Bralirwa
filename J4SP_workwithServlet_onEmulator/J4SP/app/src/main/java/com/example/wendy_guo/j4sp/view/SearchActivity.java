package com.example.wendy_guo.j4sp.view;

import android.app.ActionBar;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.wendy_guo.j4sp.Constants;
import com.example.wendy_guo.j4sp.R;
import com.example.wendy_guo.j4sp.ViewRecordListActivity;
import com.example.wendy_guo.j4sp.pojo.OnTaskCompleted;

import org.apache.http.HttpResponse;
import org.apache.http.HttpVersion;
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
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.Locale;

public class SearchActivity extends Activity implements OnTaskCompleted {

    private Button mButton;
    private EditText mName;
    private EditText mTotal;
    private static TextView mDate;
    private String name;
    private String date;
    private String total;

    private SharedPreferences sharedPref ;

    private SimpleDateFormat sdfDisplay = new SimpleDateFormat(Constants.TIME_STAMP_FORMAT_DISPLAY, Locale.US);
    private LinkedHashMap<String, String> LoginMap ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        final ActionBar actionBar = getActionBar();
        actionBar.hide();
        sharedPref = getSharedPreferences(
                getString(R.string.preference_file_key), Context.MODE_PRIVATE);
        mName = (EditText) findViewById(R.id.search_name);
        mDate = (TextView) findViewById(R.id.search_date);
        mDate.setText(sdfDisplay.format(new Date()));

        mTotal = (EditText) findViewById(R.id.search_total);
        mButton = (Button) findViewById(R.id.search_button);
        mButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                name = mName.getText().toString().trim();
                total = mTotal.getText().toString().trim();
                date = mDate.getText().toString().trim();

                if(name.isEmpty() && total.isEmpty() && date.isEmpty()){
                    AlertDialog.Builder builder = new AlertDialog.Builder(SearchActivity.this);
                    builder.setMessage(R.string.no_input)
                            .setTitle(R.string.error_title)
                            .setPositiveButton(android.R.string.ok, null);
                    AlertDialog dialog = builder.create();
                    dialog.show();
                    return;
                }

                ConnectivityManager connManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
                NetworkInfo ni = connManager.getActiveNetworkInfo();
                if (ni == null) {
                    // There are no active networks.
                    Toast.makeText(SearchActivity.this, "Can not search. \nPlease check your Internet Connection", Toast.LENGTH_LONG).show();

                } else{
                    LoginMap = new LinkedHashMap<String, String>();
                    if(name != null && name.length() > 0)
                        LoginMap.put("name", name);
                    if(total != null && total.length() > 0)
                        LoginMap.put("total", total);
                    LoginMap.put("date", date);
                    LoginMap.put("ID", sharedPref.getInt(Constants.USER_ID, -1) + "");
                    Toast.makeText(SearchActivity.this, "Will Search", Toast.LENGTH_LONG).show();
                    String[] s = new String[]{Constants.HOST+ Constants.PORT + "/ReceiptTracker/search"};
                    new GET(SearchActivity.this).execute(s);

                }
            }


        });

        mDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDatePickerDialog();
            }
        });

    }
    public void showDatePickerDialog() {
        DialogFragment newFragment = new DatePickerFragment();
        newFragment.show(this.getFragmentManager(), "datePicker");
    }

    @Override
    public void onTaskCompleted(String feedback) {
        if(feedback != null) {
            System.out.println(feedback);

            Intent i = new Intent(this, ViewRecordListActivity.class);
            i.putExtra(Constants.SEARCH_RESULT, feedback);
            startActivity(i);
            finish();
        }

    }

    private class GET extends AsyncTask<String, Integer, String> {
        HttpURLConnection connection;
        private OnTaskCompleted listener;
        private String result = "";

        public GET(OnTaskCompleted listener){
            this.listener=listener;
        }
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected String doInBackground(String... params) {
            Uri.Builder builder = Uri.parse(params[0]).buildUpon();
            InputStream inputStream = null;
            for (String name : LoginMap.keySet()) {
                builder.appendQueryParameter(name, LoginMap.get(name).toString());
            }
            try {
                URL url = new URL(builder.build().toString());
                connection = (HttpURLConnection) url.openConnection();
                connection.setDoOutput(false);
                inputStream = connection.getInputStream();

                if (inputStream != null) {
                    BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
                    String line = "";
                    while ((line = bufferedReader.readLine()) != null)
                        result += line;
                    inputStream.close();
                }
            } catch (Exception e) {
                Log.d("InputStream", e.getLocalizedMessage());
            }finally {
                if(connection != null)
                    connection.disconnect();
                if(inputStream != null){
                    try {
                        inputStream.close();
                    }catch (IOException e){}
                }
            }

            return result;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            listener.onTaskCompleted(s);
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
            if(month < 9){
                date = date + "0"+(month+1) + "-";
            }
            else
                date = date + (month+1) + "-";
            if(day < 10){
                date = date + "0" + day;
            }
            else
                date = date + day;
            mDate.setText(date);
        }
    }

}