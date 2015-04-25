package com.example.wendy_guo.j4sp.fragments;

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
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.wendy_guo.j4sp.Constants;
import com.example.wendy_guo.j4sp.R;
import com.example.wendy_guo.j4sp.ViewRecordListActivity;
import com.example.wendy_guo.j4sp.pojo.ActivityCallBack;
import com.example.wendy_guo.j4sp.pojo.OnTaskCompleted;

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


public class SearchFragment extends Fragment implements OnTaskCompleted {

    private static TextView mDate;
    private static String date;
    private static ActivityCallBack mActivityCallBack;

    private Button mButton;
    private EditText mName;
    private EditText mTotal;
    private String name;
    private String total;
    private SharedPreferences sharedPref;

    private SimpleDateFormat sdfDisplay = new SimpleDateFormat(Constants.TIME_STAMP_FORMAT_DISPLAY, Locale.US);
    private LinkedHashMap<String, String> QueryMap;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        mActivityCallBack = (ActivityCallBack) activity;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        final FrameLayout layout = (FrameLayout) inflater.inflate(R.layout.fragment_search, container, false);
        sharedPref = getActivity().getSharedPreferences(
                getString(R.string.preference_file_key), Context.MODE_PRIVATE);
        mName = (EditText) layout.findViewById(R.id.search_name_f);
        mDate = (TextView) layout.findViewById(R.id.search_date_f);
        mTotal = (EditText) layout.findViewById(R.id.search_total_f);
        mButton = (Button) layout.findViewById(R.id.search_button_f);
        if (date == null) {
            date = sdfDisplay.format(new Date());
            mDate.setText(date);
        } else
            mDate.setText(date);
        mDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDatePickerDialog(layout);
            }
        });
        mButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                name = mName.getText().toString().trim();
                total = mTotal.getText().toString().trim();
                date = mDate.getText().toString().trim();
                Log.i("try", "");
                Toast.makeText(getActivity(), name, Toast.LENGTH_LONG).show();
                if (name.isEmpty() && total.isEmpty() && date.isEmpty()) {
                    AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                    builder.setMessage(R.string.no_input)
                            .setTitle(R.string.error_title)
                            .setPositiveButton(android.R.string.ok, null);
                    AlertDialog dialog = builder.create();
                    dialog.show();
                    return;
                }

                ConnectivityManager connManager = (ConnectivityManager) getActivity().getSystemService(Context.CONNECTIVITY_SERVICE);
                NetworkInfo ni = connManager.getActiveNetworkInfo();
                if (ni == null) {
                    // There are no active networks.
                    Toast.makeText(getActivity(), "Can not search. \nPlease check your Internet Connection", Toast.LENGTH_LONG).show();
                    return;
                } else {
                    QueryMap = new LinkedHashMap<String, String>();
                    if (name != null && name.length() > 0)
                        QueryMap.put("name", name);
                    if (total != null && total.length() > 0)
                        QueryMap.put("total", total);
                    QueryMap.put("date", date);
                    QueryMap.put("ID", sharedPref.getInt(Constants.USER_ID, -1) + "");
                    Toast.makeText(getActivity(), "Will Search", Toast.LENGTH_LONG).show();
                    String[] s = new String[]{Constants.HOST + Constants.PORT + "/ReceiptTracker/search"};
                    new GET(SearchFragment.this).execute(s);

                }


            }


        });
        return layout;

    }

    public void showDatePickerDialog(View v) {
        DialogFragment newFragment = new DatePickerFragment();
        newFragment.show(getActivity().getFragmentManager(), "Date Picker");
    }

    @Override
    public void onTaskCompleted(String feedback) {
        if (feedback != null) {
            System.out.println(feedback);

            Intent i = new Intent(getActivity(), ViewRecordListActivity.class);
            i.putExtra(Constants.SEARCH_RESULT, feedback);
            startActivity(i);
            mName.setText("");
            mTotal.setText("");
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
            for (String name : QueryMap.keySet()) {
                builder.appendQueryParameter(name, QueryMap.get(name).toString());
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
            date = year + "-";
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
            Log.i("here",date);
            mActivityCallBack.callBack();


        }
    }

}
