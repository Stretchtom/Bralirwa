package com.example.wendy_guo.j4sp.view;

import android.app.ActionBar;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.example.wendy_guo.j4sp.Constants;
import com.example.wendy_guo.j4sp.R;


public class SettingActivity extends Activity {
    Button mButton;

    Switch mQualitySwitch;
    Switch mOfflineModeSwitch;
    Switch mSaveSwitch;
    TextView account;
    TextView currency;
    DialogInterface.OnClickListener mDialogListener;
    SharedPreferences sharedPref;
    SharedPreferences.Editor editor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);
        final ActionBar actionBar = getActionBar();
        actionBar.hide();
        sharedPref = this.getSharedPreferences(
                getString(R.string.preference_file_key), Context.MODE_PRIVATE);
        editor = sharedPref.edit();

        mButton = (Button) findViewById(R.id.logoutButton);
        mQualitySwitch = (Switch) findViewById(R.id.Quality_Switch);
        mQualitySwitch.setChecked(sharedPref.getBoolean(Constants.PREF_KEY_IMAGE_QUALITY, false));
        //mQualitySwitch.setChecked(mPreferencesManager.getBooleanPreference(Constants.PREF_KEY_IMAGE_QUALITY));

        mOfflineModeSwitch = (Switch) findViewById(R.id.sync_when_wifi_available_Switch);
        mOfflineModeSwitch.setChecked(sharedPref.getBoolean(Constants.PREF_KEY_UPLOAD_WIFI,false));

        mSaveSwitch= (Switch) findViewById(R.id.save_to_gallery_Switch);
        mSaveSwitch.setChecked(sharedPref.getBoolean(Constants.PREF_KEY_SAVE_DOWNLOAD,false));
        account = (TextView) findViewById(R.id.user_name_display);
        currency = (TextView) findViewById(R.id.currency);



        int userId = sharedPref.getInt(Constants.USER_ID, -1);
        if (userId < 0) {
            Toast.makeText(this, R.string.expired,
                    Toast.LENGTH_LONG).show();
            navigateToLogin();
        }

        account.setText("account: email");
        mButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                editor.putInt(Constants.USER_ID, -1);
                editor.commit();
                navigateToLogin();
            }
        });

        mSaveSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    editor.putBoolean(Constants.PREF_KEY_SAVE_DOWNLOAD, true);
                    editor.commit();
                } else {
                    editor.putBoolean(Constants.PREF_KEY_SAVE_DOWNLOAD, false);
                    editor.commit();
                }

            }


        });
        mOfflineModeSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    editor.putBoolean(Constants.PREF_KEY_UPLOAD_WIFI, true);
                    editor.commit();
                    Intent alarmIntent = new Intent();
                    alarmIntent.setAction(Constants.ALARM_SET);
                    sendBroadcast(alarmIntent);
                    Toast.makeText(SettingActivity.this, "Offline Mode Enabled",
                            Toast.LENGTH_LONG).show();
                } else {
                    editor.putBoolean(Constants.PREF_KEY_UPLOAD_WIFI, false);
                    editor.commit();
                    Intent cancelAlarmIntent = new Intent();
                    cancelAlarmIntent.setAction(Constants.ALARM_CANCEL);
                    sendBroadcast(cancelAlarmIntent);
                    Toast.makeText(SettingActivity.this, "Offline Mode Disabled",
                            Toast.LENGTH_LONG).show();
                }
            }


        });
        mQualitySwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    editor.putBoolean(Constants.PREF_KEY_IMAGE_QUALITY, true);
                    editor.commit();
                } else {
                    editor.putBoolean(Constants.PREF_KEY_IMAGE_QUALITY, false);
                    editor.commit();
                }
            }


        });
        mDialogListener =
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        switch(which) {
                            case 0:
                                currency.setText("USD$");
                                editor.putString(Constants.PREF_KEY_CURRENCY, "USD$");
                                editor.commit();
                                break;
                            case 1:
                                currency.setText("RMB");
                                editor.putString(Constants.PREF_KEY_CURRENCY, "RMB");
                                editor.commit();
                                break;
                            case 2:
                                currency.setText("EUR");
                                editor.putString(Constants.PREF_KEY_CURRENCY, "EUR");
                                editor.commit();

                                break;
                            case 3:
                                currency.setText("INR");
                                editor.putString(Constants.PREF_KEY_CURRENCY, "INR");
                                editor.commit();

                                break;
                            case 4:
                                currency.setText("AUD");
                                editor.putString(Constants.PREF_KEY_CURRENCY, "AUD");
                                editor.commit();
                                break;
                            case 5:
                                currency.setText("CAD");
                                editor.putString(Constants.PREF_KEY_CURRENCY, "CAD");
                                editor.commit();
                                break;
                        }
                    }
                };

                    currency.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(SettingActivity.this);
                builder.setItems(R.array.currency_choices, mDialogListener);
                AlertDialog dialog = builder.create();
                dialog.show();


            }
        });
    }
    private void navigateToLogin() {
        Intent intent = new Intent(this, LoginActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
    }

}
