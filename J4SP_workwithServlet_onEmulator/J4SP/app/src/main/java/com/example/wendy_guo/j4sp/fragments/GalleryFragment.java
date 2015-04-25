package com.example.wendy_guo.j4sp.fragments;

import android.app.Activity;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;

import com.example.wendy_guo.j4sp.R;
import com.example.wendy_guo.j4sp.adapter.DashBoardAdapter;
import com.example.wendy_guo.j4sp.adapter.PhotoGridAdapter;


public class GalleryFragment extends Fragment {


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_gallery,container,false);
        GridView gridView = (GridView) view.findViewById(R.id.photo_grid);
        gridView.setAdapter(new PhotoGridAdapter(view.getContext()));
        return view;
    }

}
