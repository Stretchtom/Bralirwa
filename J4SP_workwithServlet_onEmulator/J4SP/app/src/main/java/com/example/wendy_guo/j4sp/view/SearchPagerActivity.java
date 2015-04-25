package com.example.wendy_guo.j4sp.view;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.os.Bundle;
import android.util.Log;
import android.view.Window;

import com.example.wendy_guo.j4sp.Constants;
import com.example.wendy_guo.j4sp.R;
import com.example.wendy_guo.j4sp.adapter.SearchPagerAdapter;
import com.example.wendy_guo.j4sp.fragments.SearchFragment;
import com.example.wendy_guo.j4sp.pojo.ActivityCallBack;

import java.util.List;

public class SearchPagerActivity extends FragmentActivity implements ActivityCallBack {


    PagerAdapter mPagerAdapter;
    ViewPager mViewPager;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        getWindow().requestFeature(Window.FEATURE_ACTION_BAR);
        setContentView(R.layout.pager_strip);

        setPage();

    }

    private void clear() {
        mViewPager.removeViewInLayout(getWindow().getDecorView().getRootView());

        mViewPager.removeAllViews();
        android.support.v4.app.FragmentManager fm = getSupportFragmentManager();

        List<Fragment> fragments = fm.getFragments();
        if (fragments != null) {
            Log.i("remove size",fragments.size()+"");
            android.support.v4.app.FragmentTransaction ft = fm.beginTransaction();
            for (Fragment f : fragments) {
                //You can perform additional check to remove some (not all) fragments:
                if (f instanceof SearchFragment) {

                    ft.remove(f);

                }
            }
            fragments.clear();

            ft.commitAllowingStateLoss();
        }
    }

    private void setPage(){

        mPagerAdapter = new SearchPagerAdapter(this,
                getSupportFragmentManager());

        mViewPager = (ViewPager) findViewById(R.id.pager);

        mViewPager.setAdapter(mPagerAdapter);



        Bundle bundle = new Bundle();
        bundle.putInt(Constants.SHOW_RECORD,0);

    }

    @Override
    public void callBack() {
        mPagerAdapter.notifyDataSetChanged();
        Log.i("refreshed","here");
    }
}
