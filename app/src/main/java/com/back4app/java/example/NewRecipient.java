package com.back4app.java.example;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;

import com.back4app.java.example.ui.databaseMethods;
import com.parse.ParseException;
import com.parse.ParseObject;

import java.util.ArrayList;
import java.util.List;

public class NewRecipient extends AppCompatActivity {

    @Override


    protected void onCreate(Bundle savedInstanceState) {
        List<ParseObject> accountsList = new ArrayList<ParseObject>();
        try {
            accountsList = databaseMethods.getAccounts();
        } catch (ParseException e) {
            e.printStackTrace();

        }



        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_new_recipient);


        String outgoingEmail = ((EditText) findViewById(R.id.emailInput)).getText().toString();
        Button submit = (Button) findViewById(R.id.sendbutton);
        List<ParseObject> finalAccountsList = accountsList;
        submit.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), TransferComplete.class);
                startActivity(intent);


                if (!outgoingEmail.equals("")) {

                }
            }
        });

        populateSpinner(accountsList);
    }
    public void populateSpinner(List<ParseObject> accountsList) {
        ArrayList<String> accountsNameList = new ArrayList<String>();
        for (int i = 0; i < accountsList.size(); i++) {
            accountsNameList.add(accountsList.get(i).getString("accountName"));
        }

        Spinner spinner = (Spinner) findViewById(R.id.accounts_spinner);
            ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, accountsNameList);
            dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            //System.out.println(accountsNameList);
            //ArrayAdapter<CharSequence> dataAdapter = new ArrayAdapter.createFromResource(this,
                  // accountsNameList, android.R.layout.simple_spinner_item);
            //adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            spinner.setAdapter(dataAdapter);

           // public void onItemSelected(AdapterView<?> parent, View view, int pos, long id) {

     //   }
           // public void onNothingSelected(AdapterView<?> parent) {

      //  }
        //spinner.setOnItemSelectedListener(this);

    }
    }

