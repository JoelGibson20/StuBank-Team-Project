package com.back4app.java.example.ui.card;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.InputType;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

import com.back4app.java.example.HomeScreen;
import com.back4app.java.example.R;
import com.back4app.java.example.ui.graph.GraphActivity;
import com.back4app.java.example.ui.pound.PoundActivity;

import com.back4app.java.example.ui.settings.SettingsActivity;
import com.back4app.java.example.ui.databaseMethods;
import com.parse.GetCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;


public class CardActivity extends AppCompatActivity {
    final String[] cardDetails = new String[4];
    String username = "";
    private String userInputPassword = "";
    private String userInputOldPIN = "";
    private String userInputNewPIN = "";
    private String userInputNewPINReenter = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_card);
        getCardDetails();
        getUsername();

        final ImageButton homeImageButton = findViewById(R.id.homeImageButton);
        final ImageButton graphImageButton = findViewById(R.id.graphImageButton);
        final ImageButton poundImageButton = findViewById(R.id.poundImageButton);
        final ImageButton cardImageButton = findViewById(R.id.cardImageButton);
        final ImageButton gearsImageButton = findViewById(R.id.gearsImageButton);

    }

    public void getUsername(){
        String userObjectId = databaseMethods.getCurrentUser().getObjectId();
        ParseQuery<ParseObject> query1 = ParseQuery.getQuery("_User");
        query1.getInBackground(userObjectId, new GetCallback<ParseObject>() {
            public void done(ParseObject result, ParseException e) {
                if (e == null) {
                    username = result.getString("username");
                }
            }
        });
    }

    public void getCardDetails(){
        ParseQuery<ParseObject> query = ParseQuery.getQuery("Cards");
        // The query will search for a ParseObject, given its objectId.
        // When the query finishes running, it will invoke the GetCallback
        // with either the object, or the exception thrown
        query.getInBackground("HXVXy0xBT3", new GetCallback<ParseObject>() {
            public void done(ParseObject result, ParseException e) {
                if (e == null) {
                    cardDetails[0] = result.getString("cardNumber");
                    cardDetails[1] = result.getString("cvv");
                    cardDetails[2] = String.valueOf(result.getString("expiryDate"));
                    cardDetails[3] = result.getString("PIN");
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
    public void addCardButtonOnClick(View v){
        Intent intent = new Intent(getApplicationContext(), createCard.class);
        startActivity(intent);
    }
    public void gearsButtonOnClick(View v){
        Intent intent = new Intent(getApplicationContext(), SettingsActivity.class);
        startActivity(intent);
    }
    public void showCardDetailsOnClick(View v){
        Button cardDetailsButton = findViewById(R.id.cardDetails);
        cardDetailsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder builder = new AlertDialog.Builder(CardActivity.this);
                builder.setCancelable(true);
                builder.setTitle("These are your card details:");
                builder.setMessage("Card Number: " + cardDetails[0] + "\n" +"CVV: " + cardDetails[1] + "\n" +"Expiry Date: "+ cardDetails[2]);
                builder.setPositiveButton("Copy Card Number to Clipboard", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        ClipboardManager clipboard = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
                        ClipData clip = ClipData.newPlainText("card number", cardDetails[0]);
                        clipboard.setPrimaryClip(clip);
                    }
                });
                builder.show();
            }
        });
    }
    public void viewPINOnClick(View v){
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
                                builder.setMessage("PIN: " + cardDetails[3]);
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
        ParseQuery<ParseObject> query = ParseQuery.getQuery("Cards");

        // Retrieve the object by id
        query.getInBackground("HXVXy0xBT3", new GetCallback<ParseObject>() {
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

    public void changePINOnClick(View v){
        Button changePINbutton = findViewById(R.id.changePIN);
        changePINbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(CardActivity.this);
                builder.setCancelable(true);
                builder.setTitle("Please enter your current PIN");
                // Set up the input
                final EditText input = new EditText(CardActivity.this);
                // Specify the type of input expected; this, for example, sets the input as a password, and will mask the text
                input.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
                builder.setView(input);
                builder.setPositiveButton("Next", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        userInputOldPIN = input.getText().toString();
                        AlertDialog.Builder builder = new AlertDialog.Builder(CardActivity.this);
                        builder.setCancelable(true);
                        if(userInputOldPIN.equals(cardDetails[3])){
                            builder.setTitle("Please choose a new 4-digit PIN");
                            // Set up the input
                            final EditText input1 = new EditText(CardActivity.this);
                            // Specify the type of input expected; this, for example, sets the input as a password, and will mask the text
                            input1.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD );
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
                                    input2.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD );
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