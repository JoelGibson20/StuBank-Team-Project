package com.back4app.java.example.ui.graph;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.back4app.java.example.R;
import com.parse.GetCallback;
import com.parse.Parse;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;

import java.sql.Ref;

public class DateTransferActivity extends CalendarActivity {
    private static final String TAG = "DateTransferActivity";


    TextView transactionvalue;
    TextView transactionreference;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_datetransfer);

        final Button backbutton = findViewById(R.id.backbutton);

        ParseQuery<ParseObject> query = ParseQuery.getQuery("Transactions");
        query.whereEqualTo("objectId", "RdTPBzuFrs");
        query.getFirstInBackground((transaction, e) -> {
            if (e == null) {
                String Value = transaction.getString("value");
                String Reference = transaction.getString("reference");
                transactionreference.setText(Reference);
                transactionvalue.setText(Value);
                Log.d(TAG, "transaction: " + Reference);
                Log.d(TAG, "Amount: " + Value);

            }else{
                Log.d(TAG, "error");
            }
        });



    }




    public void backbuttonOnClick(View v) {
        Intent intent = new Intent(getApplicationContext(), CalendarActivity.class);
        startActivity(intent);

    }

    //const Transactions = Parse.Object.extent("Transactions");
    //const transactions = new Parse.Query


    }



