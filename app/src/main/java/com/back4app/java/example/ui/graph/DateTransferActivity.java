package com.back4app.java.example.ui.graph;



import com.back4app.java.example.ui.databaseMethods;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.back4app.java.example.HomeScreen;
import com.back4app.java.example.R;
import com.back4app.java.example.ui.card.CardActivity;
import com.back4app.java.example.ui.pound.PoundActivity;
import com.back4app.java.example.ui.settings.SettingsActivity;

import com.back4app.java.example.R;
import com.parse.GetCallback;
import com.parse.Parse;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;

import java.sql.Ref;
import java.util.ArrayList;
import java.util.List;

public class DateTransferActivity extends CalendarActivity {
    private static final String TAG = "DateTransferActivity";

    //ListView listView;


    String SelectedAccount;
    String AccountNumber;

    private RecyclerView recyclerView;
    private RecyclerView.Adapter adapter;
    private RecyclerView.LayoutManager layoutManager;





    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_datetransfer);


        Intent intent=getIntent();
        //SelectedAccount = intent.getStringExtra("selectedAccount");
        //SelectedAccount = intent.getStringExtra("accountnumber");

        //listView=(ListView)findViewById(R.id.listview);


        Log.d(TAG, "------------------------------------------------------------------");
        Log.d(TAG, "------------------------------------------------------------------");
        Log.d(TAG, selectedAccount);
        Log.d(TAG, "------------------------------------------------------------------");
        Log.d(TAG, "------------------------------------------------------------------");




        final Button backbutton = findViewById(R.id.backbutton);

        //ParseQuery<ParseObject> query = ParseQuery.getQuery("Transactions");
        //query.whereEqualTo("objectId", "RdTPBzuFrs");
        //query.getFirstInBackground((transaction, e) -> {
            //if (e == null) {
                //String Value = transaction.getString("value");
                //String Reference = transaction.getString("reference");
                //transactionreference.setText(Reference);
                //transactionvalue.setText(Value);
                //Log.d(TAG, "transaction: " + Reference);
               // Log.d(TAG, "Amount: " + Value);

            //}else{
                //Log.d(TAG, "error");
            //}
        //});


        readObject(accountnumber, myDate.toString());


        ArrayList<ExampleItem> exampleList = new ArrayList<>();
        exampleList.add(new ExampleItem(R.drawable.ic_right_arrow, "Line 1", "Line 2"));
        exampleList.add(new ExampleItem(R.drawable.ic_right_arrow, "Line 3", "Line 4"));
        exampleList.add(new ExampleItem(R.drawable.ic_right_arrow, "Line 5", "Line 6"));


        recyclerView = findViewById(R.id.recyclerView);
        //recyclerView.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(this);
        adapter = new ExampleAdapter(exampleList);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(adapter);




    }

    public void readObject(String Accountnum, String date) {

        Log.d(TAG, "================");
        Log.d(TAG, "INSIDE READ OBJECT");

        List<ParseObject> datetransactionList = databaseMethods.getTransaction(Accountnum, date);

        for (ParseObject transaction : datetransactionList) {
            String reference = transaction.getString("reference");
            Log.d(TAG, "================");
            Log.d(TAG, reference);
            String value = transaction.getString("value");
            Log.d(TAG, "================");
            Log.d(TAG, value);
            System.out.println("TEST");

        }

    }



    public void backbuttonOnClick(View v) {
        Intent intent = new Intent(getApplicationContext(), CalendarActivity.class);
        startActivity(intent);

    }
}



