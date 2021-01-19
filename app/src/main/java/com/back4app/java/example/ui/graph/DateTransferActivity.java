package com.back4app.java.example.ui.graph;



import com.back4app.java.example.ui.databaseMethods;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

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
import java.util.List;

public class DateTransferActivity extends CalendarActivity {
    private static final String TAG = "DateTransferActivity";

    //ListView listView;
    TextView transactionvalue;
    TextView transactionreference;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_datetransfer);

        //listView=(ListView)findViewById(R.id.listview);
        transactionreference=(TextView)findViewById(R.id.transactionreference);
        transactionvalue=(TextView)findViewById(R.id.transactionreference);


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


        readObject("RdTPBzuFrs");


    }


    public void readObject(String Object_ID){

        List<ParseObject> datetransactionList = databaseMethods.getTransaction(Object_ID);



        for(ParseObject transaction:datetransactionList){
            String reference = transaction.getString("reference");
            String value = transaction.getString("value");
            transactionreference.setText(reference);
            transactionvalue.setText(value);
            System.out.println("TEST");


        }



    }



    public void backbuttonOnClick(View v) {
        Intent intent = new Intent(getApplicationContext(), CalendarActivity.class);
        startActivity(intent);

    }




    }



