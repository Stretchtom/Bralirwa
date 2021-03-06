package com.example.rodrigue.bralirwa;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;

import java.util.ArrayList;


public class depot_fragment extends android.support.v4.app.Fragment{
    private View rootView;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        rootView = inflater.inflate(R.layout.activity_depot_fragment, container, false);

        return rootView;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        ArrayList<String> products=new ArrayList<>();
        products.add("Heineken");
        products.add("Mutziig");
        products.add("Primus");
        products.add("Sprite");
        products.add("Orange");
        products.add("Fiesta");


        String[] array=new String[products.size()];
        for (int i=0; i<products.size(); i++){
            array[i]=products.get(i);
        }
        ArrayAdapter adapter = new ArrayAdapter<String>(getActivity().getApplicationContext(),
                R.layout.activity_listview, array);

        ListView listView = (ListView) rootView.findViewById(R.id.products_list);
        listView.setAdapter(adapter);

    }
}
