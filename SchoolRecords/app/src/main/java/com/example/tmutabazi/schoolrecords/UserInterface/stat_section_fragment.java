package com.example.tmutabazi.schoolrecords.UserInterface;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.method.ScrollingMovementMethod;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.tmutabazi.schoolrecords.R;

/**
 * Created by tmutabazi on 4/3/2015.
 */
public class stat_section_fragment extends Fragment {

    private static TextView disp_stats;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.statistiv_view_fragment, container, false);
        disp_stats = (TextView) view.findViewById(R.id.statistic_view);
        return view;
    }

    public void SetStatFragmentText(String entry)
    {
        disp_stats.setMovementMethod(new ScrollingMovementMethod());
        disp_stats.setText(entry);

    }
}
