package com.back4app.java.example.ui.pound;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Spinner;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;

import com.back4app.java.example.R;
import com.back4app.java.example.ui.databaseMethods;
import com.parse.ParseException;
import com.parse.ParseObject;

import org.jetbrains.annotations.NotNull;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

public class StubankFragment extends Fragment implements View.OnClickListener {
    final Context context = getActivity();
    String selectedAccount = "";
    private static final String TAG = "StubankFragment";
    List<ParseObject> accountsList = new ArrayList<ParseObject>();
    EditText emailET;
    EditText phoneET;
    EditText amountET;
    EditText referenceET;
    boolean checked = false;
    View view;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        //Get text inputted by user
        view = inflater.inflate(R.layout.stubank_fragment, container, false);
        emailET = (EditText) view.findViewById(R.id.emailInputStubank);
        amountET = (EditText) view.findViewById(R.id.amountStubank);
        phoneET = (EditText) view.findViewById(R.id.phoneNumberInput);
        referenceET = (EditText) view.findViewById(R.id.referenceStubank);

        //Get value of checkbox to know whether or not to save payee
        CheckBox checkbox = (CheckBox) view.findViewById(R.id.checkBoxStubank);
        checkbox.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                checked = checkbox.isChecked();
            }


            });

        List<ParseObject> accountsList = new ArrayList<ParseObject>();
        //Create accounts list
        try {
            accountsList = databaseMethods.getAccounts();
        } catch (ParseException e) {
            e.printStackTrace();
        }
        //Populate spinner with accounts
        populateSpinner(accountsList, view);
        Button submit = (Button) view.findViewById(R.id.sendbuttonStubank);
        List<ParseObject> finalAccountsList = accountsList;
        submit.setOnClickListener(this);

        return view;
    }
        @Override
        public void onClick(View view) {

            try {
                    //Get outgoing account details
                    ParseObject outgoingAccount = databaseMethods.outgoingAccount(selectedAccount);
                    String originalBalance = outgoingAccount.get("balance").toString();
                    String outgoingAccountNumber = outgoingAccount.get("accountNumber").toString();
                    String outgoingAccountSort = outgoingAccount.get("sortCode").toString();
                    outgoingAccountNumber = outgoingAccountSort + " " + outgoingAccountNumber;
                    String currencySymbol = originalBalance.substring(0, 1);
                    Double originalBalanceDob = Double.parseDouble(originalBalance.substring(1));

                String amount = amountET.getText().toString();
                Double amountDob = null;
                if (!amount.equals("")) {
                    amountDob = Double.parseDouble(amount);
                }
                String email = emailET.getText().toString();
                String phone = phoneET.getText().toString();


                String reference = referenceET.getText().toString();
                String currentAccount = "";
                //Ensure fields not left blank
                if (TextUtils.isEmpty(referenceET.getText())) {
                    referenceET.setError("Please enter a reference!");

                } else if (TextUtils.isEmpty(amountET.getText())) {
                    amountET.setError("Please enter an amount!");
                } else {
                //Get user by email if it is not null
                if (!email.equals("")) {
                    currentAccount = databaseMethods.getUser(email);
                }
                //Else get by phone
                else {
                    currentAccount = databaseMethods.getUserByPhone(phone);
                }

                String payeeName = databaseMethods.getUserNames(currentAccount);
                ParseObject incomingAccount = databaseMethods.getUserCurrentAccount(currentAccount);

                //Get incoming account details
                String oldIncomingBalance = incomingAccount.get("balance").toString();
                String incomingAccountName = incomingAccount.get("accountName").toString();
                String incomingAccountNumber = incomingAccount.get("sortCode").toString();
                incomingAccountNumber = incomingAccountNumber + " " + incomingAccount.get("accountNumber");
                Double oldIncomingBalanceDob = Double.parseDouble(oldIncomingBalance.substring(1));


                    //Only execute transfer if user will not be overdrawn
                    if (amountDob <= originalBalanceDob) {


                        //Ask user to confirm transfer
                        AlertDialog ad2 = new AlertDialog.Builder(getActivity()).create();
                        ad2.setTitle("Confirm Transfer");
                        DecimalFormat df = new DecimalFormat("#.00");
                        String messageAmount = df.format(amountDob);
                        ad2.setMessage("You are transferring " + currencySymbol + messageAmount + " to " + payeeName);
                        Double finalAmountDob = amountDob;
                        String finalOutgoingAccountNumber = outgoingAccountNumber;
                        String finalIncomingAccountNumber = incomingAccountNumber;
                        ad2.setButton(AlertDialog.BUTTON_POSITIVE, "Confirm",
                                new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int which) {



                                        //Execute transfer and create transaction
                                        String transactionAmount = updateBalance(oldIncomingBalanceDob, finalAmountDob, currencySymbol,
                                                incomingAccount, originalBalanceDob, outgoingAccount);

                                        try {

                                            //Prevents payee being saved twice
                                            boolean exists = databaseMethods.payeeAlreadySaved(finalOutgoingAccountNumber);
                                            if (exists) {
                                               // checked = false;
                                            }
                                            databaseMethods.createTransaction(finalOutgoingAccountNumber, reference, transactionAmount, finalIncomingAccountNumber, checked, currencySymbol);
                                        } catch (ParseException e) {
                                            e.printStackTrace();
                                        }
                                        //Take user to confirmation page
                                        Intent intent = new Intent(getContext(), TransferComplete.class);
                                        startActivity(intent);



                                        dialog.dismiss();
                                    }
                                });
                        ad2.setButton(AlertDialog.BUTTON_NEGATIVE, "Cancel",
                                new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int i) {
                                        dialog.dismiss();
                                    }
                                });
                                ad2.show();







                    } else {
                        AlertDialog ad1 = new AlertDialog.Builder(getActivity()).create();
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


                }


          //If an incorrect email has been entered, alert the user
            } catch (ParseException e) {
                e.printStackTrace();
                AlertDialog ad3 = new AlertDialog.Builder(getActivity()).create();
                ad3.setTitle("Stubank Account Not Found");
                ad3.setMessage("Please try again");
                ad3.setButton(AlertDialog.BUTTON_NEUTRAL, "OK",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                            }
                        });
                ad3.show();
            }


        }


    public void onViewCreated(@NotNull View view, @Nullable Bundle savedInstanceState) {
        List<ParseObject> accountsList = new ArrayList<ParseObject>();
        try {
            accountsList = databaseMethods.getAccounts();
        } catch (ParseException e) {
            e.printStackTrace();


            super.onViewCreated(view, savedInstanceState);

            List<ParseObject> finalAccountsList = accountsList;

        }

    }
    //Provides adapter and data source for spinner
    public String populateSpinner(List<ParseObject> accountsList, View view) {
        ArrayList<String> accountsNameList = new ArrayList<String>();
        for (int i = 0; i < accountsList.size(); i++) {
            accountsNameList.add(accountsList.get(i).getString("accountName"));
        }
        Spinner spinner = (Spinner) view.findViewById(R.id.accounts_spinner_stubank);
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                selectedAccount = adapterView.getItemAtPosition(i).toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }});


        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this.getActivity(), android.R.layout.simple_spinner_item, accountsNameList);
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(dataAdapter);
        // String selectedAccount = spinner.getSelectedItem().toString();
        System.out.println(selectedAccount);
        return selectedAccount;}

    public String updateBalance(Double oldIncomingBalanceDob, Double amountDob, String currencySymbol,
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
        String transactionAmount = df.format(amountDob);
        return transactionAmount;

    }

}


