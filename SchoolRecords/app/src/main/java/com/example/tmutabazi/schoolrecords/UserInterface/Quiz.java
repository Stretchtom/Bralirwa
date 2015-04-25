package com.example.tmutabazi.schoolrecords.UserInterface;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.example.tmutabazi.schoolrecords.Model.Generating;
import com.example.tmutabazi.schoolrecords.Model.Student;
import com.example.tmutabazi.schoolrecords.R;
import com.example.tmutabazi.schoolrecords.Util.DBhandler;

import java.util.ArrayList;

public class Quiz extends ActionBarActivity {

    ArrayList<Student> stu = new ArrayList<Student>();
    DBhandler  db;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quiz);

        final EditText SID = (EditText) findViewById(R.id.SID);
        final EditText quiz1 = (EditText) findViewById(R.id.QUIZ1);
       final EditText quiz2 = (EditText) findViewById(R.id.QUIZ2);
        final EditText quiz3 = (EditText) findViewById(R.id.QUIZ3);
        final EditText quiz4 = (EditText) findViewById(R.id.QUIZ4);
       final EditText quiz5 = (EditText) findViewById(R.id.QUIZ5);
        Button done = (Button) findViewById(R.id.done);
        db= new DBhandler(this);


        done.setOnClickListener(
                new Button.OnClickListener() {
                    public void onClick(View v) {
                        try {

                            int ID = Integer.parseInt(SID.getText().toString());
                            int q1 = Integer.parseInt(quiz1.getText().toString());
                            int q2 = Integer.parseInt(quiz2.getText().toString());
                            int q3 = Integer.parseInt(quiz3.getText().toString());
                            int q4 = Integer.parseInt(quiz4.getText().toString());
                            int q5 = Integer.parseInt(quiz5.getText().toString());

                            Generating gen = new Generating();

                            int[] mrks = new int[] {q1,q2,q3,q4,q5};
                            stu = gen.one_entry(ID,mrks);
                            db.insertData(stu);

                            Intent myIntent = new Intent(Quiz.this, UserInterface.class);
                            Quiz.this.startActivity(myIntent);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }

                    }
                }
        );
    }


}