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
import com.back4app.java.example.ui.signup.SignUpActivity;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.parse.ParseException;
import com.parse.ParseObject;

import java.util.ArrayList;
import java.util.List;

public class PoundActivity extends AppCompatActivity {
    //Variable returned after Spinner method called
    String selectedAccount = "";
    //Selected outgoing account
    String selectedAccount1 = "";
    //Selected incoming account
    String selectedAccount2 = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        List<ParseObject> accountsList = new ArrayList<ParseObject>();
        try {
            accountsList = databaseMethods.getAccounts();
        } catch (ParseException e) {
            e.printStackTrace();

        }


        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pound);

        final ImageButton homeImageButton = findViewById(R.id.homeImageButton);
        final ImageButton graphImageButton = findViewById(R.id.graphImageButton);
        final ImageButton poundImageButton = findViewById(R.id.poundImageButton);
        final ImageButton cardImageButton = findViewById(R.id.cardImageButton);
        final ImageButton gearsImageButton = findViewById(R.id.gearsImageButton);
        Button ownAccount = findViewById(R.id.ownAccountButton);
        Button otherAccount = findViewById(R.id.otherAccountButton);


        ownAccount.setOnClickListener(new View.OnClickListener() {
            public void onClick (View view){
                Intent intent = new Intent(getApplicationContext(), TransferBetweenOwnAccounts.class);

                startActivity(intent);


            }
        });
          otherAccount.setOnClickListener(new View.OnClickListener() {
        public void onClick (View view) {
            Intent intent = new Intent(getApplicationContext(), TransferToOthers.class);

            startActivity(intent);
        }});}
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
    public void cardButtonOnClick(View v){
        Intent intent;
        if (databaseMethods.hasCard){
            intent = new Intent(getApplicationContext(), CardPage.class);
        }
        else {
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



    public String populateSpinner(List<ParseObject> accountsList, String spinnerToUse) {
        ArrayList<String> accountsNameList = new ArrayList<String>();
        for (int i = 0; i < accountsList.size(); i++) {
            accountsNameList.add(accountsList.get(i).getString("accountName"));
        }
        final String chosenSpinner = spinnerToUse;
        Integer id = getResources().getIdentifier(spinnerToUse, "id", getPackageName());
        Spinner spinner = (Spinner) findViewById(id);
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {;

            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                //If the method is being called for outgoing account
                if (chosenSpinner.equals("outgoing_spinner")) {
                selectedAccount1 = adapterView.getItemAtPosition(i).toString();
            }
                else {
                    selectedAccount2 = adapterView.getItemAtPosition(i).toString();
                }
  }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }});


        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, accountsNameList);
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(dataAdapter);
        // String selectedAccount = spinner.getSelectedItem().toString();
        //Outgoing account has not been changed meaning this spinner will be incoming account
        //Set returned value depending on which spinner is being called
        if (selectedAccount1.equals("")){
            selectedAccount = selectedAccount2;
        }
        //
        else {
            selectedAccount = selectedAccount1;
        }
        //Return corresponding account
        return selectedAccount;}


}




