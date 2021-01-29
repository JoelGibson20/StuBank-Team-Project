package com.back4app.java.example.ui.pound;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.Spinner;

import com.back4app.java.example.ui.HomeScreen;
import com.back4app.java.example.R;
import com.back4app.java.example.ui.card.CardPage;
import com.back4app.java.example.ui.card.CreateCardPage;
import com.back4app.java.example.ui.databaseMethods;
import com.back4app.java.example.ui.graph.GraphActivity;
import com.back4app.java.example.ui.settings.SettingsActivity;
import com.parse.ParseException;
import com.parse.ParseObject;

import java.util.ArrayList;
import java.util.List;


public class PoundActivity extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        List<ParseObject> accountsList = new ArrayList<ParseObject>();
        try {
            accountsList = databaseMethods.getAccounts();
        } catch (ParseException e) {
            e.printStackTrace();

        }
        databaseMethods.checkIfHasCard();


        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pound);
        //Set buttons at bottom of screen
        final ImageButton homeImageButton = findViewById(R.id.homeImageButton);
        final ImageButton graphImageButton = findViewById(R.id.graphImageButton);
        final ImageButton poundImageButton = findViewById(R.id.poundImageButton);
        final ImageButton cardImageButton = findViewById(R.id.cardImageButton);
        final ImageButton gearsImageButton = findViewById(R.id.gearsImageButton);
        Button ownAccount = findViewById(R.id.ownAccountButton);
        Button otherAccount = findViewById(R.id.otherAccountButton);


        ownAccount.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), TransferBetweenOwnAccounts.class);

                startActivity(intent);


            }
        });
        otherAccount.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), TransferToOthers.class);

                startActivity(intent);
            }
        });
    }

    public void homeButtonOnClick(View v) {
        Intent intent = new Intent(getApplicationContext(), HomeScreen.class);
        startActivity(intent);
    }

    public void graphButtonOnClick(View v) {
        Intent intent = new Intent(getApplicationContext(), GraphActivity.class);
        startActivity(intent);
    }

    public void poundButtonOnClick(View v) {
/*        Intent intent = new Intent(getApplicationContext(), PoundActivity.class);
        startActivity(intent);*/
    }

    public void cardButtonOnClick(View v) {
        Intent intent;
        if (databaseMethods.hasCard) {
            intent = new Intent(getApplicationContext(), CardPage.class);
        } else {
            intent = new Intent(getApplicationContext(), CreateCardPage.class);
        }
        startActivity(intent);
    }

    public void gearsButtonOnClick(View v) {
        Intent intent = new Intent(getApplicationContext(), SettingsActivity.class);
        startActivity(intent);
    }

    public void ownAccountOnClick(View v) {
        Intent intent = new Intent(getApplicationContext(), TransferBetweenOwnAccounts.class);
        startActivity(intent);

    }

    public void otherAccountOnClick(View v) {
        Intent intent = new Intent(getApplicationContext(), TransferToOthers.class);
        startActivity(intent);
    }

}