package com.example.wendy_guo.j4sp.adapter;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.example.wendy_guo.j4sp.R;
import com.example.wendy_guo.j4sp.fragments.ViewRecordFragment;

import java.util.Locale;

/**
 * Created by Wendy_Guo on 3/17/15.
 */
public class RecordsPagerAdapter extends FragmentPagerAdapter {

    protected Context mContext;

    public RecordsPagerAdapter(Context context, FragmentManager fm) {
        super(fm);
        mContext = context;
    }

    @Override
    public Fragment getItem(int position) {


        switch(position) {
            case 0:
                return new ViewRecordFragment();
            case 1:
                return new ViewRecordFragment();
        }

        return null;
    }

    @Override
    public int getCount() {
        return 2;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        Locale l = Locale.getDefault();
        switch (position) {
            case 0:
                return mContext.getString(R.string.title_section1).toUpperCase(l);
            case 1:
                return mContext.getString(R.string.title_section2).toUpperCase(l);
        }
        return null;
    }
}