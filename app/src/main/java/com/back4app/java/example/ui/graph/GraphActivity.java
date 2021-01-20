package com.back4app.java.example.ui.graph;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.back4app.java.example.ui.databaseMethods;


import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
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
import java.util.Set;

public class GraphActivity extends AppCompatActivity {
    private final String TAG = "GraphActivityTag";

    private Spinner spinner;
    private Account selectedAccount;

    private RecyclerView mRecyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
    private ArrayList<ExampleItem> exampleItems = new ArrayList<>();






    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_graph);

        //creates buttons for bottom navigation bar
        final ImageButton homeImageButton = findViewById(R.id.homeImageButton);
        final ImageButton graphImageButton = findViewById(R.id.graphImageButton);
        final ImageButton poundImageButton = findViewById(R.id.poundImageButton);
        final ImageButton cardImageButton = findViewById(R.id.cardImageButton);
        final ImageButton gearsImageButton = findViewById(R.id.gearsImageButton);
        final ImageButton calendarImageButton = findViewById(R.id.calendarImageButton);


        /*ArrayList<ExampleItem> exampleItems = new ArrayList<>();
        exampleItems.add(new ExampleItem(R.drawable.ic_android, "Line 1", "Line 2"));
        exampleItems.add(new ExampleItem(R.drawable.ic_left_arrow, "Line 3", "Line 4"));
        exampleItems.add(new ExampleItem(R.drawable.ic_right_arrow, "Line 5", "Line 6"));
        exampleItems.add(new ExampleItem(R.drawable.ic_right_arrow, "Line 7", "Line 8"));
        exampleItems.add(new ExampleItem(R.drawable.ic_right_arrow, "Line 9", "Line 10"));
        exampleItems.add(new ExampleItem(R.drawable.ic_right_arrow, "Line 11", "Line 12"));
        exampleItems.add(new ExampleItem(R.drawable.ic_right_arrow, "Line 13", "Line 14"));
        exampleItems.add(new ExampleItem(R.drawable.ic_right_arrow, "Line 15", "Line 16"));
        exampleItems.add(new ExampleItem(R.drawable.ic_right_arrow, "Line 17", "Line 18"));

         */






        mRecyclerView = findViewById(R.id.analyticsRecycler);
        //mRecyclerView.setHasFixedSize(true);
        mLayoutManager = new LinearLayoutManager(this);
        mAdapter = new ExampleAdapter(exampleItems);

        mRecyclerView.setLayoutManager(mLayoutManager);
        mRecyclerView.setAdapter(mAdapter );


        // creates a dropdown menu to select account
        spinner = findViewById(R.id.accountSpinner);
        List<Account> accountList = populateAccountList();
        ArrayAdapter<Account> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, accountList);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);

        //defines what happens when item in dropdown menu is selected
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
               selectedAccount = (Account) parent.getSelectedItem();
               fillTransactionRecycler(totalTransactionsFromAllSellers());

            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                Toast.makeText(getApplicationContext(), "Please open an account", Toast.LENGTH_SHORT).show();
            }
        });

    }

    //TODO need to add method to remove all old items from spinner
    private void fillTransactionRecycler(Map<String, String> items){
        Set<String> keys = items.keySet();
        for(String key:keys){
            exampleItems.add(new ExampleItem(R.drawable.ic_right_arrow,key,"£"+items.get(key)));
        }
        mAdapter = new ExampleAdapter(exampleItems);

        mRecyclerView.setLayoutManager(mLayoutManager);
        mRecyclerView.setAdapter(mAdapter );
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
        }

        return Integer.toString(total);
    }

    //collects totals spent on each seller and stores amount in a hashmap with seller's name as a key
    private Map<String, String> totalTransactionsFromAllSellers(){
        Map<String, String> allTotals = new HashMap<>();


        List<ParseObject> transactionList = databaseMethods.getAllTransactionsFromOneAccount(selectedAccount.getAccountNumber());
        for(ParseObject transaction:transactionList){
            String seller = transaction.getString("ingoingAccount");
            if(!allTotals.containsKey(seller)){
                allTotals.put(seller, totalTransactionsFromOneSeller(seller, transactionList));
            }
        }
        return allTotals;
    }

    // creates a List of Account objects generated from all accounts of the current user
    private List<Account> populateAccountList(){
        // queries database for all accounts from current user and adds them to a list as "Account" objects
        List<Account> accountList = new ArrayList<>();
        List<ParseObject> accounts = databaseMethods.getAllAccountsOfOneUser();
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

        String accnum = selectedAccount.getAccountNumber();

        Intent intent = new Intent(getApplicationContext(), CalendarActivity.class);
        intent.putExtra("selectedAccount", selectedAccount.toString());
        intent.putExtra("accountnumber", accnum);
        startActivity(intent);
    }


}