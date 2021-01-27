package com.back4app.java.example.ui.graph;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.back4app.java.example.ui.databaseMethods;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.Toast;

import com.back4app.java.example.HomeScreen;
import com.back4app.java.example.R;
import com.back4app.java.example.ui.card.CardPage;
import com.back4app.java.example.ui.pound.PoundActivity;
import com.back4app.java.example.ui.settings.SettingsActivity;
import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter;
import com.github.mikephil.charting.utils.ColorTemplate;
import com.parse.ParseObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class GraphActivity extends AppCompatActivity {
    private final String TAG = "GraphActivityTag"; // Tag used for Logcat messages

    private Account selectedAccount;

    private ArrayList<ExampleItem> accountSpinnerItems = new ArrayList<>();

    private List<ParseObject> transactionList;


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


        // creates a dropdown menu to select account
        Spinner spinner = findViewById(R.id.accountSpinner);
        List<Account> accountList = populateAccountList();
        ArrayAdapter<Account> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, accountList);

        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);

        //builds graphs and other analytics for the account that is selected
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
               selectedAccount = (Account) parent.getSelectedItem();
               transactionList = databaseMethods.getAllOutgoingTransactionsFromOneAccount(selectedAccount.getAccountNumber());
               fillTransactionRecycler(totalTransactionsFromAllSellers());
               buildTransactionsRecycler();
               buildPieChart();
               calculateSpendingPrediction(totalsByMonth());
               buildBarChart();

            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                Toast.makeText(getApplicationContext(), "Please open an account", Toast.LENGTH_SHORT).show();
            }
        });
    }


    // creates a bar chart which displays the total amount spent each month and then a spending prediction for the next month
    private void buildBarChart(){
        ArrayList<BarEntry> barEntries = new ArrayList<>();
        BarChart barChart = findViewById(R.id.analyticsBarChart);
        HashMap<String, Float> monthlyValues = totalsByMonth();
        int count = 0;
        ArrayList<String> labels = new ArrayList<>();
        // loops through each month that money has been spent and adds the total to entries
        for(String key: monthlyValues.keySet()){
            barEntries.add(new BarEntry(count, monthlyValues.get(key)));
            count++;
            labels.add(key);// used to preserve order of addition to bar chart so each bar is given the correct lable
        }

        //adds spending prediction as the final bar on the graph
        barEntries.add(new BarEntry(count, calculateSpendingPrediction(monthlyValues)));
        labels.add("Prediction");

        BarDataSet barDataSet = new BarDataSet(barEntries, "Monthly Totals");
        barDataSet.setColors(ColorTemplate.MATERIAL_COLORS);
        barDataSet.setValueTextColor(Color.BLACK);
        barDataSet.setValueTextSize(16f);

        BarData barData = new BarData(barDataSet);

        barChart.setFitBars(true);
        barChart.setData(barData);
        barChart.getDescription().setEnabled(false);
        barChart.animateY(1000);
        barChart.getXAxis().setValueFormatter(new IndexAxisValueFormatter(labels));

        // keeps labels in correct place when zooming on bar chart
        XAxis xAxis = barChart.getXAxis();
        xAxis.setGranularity(1f);
        xAxis.setGranularityEnabled(true);

        barChart.invalidate();
    }


    //creates a hashmap where the keys are months money was spent and the values are how much money was spent in that month
    private HashMap<String, Float> totalsByMonth(){
        HashMap<String,Float> monthlyTotals = new HashMap<>();
        //goes through every transaction from a given account
        for (ParseObject transaction: transactionList){
            //parses transaction date to get month and year to create keys for the hashmap
            String date = transaction.getDate("transactionDate").toString();
            String month = date.substring(4,7);
            String year = date.substring(24);
            String key = month + year;
            float currentValue = Float.parseFloat(transaction.getString("value").substring(1)); // substring removes £ from beginning of string

            //creates new entry in hashmap if this month has not encountered yet
            if(!monthlyTotals.containsKey(month+year)){
                monthlyTotals.put(key, currentValue);
            }
            //if month has already been encountered, adds to that months current total
            else{
                float totalValue = monthlyTotals.get(key);
                monthlyTotals.put(key, totalValue + currentValue);
            }
        }
        return monthlyTotals;
    }


    //calculates mean of all previous months totals
    private float calculateSpendingPrediction(HashMap<String, Float> monthlyTotals){
        float total = 0f;
        for(String key: monthlyTotals.keySet()){
            total += monthlyTotals.get(key);
        }
        Log.d(TAG, Float.toString(total/monthlyTotals.keySet().size()));
        return total/monthlyTotals.keySet().size();
    }


    //builds a pie chart to display how much has been spent where
    private void buildPieChart(){
        ArrayList<PieEntry> pieEntries = new ArrayList<>();
        PieChart pieChart = findViewById(R.id.analyticsPieChart);
        Map<String, String> transactionTotals = totalTransactionsFromAllSellers();
        for(String key: transactionTotals.keySet()){
            Float value = Float.parseFloat(transactionTotals.get(key));
            pieEntries.add( new PieEntry(value, key));
        }

        PieDataSet pieDataSet = new PieDataSet(pieEntries, "Transaction Totals");
        pieDataSet.setColors(ColorTemplate.COLORFUL_COLORS);
        pieDataSet.setValueTextColor(Color.BLACK);
        pieDataSet.setValueTextSize(16f);
        pieDataSet.setDrawValues(true);

        PieData pieData = new PieData(pieDataSet);
        pieChart.setData(pieData);
        pieChart.getDescription().setEnabled(false);
        pieChart.setCenterText("Transaction Totals");
        pieChart.setDrawEntryLabels(false);
        pieChart.invalidate();
    }


    //creates a recycler view to display all transaction totals by seller
    private void buildTransactionsRecycler(){
        RecyclerView mRecyclerView = findViewById(R.id.analyticsRecycler);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(this);
        RecyclerView.Adapter mAdapter = new ExampleAdapter(accountSpinnerItems);
        mRecyclerView.setLayoutManager(mLayoutManager);
        mRecyclerView.setAdapter(mAdapter);
    }


    //fills recycler view with transaction totals
    private void fillTransactionRecycler(Map<String, String> items){
        accountSpinnerItems = new ArrayList<>();
        Set<String> keys = items.keySet();
        for(String key:keys){
            accountSpinnerItems.add(new ExampleItem(R.drawable.ic_right_arrow,key,"£"+items.get(key)));
        }
    }


    // returns how much was payed to a given seller from one account
    private String totalTransactionsFromOneSeller(String seller){
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
        for(ParseObject transaction:transactionList){
            String seller = transaction.getString("ingoingAccount");
            if(!allTotals.containsKey(seller)){
                allTotals.put(seller, totalTransactionsFromOneSeller(seller));
            }
        }
        return allTotals;
    }


    // creates a List of Account objects generated from all accounts of the current user
    private List<Account> populateAccountList(){
        // queries database for all accounts from current user and adds them to a list as "Account" objects
        List<Account> accountList = new ArrayList<>();
        for (ParseObject account: databaseMethods.getAllAccountsOfOneUser()) {
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
        Intent intent = new Intent(getApplicationContext(), CardPage.class);
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