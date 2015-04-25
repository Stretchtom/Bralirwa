package com.example.tmutabazi.schoolrecords.UserInterface;

import android.app.Activity;
import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.tmutabazi.schoolrecords.R;

/**
 * Created by tmutabazi on 4/3/2015.
 */
public class top_section_fragment extends Fragment {

    private  EditText entry_inputs;

    TopSectListener actCommand;
    public interface TopSectListener{

        public void activityRunner(int entry);
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            actCommand = (TopSectListener) activity;
        }catch (ClassCastException e){
            throw new ClassCastException(activity.toString());
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.top_section_fragment, container, false);

        entry_inputs = (EditText) view.findViewById(R.id.entries);
        final Button button = (Button) view.findViewById(R.id.gen_res);
        final Button button1 = (Button) view.findViewById(R.id.entryButton);


        button.setOnClickListener(
                new Button.OnClickListener() {
                    public void onClick(View v) {
                        try {

                            if(Integer.parseInt(entry_inputs.getText().toString())> 40)
                            {
                                Toast.makeText(getActivity(),"Enter Less than 40",Toast.LENGTH_LONG).show();
                            }
                            else {


                                actCommand.activityRunner(Integer.parseInt(entry_inputs.getText().toString()));
                            }

                        } catch (Exception e) {
                            e.printStackTrace();
                        }

                    }
                }
        );

        button1.setOnClickListener(
                new Button.OnClickListener() {
                    public void onClick(View v) {
                        try {

                            Intent myIntent = new Intent(top_section_fragment.this.getActivity(), Quiz.class);
                            top_section_fragment.this.startActivity(myIntent);

                        } catch (Exception e) {
                            e.printStackTrace();
                        }

                    }
                }
        );
        return view;



    }


}
