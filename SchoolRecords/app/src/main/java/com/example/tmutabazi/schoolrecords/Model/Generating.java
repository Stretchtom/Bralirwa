package com.example.tmutabazi.schoolrecords.Model;

import android.util.Log;

import java.util.ArrayList;
import java.util.Random;

/**
 * Created by tmutabazi on 4/3/2015.
 */
public class Generating {

    public Generating()
    {

    }

    public int[] quizes()
    {
        int count = 0;
        int[] mrks = new int[5];
        Random rand = new Random();

        while(count < mrks.length)
        {
            mrks[count] = rand.nextInt((100 - 0) + 1) + 0;
            count++;
        }

        return mrks;
    }

    public int SID()
    {
        Random rand = new Random();

        int randomNum = rand.nextInt((2000 - 1000) + 1) + 1000;

        return randomNum;
    }

    public ArrayList<Student> object_entry(int entry)
    {
        ArrayList<Student> stu = new ArrayList<Student>();
        int i = 0;

        while(i < entry)
        {
            stu.add(new Student(SID(),quizes()));
            i++;

        }
        return stu;
    }
    public ArrayList<Student> one_entry(int SID, int[] quiz)
    {
        ArrayList<Student> stu = new ArrayList<Student>();

            stu.add(new Student(SID,quiz));


        return stu;
    }
     public String Print(ArrayList<Student>stu)
     {
         StringBuilder sb = new StringBuilder();

         sb.append("Stud" +"  \t"+ "Qu1" +"  \t"+ "QU2" +"  \t"+ "QU3" +"  \t" +  "QU4" +"  \t"+ " QU5"  );
         sb.append("  \n");
         for(int i=0; i < stu.size(); i++) {
             sb.append(stu.get(i).getSID());
             int[] l = stu.get(i).getScores();
             for (int j = 0; j < l.length; j++) {
                 sb.append("  \t");
                 sb.append(l[j]);
             }

             sb.append("  \t");
             sb.append("  \n");
         }
         return sb.toString();
     }

}
