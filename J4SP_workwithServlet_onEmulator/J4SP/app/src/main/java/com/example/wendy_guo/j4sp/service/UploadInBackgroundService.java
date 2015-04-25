package com.example.wendy_guo.j4sp.service;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.IBinder;
import android.util.Log;

import com.example.wendy_guo.j4sp.Constants;
import com.example.wendy_guo.j4sp.pojo.OnTaskCompleted;
import com.example.wendy_guo.j4sp.pojo.UploadRecord;
import com.example.wendy_guo.j4sp.sql.DBBuilder;
import com.example.wendy_guo.j4sp.util.PreferencesManager;

import org.apache.http.HttpResponse;
import org.apache.http.HttpVersion;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.mime.MultipartEntity;
import org.apache.http.entity.mime.content.FileBody;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.CoreProtocolPNames;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashSet;

public class UploadInBackgroundService extends Service implements OnTaskCompleted {
    PreferencesManager mPreferencesManager;
    private DBBuilder mDBBuilder;
    private File file;
    private String[] records;
    private HashSet<Integer> set;

    @Override
    public void onCreate() {
        super.onCreate();
        mPreferencesManager = new PreferencesManager(this);
        Log.i("SERVICES_ON", "SERVICES_ON");

        String path = this.getFilesDir() + "/" + Constants.OFFLINE_CACHE;
        file = new File(path);
        if (file.exists()) {
            Log.i("File exist", "***");

            ConnectivityManager connManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
            if (connManager != null && connManager.getActiveNetworkInfo() != null
                    && connManager.getActiveNetworkInfo().isConnected()) {
                mDBBuilder = new DBBuilder(this);
                String content = null;
                InputStream in = null;
                StringBuilder text = new StringBuilder();
                try {
                    BufferedReader br = new BufferedReader(new FileReader(file));
                    String line;
                    while ((line = br.readLine()) != null) {
                        text.append(line);
                        //text.append("LineEnd");
                    }
                    br.close();
                    content = text.toString();
                    System.out.println(content);

                } catch (Exception e) {
                    Log.i("Exception", "!!!");
                } finally {
                    if (in != null) {
                        try {
                            in.close();
                        } catch (IOException e) {
                            Log.i("Cant Close", "!!!");
                        }
                    }
                }
                if (content != null) {
                    records = content.split("LineEnd");
                    set = new HashSet<Integer>();
                    for (int i = 0; i < records.length; i++ ) {
                        Log.i(records.length+"",records[i]);

                        String[] map = new String[3];
                        map[0] = Constants.HOST + Constants.PORT + "/ReceiptTracker/upload";
                        map[1] = records[i];
                        map[2] = i+"";

                        new UploadFile(this).execute(map);
                    }

                }
                else {
                    stopSelf();
                }
            }
            else{
                Log.i("No Internet Connection", "won't upload");
                stopSelf();
            }
        } else {
            Log.i("File not exist", "***");
            stopSelf();
        }
    }

    public UploadInBackgroundService() {
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.i("SERVICES_OFF", "SERVICES_OFF");
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onTaskCompleted(String feedback) {
        if (feedback == null || feedback.split("---").length != 2) {
            Log.i("Error",feedback);
            return;
        }

        String[] ret = feedback.split("---");

//        if (ret[0].equals(Constants.STATUS_OK)) {
//            String[] map = new String[3];
//            map[0] = Constants.HOST + Constants.PORT + "/search";
//            map[1] = records[Integer.parseInt(ret[1])];
//            map[2] = Integer.parseInt(ret[1])+"";
//
//            Log.i("feedback ok :", ret[1]);
//
//            new GET(this).execute(map);
//        } else {

            long result = -1;
            String[] jsonValue = ret[0].split(",");
            feedback = jsonValue[jsonValue.length - 1].replace("#", "");
            Log.i("feedback",ret[0]);
            try {
                result = Long.parseLong(feedback);
            } catch (NumberFormatException e) {
                Log.i("upload Error","upload Error");
            }
            if (result > 0) {
                long rowID = result;


                String[] rec = records[Integer.parseInt(ret[1])].split("##");
                UploadRecord record = new UploadRecord(rec[0], rec[2], rec[1],
                        rec[3], rec[5].split("app_cache_folder/")[1], rowID);
                mDBBuilder.insertUploadRecord(record);
                set.add(Integer.parseInt(ret[1]));
//                record.setID(id);
                Log.i("added","local db");
                Log.i("read finished", "DELETE SUCCESS");
                //boolean test = file.delete();
                boolean test2 = new File(rec[5]).delete();
                Log.i("test2 ", test2 + "");

                if(set.size() == records.length){
                    boolean test = file.delete();
                    Log.i("test ", test + "");
                    stopSelf();
                }


            } else {

                Log.i("feedback",feedback);



            }


       // }
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


            String[] each = params[1].split("##");
            builder.appendQueryParameter("name", each[0]);
            builder.appendQueryParameter("total", each[1]);
            builder.appendQueryParameter("date", each[2]);
            builder.appendQueryParameter("review", each[3]);
            builder.appendQueryParameter("location", each[4]);
            builder.appendQueryParameter("file_path", each[5].split("app_cache_folder/")[1]);
            builder.appendQueryParameter("ID", each[6]);

            String url = builder.build().toString();
            httpPost = new HttpPost(url);
            MultipartEntity mpEntity = new MultipartEntity();

            FileBody cbFile = new FileBody(new File(each[5]), "image/png");

            Log.i("PATH",each[5]);
            cbFile.getMediaType();
            mpEntity.addPart("userfile", cbFile);
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

            return result + "---" +params[2];

        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            listener.onTaskCompleted(s);
        }

    }

//    private class GET extends AsyncTask<String, Integer, String> {
//        HttpURLConnection connection;
//        private OnTaskCompleted listener;
//        private String result = "";
//
//        public GET(OnTaskCompleted listener) {
//            this.listener = listener;
//        }
//
//        @Override
//        protected void onPreExecute() {
//            super.onPreExecute();
//        }
//
//        @Override
//        protected String doInBackground(String... params) {
//            Uri.Builder builder = Uri.parse(params[0]).buildUpon();
//
//
//            String[] each = params[1].split("##");
//            builder.appendQueryParameter("name", each[0]);
//            builder.appendQueryParameter("total", each[1]);
//            builder.appendQueryParameter("date", each[2]);
//            builder.appendQueryParameter("review", each[3]);
//            builder.appendQueryParameter("location", each[4]);
//            builder.appendQueryParameter("file_path", each[5].split("app_cache_folder/")[1]);
//            builder.appendQueryParameter("ID", each[6]);
//
//            try {
//                URL url = new URL(builder.build().toString());
//                connection = (HttpURLConnection) url.openConnection();
//                connection.setDoOutput(false);
//                InputStream inputStream = connection.getInputStream();
//                if (inputStream != null) {
//                    BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
//                    String line = "";
//                    while ((line = bufferedReader.readLine()) != null)
//                        result += line;
//                    inputStream.close();
//                }
//            } catch (Exception e) {
//                Log.d("InputStream", e.getLocalizedMessage());
//            }
//
//            return result + "---" +params[2];
//        }
//
//
//        @Override
//        protected void onPostExecute(String s) {
//            super.onPostExecute(s);
//            listener.onTaskCompleted(s);
//        }
//
//
//    }


}
