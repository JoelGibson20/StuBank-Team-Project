package com.back4app.java.example;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.back4app.java.example.ui.card.CardActivity;
import com.back4app.java.example.ui.graph.GraphActivity;
import com.back4app.java.example.ui.pound.PoundActivity;
import com.back4app.java.example.ui.settings.SettingsActivity;
import com.parse.ParseException;
import com.parse.ParseObject;

import java.util.ArrayList;
import java.util.List;
import com.back4app.java.example.ui.databaseMethods;

public class HomeScreen extends AppCompatActivity {
    @RequiresApi(api = Build.VERSION_CODES.Q)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_screen);

        //Load all the clickable buttons on the page
        final ImageButton homeImageButton = findViewById(R.id.homeImageButton);
        final ImageButton graphImageButton = findViewById(R.id.graphImageButton);
        final ImageButton poundImageButton = findViewById(R.id.poundImageButton);
        final ImageButton cardImageButton = findViewById(R.id.cardImageButton);
        final ImageButton gearsImageButton = findViewById(R.id.gearsImageButton);
        //Load the greeting text
        final TextView greeting = findViewById(R.id.greeting);


        ParseObject userDetails = null;
        //Gets the user's name to greet them by name
        greeting.setText(String.format("Welcome %s", databaseMethods.getCurrentUser().getString("firstName")));


        //Create cards for the user's accounts
        List<ParseObject> accountsList = new ArrayList<ParseObject>();
        try {
            accountsList = databaseMethods.getAccounts();
        } catch (ParseException e) {
            e.printStackTrace();
        }
        for(int i = 0; i < accountsList.size(); i++){
            System.out.println(accountsList.get(i).getString("accountName"));
            createMyCardView(accountsList.get(i).getString("accountName"),
                    accountsList.get(i).getString("accountNumber"),
                    accountsList.get(i).getString("sortCode"),
                    accountsList.get(i).getString("balance"));
        }

    }


    @RequiresApi(api = Build.VERSION_CODES.Q)
    public void homeButtonOnClick(View v){
        //Disabled for home screen as unnecessary when already on home screen
    }
    public void graphButtonOnClick(View v){
        Intent intent = new Intent(getApplicationContext(), GraphActivity.class);
        startActivity(intent);
    }
    public void poundButtonOnClick(View v){
        Intent intent = new Intent(getApplicationContext(), PoundActivity.class);
        startActivity(intent);
    }
    public void cardButtonOnClick(View v){
        Intent intent = new Intent(getApplicationContext(), CardActivity.class);
        startActivity(intent);
    }
    public void gearsButtonOnClick(View v){
        Intent intent = new Intent(getApplicationContext(), SettingsActivity.class);
        startActivity(intent);
    }


    @RequiresApi(api = Build.VERSION_CODES.Q)
    public void createMyCardView(String accountName, String accountNumber, String sortCode, String balance){
        LinearLayout linearLayout = findViewById(R.id.linearLayout);
        // Initialise the CardView
        CardView cardview = new CardView(getApplicationContext());

       //Layout params which will be applied to the cardView
        LinearLayout.LayoutParams layoutparams = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
        );

        //Set params for CardView
        cardview.setUseCompatPadding(true);
        cardview.setLayoutParams(layoutparams);
        cardview.setMinimumHeight(60);
        cardview.setPreventCornerOverlap(true);
        cardview.setPadding(0,0,0,80);
        cardview.setCardBackgroundColor(Color.parseColor("#FF03DAC5"));
        cardview.setRadius(20);
        cardview.setMaxCardElevation(20);
        cardview.setPadding(0,100,0,20);
        cardview.setClipToPadding(true);

        cardview.setOnClickListener(this::poundButtonOnClick);


        //Create a TextView for the account name
        TextView accountNameText = new TextView(getApplicationContext());
        accountNameText.setPadding(10,10,0,0);
        accountNameText.setLayoutParams(layoutparams);
        accountNameText.setText(accountName);
        accountNameText.setTextAppearance(android.R.style.TextAppearance_Material_Headline);
        accountNameText.setTextColor(Color.WHITE);
        accountNameText.setTextSize(16);
        accountNameText.setTypeface(accountNameText.getTypeface(), Typeface.BOLD);




        //Create a TextView for the account number
        TextView accountNumberText = new TextView(getApplicationContext());
        accountNumberText.setPadding(10,70,0,0);
        accountNumberText.setLayoutParams(layoutparams);
        accountNumberText.setText(accountNumber);
        accountNumberText.setTextAppearance(android.R.style.TextAppearance_Material_Headline);
        accountNumberText.setTextColor(Color.WHITE);
        accountNumberText.setTextSize(16);

        //Create a TextView for the sort code
        TextView sortCodeText = new TextView(getApplicationContext());
        sortCodeText.setPadding(10,130,0,10);
        sortCodeText.setLayoutParams(layoutparams);
        sortCodeText.setText(sortCode);
        sortCodeText.setTextAppearance(android.R.style.TextAppearance_Material_Headline);
        sortCodeText.setTextColor(Color.WHITE);
        sortCodeText.setTextSize(16);


        //Create a TextView for the account balance
        TextView balanceText = new TextView(getApplicationContext());
        balanceText.setPadding(700,70,10,0);
        balanceText.setLayoutParams(layoutparams);
        balanceText.setText(balance);
        balanceText.setTextAppearance(android.R.style.TextAppearance_Material_Headline);
        balanceText.setTextColor(Color.WHITE);
        balanceText.setTextSize(16);
        balanceText.setTypeface(accountNameText.getTypeface(), Typeface.BOLD);

        //Add the TextViews to the CardView
        cardview.addView(accountNameText);
        cardview.addView(accountNumberText);
        cardview.addView(sortCodeText);
        cardview.addView(balanceText);


        //Add the CardView to the linear layout in the ScrollView
        linearLayout.addView(cardview);
    }


}