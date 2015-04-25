package com.example.tmutabazi.mortgage.UserInterface;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;

import com.example.tmutabazi.mortgage.Util.DatabaseConnector;
import com.example.tmutabazi.mortgage.Model.Mortgage;
import com.example.tmutabazi.mortgage.R;


public class UserInterface extends ActionBarActivity {


    // EditTexts for calculator information
    private EditText purchasePriceEditText;
    private EditText downPaymentEditText;
    private EditText mortgageTermEditText;
    private EditText interestRateEditText;
    private EditText propertyTaxEditText;
    private EditText propertyInsuranceEditText;
    private Spinner monthEditText;
    private Spinner yearEditText;
    private EditText PMIEditText;
    Mortgage mort;
    DatabaseConnector db;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        purchasePriceEditText = (EditText) findViewById(R.id.purchase_price);
        downPaymentEditText = (EditText) findViewById(R.id.down_payment);
        mortgageTermEditText = (EditText) findViewById(R.id.mortgage_term);
        interestRateEditText = (EditText) findViewById(R.id.interest_rate);
        propertyTaxEditText = (EditText) findViewById(R.id.property_tax);
        propertyInsuranceEditText = (EditText) findViewById(R.id.property_insurance);
        monthEditText = (Spinner) findViewById(R.id.month);
        yearEditText = (Spinner) findViewById(R.id.year);
        db = new DatabaseConnector(this);
      //  PMIEditText = (EditText) findViewById(R.id.pmi);

        Button submitButton = (Button) findViewById(R.id.submit);
        submitButton.setOnClickListener(
                new Button.OnClickListener() {
                    public void onClick(View v) {
                        try {


                            double purchasePrice = Double.parseDouble(purchasePriceEditText.getText().toString());
                            double downPayment = Double.parseDouble(downPaymentEditText.getText().toString());
                            int mortgageTerm = Integer.parseInt(mortgageTermEditText.getText().toString());
                            double interestRate = Double.parseDouble(interestRateEditText.getText().toString());
                            double propertyTax = Double.parseDouble(propertyTaxEditText.getText().toString());
                            double propertyInsurance = Double.parseDouble(propertyInsuranceEditText.getText().toString());
                            int month = Integer.parseInt(monthEditText.getSelectedItem().toString());
                            int year = Integer.parseInt(yearEditText.getSelectedItem().toString());
                           mort = new Mortgage(purchasePrice, downPayment, mortgageTerm, interestRate, propertyTax, propertyInsurance,month,year);
                            db.insertData(mort);

                            Intent myIntent = new Intent(UserInterface.this, CalculatedMortgage.class);
                            myIntent.putExtra("mort", mort);
                            UserInterface.this.startActivity(myIntent);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }

                    }


                }

        );
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
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
