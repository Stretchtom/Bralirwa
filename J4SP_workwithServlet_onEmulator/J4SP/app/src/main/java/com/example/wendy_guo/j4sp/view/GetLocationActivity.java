package com.example.wendy_guo.j4sp.view;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;

import android.widget.Toast;

import com.example.wendy_guo.j4sp.Constants;
import com.example.wendy_guo.j4sp.R;


public class GetLocationActivity extends Activity {
    LocationManager mLocationManager;
    LocationListener mLocationListener;
    private Location mLocation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_get_location);
        mLocationManager = (LocationManager) this
                .getSystemService(Context.LOCATION_SERVICE);
        mLocationListener = new LocationListener() {

            @Override
            public void onLocationChanged(Location location) {

            }

            @Override
            public void onStatusChanged(String provider, int status, Bundle extras) {

            }

            @Override
            public void onProviderEnabled(String provider) {

            }

            @Override
            public void onProviderDisabled(String provider) {

            }
        };
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (isEnabled(this)) {
            getLocation();
        } else{
            Toast.makeText(this, "NO LOCATION SERVICE AVAILABLE", Toast.LENGTH_LONG).show();
            setResult(RESULT_CANCELED);
            finish();
        }
    }

    private boolean isEnabled(Context context) {
        if (mLocationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
            return true;
        }
        if (mLocationManager
                .isProviderEnabled(LocationManager.NETWORK_PROVIDER)) {
            return true;
        }
        return false;
    }

    private void getLocation() {

        Criteria criteria = new Criteria();
        criteria.setAccuracy(Criteria.ACCURACY_FINE);
        criteria.setAltitudeRequired(false);
        criteria.setBearingRequired(false);
        criteria.setCostAllowed(true);
        criteria.setPowerRequirement(Criteria.POWER_LOW);


        String provider = mLocationManager.getBestProvider(criteria, true);

        Toast.makeText(this, provider, Toast.LENGTH_LONG).show();

        mLocationManager.requestLocationUpdates(provider, 1000, 0, mLocationListener);


        //wait ....
        waitForUpdates(provider);

        if (mLocation != null) {

            Intent data = new Intent();
            data.putExtra(Constants.KEY_LOCATION, mLocation.getLatitude() + "\t"
                    + mLocation.getLongitude() + "\t" + mLocation.getTime());
            data.putExtra(Constants.KEY_PROVIDER, provider);
            setResult(RESULT_OK, data);
        } else {
            Toast.makeText(this,"FAIL TO GET LOCATION",Toast.LENGTH_LONG).show();
            setResult(RESULT_CANCELED);
        }
        finish();
    }

    private void waitForUpdates(String provider) {
        mLocation = mLocationManager.getLastKnownLocation(provider);
        int i = 0;
        while (mLocation == null && i < 3000) {
            i++;
            mLocation = mLocationManager.getLastKnownLocation(provider);
        }
    }


}



