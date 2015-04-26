package com.example.rodrigue.bralirwa;

import android.app.ActionBar;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;


public class Register extends Activity {

    private EditText username;
    private EditText password;
    private EditText address;
    private Button save;
    private ActionBar actionBar1;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        actionBar1 = getActionBar();

        actionBar1.setBackgroundDrawable(new ColorDrawable(Color.parseColor("#006400")));

        username=(EditText)findViewById(R.id.username);
        password=(EditText)findViewById(R.id.password);
        address=(EditText)findViewById(R.id.address);

        save = (Button) findViewById(R.id.submit);
        save.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                // Do something in response to button click
                Intent launchMain = new Intent(Register.this, MainActivity.class);
                startActivity(launchMain);
            }
        });
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_register, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the homebackgroud/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
