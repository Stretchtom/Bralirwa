package com.example.rodrigue.bralirwa;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

/**
 * Created by Rodrigue on 4/23/2015.
 */
public class TabsPagerAdapter extends FragmentPagerAdapter {
    public TabsPagerAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int i) {

        switch (i) {
            case 0:
                return new depot_fragment();

            case 1:
                return new blah_fragment();
            case 2:
                return new orders_fragment();
            case 3:
                return new customers_fragment();
        }

        return null;
    }

    @Override
    public int getCount() {
        return 4;
    }
}
