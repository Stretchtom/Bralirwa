package com.example.rodrigue.bralirwa;

import android.app.Activity;
import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;


public class customers_fragment extends android.support.v4.app.Fragment {
    private View rootView;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        rootView = inflater.inflate(R.layout.activity_customers_fragment, container, false);

        return rootView;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        ArrayList<String> products = new ArrayList<>();
        products.add("Muhirwa Depot");
        products.add("Nziza Depot");
        products.add("Kuki Mutanywa Depot");
        products.add("Chez Tom Depot");
        products.add("Vayo Depot");
        products.add("Blah blah Depot");


        String[] array = new String[products.size()];
        for (int i = 0; i < products.size(); i++) {
            array[i] = products.get(i);
        }
        ArrayAdapter adapter = new ArrayAdapter<String>(getActivity().getApplicationContext(),
                R.layout.activity_listview, array);

        ListView listView = (ListView) rootView.findViewById(R.id.products_list);
        listView.setAdapter(adapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener()
        {
            @Override
            public void onItemClick(AdapterView<?> arg0, View arg1,int position, long arg3)
            {
                Intent launchOrder = new Intent(getActivity().getApplicationContext(), Order.class);
                startActivity(launchOrder);
            }
        });
    }
}
