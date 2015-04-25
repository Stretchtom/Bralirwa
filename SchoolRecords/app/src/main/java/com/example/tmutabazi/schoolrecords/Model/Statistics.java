package com.example.tmutabazi.schoolrecords.Model;

import java.util.ArrayList;

/**
 * Created by tmutabazi on 4/4/2015.
 */
public class Statistics {

    private	int [] lowscores = new int [5];
    private	int [] highscores = new int [5];
    private	float [] avgscores = new float [5];
    private	int[] quiz1 = new int[40];
    private	int[] quiz2 = new int[40];
    private	int[] quiz3 = new int[40];
    private	int[] quiz4 = new int[40];
    private	int[] quiz5 = new int[40];


    //This method will find lowest score and store it in an array names lowscores
  public String findlow(ArrayList<Student> stu){
        int minVal1 = 100;
        int minVal2 = 100;
        int minVal3 = 100;
        int minVal4 = 100;
        int minVal5 = 100;
        int i = 0;

        quizArray(stu);

        // This loop finds the minimum in the quiz arrays
        for(int j = 0; j < stu.size(); j++)
        {
            if(quiz1[j] < minVal1)
            {
                minVal1 = quiz1[j];
            }
            if(quiz2[j] < minVal2)
            {
                minVal2 = quiz2[j];
            }
            if(quiz3[j] < minVal3)
            {
                minVal3 = quiz3[j];
            }
            if(quiz4[j] < minVal4)
            {
                minVal4 = quiz4[j];
            }
            if(quiz5[j] < minVal5)
            {
                minVal5 = quiz5[j];
            }
        }

        int[]  lowscores = {minVal1,minVal2,minVal3,minVal4,minVal5};
      String low = printInt("Low Scores", lowscores);
      return low;

    }
    //This method will find highest score and store it in an array names highscores
  public String findhigh(ArrayList<Student> stu){
        int maxVal1 = 0;
        int maxVal2 = 0;
        int maxVal3 = 0;
        int maxVal4 = 0;
        int maxVal5 = 0;


        quizArray(stu);
        // This loop is for finding out the maximum in the array quizes

        for(int j = 0; j < stu.size(); j++)
        {
            if(quiz1[j] >  maxVal1)
            {
                maxVal1 = quiz1[j];
            }
            if(quiz2[j] > maxVal2)
            {
                maxVal2 = quiz2[j];
            }
            if(quiz3[j] > maxVal3)
            {
                maxVal3 = quiz3[j];
            }
            if(quiz4[j] > maxVal4)
            {
                maxVal4 = quiz4[j];
            }
            if(quiz5[j] > maxVal5)
            {
                maxVal5 = quiz5[j];
            }
        }

        int[] highscores = {maxVal1,maxVal2,maxVal3,maxVal4,maxVal5};
       String high = printInt("Higscores", highscores);
      return high;
    }


    //This method will find avg score for each quiz and store it in an array names
   public String findavg(ArrayList<Student> stu){
        int avg1 = 0;
        int avg2 = 0;
        int avg3 = 0;
        int avg4 = 0;
        int avg5 = 0;
        int i;

        quizArray(stu);

        for( i=0; i < stu.size(); i++)
        {
            avg1 = avg1 + quiz1[i];
            avg2 = avg2 + quiz2[i];
            avg3 = avg3 + quiz3[i];
            avg4 = avg4 + quiz4[i];
            avg5 = avg5 + quiz5[i];
        }

        float [] avgscores = {avg1/i,avg2/i,avg3/i,avg4/i,avg5/i};

      String avg = printFloat("AVERAGE", avgscores);
       return avg;

    }

   public void quizArray(ArrayList<Student> stu){
        int i = 0;
        while(i < stu.size())
        {
            int[] temp = new int[5];


            temp = stu.get(i).getScores();
            quiz1[i] = temp[0];
            quiz2[i] = temp[1];
            quiz3[i] = temp[2];
            quiz4[i] = temp[3];
            quiz5[i] = temp[4];
            i++;
        }

    }
    // This method is used to print the highscores and lowscores
    public String printInt(String name, int[] array)
    {
        StringBuilder sb = new StringBuilder();
        sb.append(name);
        sb.append("  \t");

        for(int z=0; z < array.length; z++ )
        {

            sb.append(array[z]);
            sb.append("  \t");

        }
        sb.append("  \n");
        return sb.toString();

    }
    // This method is used to print the average
    public String printFloat(String name, float[] array)
    {

        StringBuilder sb = new StringBuilder();
        sb.append(name);
        sb.append("  \t");

        for(int z=0; z < array.length; z++ )
        {

            sb.append(array[z]);
            sb.append("  \t");

        }
        sb.append("  \n");
        return sb.toString();
    }
    // Getters used in this class
    public int[] getLowScores()
    {
        return lowscores;
    }

    public int[] getHighScores()
    {
        return highscores;
    }
    public float[] getAverageScores()
    {
        return avgscores;
    }
}
