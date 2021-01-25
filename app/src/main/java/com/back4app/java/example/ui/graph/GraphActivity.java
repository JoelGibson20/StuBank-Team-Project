package com.back4app.java.example.ui.graph;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.back4app.java.example.ui.databaseMethods;


import android.content.Intent;
import android.graphics.Color;
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
import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter;
import com.github.mikephil.charting.utils.ColorTemplate;
import com.parse.FindCallback;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
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

    private ArrayList<PieEntry> pieEntries;

    private ArrayList<BarEntry> barEntries;


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

    private void buildBarChart(){
        barEntries = new ArrayList<>();
        BarChart barChart = findViewById(R.id.analyticsBarChart);
        HashMap<String, Float> monthlyValues = totalsByMonth();
        int count = 0;
        ArrayList<String> lables = new ArrayList<>();
        for(String key: monthlyValues.keySet()){
            barEntries.add(new BarEntry(count, monthlyValues.get(key)));
            count++;
            lables.add(key);
        }

        barEntries.add(new BarEntry(count, calculateSpendingPrediction(monthlyValues)));
        lables.add("Prediction");

        BarDataSet barDataSet = new BarDataSet(barEntries, "Monthly Totals");
        barDataSet.setColors(ColorTemplate.MATERIAL_COLORS);
        barDataSet.setValueTextColor(Color.BLACK);
        barDataSet.setValueTextSize(16f);

        BarData barData = new BarData(barDataSet);

        barChart.setFitBars(true);
        barChart.setData(barData);
        barChart.getDescription().setEnabled(false);
        barChart.animateY(1000);
        barChart.getXAxis().setValueFormatter(new IndexAxisValueFormatter(lables));

        XAxis xAxis = barChart.getXAxis();
        xAxis.setGranularity(1f);
        xAxis.setGranularityEnabled(true);

        barChart.invalidate();


    }

    private HashMap<String, Float> totalsByMonth(){
        List<ParseObject> transactionList = databaseMethods.getAllOutgoingTransactionsFromOneAccount(selectedAccount.getAccountNumber());
        HashMap<String,Float> monthlyTotals = new HashMap<>();
        for (ParseObject transaction: transactionList){
            String date = transaction.getDate("transactionDate").toString();
            String month = date.substring(4,7);
            String year = date.substring(24);
            String key = month + year;
            float currentValue = Float.parseFloat(transaction.getString("value").substring(1));

            if(!monthlyTotals.containsKey(month+year)){
                monthlyTotals.put(key, currentValue);
            }
            else{
                float totalValue = monthlyTotals.get(key);
                monthlyTotals.put(key, totalValue + currentValue);
            }
            //Log.d(TAG, day + " " + month + " " + year);
        }
        Log.d(TAG, monthlyTotals.toString());
        return monthlyTotals;
    }

    private float calculateSpendingPrediction(HashMap<String, Float> monthlyTotals){
        float total = 0f;
        for(String key: monthlyTotals.keySet()){
            total += monthlyTotals.get(key);
        }

        Log.d(TAG, Float.toString(total/monthlyTotals.keySet().size()));
        return total/monthlyTotals.keySet().size();

    }



    private void buildPieChart(){
        pieEntries = new ArrayList<>();
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
        pieChart.animate();
        pieChart.invalidate();
    }

    private void buildTransactionsRecycler(){
        mRecyclerView = findViewById(R.id.analyticsRecycler);
        //mRecyclerView.setHasFixedSize(true);
        mLayoutManager = new LinearLayoutManager(this);
        mAdapter = new ExampleAdapter(exampleItems);
        mRecyclerView.setLayoutManager(mLayoutManager);
        mRecyclerView.setAdapter(mAdapter );

    }

    private void fillTransactionRecycler(Map<String, String> items){
        exampleItems = new ArrayList<>();
        Set<String> keys = items.keySet();
        for(String key:keys){
            exampleItems.add(new ExampleItem(R.drawable.ic_right_arrow,key,"£"+items.get(key)));
        }
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


        List<ParseObject> transactionList = databaseMethods.getAllOutgoingTransactionsFromOneAccount(selectedAccount.getAccountNumber());
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
        Log.d(TAG, "8888888888888888888888888888888888");
        Log.d(TAG, accnum);

        Intent intent = new Intent(getApplicationContext(), CalendarActivity.class);
        intent.putExtra("selectedAccount", selectedAccount.toString());
        intent.putExtra("accountnumber", accnum);
        startActivity(intent);
    }


}