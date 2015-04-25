package com.example.wendy_guo.j4sp.view;

import android.app.ActionBar;
import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.widget.Toast;

import com.example.wendy_guo.j4sp.R;


public class SMSActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sms);

        final ActionBar actionBar = getActionBar();
        actionBar.hide();
        Intent smsIntent = new Intent(Intent.ACTION_SEND);
        smsIntent.setData(Uri.parse("smsto:"));
        smsIntent.setType("image/gif");
        smsIntent.putExtra(Intent.EXTRA_STREAM, Uri.parse(getIntent().getData().toString()));

        try {
            startActivity(smsIntent);
        } catch (android.content.ActivityNotFoundException ex) {
            Toast.makeText(this, "Oops! cant send text message on this device", Toast.LENGTH_SHORT).show();
        }

        finish();

    }


}
