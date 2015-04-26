package com.example.rodrigue.bralirwa;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.util.ArrayList;


public class orders_made extends android.support.v4.app.Fragment {

    private View rootView;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_orders_made, container, false);

        return rootView;
    }

    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        ArrayList<String> products = new ArrayList<>();
        products.add("Muhirwa Depot's order");
        products.add("Nziza Depot's order");
        products.add("Kuki Mutanywa Depot");
        products.add("Chez Tom Depot's order");
        products.add("Vayo Depot's order");
        products.add("Blah blah Depot's order");


        String[] array = new String[products.size()];
        for (int i = 0; i < products.size(); i++) {
            array[i] = products.get(i);
        }
        ArrayAdapter adapter = new ArrayAdapter<String>(getActivity().getApplicationContext(),
                R.layout.activity_listview, array);

        ListView listView = (ListView) rootView.findViewById(R.id.listView);
        listView.setAdapter(adapter);
    }
}
