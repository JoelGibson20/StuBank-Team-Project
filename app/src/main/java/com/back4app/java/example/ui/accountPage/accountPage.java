package com.back4app.java.example.ui.accountPage;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.TextView;

import com.back4app.java.example.R;
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

        //Retrieve the account in question from the intent
        ParseObject accountParseObject = (ParseObject) getIntent().getExtras().get("accountParseObject");

        accountName.setText(accountParseObject.getString("accountName"));
        accountNumber.setText(String.format("Account Number: %s",accountParseObject.getString("accountNumber")));
        sortCode.setText(String.format("Sort Code: %s",accountParseObject.getString("sortCode")));
        balance.setText(accountParseObject.getString("balance"));
    }
}