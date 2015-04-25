package com.example.wendy_guo.j4sp.adapter;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.wendy_guo.j4sp.R;
import com.example.wendy_guo.j4sp.pojo.UploadRecord;

import java.util.List;

/**
 * Created by Wendy_Guo on 3/16/15.
 */
public class RecordListAdapter extends BaseAdapter {

    private List<UploadRecord> records;
    private Context context;
    public RecordListAdapter(Context context, List<UploadRecord> records) {
        this.context = context;
        this.records = records;
    }

    @Override
    public int getCount() {
        return records.size();
    }

    @Override
    public Object getItem(int i) {
        return records.get(i);
    }

    @Override
    public long getItemId(int i) {
        return records.indexOf(getItem(i));
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if(convertView == null) {
            LayoutInflater inflater = (LayoutInflater)this.context.getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.record_item, null);
        }

        TextView textView = (TextView)convertView.findViewById(R.id.record_i);

        UploadRecord record = this.records.get(position);
        textView.setText(record.getName());

        return convertView;
    }


}
