package com.back4app.java.example;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Build;
import android.os.Bundle;
import android.text.InputType;
import android.text.TextUtils;
import android.view.View;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.back4app.java.example.HomeScreen;
import com.back4app.java.example.NewRecipient;
import com.back4app.java.example.R;
import com.back4app.java.example.ui.card.CardActivity;
import com.back4app.java.example.ui.databaseMethods;
import com.back4app.java.example.ui.graph.GraphActivity;
import com.back4app.java.example.ui.settings.SettingsActivity;
import com.back4app.java.example.ui.signup.SignUpActivity;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.parse.ParseException;
import com.parse.ParseObject;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class TransferToOthers extends AppCompatActivity {
    String selectedAccount;
    EditText amountET;
    EditText referenceET;
    ParseObject outgoingAccountOwnerObject;



    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        //List of Parse Objects of all accounts
        List<ParseObject> accountsList = new ArrayList();
        try {
            accountsList = databaseMethods.getAccounts();
        } catch (ParseException e) {
            e.printStackTrace();
        }
        Context context = this;
        String outgoingUser = null;
        String text = null;

        super.onCreate(savedInstanceState);

        //Set view to corresponding layout
        setContentView(R.layout.activity_transfer_to_others);
        final FloatingActionButton addButton = findViewById(R.id.add);

        //List of Parse Objects
        List<ParseObject> savedTransactions = new ArrayList();
        List<ParseObject> savedTransactionsFromAccount = new ArrayList();

        //Gets the userID of the user logged in to allow the app to show saved payees only for themselves
        String currentUser = databaseMethods.getCurrentUser().getObjectId().toString();
        ParseObject currentAccount = null;

        //Creates a parse object of the user logged in
        try {
            currentAccount = databaseMethods.getUserCurrentAccount(currentUser);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        try {

           //Iterates through a user's accounts ensuring saved transactions are presented from all
            //of them
            for (int x=0;x<accountsList.size();x++) {
                ParseObject account = accountsList.get(x);
                String accountNumber = account.get("accountNumber").toString();
                String sortCode = account.get("sortCode").toString();
                accountNumber = sortCode + " " + accountNumber;
                savedTransactions.addAll(databaseMethods.getSavedTransactions(accountNumber));

            }
        }
        catch (ParseException e) {
            e.printStackTrace();
        }
        //Add transactions to set to remove duplicates
        Set<ParseObject> savedTransactionsSet = new HashSet<>(savedTransactions);
        savedTransactions.clear();
        savedTransactions.addAll(savedTransactionsSet);

       //Find layout to use with Card View
        LinearLayout linearLayout = findViewById(R.id.linearLayoutTransactions);
        LinearLayout.LayoutParams layoutparams = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
        );

        for (int i = 0; i < savedTransactions.size(); i++) {
            if (savedTransactions.size()<1) {
                TextView tv = (TextView) findViewById(R.id.noPayees);
                tv.setVisibility(View.VISIBLE);
            }

            String incomingAccount = savedTransactions.get(i).getString("ingoingAccount");
            try {
                outgoingAccountOwnerObject = databaseMethods.getAccountByNumber(incomingAccount);
                final String outgoingAccountOwner = outgoingAccountOwnerObject.get("accountOwner").toString();
                outgoingUser = databaseMethods.getUserNames(outgoingAccountOwner);
                text = outgoingUser + " " + "(" + incomingAccount + ")";
            }
            catch (ParseException e) {
                e.printStackTrace();
            }

            CardView savedPayees = new CardView(getApplicationContext());

            //Set Card View properties
            savedPayees.setUseCompatPadding(true);
            savedPayees.setLayoutParams(layoutparams);
            savedPayees.setMinimumHeight(100);
            savedPayees.setPreventCornerOverlap(true);
            savedPayees.setCardBackgroundColor(Color.parseColor("#FFFFFF"));
            savedPayees.setRadius(20);
            savedPayees.setMaxCardElevation(20);
            savedPayees.setPadding(0, 100, 0, 20);
            savedPayees.setClipToPadding(true);


            String finalOutgoingUser = outgoingUser;
            ParseObject finalIncoming = outgoingAccountOwnerObject;
            List<ParseObject> finalAccountsList = accountsList;
            savedPayees.setOnClickListener(new View.OnClickListener() {
                //Create onClick listener for the saved payees
                public void onClick(View v) {

                    View spinnerView = getLayoutInflater().inflate(R.layout.dialog_spinner, null);
                    amountET = (EditText) spinnerView.findViewById(R.id.amountDialog);
                    referenceET = (EditText) spinnerView.findViewById(R.id.refDialog);
                    AlertDialog.Builder builder = new AlertDialog.Builder(context);


                    builder.setTitle("Transfer to " + finalOutgoingUser);
                    Spinner spinner = (Spinner) spinnerView.findViewById(R.id.dialogSpinner);

                    ArrayList<String> accountsNameList = new ArrayList<String>();
                    for (int i = 0; i < finalAccountsList.size(); i++) {
                        accountsNameList.add(finalAccountsList.get(i).getString("accountName"));
                    }

                    spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

                        @Override
                        public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                            selectedAccount = adapterView.getItemAtPosition(i).toString();
                        }

                        @Override
                        public void onNothingSelected(AdapterView<?> adapterView) {

                        }
                    });


                    ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(TransferToOthers.this, android.R.layout.simple_spinner_item, accountsNameList);
                    dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    spinner.setAdapter(dataAdapter);

                    builder.setView(spinnerView);
                    builder.setPositiveButton("Confirm", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                    ParseObject outgoingAccount = null;
                                    try {
                                        outgoingAccount = databaseMethods.outgoingAccount(selectedAccount);
                                    } catch (ParseException e) {
                                        System.out.println("Fail 2");
                                        e.printStackTrace();
                                    }
                                    String originalBalance = outgoingAccount.get("balance").toString();
                                    String outgoingAccountNumber = outgoingAccount.get("accountNumber").toString();
                                    String outgoingAccountSort = outgoingAccount.get("sortCode").toString();
                                    outgoingAccountNumber = outgoingAccountSort + " " + outgoingAccountNumber;
                                    String currencySymbol = originalBalance.substring(0, 1);
                                    Double originalBalanceDob = Double.parseDouble(originalBalance.substring(1));


                                    if (TextUtils.isEmpty(referenceET.getText())) {
                                        referenceET.setError("Please enter a reference!");
                                        Toast.makeText(getBaseContext(), "Please enter a reference", Toast.LENGTH_LONG).show();
                                    } else if (TextUtils.isEmpty(amountET.getText())) {
                                        Toast.makeText(getBaseContext(), "Please enter an amount", Toast.LENGTH_LONG).show();
                                    } else {
                                        String amount = amountET.getText().toString();
                                        Double amountDob = null;
                                        if (!amount.equals("")) {
                                            amountDob = Double.parseDouble(amount);
                                        }
                                        String reference = referenceET.getText().toString();

                                        ParseObject incomingAccount = outgoingAccountOwnerObject;

                                        String oldIncomingBalance = finalIncoming.get("balance").toString();
                                        String incomingAccountName = finalIncoming.get("accountName").toString();
                                        String incomingAccountNumber = finalIncoming.get("sortCode").toString();
                                        incomingAccountNumber = incomingAccountNumber + " " + finalIncoming.get("accountNumber");
                                        Double oldIncomingBalanceDob = Double.parseDouble(oldIncomingBalance.substring(1));


                                        if (amountDob <= originalBalanceDob) {


                                            AlertDialog ad2 = new AlertDialog.Builder(context).create();
                                            ad2.setTitle("Confirm Transfer");
                                            Double finalAmountDob = amountDob;
                                            String finalOutgoingAccountNumber = outgoingAccountNumber;
                                            String finalIncomingAccountNumber = incomingAccountNumber;
                                            final ParseObject finalOA = outgoingAccount;
                                            ad2.setButton(AlertDialog.BUTTON_POSITIVE, "Confirm",
                                             new DialogInterface.OnClickListener() {
                                             public void onClick(DialogInterface dialog, int which) {


                                                 StubankFragment sf = new StubankFragment();
                                                 String transactionAmount = sf.updateBalance(oldIncomingBalanceDob, finalAmountDob, currencySymbol,
                                                         finalIncoming, originalBalanceDob, finalOA);

                                                 try {
                                                     databaseMethods.createTransaction(finalOutgoingAccountNumber, reference, transactionAmount, finalIncomingAccountNumber, false, currencySymbol);
                                                 } catch (ParseException e) {
                                                     e.printStackTrace();
                                                 }


                                                 Intent intent = new Intent(getApplicationContext(), TransferComplete.class);

                                                 startActivity(intent);
                                             }});
                                            ad2.setButton(AlertDialog.BUTTON_NEGATIVE, "Cancel",
                                                    new DialogInterface.OnClickListener() {
                                                        @Override
                                                        public void onClick(DialogInterface dialog, int i) {
                                                            dialog.dismiss();
                                                        }
                                                    });
                                            ad2.show();






                                        } else {
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
                                        // );

                                        //}
                                    } }});

                    builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            dialogInterface.cancel();
                        }
                    });

                    builder.show();
                }
            });

            TextView payeeName = new TextView(getApplicationContext());
            payeeName.setPadding(10, 40, 0, 40);
            payeeName.setLayoutParams(layoutparams);
            payeeName.setText(text);
            payeeName.setTextAppearance(android.R.style.TextAppearance_Material);
            payeeName.setTextColor(Color.BLACK);
            payeeName.setTextSize(20);
            payeeName.setTypeface(payeeName.getTypeface());

            savedPayees.addView(payeeName);
            linearLayout.addView(savedPayees);

        }

                            addButton.setOnClickListener(new View.OnClickListener()

    {
        public void onClick (View view){
        Intent intent = new Intent(getApplicationContext(), NewRecipientTabbed.class);

        startActivity(intent);


    }
    });}}