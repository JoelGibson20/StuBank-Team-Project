package com.back4app.java.example.ui.card;

import android.annotation.SuppressLint;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.text.InputFilter;
import android.text.InputType;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.ToggleButton;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.res.ResourcesCompat;

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
        getCardDetails();
        getUsername();


        //show card details on click
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


        //view PIN on click, asks user for their account password
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
                            assert messageText != null;
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

        //remove card on click, asks user for their card PIN
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
                            Handler mHandler = new Handler();
                            mHandler.postDelayed(new Runnable() {

                                @Override
                                public void run() {
                                    Intent intent = new Intent(getApplicationContext(), HomeScreen.class);
                                    startActivity(intent);
                                }

                            }, 1000L);
                        }
                        else {
                            builder.setMessage("Incorrect PIN. Please try again.");
                        }
                        AlertDialog dialog5 = builder.show();
                        TextView messageText = (TextView)dialog5.findViewById(android.R.id.message);
                        assert messageText != null;
                        messageText.setGravity(Gravity.CENTER);
                        dialog5.show();
                    }
                });
                builder.show();
            }
        });


        //change PIN on click, asks user for current PIN
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
                        AlertDialog.Builder builder1 = new AlertDialog.Builder(CardActivity.this);
                        builder1.setCancelable(true);
                        if(userInputOldPIN.equals(PIN)){
                            builder1.setTitle("Please choose a new 4-digit PIN");
                            // Set up the input
                            final EditText input1 = new EditText(CardActivity.this);
                            // Specify the type of input expected; this, for example, sets the input as a password, and will mask the text
                            input1.setInputType(InputType.TYPE_CLASS_NUMBER | InputType.TYPE_NUMBER_VARIATION_PASSWORD);
                            input1.setFilters(new InputFilter[] { new InputFilter.LengthFilter(4) });
                            input1.setGravity(Gravity.CENTER);
                            builder1.setView(input1);
                            builder1.setPositiveButton("Next", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    userInputNewPIN = input1.getText().toString();
                                    AlertDialog.Builder builder = new AlertDialog.Builder(CardActivity.this);
                                    builder.setCancelable(true);
                                    if (userInputNewPIN.length() == 4) {
                                        builder.setTitle("Please re-enter your new 4-digit PIN");
                                        // Set up the input
                                        final EditText input2 = new EditText(CardActivity.this);
                                        // Specify the type of input expected; this, for example, sets the input as a password, and will mask the text
                                        input2.setInputType(InputType.TYPE_CLASS_NUMBER | InputType.TYPE_NUMBER_VARIATION_PASSWORD);
                                        input2.setFilters(new InputFilter[]{new InputFilter.LengthFilter(4)});
                                        input2.setGravity(Gravity.CENTER);
                                        builder.setView(input2);
                                        builder.setPositiveButton("Done", new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface dialog, int which) {
                                                userInputNewPINReenter = input2.getText().toString();
                                                AlertDialog.Builder builder = new AlertDialog.Builder(CardActivity.this);
                                                builder.setCancelable(true);
                                                if (userInputNewPIN.equals(userInputNewPINReenter)) {
                                                    updatePINInDB(userInputNewPIN);
                                                    PIN = userInputNewPIN;
                                                    builder.setMessage("PIN has been successfully changed!");
                                                } else {
                                                    builder.setMessage("Your new PINs don't match! Please try again.");
                                                }
                                                AlertDialog dialog4 = builder.show();
                                                TextView messageText = (TextView) dialog4.findViewById(android.R.id.message);
                                                assert messageText != null;
                                                messageText.setGravity(Gravity.CENTER);
                                                dialog4.show();
                                            }
                                        });
                                        builder.show();
                                    }
                                    else {
                                        builder.setMessage("Your new PIN needs to have 4 digits!");
                                        AlertDialog dialog4 = builder.show();
                                        TextView messageText = (TextView) dialog4.findViewById(android.R.id.message);
                                        assert messageText != null;
                                        messageText.setGravity(Gravity.CENTER);
                                        dialog4.show();
                                    }
                                }
                            });
                            builder1.show();
                        }
                        else {
                            builder1.setMessage("Incorrect PIN");
                            AlertDialog dialog3 = builder1.show();
                            TextView messageText = (TextView)dialog3.findViewById(android.R.id.message);
                            assert messageText != null;
                            messageText.setGravity(Gravity.CENTER);
                            dialog3.show();
                        }
                    }
                });
                builder.show();
            }
        });
    }

    //gets users username; used in view PIN to check if password is correct
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

    //removes card from database
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

    //removes accountID from card when freezing - technically should stop any transactions being made
    public void disconnectCardFromAccountOnFreeze(){
        ParseQuery<ParseObject> query1 = ParseQuery.getQuery("Cards");
        query1.getInBackground(cardID, new GetCallback<ParseObject>() {
            public void done(ParseObject card, ParseException e) {
                if (e == null) {
                    card.remove("accountID");
                    card.saveInBackground();
                }
                else {
                    e.printStackTrace();
                }
            }
        });

    }

    //adds accountID to card when unfreezing
    public void connectCardToAccountOnUnfreeze(){
        ParseQuery<ParseObject> query = ParseQuery.getQuery("Cards");
        query.getInBackground(cardID, new GetCallback<ParseObject>() {
            public void done(ParseObject card, ParseException e) {
                if (e == null) {
                    card.put("accountID", accountID);
                    card.saveInBackground();
                }
                else{
                    e.printStackTrace();
                }
            }
        });
    }

    //updates DB if card is frozen/unfrozen by user
    public void updateIsFrozenInDB(boolean cardFreeze){
        ParseQuery<ParseObject> query = ParseQuery.getQuery("Cards");
        // Retrieve the object by id
        query.getInBackground(cardID, new GetCallback<ParseObject>() {
            public void done(ParseObject entity, ParseException e) {
                if (e == null) {
                    entity.put("Frozen", cardFreeze);
                    // All other fields will remain the same
                    entity.saveInBackground();
                }
            }
        });
    }

    //freeze/unfreeze card on click, enables/disables buttons, changes cards appearance when frozen
    @RequiresApi(api = Build.VERSION_CODES.M)
    public void freezeCardOnClick(View v) {
        ToggleButton toggleFreeze = (ToggleButton) findViewById(R.id.freezeCard);
        ImageView card = (ImageView) findViewById(R.id.card);
        Button cardDetailsButton = findViewById(R.id.cardDetails);
        Button cardDetailsButton2 = findViewById(R.id.changePIN);
        Button cardDetailsButton3 = findViewById(R.id.viewPIN);
        cardDetailsButton.setEnabled(!toggleFreeze.isChecked());
        cardDetailsButton2.setEnabled(!toggleFreeze.isChecked());
        cardDetailsButton3.setEnabled(!toggleFreeze.isChecked());
        updateIsFrozenInDB(toggleFreeze.isChecked());
        if (toggleFreeze.isChecked()){
            card.setForeground(ResourcesCompat.getDrawable(getResources(), R.drawable.frosdt, null));
            disconnectCardFromAccountOnFreeze();
        }
        else {
            card.setForeground(null);
            connectCardToAccountOnUnfreeze();
        }
    }

    //gets account details and displays them on the card
    public void getAccountDetails(){
        ParseQuery<ParseObject> query = ParseQuery.getQuery("Accounts");
        query.whereEqualTo("accountOwner", userObjectId);
        query.whereEqualTo("accountType", "currentAccount");
        query.getFirstInBackground(new GetCallback<ParseObject>() {
            @SuppressLint("SetTextI18n")
            public void done(ParseObject account, ParseException e) {
                if (e == null) {
                    accountNumber = account.getString("accountNumber");
                    sortCode = account.getString("sortCode");
                    accountID = account.getObjectId();
                    ((TextView)findViewById(R.id.accountNumber)).setText("Account Number:" + " " + accountNumber);
                    ((TextView)findViewById(R.id.sortCode)).setText("Sort Code:" + " " + sortCode);
                }
            }
        });
    }

    //sets toggle button isChecked depending on what is in the database
    @RequiresApi(api = Build.VERSION_CODES.M)
    public void setFreezeChecked(){
        ToggleButton toggleFreeze = (ToggleButton) findViewById(R.id.freezeCard);
        ImageView card = (ImageView) findViewById(R.id.card);
        toggleFreeze.setChecked(isFrozen);
        if (isFrozen) {
            card.setForeground(ResourcesCompat.getDrawable(getResources(), R.drawable.frosdt, null));
        }
        else {
            card.setForeground(null);
        }
        Button cardDetailsButton = findViewById(R.id.cardDetails);
        Button cardDetailsButton2 = findViewById(R.id.changePIN);
        Button cardDetailsButton3 = findViewById(R.id.viewPIN);
        cardDetailsButton.setEnabled(!toggleFreeze.isChecked());
        cardDetailsButton2.setEnabled(!toggleFreeze.isChecked());
        cardDetailsButton3.setEnabled(!toggleFreeze.isChecked());
    }

    //gets card details
    public void getCardDetails(){
        ParseQuery<ParseObject> query = ParseQuery.getQuery("Cards");
        // The query will search for a ParseObject, given its objectId.
        // When the query finishes running, it will invoke the GetCallback
        // with either the object, or the exception thrown
        query.whereEqualTo("cardOwner", userObjectId);
        query.getFirstInBackground(new GetCallback<ParseObject>() {
            @RequiresApi(api = Build.VERSION_CODES.M)
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

    public void gearsButtonOnClick(View v){
        Intent intent = new Intent(getApplicationContext(), SettingsActivity.class);
        startActivity(intent);
    }

    //updates PIN in DB after user changes it
    public void updatePINInDB(String newPIN){
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
}

