package com.back4app.java.example;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import com.back4app.java.example.ui.databaseMethods;
import com.parse.ParseException;
import com.parse.ParseObject;

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
