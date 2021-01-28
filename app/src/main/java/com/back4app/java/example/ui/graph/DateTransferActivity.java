package com.back4app.java.example.ui.graph;



import com.back4app.java.example.ui.databaseMethods;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.back4app.java.example.R;

import com.parse.ParseObject;

import java.util.ArrayList;
import java.util.List;

public class DateTransferActivity extends CalendarActivity {
    private static final String TAG = "DateTransferActivity";


    String SelectedAccount;
    String AccountNumber;
    String transferdate;

    private RecyclerView recyclerView;
    private RecyclerView.Adapter adapter;
    private RecyclerView.LayoutManager layoutManager;





    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_datetransfer);

        Intent intent=getIntent();
        transferdate = intent.getStringExtra("TransferDate");

        final Button backbutton = findViewById(R.id.backbutton);


        readObject(accountnumber, transferdate);


    }

    //this method reads the data from the database and collects specific bits of data from the inputted date
    public void readObject(String Accountnum, String date) {
        ArrayList<ExampleItem> exampleList = new ArrayList<>();
        //method is called from database methods to retrieve specific bits of data.
        //the data is added to a list to be used in this function.
        List<ParseObject> datetransactionList = databaseMethods.getTransaction(Accountnum, date);
        //goes through the list and displaying the data wanted from each transaction.
        for (ParseObject transaction : datetransactionList) {
            String reference = transaction.getString("reference");
            String value = transaction.getString("value");
            //gets the date from the database and changes the format to suit the query.
            String transactionDate = transaction.getDate("transactionDate").toString();
            String[] separatedDate = transactionDate.split(" ");
            String justDate = separatedDate[2] + " " + separatedDate[1] + " " + separatedDate[5];
            if (justDate.equals(transferdate)) {
                exampleList.add(new ExampleItem(R.drawable.ic_right_arrow, reference, value));
            }



        }
//checks whether there are any transfers on that day and displays a message for the user if not
        boolean listcheck = exampleList.isEmpty();

        if (listcheck){
            exampleList.add(new ExampleItem(R.drawable.ic_android,"No Transfers on this day", ""));
        }
        //creates the recycler view from the data.
        recyclerView = findViewById(R.id.recyclerView);
        layoutManager = new LinearLayoutManager(this);
        adapter = new ExampleAdapter(exampleList);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(adapter);

    }

    //goes back to previous page when button is clicked.
    public void backbuttonOnClick(View v) {
        Intent intent = new Intent(getApplicationContext(), CalendarActivity.class);
        startActivity(intent);

    }
}



