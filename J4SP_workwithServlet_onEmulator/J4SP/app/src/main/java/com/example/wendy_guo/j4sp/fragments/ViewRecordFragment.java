package com.example.wendy_guo.j4sp.fragments;

import android.app.AlertDialog;
import android.support.v4.app.ListFragment;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.example.wendy_guo.j4sp.Constants;
import com.example.wendy_guo.j4sp.pojo.OnTaskCompleted;
import com.example.wendy_guo.j4sp.R;
import com.example.wendy_guo.j4sp.adapter.RecordListAdapter;
import com.example.wendy_guo.j4sp.pojo.UploadRecord;
import com.example.wendy_guo.j4sp.sql.DBBuilder;
import com.example.wendy_guo.j4sp.util.PreferencesManager;

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
import java.util.ArrayList;
import java.util.LinkedHashMap;

import com.example.wendy_guo.j4sp.view.ViewSummaryActivity;

public class ViewRecordFragment extends ListFragment implements OnTaskCompleted {

    private RecordListAdapter mAdapter;
    private PreferencesManager mPreferencesManager;
    private DBBuilder mDBBuilder;
    private LinkedHashMap<String, String> LoginMap ;
    DialogInterface.OnClickListener  mDialogListener;
    long rID;
    String pathToDelete;

    private DBBuilder builder;



    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        builder = new DBBuilder(getActivity());
        ArrayList<UploadRecord> list = builder.readUploadHistory();
        Log.i("Table SIZE", list.size()+"");
        mAdapter = new RecordListAdapter(getActivity(), builder.readUploadHistory());


        mDialogListener =
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        switch (which) {
                            case 0:
                                LoginMap = new LinkedHashMap<String, String>();
                                LoginMap.put("rID", rID+"");
                                String[] s = new String[]{Constants.HOST + Constants.PORT +"/ReceiptTracker/delete"};
                                new GET(ViewRecordFragment.this).execute(s);
                                break;
                            case 1:
                                break;


                        }
                    }
                };
    }
    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);


        setListAdapter(mAdapter);
        mPreferencesManager = new PreferencesManager(getActivity());

        getListView().setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {

            @Override
            public boolean onItemLongClick(AdapterView<?> parent,
                                           View view, int position, long id) {

                UploadRecord record = (UploadRecord) getListAdapter().getItem(position);

                rID = record.getrID();

                pathToDelete = record.getPath();
                mDBBuilder = new DBBuilder(getActivity());
                Toast.makeText(getActivity(), "Will delete", Toast.LENGTH_LONG).show();

                mDBBuilder.deleteUploadRecordByID(rID);
                AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                builder.setItems(R.array.Choice, mDialogListener);
                builder.setTitle("Do you want to delete this record on backend? ");
                AlertDialog dialog = builder.create();
                dialog.show();

                refreshRecord();
                return true;
            }
        });
    }
        @Override
    public void onListItemClick(ListView l, View v, int position, long id) {
        UploadRecord record = (UploadRecord) getListAdapter().getItem(position);
//        Toast.makeText(this, record.getID() + " is selected", Toast.LENGTH_LONG).show();
        Intent summaryIntent = new Intent(getActivity(), ViewSummaryActivity.class);
        summaryIntent.putExtra("record", record);
        startActivity(summaryIntent);
    }
    @Override
    public void onResume() {
        super.onResume();
        SharedPreferences sharedPref = getActivity().getSharedPreferences(
                getString(R.string.preference_file_key), Context.MODE_PRIVATE);
        boolean refresh = sharedPref.getBoolean(Constants.PREF_KEY_REFRESH, false);
        if(refresh){
            Toast.makeText(getActivity(), " Frag Refreshing", Toast.LENGTH_LONG).show();
            refreshRecord();
            mPreferencesManager.setBooleanPreference(Constants.PREF_KEY_REFRESH,false);

        }
    }

    private void refreshRecord(){
        DBBuilder builder = new DBBuilder(getActivity());
        mAdapter = new RecordListAdapter(getActivity(), builder.readUploadHistory());
        setListAdapter(mAdapter);
    }

    @Override
    public void onTaskCompleted(String feedback) {
        if (feedback == null || feedback.length() == 0) {
            Toast.makeText(getActivity(), "Connection error", Toast.LENGTH_LONG).show();
            return;
        }
        Log.i("DELETE RECORD :", feedback);

        if (feedback.equals(Constants.DELETE_FILE_OK)) {
            Toast.makeText(getActivity(), "DELETE SUCCESS!", Toast.LENGTH_LONG).show();

            return;
        }
         else {

            int result = -1;
            try {
                result = Integer.parseInt(feedback);
            } catch (NumberFormatException e) {
                Toast.makeText(getActivity(), "ERROR", Toast.LENGTH_LONG).show();

            }
            if (result == 1) {
                String[] s = new String[]{Constants.HOST + Constants.PORT+"/removeFile"};
                //setProgressBarIndeterminateVisibility(true);
                LoginMap = new LinkedHashMap<String, String>();
                LoginMap.put("path",pathToDelete);
                new GET(this).execute(s);

            } else {
                Toast.makeText(getActivity(), feedback, Toast.LENGTH_LONG).show();
                AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                String error = "ERROR";

                builder.setMessage(error)
                        .setTitle(R.string.error_title)
                        .setPositiveButton(android.R.string.ok, null);
                AlertDialog dialog = builder.create();
                dialog.show();

            }
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
}
