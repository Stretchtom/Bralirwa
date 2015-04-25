package com.example.wendy_guo.j4sp.view;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;

import com.example.wendy_guo.j4sp.Constants;
import com.example.wendy_guo.j4sp.fragments.DownloadRecordsFragment;


public class SearchResultListActivity extends Activity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        String result = getIntent().getStringExtra(Constants.SEARCH_RESULT);

        Bundle bundle = new Bundle();
        bundle.putInt(Constants.SHOW_RECORD,1);

        bundle.putString(Constants.SEARCH_RESULT, result);
        Log.i("PUT into BUNDLE", result);


        if (getFragmentManager().findFragmentById(android.R.id.content) == null) {
            DownloadRecordsFragment list = new DownloadRecordsFragment();

            list.setArguments(bundle);
            getFragmentManager().beginTransaction().add(android.R.id.content, list).commit();
        }
    }
}
