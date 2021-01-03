package com.back4app.java.example;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
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

        final ImageButton homeImageButton = findViewById(R.id.homeImageButton);
        final ImageButton graphImageButton = findViewById(R.id.graphImageButton);
        final ImageButton poundImageButton = findViewById(R.id.poundImageButton);
        final ImageButton cardImageButton = findViewById(R.id.cardImageButton);
        final ImageButton gearsImageButton = findViewById(R.id.gearsImageButton);

        final TextView greeting = findViewById(R.id.greeting);
        ParseObject userDetails = null;


        try {
            userDetails = databaseMethods.retrieveUserDetails(getIntent().getStringExtra("USERNAME"));
            greeting.setText(String.format("Welcome %s", userDetails.getString("firstName")));
        } catch (ParseException e2) {
            e2.printStackTrace();
        }
        
    }

    public void homeButtonOnClick(View v){
        System.out.println("test 1");
        //Do something
    }
    public void graphButtonOnClick(View v){
        System.out.println("test 2");
        //Do something
    }
    public void poundButtonOnClick(View v){
        System.out.println("test 3");
        //Do something
    }
    public void cardButtonOnClick(View v){
        System.out.println("test 4");
        //Do something
    }
    public void gearsButtonOnClick(View v){
        System.out.println("test 5");
        //Do something
    }


}