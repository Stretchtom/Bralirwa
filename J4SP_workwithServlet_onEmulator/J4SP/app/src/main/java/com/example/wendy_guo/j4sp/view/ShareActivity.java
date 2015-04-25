package com.example.wendy_guo.j4sp.view;

import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.Toast;

import com.example.wendy_guo.j4sp.R;


public class ShareActivity extends ActionBarActivity {


    private EditText mailAddress;
    private EditText subject;
    private EditText body;
    private String[] addr;
    private String subj;
    private String bd;
    private Uri mUri;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_share);

        mailAddress = (EditText) findViewById(R.id.mail_address);
        subject =(EditText) findViewById(R.id.email_subject);
        body = (EditText) findViewById(R.id.email_body);
        mUri = getIntent().getData();

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
         MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_share, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();

        if (id == R.id.icon_email) {
            addr = mailAddress.getText().toString().split(";");
            for(String s : addr)
                s = s.trim();
            subj = subject.getText().toString();
            bd = body.getText().toString();

            //send email
            Intent emailIntent = new Intent(Intent.ACTION_SEND);
            emailIntent.setType("message/rfc822");
            emailIntent.putExtra(Intent.EXTRA_EMAIL, addr);
            emailIntent.putExtra(Intent.EXTRA_SUBJECT, subj);
            emailIntent.putExtra(Intent.EXTRA_TEXT, bd);
            emailIntent.putExtra(Intent.EXTRA_STREAM, Uri.parse(mUri.toString()));

            try {
                startActivity(Intent.createChooser(emailIntent, "Send mail..."));
            } catch (android.content.ActivityNotFoundException ex) {
                Toast.makeText(ShareActivity.this, "There are no email clients installed.", Toast.LENGTH_SHORT).show();
            }

            finish();
            return true;

        }

        return super.onOptionsItemSelected(item);
    }
}
