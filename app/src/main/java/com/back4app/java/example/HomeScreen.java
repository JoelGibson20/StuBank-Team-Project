package com.back4app.java.example;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;

import java.util.List;
import com.back4app.java.example.ui.databaseMethods;

public class HomeScreen extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_screen);

        final TextView greeting = findViewById(R.id.greeting);
        ParseObject userDetails = null;

        try{
            greeting.setText(String.format("Welcome %s", getIntent().getStringExtra("FIRSTNAME")));
        } catch(NullPointerException e){
            try {
                userDetails = databaseMethods.retrieveUserDetails(getIntent().getStringExtra("USERNAME"));
                greeting.setText(String.format("Welcome %s", userDetails.getString("firstName")));
            } catch (ParseException e2) {
                e2.printStackTrace();
            }

        }
        }


}