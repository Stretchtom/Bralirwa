package com.example.wendy_guo.j4sp.adapter;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.PagerAdapter;
import android.util.Log;
import android.view.ViewGroup;

import com.example.wendy_guo.j4sp.R;
import com.example.wendy_guo.j4sp.fragments.DownloadRecordsFragment;
import com.example.wendy_guo.j4sp.fragments.GalleryFragment;
import com.example.wendy_guo.j4sp.fragments.SearchFragment;
import com.example.wendy_guo.j4sp.fragments.ViewRecordFragment;

import java.util.List;
import java.util.Locale;

/**
 * Created by Wendy_Guo on 4/2/15.
 */
public class SearchPagerAdapter extends FragmentStatePagerAdapter {
    protected Context mContext;

    public SearchPagerAdapter(Context context, FragmentManager fm) {
        super(fm);
        mContext = context;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        super.destroyItem(container, position, object);

    }

    @Override
    public Fragment getItem(int position) {


        switch(position) {
            case 0:
                SearchFragment sf = new SearchFragment();
                return sf;
            case 1:
                return new GalleryFragment();
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
                return mContext.getString(R.string.title_section1s).toUpperCase(l);
            case 1:
                return mContext.getString(R.string.title_section2s).toUpperCase(l);
        }
        return null;
    }

    @Override
    public int getItemPosition(Object object){
        return PagerAdapter.POSITION_NONE;
    }
}