package com.example.tmutabazi.schoolrecords.UserInterface;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.method.ScrollingMovementMethod;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.tmutabazi.schoolrecords.R;

/**
 * Created by tmutabazi on 4/3/2015.
 */
public class disp_section_fragment extends android.support.v4.app.Fragment {

   private static TextView disp_results;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.disp_section_fragment, container, false);
        disp_results = (TextView) view.findViewById(R.id.generated_view);

        return view;
    }

    public void SetFragmentText(String entry)
    {
       disp_results.setMovementMethod(new ScrollingMovementMethod());
       disp_results.setText(entry);

    }

}

