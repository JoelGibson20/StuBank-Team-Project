package com.back4app.java.example;

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
import android.widget.EditText;
import android.widget.Spinner;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;

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

    View view;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.stubank_fragment, container, false);
        emailET = (EditText) view.findViewById(R.id.emailInputStubank);
        amountET = (EditText) view.findViewById(R.id.amountStubank);
        phoneET = (EditText) view.findViewById(R.id.phoneNumberInput);
        referenceET = (EditText) view.findViewById(R.id.referenceStubank);


        List<ParseObject> accountsList = new ArrayList<ParseObject>();
        try {
            accountsList = databaseMethods.getAccounts();
        } catch (ParseException e) {
            e.printStackTrace();
        }
        populateSpinner(accountsList, view);
        Button submit = (Button) view.findViewById(R.id.sendbuttonStubank);
        List<ParseObject> finalAccountsList = accountsList;
        submit.setOnClickListener(this);

        return view;
    }
        @Override
        public void onClick(View view) {

            try {

                ParseObject outgoingAccount = databaseMethods.outgoingAccount(selectedAccount);
                String originalBalance = outgoingAccount.get("balance").toString();
                System.out.println(originalBalance);
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
                if (TextUtils.isEmpty(referenceET.getText())) {
                    referenceET.setError("Please enter a reference!");

                } else if (TextUtils.isEmpty(amountET.getText())) {
                    amountET.setError("Please enter an amount!");
                } else {

                if (!email.equals("")) {
                    currentAccount = databaseMethods.getUser(email);
                } else {
                    currentAccount = databaseMethods.getUserByPhone(phone);
                }


                ParseObject incomingAccount = databaseMethods.getUserCurrentAccount(currentAccount);

                String oldIncomingBalance = incomingAccount.get("balance").toString();
                String incomingAccountName = incomingAccount.get("accountName").toString();
                System.out.println(oldIncomingBalance);
                Double oldIncomingBalanceDob = Double.parseDouble(oldIncomingBalance.substring(1));



                    if (amountDob <= originalBalanceDob) {
                        String transactionAmount = updateBalance(oldIncomingBalanceDob, amountDob, currencySymbol,
                                incomingAccount, originalBalanceDob, outgoingAccount);


                        databaseMethods.createTransaction(selectedAccount, reference, transactionAmount, incomingAccountName, false, currencySymbol);
                        System.out.println(incomingAccountName);
                        Intent intent = new Intent(getContext(), TransferComplete.class);
                        startActivity(intent);


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






            } catch (ParseException e) {
                e.printStackTrace();
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


           // populateSpinner(accountsList, view);
        }

    }
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

       /*     try {
                databaseMethods.currentAccount(outgoingAccount);
            } catch (ParseException e) {
                e.printStackTrace();
            }
     */   }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }});


        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this.getActivity(), android.R.layout.simple_spinner_item, accountsNameList);
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(dataAdapter);
        // String selectedAccount = spinner.getSelectedItem().toString();
        System.out.println(selectedAccount);
        return selectedAccount;}

    public  String updateBalance(Double oldIncomingBalanceDob, Double amountDob, String currencySymbol,
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


