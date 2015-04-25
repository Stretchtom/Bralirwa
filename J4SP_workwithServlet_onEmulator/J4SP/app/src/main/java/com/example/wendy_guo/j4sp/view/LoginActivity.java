package com.example.wendy_guo.j4sp.view;

import android.app.ActionBar;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;


import com.example.wendy_guo.j4sp.Constants;
import com.example.wendy_guo.j4sp.pojo.OnTaskCompleted;
import com.example.wendy_guo.j4sp.R;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.LinkedHashMap;


public class LoginActivity extends Activity implements OnTaskCompleted {

    protected EditText mUsername;
    protected EditText mPassword;
    protected Button mLoginButton;

    protected TextView mSignUpTextView;
    private String username;
    private String password;
    private LinkedHashMap<String, String> LoginMap = new LinkedHashMap<String, String>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        requestWindowFeature(Window.FEATURE_INDETERMINATE_PROGRESS);
        setContentView(R.layout.activity_login);

        final ActionBar actionBar = getActionBar();
        actionBar.hide();
        mSignUpTextView = (TextView) findViewById(R.id.signUpText);
        mSignUpTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LoginActivity.this, SignUpActivity.class);
                startActivity(intent);
            }
        });

        mUsername = (EditText) findViewById(R.id.usernameField);
        mPassword = (EditText) findViewById(R.id.passwordField);
        mLoginButton = (Button) findViewById(R.id.loginButton);
        mLoginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                username = mUsername.getText().toString();
                password = mPassword.getText().toString();

                username = username.trim();
                password = password.trim();

                if (username.isEmpty() || password.isEmpty()) {
                    AlertDialog.Builder builder = new AlertDialog.Builder(LoginActivity.this);
                    builder.setMessage(R.string.login_error_message)
                            .setTitle(R.string.error_title)
                            .setPositiveButton(android.R.string.ok, null);
                    AlertDialog dialog = builder.create();
                    dialog.show();
                } else {
                    // Login
                    setProgressBarIndeterminateVisibility(true);
                    LoginMap.put("username", username);
                    LoginMap.put("password", password);


                    String[] s = new String[]{Constants.HOST + Constants.PORT + "/ReceiptTracker/login"};
                   // String[] s = new String[]{"http://10.0.2.2:8080"+ "/ReceiptTracker/login"};
                    new GET(LoginActivity.this).execute(s);

                }
            }
        });
    }

    @Override
    public void onTaskCompleted(String feedback) {
        if (feedback == null || feedback.length() == 0) {
            Toast.makeText(this, "Connection error", Toast.LENGTH_LONG).show();
        }
        int result = -1;
        try {
            result = Integer.parseInt(feedback);
            Log.i("Log in :", result + "");
        } catch (NumberFormatException e) {
            Toast.makeText(this, "Sign Up Error", Toast.LENGTH_LONG).show();

        }
        if (result > 0) {
            Toast.makeText(this, "HELLO, " + result + "!", Toast.LENGTH_SHORT).show();

            SharedPreferences sharedPref = this.getSharedPreferences(
                    getString(R.string.preference_file_key), Context.MODE_PRIVATE);
            SharedPreferences.Editor editor = sharedPref.edit();
            editor.putInt(Constants.USER_ID, result);
            editor.commit();


            long ID = sharedPref.getInt(Constants.USER_ID, -1);

            Intent intent = new Intent(this, MainActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
        } else {
            Toast.makeText(this, feedback, Toast.LENGTH_LONG).show();
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setMessage("The username or password you entered is incorrect! \nPlease try again")
                    .setTitle(R.string.error_title)
                    .setPositiveButton(android.R.string.ok, null);
            AlertDialog dialog = builder.create();
            dialog.show();

        }
    }


    private class GET extends AsyncTask<String, Integer, String> {

        HttpURLConnection connection;
        private OnTaskCompleted listener;
        private String result = "";

        public GET(OnTaskCompleted listener) {
            this.listener = listener;
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
                Log.i("URL", builder.build().toString());
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
            } finally {
                if (connection != null)
                    connection.disconnect();
                if (inputStream != null)
                    try {
                        inputStream.close();
                    } catch (IOException e) {
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
