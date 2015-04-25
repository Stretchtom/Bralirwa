package com.example.wendy_guo.j4sp;

import android.app.ListActivity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.example.wendy_guo.j4sp.adapter.RecordListAdapter;
import com.example.wendy_guo.j4sp.pojo.UploadRecord;

import java.util.ArrayList;

import com.example.wendy_guo.j4sp.view.ViewSummaryActivity;


public class ViewRecordListActivity extends ListActivity {
    RecordListAdapter mAdapter;
    ArrayList<UploadRecord> records;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_record_list);

        records = new ArrayList<UploadRecord>();

        String rawResult = getIntent().getStringExtra(Constants.SEARCH_RESULT);
        Log.i("Download RECEIVE ", rawResult);
        //if(rawResult != null)
        String[] rawList = rawResult.split("#");
        Log.i("rawList", rawList.length+"");

        for (String r : rawList) {
            Log.i("i", r );
            String[] record_flat = r.split(",");
            Log.i("record_flat", record_flat.length+"");
            if(record_flat.length == 6){
                UploadRecord record = new UploadRecord(record_flat[0],
                    record_flat[1],
                    record_flat[2],
                    record_flat[3],
                    record_flat[4],
                    Long.parseLong(record_flat[5]));
                records.add(record);
            }

        }
        Log.i("RECORD SIZE", records.size()+"");

        mAdapter = new RecordListAdapter(this, records);
        setListAdapter(mAdapter);
    }

    protected void onListItemClick(ListView l, View v, int position, long id) {
        UploadRecord record = (UploadRecord) getListAdapter().getItem(position);
        Intent summaryIntent = new Intent(this, ViewSummaryActivity.class);
        summaryIntent.putExtra("record", record);
        summaryIntent.putExtra(Constants.SUMMARY_CODE,1);
        startActivity(summaryIntent);
    }
    protected AdapterView.OnItemClickListener mOnItemClickListener = new AdapterView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
            UploadRecord record = (UploadRecord) adapterView.getAdapter().getItem(i);
            Intent intent = new Intent(ViewRecordListActivity.this, ViewSummaryActivity.class);
            startActivity(intent);
        }
    };

}