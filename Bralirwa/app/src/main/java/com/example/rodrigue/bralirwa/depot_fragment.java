package com.example.rodrigue.bralirwa;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;


public class depot_fragment extends android.support.v4.app.Fragment {
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.activity_depot_fragment, container, false);

        String[] videos= {};

        final ImageView imageView1 = (ImageView) rootView.findViewById(R.id.imageView);
        final ImageView imageView2 = (ImageView) rootView.findViewById(R.id.imageView2);
        final ImageView imageView3 = (ImageView) rootView.findViewById(R.id.imageView3);
        final ImageView imageView4 = (ImageView) rootView.findViewById(R.id.imageView4);
        final ImageView imageView5 = (ImageView) rootView.findViewById(R.id.imageView5);
        final ImageView imageView6 = (ImageView) rootView.findViewById(R.id.imageView6);

        imageView1.setImageResource(R.drawable.heineken);
        imageView2.setImageResource(R.drawable.mutziig);
        imageView3.setImageResource(R.drawable.primus);
        imageView4.setImageResource(R.drawable.fiesta);
        imageView5.setImageResource(R.drawable.sprite);
        imageView6.setImageResource(R.drawable.orange);
        return rootView;
    }
}
