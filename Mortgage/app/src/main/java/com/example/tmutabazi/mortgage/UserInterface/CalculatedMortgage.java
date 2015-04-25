package com.example.tmutabazi.mortgage.UserInterface;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

import com.example.tmutabazi.mortgage.Util.DatabaseConnector;
import com.example.tmutabazi.mortgage.Model.Mortgage;
import com.example.tmutabazi.mortgage.R;

public class CalculatedMortgage extends ActionBarActivity {

    DatabaseConnector db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_calculated_mortgage);
        Mortgage mort;
        double purchasePrice = 0;
        double downPayment = 0;
        int mortgageTerm = 0;
        double interestRate = 0;
        double propertyTax = 0;
        double propertyInsurance = 0;
        int month=0;
        int year=0;


        Intent intent = getIntent();
        mort = (Mortgage) intent.getSerializableExtra("mort");
        // monthly pay
        double monthlyPay = mort.calculateMonthlyPayment(mort.getPurchasePrice(),mort.getMortgageTerm(),mort.getInterestRate());

        // Monthly Pay
        TextView mortgageText = (TextView) findViewById(R.id.tmpMoney);
        mortgageText.setText(Double.toString(monthlyPay));

        // Total Payment for Mortgage Term
       TextView total = (TextView) findViewById(R.id.total_payment);
       total.setText(Double.toString(mort.TotalMortagePayment(monthlyPay,mort.getMortgageTerm())));

        // Pay off date
        TextView date = (TextView) findViewById(R.id.date);
        date.setText(mort.payOffDate(mort.getMonth(),mort.getYear(),mort.getMortgageTerm()));


    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_calculated_mortgage, menu);
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
