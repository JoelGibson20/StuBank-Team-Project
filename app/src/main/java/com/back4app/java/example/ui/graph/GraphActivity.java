package com.back4app.java.example.ui.graph;

import androidx.appcompat.app.AppCompatActivity;

import com.back4app.java.example.ui.databaseMethods;


import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.Toast;

import com.back4app.java.example.ui.graph.CalendarActivity;
import com.back4app.java.example.HomeScreen;
import com.back4app.java.example.R;
import com.back4app.java.example.ui.card.CardActivity;
import com.back4app.java.example.ui.pound.PoundActivity;
import com.back4app.java.example.ui.settings.SettingsActivity;
import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class GraphActivity extends AppCompatActivity {
    private Spinner spinner;
    private Account selectedAccount;
    private final String TAG = "GraphActivityTag";

    //ImageButton calendarButton;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_graph);

        final ImageButton homeImageButton = findViewById(R.id.homeImageButton);
        final ImageButton graphImageButton = findViewById(R.id.graphImageButton);
        final ImageButton poundImageButton = findViewById(R.id.poundImageButton);
        final ImageButton cardImageButton = findViewById(R.id.cardImageButton);
        final ImageButton gearsImageButton = findViewById(R.id.gearsImageButton);
        final ImageButton calendarImageButton = findViewById(R.id.calendarImageButton);

        spinner = findViewById(R.id.accountSpinner);

        // creates a dropdown menu to select account
        List<Account> accountList = populateAccountList();
        ArrayAdapter<Account> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, accountList);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);



        //displays the graph of the account is selected
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
               selectedAccount = (Account) parent.getSelectedItem();
               String total = totalTransactionsFromOneSeller("test", databaseMethods.getAllTransactionsFromOneAccount(selectedAccount));

               Toast.makeText(getApplicationContext(), total, Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });


        //calendarButton = findViewById(R.id.calendarImageButton);

        //calendarButton.setOnClickListener(v -> {
            //Intent intent = new Intent(GraphActivity.this, CalendarActivity.class);
            //startActivity(intent);
        //});

    }



    // returns how much was payed to a given seller from one account
    private String totalTransactionsFromOneSeller(String seller, List<ParseObject> transactionList){
        String userAccountNumber = selectedAccount.getAccountNumber();
        int total = 0;

        for (ParseObject transaction : transactionList) {
            if(transaction.getString("ingoingAccount").equals(seller)){
                String valueAsString = transaction.getString("value");
                total += Integer.parseInt(valueAsString.substring(1));

            }
        };

        return Integer.toString(total);
    }


    // creates a List of Account objects generated from all accounts of the current user
    private List<Account> populateAccountList(){
        // queries database for all accounts from current user and adds them to a list as "Account" objects
        List<Account> accountList = new ArrayList<>();
        List<ParseObject> accounts = databaseMethods.getAllAccountsofOneUser();
        for (ParseObject account: accounts) {
            accountList.add(new Account(
                    account.getString("objectId"),
                    account.getString("accountOwner"),
                    account.getString("sortCode"),
                    account.getString("accountNumber"),
                    account.getString("accountName"),
                    account.getDate("createdAt"),
                    account.getString("balance")
            ));
        }
        return accountList;
    }

    //TODO need to actually generate a graph, just a placeholder method for now
    public void displayGraph(){
        String x = "";
    }



    public void openCalendar(){
        Intent intent = new Intent(GraphActivity.this, CalendarActivity.class);
        startActivity(intent);
    }

    public void homeButtonOnClick(View v) {
        Intent intent = new Intent(getApplicationContext(), HomeScreen.class);
        startActivity(intent);
    }

    public void graphButtonOnClick(View v) {
/*        Intent intent = new Intent(getApplicationContext(), GraphActivity.class);
        startActivity(intent);*/
    }

    public void poundButtonOnClick(View v) {
        Intent intent = new Intent(getApplicationContext(), PoundActivity.class);
        startActivity(intent);
    }

    public void cardButtonOnClick(View v) {
        Intent intent = new Intent(getApplicationContext(), CardActivity.class);
        startActivity(intent);
    }

    public void gearsButtonOnClick(View v) {
        Intent intent = new Intent(getApplicationContext(), SettingsActivity.class);
        startActivity(intent);
    }

    public void calendarButtonOnClick(View v) {
        Intent intent = new Intent(getApplicationContext(), CalendarActivity.class);
        startActivity(intent);
    }


}