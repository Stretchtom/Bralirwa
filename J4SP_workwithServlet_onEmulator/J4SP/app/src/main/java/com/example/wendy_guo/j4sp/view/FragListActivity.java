package com.example.wendy_guo.j4sp.view;

import android.content.Intent;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.Window;

import com.example.wendy_guo.j4sp.Constants;
import com.example.wendy_guo.j4sp.R;
import com.example.wendy_guo.j4sp.adapter.RecordsPagerAdapter;


public class FragListActivity extends FragmentActivity {


    PagerAdapter mPagerAdapter;
    ViewPager mViewPager;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        getWindow().requestFeature(Window.FEATURE_ACTION_BAR);
        setContentView(R.layout.pager_strip);


        mPagerAdapter = new RecordsPagerAdapter(this,
                getSupportFragmentManager());

        mViewPager = (ViewPager) findViewById(R.id.pager);
        mViewPager.setAdapter(mPagerAdapter);

        Bundle bundle = new Bundle();
        bundle.putInt(Constants.SHOW_RECORD,0);

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_frag_list, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.icon_search) {
            Intent i = new Intent(FragListActivity.this, SearchActivity.class);
            startActivity(i);


            return true;

        }


        return super.onOptionsItemSelected(item);
    }



}
