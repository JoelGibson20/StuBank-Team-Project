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

public class OtherFragment extends Fragment implements View.OnClickListener{
    final Context context = getActivity();
    String selectedAccount = "";
    EditText amountET;
    EditText sortCodeET;
    EditText accountNumberET;
    EditText referenceET;
    EditText payeeNameET;
    String outgoingAccountNumber="";
    boolean checked = false;
    private static final String TAG = "StubankFragment";
    String currencySymbol = "";
    Double originalBalanceDob;
    ParseObject outgoingAccount;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.other_fragment, container, false);
        amountET = (EditText) view.findViewById(R.id.amountOther);
        referenceET = (EditText) view.findViewById(R.id.referenceOther);
        sortCodeET = (EditText) view.findViewById(R.id.sortOther);
        accountNumberET = (EditText) view.findViewById(R.id.accountOther);
        payeeNameET = (EditText) view.findViewById(R.id.payeeOther);


        List<ParseObject> accountsList = new ArrayList<ParseObject>();
        try {
            accountsList = databaseMethods.getAccounts();
        } catch (ParseException e) {
            e.printStackTrace();
        }

        populateSpinner(accountsList, view);
        Button submit = (Button) view.findViewById(R.id.sendbutton);
        List<ParseObject> finalAccountsList = accountsList;
        submit.setOnClickListener(this);




        return view;
    }
    @Override
    public void onClick(View view) {
        try {
            outgoingAccount = databaseMethods.outgoingAccount(selectedAccount);
            String originalBalance = outgoingAccount.get("balance").toString();
            outgoingAccountNumber = outgoingAccount.get("accountNumber").toString();
            String outgoingAccountSort = outgoingAccount.get("sortCode").toString();
            outgoingAccountNumber = outgoingAccountSort + " " + outgoingAccountNumber;
             currencySymbol = originalBalance.substring(0, 1);
             originalBalanceDob = Double.parseDouble(originalBalance.substring(1));

        }



        catch (ParseException e) {
            e.printStackTrace();
       System.out.println("Fail2");
        }

        String amount = amountET.getText().toString();
        Double amountDob = null;
        if (!amount.equals("")) {
            amountDob = Double.parseDouble(amount);
        }

        String reference = referenceET.getText().toString();
        String currentAccount = "";
        if (TextUtils.isEmpty(referenceET.getText())) {
            referenceET.setError("Please enter a reference!");

        } else if (TextUtils.isEmpty(amountET.getText())) {
            amountET.setError("Please enter an amount!");
        }
        else if (TextUtils.isEmpty(sortCodeET.getText())) {
            sortCodeET.setError("Please enter a sort code!");
        }
        else if (TextUtils.isEmpty(payeeNameET.getText())) {
            payeeNameET.setError("Please enter a name!");
        }
        else {

                String payeeName = payeeNameET.getText().toString();
                ParseObject incomingAccount =null;
                String sortCode = sortCodeET.getText().toString();
                String formattedSortCode = sortCode.substring(0,2) + "-" + sortCode.substring(2,4) +
                        "-" + sortCode.substring(4,6);
                String accountNumber = accountNumberET.getText().toString();
                accountNumber = formattedSortCode + " " + accountNumber;
            System.out.println(accountNumber);
                try {
                    final ParseObject finalIncomingAccount = databaseMethods.getAccountByNumber(accountNumber);
                    String oldIncomingBalance = finalIncomingAccount.get("balance").toString();
                    String incomingAccountName = finalIncomingAccount.get("accountName").toString();
                    String incomingAccountNumber = finalIncomingAccount.get("sortCode").toString();
                    incomingAccountNumber = incomingAccountNumber + " " + finalIncomingAccount.get("accountNumber");
                    Double oldIncomingBalanceDob = Double.parseDouble(oldIncomingBalance.substring(1));



                    if (amountDob <= originalBalanceDob) {



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



                                        StubankFragment sf = new StubankFragment();
                                        String transactionAmount = sf.updateBalance(oldIncomingBalanceDob, finalAmountDob, currencySymbol,
                                                finalIncomingAccount, originalBalanceDob, outgoingAccount);

                                        try {

                                            //Prevents payee being saved twice
                                            boolean exists = databaseMethods.payeeAlreadySaved(finalOutgoingAccountNumber);
                                            if (exists) {
                                                 checked = false;
                                            }
                                            databaseMethods.createTransaction(finalOutgoingAccountNumber, reference, transactionAmount, finalIncomingAccountNumber, checked, currencySymbol);
                                        } catch (ParseException e) {
                                            e.printStackTrace();
                                        }
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





//If the user tries to transfer to an account without Stubank, just their balance is updated
                //and not the payee
         catch (ParseException e) {
            e.printStackTrace();

             if (amountDob <= originalBalanceDob) {



                 AlertDialog ad2 = new AlertDialog.Builder(getActivity()).create();
                 ad2.setTitle("Confirm Transfer");
                 DecimalFormat df = new DecimalFormat("#.00");
                 String messageAmount = df.format(amountDob);
                 ad2.setMessage("You are transferring " + currencySymbol + messageAmount + " to " + payeeName);
                 Double finalAmountDob = amountDob;
                 String finalOutgoingAccountNumber = outgoingAccountNumber;
                 String finalIncomingAccountNumber = accountNumber;
                 Double finalAmountDob1 = amountDob;
                 ad2.setButton(AlertDialog.BUTTON_POSITIVE, "Confirm",
                         new DialogInterface.OnClickListener() {
                             public void onClick(DialogInterface dialog, int which) {


                                final Double finalAD = finalAmountDob1;
                                 StubankFragment sf = new StubankFragment();
                                 DecimalFormat df = new DecimalFormat("#.00");
                                 Double newOutgoingBalance = (originalBalanceDob - finalAmountDob1);
                                 String newOutgoingBalanceStr = newOutgoingBalance.toString();
                                 newOutgoingBalanceStr = df.format(newOutgoingBalance);
                                 newOutgoingBalanceStr = currencySymbol + newOutgoingBalanceStr;
                                 outgoingAccount.put("balance", newOutgoingBalanceStr);
                                 outgoingAccount.saveInBackground();
                                 String transactionAmount = df.format(finalAD);

                                 try {

                                     //Prevents payee being saved twice
                                     boolean exists = databaseMethods.payeeAlreadySaved(finalOutgoingAccountNumber);
                                     if (exists) {
                                         checked = false;
                                     }
                                     databaseMethods.createTransaction(finalOutgoingAccountNumber, reference, transactionAmount, finalIncomingAccountNumber, checked, currencySymbol);
                                 } catch (ParseException e) {
                                     e.printStackTrace();
                                 }
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


    }

                }





    public String populateSpinner(List<ParseObject> accountsList, View view) {
        ArrayList<String> accountsNameList = new ArrayList<String>();
        for (int i = 0; i < accountsList.size(); i++) {
            accountsNameList.add(accountsList.get(i).getString("accountName"));
        }
        Spinner spinner = (Spinner) view.findViewById(R.id.accounts_spinner_other);
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

        System.out.println(selectedAccount);
        return selectedAccount;}


    public void onViewCreated(@NotNull View view, @Nullable Bundle savedInstanceState) {
        List<ParseObject> accountsList = new ArrayList<ParseObject>();
        try {
            accountsList = databaseMethods.getAccounts();
        } catch (ParseException e) {
            e.printStackTrace();

        }}







}





