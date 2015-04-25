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

public class SignUpActivity extends Activity implements OnTaskCompleted {

    protected EditText mUsername;
    protected EditText mPassword;
    protected EditText mEmail;
    protected Button mSignUpButton;
    private LinkedHashMap<String, String> signupMap = new LinkedHashMap<String, String>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_INDETERMINATE_PROGRESS);
        setContentView(R.layout.activity_sign_up);

        final ActionBar actionBar = getActionBar();
        actionBar.hide();
        mUsername = (EditText) findViewById(R.id.usernameField);
        mPassword = (EditText) findViewById(R.id.passwordField);
        mEmail = (EditText) findViewById(R.id.emailField);
        mSignUpButton = (Button) findViewById(R.id.signupButton);
        mSignUpButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String username = mUsername.getText().toString();
                String password = mPassword.getText().toString();
                String email = mEmail.getText().toString();

                username = username.trim();
                password = password.trim();
                email = email.trim();

                if (username.isEmpty() || password.isEmpty() || email.isEmpty()) {
                    AlertDialog.Builder builder = new AlertDialog.Builder(SignUpActivity.this);
                    builder.setMessage(R.string.signup_error_message)
                            .setTitle(R.string.error_title)
                            .setPositiveButton(android.R.string.ok, null);
                    AlertDialog dialog = builder.create();
                    dialog.show();
                } else {
                    signupMap.put("username", username);
                    signupMap.put("password", password);
                    signupMap.put("email", email);
                    String[] s = new String[]{Constants.HOST + Constants.PORT + "/ReceiptTracker/signup"};

                    setProgressBarIndeterminateVisibility(true);
                    new GET(SignUpActivity.this).execute(s);
                }


            }
        });

    }


    @Override
    public void onTaskCompleted(String feedback) {
        if (feedback == null || feedback.length() == 0) {
            Toast.makeText(this, "Connection error", Toast.LENGTH_LONG).show();
            return;
        }
        Log.i("Sign up :", feedback);

        int result = -1;
        try {
            result = Integer.parseInt(feedback);
        } catch (NumberFormatException e) {
            Toast.makeText(this, "Sign Up Error", Toast.LENGTH_LONG).show();
        }
        if (result > 0) {
            Toast.makeText(this, "HELLO, " + result + "!", Toast.LENGTH_LONG).show();
            SharedPreferences sharedPref = this.getSharedPreferences(
                    getString(R.string.preference_file_key), Context.MODE_PRIVATE);
            SharedPreferences.Editor editor = sharedPref.edit();
            editor.putInt(Constants.USER_ID, result);
            editor.commit();

            Intent intent = new Intent(SignUpActivity.this, MainActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
        } else {
            Toast.makeText(this, feedback, Toast.LENGTH_LONG).show();
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            String error;
            if (feedback.contains("username")) {
                error = "This user name has already been taken! \nPlease try another";
            } else
                error = "This e-mail already exist! \nIf you already have an account please sign in";
            builder.setMessage(error)
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
            for (String name : signupMap.keySet()) {
                builder.appendQueryParameter(name, signupMap.get(name).toString());
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
            } finally {
                if (connection != null)
                    connection.disconnect();
                if (inputStream != null) {
                    try {
                        inputStream.close();
                    } catch (IOException e) {
                    }
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

