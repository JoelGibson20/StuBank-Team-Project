package com.back4app.java.example.ui.accountPage;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.back4app.java.example.HomeScreen;
import com.back4app.java.example.R;
import com.back4app.java.example.ui.databaseMethods;
import com.parse.ParseException;
import com.parse.ParseObject;

public class accountPage extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_account_page);

        //Load the TextViews for modification
        final TextView accountName = findViewById(R.id.accountName);
        final TextView accountNumber = findViewById(R.id.accountNumber);
        final TextView sortCode = findViewById(R.id.sortCode);
        final TextView balance = findViewById(R.id.balance);

        //Load buttons to set onclick listeners for them
        final Button renameButton = findViewById(R.id.renameButton);
        final Button lockButton = findViewById(R.id.lockButton);
        final Button statementButton = findViewById(R.id.statementButton);
        final Button shareDetailsButton = findViewById(R.id.shareDetailsButton);

        //Retrieve the account in question from the intent
        ParseObject accountParseObject = (ParseObject) getIntent().getExtras().get("accountParseObject");

        accountName.setText(accountParseObject.getString("accountName"));
        accountNumber.setText(String.format("Account Number: %s",accountParseObject.getString("accountNumber")));
        sortCode.setText(String.format("Sort Code: %s",accountParseObject.getString("sortCode")));
        balance.setText(accountParseObject.getString("balance"));



        renameButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                System.out.println("Rename button clicked");
                try {
                    //Changes name to account
                    //Want a text-bx pop-up here to allow user input for new account name
                    databaseMethods.changeAccountName(accountParseObject,"name change test");
                    accountName.setText(accountParseObject.getString("accountName"));

                } catch (ParseException e) {
                    e.printStackTrace();
                }
            }
        });

        lockButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                System.out.println("Lock button clicked");
            }
        });

        statementButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                System.out.println("Statement button clicked");
            }
        });

        shareDetailsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                System.out.println("Share Details button clicked");
            }
        });

    }
}