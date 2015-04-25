package com.example.tmutabazi.schoolrecords.UserInterface;

import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;

import com.example.tmutabazi.schoolrecords.Model.Generating;
import com.example.tmutabazi.schoolrecords.Model.Statistics;
import com.example.tmutabazi.schoolrecords.Model.Student;
import com.example.tmutabazi.schoolrecords.R;
import com.example.tmutabazi.schoolrecords.Util.DBhandler;

import java.util.ArrayList;

public class UserInterface extends ActionBarActivity implements top_section_fragment.TopSectListener {

    DBhandler db;
    Generating gen;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        db= new DBhandler(this);
        setContentView(R.layout.activity_main);
    }

    @Override
    public void activityRunner(int entry) {
       Generating gen = new Generating();
        Statistics stat = new Statistics();
      ArrayList<Student> stu = new ArrayList<Student>();
        ArrayList<Student> stud = new ArrayList<Student>();
        stu = gen.object_entry(entry);
        db.insertData(stu);
        stud = db.retrieve_records();

        String file = gen.Print(stud);

        disp_section_fragment dis_frag = (disp_section_fragment) getSupportFragmentManager().findFragmentById(R.id.fragment2);
        dis_frag.SetFragmentText(file);

        String stat_res = stat.findavg(stu) +"\n" + stat.findhigh(stu) + "\n" + stat.findlow(stu);
        stat_section_fragment stat_frag = (stat_section_fragment) getSupportFragmentManager().findFragmentById(R.id.fragment3);
        stat_frag.SetStatFragmentText(stat_res);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_user_interface, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
