package com.back4app.java.example.ui.pound;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;

import com.back4app.java.example.R;
import com.back4app.java.example.ui.databaseMethods;
import com.parse.ParseException;
import com.parse.ParseObject;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

public class NewRecipient extends AppCompatActivity{
String selectedAccount = "";
final Context context = this;
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
                EditText emailET = (EditText) findViewById(R.id.emailInput);
                String email = emailET.getText().toString();
                EditText sortET = (EditText) findViewById(R.id.sort);
                String sort = sortET.getText().toString();
                System.out.println(sort);
               // if (sort.equals()) {
                    //sort = sort.substring(0, 2) + "-" + sort.substring(2, 4) + "-" + sort.substring(4, 6);
               // }
                EditText accountET = (EditText) findViewById(R.id.account);
                String account = accountET.getText().toString();
                EditText payeeET = (EditText) findViewById(R.id.payee);
                String payee = payeeET.getText().toString();
                EditText referenceET = (EditText) findViewById(R.id.reference);
                referenceET.getText().toString();
                EditText amountET = (EditText) findViewById(R.id.amount);
                String amount = amountET.getText().toString();
                Double amountDob = Double.parseDouble(amount);

                try {
                    ParseObject outgoingAccount = databaseMethods.outgoingAccount(selectedAccount);
                    String originalBalance = outgoingAccount.get("balance").toString();
                    String currencySymbol = originalBalance.substring(0,1);
                    Double originalBalanceDob = Double.parseDouble(originalBalance.substring(1));
                    if (amountDob<=originalBalanceDob) {

                        if (email.equals("")) {
                            //If user chooses to use sort code and account number
                            ParseObject incomingAccount = databaseMethods.getAccount(sort, account);
                            String oldIncomingBalance = incomingAccount.get("balance").toString();
                            Double oldIncomingBalanceDob = Double.parseDouble(oldIncomingBalance.substring(1));
                            updateBalance(oldIncomingBalanceDob, amountDob, currencySymbol,
                                    incomingAccount, originalBalanceDob, outgoingAccount);
                        }
                        else {
                            //If user chooses to use email address
                            String currentAccount = databaseMethods.getUser(email);
                            ParseObject incomingAccount = databaseMethods.getUserCurrentAccount(currentAccount);
                            String oldIncomingBalance = incomingAccount.get("balance").toString();
                            Double oldIncomingBalanceDob = Double.parseDouble(oldIncomingBalance.substring(1));




                            updateBalance(oldIncomingBalanceDob, amountDob, currencySymbol,
                                    incomingAccount, originalBalanceDob, outgoingAccount);
                        }

                    }
                    else {

                        //Create dialogue which is presented to user if the outgoing account has a
                        //balance less than the amount they wish to transfer
                        AlertDialog ad1 = new AlertDialog.Builder(context).create();
                        ad1.setTitle("Insufficient Funds");
                        ad1.setMessage("Please add funds or transfer from a different account");
                        ad1.setButton(AlertDialog.BUTTON_NEUTRAL, "OK",
                                new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int which) {
                                        dialog.dismiss();
                                    }
                                });
                        ad1.show();
                    }

                    System.out.println("balance updated");
                }
                catch (ParseException e) {
                    e.printStackTrace();
                }

                Intent intent = new Intent(getApplicationContext(), TransferComplete.class);
                startActivity(intent);


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

    public void updateBalance(Double oldIncomingBalanceDob, Double amountDob, String currencySymbol,
                              ParseObject incomingAccount, Double originalBalanceDob, ParseObject outgoingAccount) {
        DecimalFormat df = new DecimalFormat("#.00");


        Double newIncomingBalance = (oldIncomingBalanceDob + amountDob);
        String newIncomingBalanceStr = newIncomingBalance.toString();
        newIncomingBalanceStr = df.format(newIncomingBalance);
        newIncomingBalanceStr = currencySymbol + newIncomingBalanceStr;
        incomingAccount.put("balance", newIncomingBalanceStr);
        incomingAccount.saveInBackground();


        Double newOutgoingBalance = (originalBalanceDob - amountDob);
        String newOutgoingBalanceStr = newOutgoingBalance.toString();
        newOutgoingBalanceStr = df.format(newOutgoingBalance);
        newOutgoingBalanceStr = currencySymbol + newOutgoingBalanceStr;
        outgoingAccount.put("balance", newOutgoingBalanceStr);
        outgoingAccount.saveInBackground();
    }

}


