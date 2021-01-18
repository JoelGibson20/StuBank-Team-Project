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
import java.util.List;

public class GraphActivity extends AppCompatActivity {
    private Spinner spinner;
    private Account selectedAccount;
    private final String TAG = "GraphActivity";

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
                Account account = (Account) parent.getSelectedItem();
                String accountName = account.getAccountName();
                Toast.makeText(getApplicationContext(), accountName, Toast.LENGTH_SHORT).show();

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

    private List<ParseObject> getAllAccounts() {
        ParseQuery<ParseObject> query = ParseQuery.getQuery("Accounts");
        query.whereEqualTo("accountOwner", databaseMethods.getCurrentUser().getObjectId());
        try {
            return (query.find());
        }
        catch (ParseException e){
            Log.d(TAG, e.toString());
            return null;
        }
    }

    private List<Account> populateAccountList(){
        // queries database for all accounts from current user and adds them to a list as "Account" objects
        List<Account> accountList = new ArrayList<>();
        List<ParseObject> accounts = getAllAccounts();
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
        ParseQuery<ParseObject> query = ParseQuery.getQuery("Transactions");
        query.whereEqualTo("outgoingAccount", "00001111");
        query.findInBackground(new FindCallback<ParseObject>() {
            @Override
            public void done(List<ParseObject> transactions, ParseException e) {
                if (e == null){
                    String value = transactions.get(0).getString("value");
                    Log.d("transactions", value);
                }
                else{
                    Log.d("transactions","Error", e);
                }

            }
        });

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