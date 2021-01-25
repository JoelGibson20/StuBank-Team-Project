package com.back4app.java.example.ui.graph;



import com.back4app.java.example.data.databaseMethods;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.back4app.java.example.R;

import com.parse.ParseObject;

import java.util.List;

public class DateTransferActivity extends CalendarActivity {
    private static final String TAG = "DateTransferActivity";

    //ListView listView;
    TextView transactionvalue;
    TextView transactionreference;

    String SelectedAccount;
    String AccountNumber;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_datetransfer);


        Intent intent=getIntent();
        SelectedAccount = intent.getStringExtra("selectedAccount");
        SelectedAccount = intent.getStringExtra("accountnumber");

        //listView=(ListView)findViewById(R.id.listview);
        transactionreference=(TextView)findViewById(R.id.transactionreference);
        transactionvalue=(TextView)findViewById(R.id.transactionreference);

        Log.d(TAG, "------------------------------------------------------------------");
        Log.d(TAG, "------------------------------------------------------------------");
        Log.d(TAG, SelectedAccount);
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


        //readObject(SelectedAccount.getAccountNumber());

    }

    public void readObject(String Accountnum){

        List<ParseObject> datetransactionList = databaseMethods.getTransaction(Accountnum);

        for(ParseObject transaction:datetransactionList){
            String reference = transaction.getString("reference");
            String value = transaction.getString("value");
            //transactionreference.setText(selectedAccount);
            transactionvalue.setText(value);
            System.out.println("TEST");

        }

    }



    public void backbuttonOnClick(View v) {
        Intent intent = new Intent(getApplicationContext(), CalendarActivity.class);
        startActivity(intent);

    }
}



