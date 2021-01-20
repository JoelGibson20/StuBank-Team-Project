package com.back4app.java.example.ui.card;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.InputFilter;
import android.text.InputType;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.ToggleButton;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.back4app.java.example.HomeScreen;
import com.back4app.java.example.R;
import com.back4app.java.example.ui.databaseMethods;
import com.back4app.java.example.ui.graph.GraphActivity;
import com.back4app.java.example.ui.pound.PoundActivity;
import com.back4app.java.example.ui.settings.SettingsActivity;
import com.parse.GetCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;


public class CardActivity extends AppCompatActivity {
    private String cardNumber;
    private String PIN;
    private String cvv;
    private String expiryDate;
    private boolean isFrozen;
    private String username;
    private String userInputPassword;
    private String userInputOldPIN;
    private String userInputNewPIN;
    private String userInputNewPINReenter;
    private final String userObjectId = databaseMethods.getCurrentUser().getObjectId();
    private String accountNumber;
    private String sortCode;
    private String accountID;
    private String cardID;
    private String userInputPIN;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_card);
        getAccountDetails();
        getUsername();

    }
    public void getUsername(){
        ParseQuery<ParseObject> query1 = ParseQuery.getQuery("_User");
        query1.getInBackground(userObjectId, new GetCallback<ParseObject>() {
            public void done(ParseObject result, ParseException e) {
                if (e == null) {
                    username = result.getString("username");
                }
            }
        });
    }

    public void removeCardFromDB() {
        ParseQuery<ParseObject> query = ParseQuery.getQuery("Cards");
        // Retrieve the object by id
        query.getInBackground(cardID, new GetCallback<ParseObject>() {
            public void done(ParseObject entity, ParseException e) {
                if (e == null) {
                        entity.deleteInBackground();
                    }
                }
        });
    }

    public void updateIsFrozenInDB(boolean issFrozen){
        ParseQuery<ParseObject> query = ParseQuery.getQuery("Cards");
        // Retrieve the object by id
        query.getInBackground(cardID, new GetCallback<ParseObject>() {
            public void done(ParseObject entity, ParseException e) {
                if (e == null) {
                    // Update the fields we want to
                    entity.put("Frozen", issFrozen);
                    // All other fields will remain the same
                    entity.saveInBackground();
                }
            }
        });

    }

    public void freezeCardOnClick(View v){
        ToggleButton toggle = (ToggleButton) findViewById(R.id.freezeCard);
        Button cardDetailsButton1 = findViewById(R.id.removeCard);
        Button cardDetailsButton = findViewById(R.id.cardDetails);
        Button cardDetailsButton2 = findViewById(R.id.changePIN);
        Button cardDetailsButton3 = findViewById(R.id.viewPIN);
        cardDetailsButton.setClickable(!toggle.isChecked());
        cardDetailsButton1.setClickable(!toggle.isChecked());
        cardDetailsButton2.setClickable(!toggle.isChecked());
        cardDetailsButton3.setClickable(!toggle.isChecked());
        updateIsFrozenInDB(toggle.isChecked());
    }

    //remember to put this into homescreen oncreate, change the var and method to static and run carddeets in this oncreate and the text views in a separate method on top !!!!!!!!!!!!!!!!!!!!!!
    public void getAccountDetails(){
        ParseQuery<ParseObject> query = ParseQuery.getQuery("Accounts");
        query.whereEqualTo("accountOwner", userObjectId);
        query.whereEqualTo("accountType", "current");
        query.getFirstInBackground(new GetCallback<ParseObject>() {
            public void done(ParseObject account, ParseException e) {
                if (e == null) {
                    accountNumber = account.getString("accountNumber");
                    sortCode = account.getString("sortCode");
                    accountID = account.getObjectId();
                    ((TextView)findViewById(R.id.accountNumber)).setText(accountNumber);
                    ((TextView)findViewById(R.id.sortCode)).setText(sortCode);
                    getCardDetails();
                }
            }
        });
    }

    public void setFreezeChecked(){
        ToggleButton toggle = (ToggleButton) findViewById(R.id.freezeCard);
        toggle.setChecked(isFrozen);
        Button cardDetailsButton1 = findViewById(R.id.removeCard);
        Button cardDetailsButton = findViewById(R.id.cardDetails);
        Button cardDetailsButton2 = findViewById(R.id.changePIN);
        Button cardDetailsButton3 = findViewById(R.id.viewPIN);
        cardDetailsButton.setClickable(!toggle.isChecked());
        cardDetailsButton1.setClickable(!toggle.isChecked());
        cardDetailsButton2.setClickable(!toggle.isChecked());
        cardDetailsButton3.setClickable(!toggle.isChecked());
    }

    public void getCardDetails(){
        ParseQuery<ParseObject> query = ParseQuery.getQuery("Cards");
        // The query will search for a ParseObject, given its objectId.
        // When the query finishes running, it will invoke the GetCallback
        // with either the object, or the exception thrown
        query.whereEqualTo("accountID", accountID);
        query.getFirstInBackground(new GetCallback<ParseObject>() {
            public void done(ParseObject result, ParseException e) {
                if (e == null) {
                    cardNumber = result.getString("cardNumber");
                    cvv = result.getString("cvv");
                    expiryDate = String.valueOf(result.getString("expiryDate"));
                    PIN = result.getString("PIN");
                    cardID = result.getObjectId();
                    isFrozen = result.getBoolean("Frozen");
                    setFreezeChecked();
                }
            }
        });
    }

    public void homeButtonOnClick(View v){
        Intent intent = new Intent(getApplicationContext(), HomeScreen.class);
        startActivity(intent);
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
//        Intent intent = new Intent(getApplicationContext(), createCard.class);
//        startActivity(intent);
    }

    public void gearsButtonOnClick(View v){
        Intent intent = new Intent(getApplicationContext(), SettingsActivity.class);
        startActivity(intent);
    }

    public void showCardDetailsOnClick(View v){
//        getCardDetails();
        Button cardDetailsButton = findViewById(R.id.cardDetails);
        cardDetailsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder builder = new AlertDialog.Builder(CardActivity.this);
                builder.setCancelable(true);
                builder.setTitle("These are your card details:");
                builder.setMessage("Card Number: " + cardNumber + "\n" +"CVV: " + cvv + "\n" +"Expiry Date: "+ expiryDate);
                builder.setPositiveButton("Copy Card Number to Clipboard", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        ClipboardManager clipboard = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
                        ClipData clip = ClipData.newPlainText("card number", cardNumber);
                        clipboard.setPrimaryClip(clip);
                    }
                });
                builder.show();
            }
        });
    }

    public void viewPINOnClick(View v){
        getCardDetails();
        Button viewPINButton = findViewById(R.id.viewPIN);
        viewPINButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(CardActivity.this);
                builder.setCancelable(true);
                builder.setTitle("Please enter your password");
                // Set up the input
                final EditText input = new EditText(CardActivity.this);
                // Specify the type of input expected; this, for example, sets the input as a password, and will mask the text
                input.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
                input.setGravity(Gravity.CENTER);
                builder.setView(input);
                builder.setPositiveButton("View PIN", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        userInputPassword = input.getText().toString();
                        try {
                            boolean checkPassword = databaseMethods.attemptLogin(username, userInputPassword);
                            AlertDialog.Builder builder = new AlertDialog.Builder(CardActivity.this);
                            builder.setCancelable(true);
                            if (checkPassword){
                                builder.setMessage("PIN: " + PIN);
                            }
                            else {
                                builder.setMessage("Incorrect password!");
                            }
                            AlertDialog dialog2 = builder.show();
                            TextView messageText = (TextView)dialog2.findViewById(android.R.id.message);
                            messageText.setGravity(Gravity.CENTER);
                            dialog2.show();
                        } catch (ParseException e) {
                            e.printStackTrace();
                        }
                    }
                });
                builder.show();
            }
        });
    }

    public void updatePINInDB(String newPIN){
//        getCardDetails();
        ParseQuery<ParseObject> query = ParseQuery.getQuery("Cards");
        // Retrieve the object by id
        query.getInBackground(cardID, new GetCallback<ParseObject>() {
            public void done(ParseObject entity, ParseException e) {
                if (e == null) {
                    // Update the fields we want to
                    entity.put("PIN", newPIN);
                    // All other fields will remain the same
                    entity.saveInBackground();
                }
            }
        });
    }

    public void removeCardOnClick(View v){
        getCardDetails();
        Button removeCardButton = findViewById(R.id.removeCard);
        removeCardButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(CardActivity.this);
                builder.setCancelable(true);
                builder.setTitle("Please enter your 4-digit PIN to remove the card");
                // Set up the input
                final EditText input = new EditText(CardActivity.this);
                // Specify the type of input expected; this, for example, sets the input as a password, and will mask the text
                input.setInputType(InputType.TYPE_CLASS_NUMBER | InputType.TYPE_NUMBER_VARIATION_PASSWORD);
                input.setFilters(new InputFilter[] { new InputFilter.LengthFilter(4) });
                input.setGravity(Gravity.CENTER);
                builder.setView(input);
                builder.setPositiveButton("Confirm", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        userInputPIN = input.getText().toString();
                        AlertDialog.Builder builder = new AlertDialog.Builder(CardActivity.this);
                        builder.setCancelable(true);
                        if (userInputPIN.equals(PIN)){
                            removeCardFromDB();
                            builder.setMessage("Card successfully removed!");
                            Intent intent = new Intent(getApplicationContext(), HomeScreen.class);
                            startActivity(intent);
                        }
                        else {
                            builder.setMessage("Incorrect PIN. Please try again.");
                        }
                        AlertDialog dialog5 = builder.show();
                        TextView messageText = (TextView)dialog5.findViewById(android.R.id.message);
                        messageText.setGravity(Gravity.CENTER);
                        dialog5.show();
                    }
                });
                builder.show();
            }
        });
    }

    public void changePINOnClick(View v){
        getCardDetails();
        Button changePINButton = findViewById(R.id.changePIN);
        changePINButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(CardActivity.this);
                builder.setCancelable(true);
                builder.setTitle("Please enter your current 4-digit PIN");
                // Set up the input
                final EditText input = new EditText(CardActivity.this);
                // Specify the type of input expected; this, for example, sets the input as a password, and will mask the text
                input.setInputType(InputType.TYPE_CLASS_NUMBER | InputType.TYPE_NUMBER_VARIATION_PASSWORD);
                input.setFilters(new InputFilter[] { new InputFilter.LengthFilter(4) });
                input.setGravity(Gravity.CENTER);
                builder.setView(input);
                builder.setPositiveButton("Next", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        userInputOldPIN = input.getText().toString();
                        AlertDialog.Builder builder = new AlertDialog.Builder(CardActivity.this);
                        builder.setCancelable(true);
                        if(userInputOldPIN.equals(PIN)){
                            builder.setTitle("Please choose a new 4-digit PIN");
                            // Set up the input
                            final EditText input1 = new EditText(CardActivity.this);
                            // Specify the type of input expected; this, for example, sets the input as a password, and will mask the text
                            input1.setInputType(InputType.TYPE_CLASS_NUMBER | InputType.TYPE_NUMBER_VARIATION_PASSWORD);
                            input1.setFilters(new InputFilter[] { new InputFilter.LengthFilter(4) });
                            input1.setGravity(Gravity.CENTER);
                            builder.setView(input1);
                            builder.setPositiveButton("Next", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    userInputNewPIN = input1.getText().toString();
                                    AlertDialog.Builder builder = new AlertDialog.Builder(CardActivity.this);
                                    builder.setCancelable(true);
                                    builder.setTitle("Please reenter your new 4-digit PIN");
                                    // Set up the input
                                    final EditText input2 = new EditText(CardActivity.this);
                                    // Specify the type of input expected; this, for example, sets the input as a password, and will mask the text
                                    input2.setInputType(InputType.TYPE_CLASS_NUMBER | InputType.TYPE_NUMBER_VARIATION_PASSWORD);
                                    input2.setFilters(new InputFilter[] { new InputFilter.LengthFilter(4) });
                                    input2.setGravity(Gravity.CENTER);
                                    builder.setView(input2);
                                    builder.setPositiveButton("Done", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            userInputNewPINReenter = input2.getText().toString();
                                            AlertDialog.Builder builder = new AlertDialog.Builder(CardActivity.this);
                                            builder.setCancelable(true);
                                            if (userInputNewPIN.equals(userInputNewPINReenter)){
                                                updatePINInDB(userInputNewPIN);
                                                builder.setMessage("PIN has been successfully changed!");
                                            }
                                            else {
                                                builder.setMessage("Your new PINs don't match! Please try again.");
                                            }
                                            AlertDialog dialog4 = builder.show();
                                            TextView messageText = (TextView)dialog4.findViewById(android.R.id.message);
                                            messageText.setGravity(Gravity.CENTER);
                                            dialog4.show();
                                        }
                                    });
                                    builder.show();
                                }
                            });
                            builder.show();
                        }
                        else {
                            builder.setMessage("Incorrect PIN");
                            AlertDialog dialog3 = builder.show();
                            TextView messageText = (TextView)dialog3.findViewById(android.R.id.message);
                            messageText.setGravity(Gravity.CENTER);
                            dialog3.show();
                        }
                    }
                });
                builder.show();
            }
        });
    }
}