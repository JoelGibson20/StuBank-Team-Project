package com.back4app.java.example;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.back4app.java.example.ui.card.CardActivity;
import com.back4app.java.example.ui.graph.GraphActivity;
import com.back4app.java.example.ui.pound.PoundActivity;
import com.back4app.java.example.ui.settings.SettingsActivity;
import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;

import java.util.List;
import com.back4app.java.example.ui.databaseMethods;
import com.parse.ParseUser;

public class HomeScreen extends AppCompatActivity {
    int x = 0;
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

        
    }


    @RequiresApi(api = Build.VERSION_CODES.Q)
    public void homeButtonOnClick(View v){
        x += 50;
        CreateMyCardView(x);
      /*Intent intent = new Intent(getApplicationContext(), HomeScreen.class);
      startActivity(intent);*/
        //Disabled for home screen as unnecessary
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
    public void CreateMyCardView(int x){
        System.out.println("X =" + x);
        LinearLayout linearLayout = findViewById(R.id.linearLayout);
        // Initialise the CardView
        CardView cardview = new CardView(getApplicationContext());

        // Create the “wrap_content” layout params that you’ll apply to the various
        // UI elements we’re going to create
        LinearLayout.LayoutParams layoutparams = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
        );

        // Set the layoutParams on the CardView
        cardview.setLayoutParams(layoutparams);

        // Set the card’s corner radius
        cardview.setRadius(6);

        // Set its background color
        cardview.setCardBackgroundColor(Color.GRAY);

        // Set its maximum elevation
        cardview.setMaxCardElevation(6);

        cardview.setLeftTopRightBottom(400,x,50,0);

        // Create a TextView
        TextView textview = new TextView(getApplicationContext());

        // Apply the layoutParams (wrap_content) to this TextView
        textview.setLayoutParams(layoutparams);

        // Define the text you want to display
        textview.setText("Hello, World!");

        // Define the text’s appearance, including its color
        textview.setTextAppearance(android.R.style.TextAppearance_Material_Headline);
        textview.setTextColor(Color.BLACK);


        // Add the content to your CardView. Here, we’re adding the TextView//
        cardview.addView(textview);


        // Add the CardView to your layout
        linearLayout.addView(cardview);
    }


}