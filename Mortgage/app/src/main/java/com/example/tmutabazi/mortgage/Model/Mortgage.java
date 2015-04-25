package com.example.tmutabazi.mortgage.Model;

import java.io.Serializable;

/**
 * Created by tmutabazi on 3/29/2015.
 */
public class Mortgage implements Serializable {

    double purchasePrice;
    double downPayment;
    int mortgageTerm;
    double interestRate;
    double propertyTax;
    double propertyInsurance;
    int month;
    int year;



   public Mortgage(double purchase, double down, int mortgage, double interest, double property, double insurance,int month,int year)
   {
         this.purchasePrice = purchase;
         this.downPayment = down;
         this.mortgageTerm = mortgage;
         this.interestRate = interest;
         this.propertyTax = property;
         this.propertyInsurance = insurance;
         this.month = month;
         this.year = year;


   }

    public double getPurchasePrice()
    {
        return purchasePrice;
    }
    public double getDownPayment()
    {
        return downPayment;
    }
    public int getMortgageTerm()
    {
        return mortgageTerm;
    }
    public double getInterestRate()
    {
        return interestRate;
    }
    public double getPropertyTax()
    {
        return propertyTax;
    }
    public double getPropertyInsurance()
    {
        return propertyInsurance;
    }

    public int getMonth()
    {
        return month;
    }
    public int getYear()
    {
        return year;
    }
    public  double calculateMonthlyPayment( double loanAmount, double termInYears, double interestRate) {
        interestRate /= 100.0;
        double monthlyRate = interestRate / 12.0;
        double termInMonths = termInYears * 12;
        double monthlyPayment = (loanAmount*monthlyRate) /  (1-Math.pow(1+monthlyRate, -termInMonths));

        return monthlyPayment;
    }

    public double TotalMortagePayment(double monthlyPayment,double MortgageTerm)
    {
       double termInMonths = MortgageTerm * 12;
        double total = termInMonths * monthlyPayment;

        return total;
    }
    public String payOffDate(int month,int year,int mortgageTerm)
    {
        int monthValue = month - 1;
        int yearValue = year + mortgageTerm;
        if(monthValue == 0)
        {

            monthValue = 12;
            yearValue = yearValue - 1;
        }
        String payoffDate = monthValue + "/" + yearValue;

        return payoffDate;
    }
}

