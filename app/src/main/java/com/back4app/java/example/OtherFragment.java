package com.back4app.java.example;

import android.content.Context;
import android.os.Bundle;
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
import androidx.fragment.app.Fragment;

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


    private static final String TAG = "StubankFragment";

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.other_fragment, container, false);

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

    public void onViewCreated(@NotNull View view, @Nullable Bundle savedInstanceState) {
        List<ParseObject> accountsList = new ArrayList<ParseObject>();
        try {
            accountsList = databaseMethods.getAccounts();
        } catch (ParseException e) {
            e.printStackTrace();

        }}

    public String populateSpinner(List<ParseObject> accountsList, View view) {
        ArrayList<String> accountsNameList = new ArrayList<String>();
        for (int i = 0; i < accountsList.size(); i++) {
            accountsNameList.add(accountsList.get(i).getString("accountName"));
        }

        Spinner spinner = (Spinner) view.findViewById(R.id.accounts_spinner);
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


    @Override
    public void onClick(View view) {

    }
}





