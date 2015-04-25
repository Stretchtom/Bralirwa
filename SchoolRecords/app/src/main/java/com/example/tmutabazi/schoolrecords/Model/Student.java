package com.example.tmutabazi.schoolrecords.Model;

/**
 * Created by tmutabazi on 4/3/2015.
 */
public class Student {

    private int SID;
    private int scores[] = new int[5];

    public Student(int id, int[] marks)
    {
        this.SID = id;
        this.scores = marks;
    }
    //write public get and set methods for
    public void setSID( int ID)
    {
        this.SID = ID;
    }
    public int  getSID()
    {
        return SID;
    }
    public int[] getScores()
    {
        return scores;
    }

}
