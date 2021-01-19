package com.back4app.java.example;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;

import com.back4app.java.example.ui.databaseMethods;
import com.parse.ParseException;
import com.parse.ParseObject;

import java.util.ArrayList;
import java.util.List;

public class NewRecipient extends AppCompatActivity{
String selectedAccount = "";
    @Override


    protected void onCreate(Bundle savedInstanceState) {
        List<ParseObject> accountsList = new ArrayList<ParseObject>();
        try {
            accountsList = databaseMethods.getAccounts();
        } catch (ParseException e) {
            e.printStackTrace();

        }



        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_new_recipient);


        String outgoingEmail = ((EditText) findViewById(R.id.emailInput)).getText().toString();
        Button submit = (Button) findViewById(R.id.sendbutton);
        List<ParseObject> finalAccountsList = accountsList;
        populateSpinner(accountsList);
        submit.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                System.out.println(selectedAccount);
                String email = ((EditText) findViewById(R.id.emailInput)).toString();
                String sort = ((EditText) findViewById(R.id.sort)).toString();
                String account = ((EditText) findViewById(R.id.account)).toString();
                String payee = ((EditText) findViewById(R.id.payee)).toString();
                String reference = ((EditText) findViewById(R.id.reference)).toString();
                String amount = ((EditText) findViewById(R.id.amount)).toString();
              // String oldBalance = finalAccountsList.get("aass").getString("balance");
              //  Double newBalance =
                try {
                    List <ParseObject> outgoingAccount = databaseMethods.outgoingAccount(selectedAccount);
                    databaseMethods.updateBalance(outgoingAccount, "400");
                    System.out.println("balance updated");
                }
                catch (ParseException e) {
                    e.printStackTrace();
                }

                Intent intent = new Intent(getApplicationContext(), TransferComplete.class);
                startActivity(intent);


                if (!outgoingEmail.equals("")) {

                }
            }
        });


    }
    public String populateSpinner(List<ParseObject> accountsList) {
        ArrayList<String> accountsNameList = new ArrayList<String>();
        for (int i = 0; i < accountsList.size(); i++) {
            accountsNameList.add(accountsList.get(i).getString("accountName"));
        }

        Spinner spinner = (Spinner) findViewById(R.id.accounts_spinner);
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {;

        @Override
        public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
             selectedAccount = adapterView.getItemAtPosition(i).toString();

       /*     try {
                databaseMethods.currentAccount(outgoingAccount);
            } catch (ParseException e) {
                e.printStackTrace();
            }
     */   }

        @Override
        public void onNothingSelected(AdapterView<?> adapterView) {

        }});


        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, accountsNameList);
            dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            spinner.setAdapter(dataAdapter);
       // String selectedAccount = spinner.getSelectedItem().toString();

    return selectedAccount;}


}


