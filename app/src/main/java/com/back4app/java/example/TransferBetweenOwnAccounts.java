package com.back4app.java.example;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;

import com.back4app.java.example.ui.databaseMethods;
import com.parse.ParseException;
import com.parse.ParseObject;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;


public class TransferBetweenOwnAccounts extends AppCompatActivity {
    //Variable returned after Spinner method called
    String selectedAccount = "";
    //Selected outgoing account
    String selectedAccount1 = "";
    //Selected incoming account
    String selectedAccount2 = "";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        List<ParseObject> accountsList = new ArrayList<ParseObject>();
        try {
            accountsList = databaseMethods.getAccounts();
        } catch (ParseException e) {
            e.printStackTrace();

        }





        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_transfer_between_own_accounts);

        populateSpinner(accountsList, "outgoing_spinner");
        populateSpinner(accountsList, "incoming_spinner");

        Button send = (Button) findViewById(R.id.buttonSendOwnAccount);
        send.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                String outgoingAccount = selectedAccount1;
                String incomingAccount = selectedAccount2;
                try {
                    ParseObject outgoingAccountObject = databaseMethods.outgoingAccount(selectedAccount1);
                    ParseObject incomingAccountObject = databaseMethods.outgoingAccount(selectedAccount2);
                    String outgoingBalance = outgoingAccountObject.getString("balance");
                    String incomingBalance = incomingAccountObject.getString("balance");
                    //Remove currency symbol from account balance to allow balance to be compared against
                    //amount being transferred
                    //Save currency symbol to be added back on after operations carried out
                     String currencySymbol = outgoingBalance.substring(0,1);
                   outgoingBalance = outgoingBalance.substring(1);
                   incomingBalance = incomingBalance.substring(1);
                   Double outgoingBalanceDob = Double.parseDouble(outgoingBalance);
                    Double incomingBalanceDob = Double.parseDouble(incomingBalance);
                    EditText et = (EditText) findViewById(R.id.amountToOwnAcc);
                    String amount = et.getText().toString();
                    Double amountDob = Double.parseDouble(amount);
                    if (amountDob<=outgoingBalanceDob) {
                        Double newOutgoingBalance = (outgoingBalanceDob-amountDob);
                        Double newIncomingBalance = (incomingBalanceDob+amountDob);

                        DecimalFormat df = new DecimalFormat("#.00");
                        String newOutgoingBalanceStr = currencySymbol + " " + (df.format(newOutgoingBalance).toString());
                        String newIncomingBalanceStr = currencySymbol + " " + (df.format(newIncomingBalance).toString());

                        outgoingAccountObject.put("balance", newOutgoingBalanceStr);
                        incomingAccountObject.put("balance", newIncomingBalanceStr);
                        outgoingAccountObject.saveInBackground();
                        incomingAccountObject.saveInBackground();

                        Intent intent = new Intent(getApplicationContext(), TransferComplete.class);
                        startActivity(intent);
                    }
                    else {

                        //Create dialogue which is presented to user if the outgoing account has a
                        //balance less than the amount they wish to transfer
                        AlertDialog ad = new AlertDialog.Builder(getApplicationContext()).create();
                        ad.setTitle("Insufficient Funds");
                        ad.setMessage("Please add funds or transfer from a different account");
                        ad.setButton(AlertDialog.BUTTON_NEUTRAL, "OK",
                                new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int which) {
                                        dialog.dismiss();
                                    }
                                });
                        ad.show();
                    }


                } catch (ParseException e) {
                    e.printStackTrace();
                }




            }



        }
        );

    }


    public String populateSpinner(List<ParseObject> accountsList, String spinnerToUse) {
        ArrayList<String> accountsNameList = new ArrayList<String>();
        for (int i = 0; i < accountsList.size(); i++) {
            accountsNameList.add(accountsList.get(i).getString("accountName"));
        }
        final String chosenSpinner = spinnerToUse;
        Integer id = getResources().getIdentifier(spinnerToUse, "id", getPackageName());
        Spinner spinner = (Spinner) findViewById(id);
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {;

            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                //If the method is being called for outgoing account
                if (chosenSpinner.equals("outgoing_spinner")) {
                    selectedAccount1 = adapterView.getItemAtPosition(i).toString();
                }
                else {
                    selectedAccount2 = adapterView.getItemAtPosition(i).toString();
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }});


        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, accountsNameList);
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(dataAdapter);
        // String selectedAccount = spinner.getSelectedItem().toString();
        //Outgoing account has not been changed meaning this spinner will be incoming account
        //Set returned value depending on which spinner is being called
        if (selectedAccount1.equals("")){
            selectedAccount = selectedAccount2;
        }
        //
        else {
            selectedAccount = selectedAccount1;
        }
        //Return corresponding account
        return selectedAccount;}


}
