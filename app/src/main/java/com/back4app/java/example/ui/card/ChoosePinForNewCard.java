package com.back4app.java.example.ui.card;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

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

import java.security.SecureRandom;
import java.time.LocalDate;
import java.util.Random;


public class ChoosePinForNewCard extends AppCompatActivity {
    private String PIN;
    private String PINRE;
    static final private String DIGITS = "0123456789";
    final private Random random = new SecureRandom();
    private final String userObjectId = databaseMethods.getCurrentUser().getObjectId();
    String accountID;
    String cardID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_choose_pin_for_new_card);
        getAccountID();
        Button button = (Button) findViewById(R.id.button2);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EditText pin = (EditText) findViewById(R.id.editTextNumberPassword);
                EditText pin2 = (EditText) findViewById(R.id.editTextNumberPassword2);
                PIN = pin.getText().toString();
                PINRE = pin2.getText().toString();
                AlertDialog.Builder builder = new AlertDialog.Builder(ChoosePinForNewCard.this);
                if(!PIN.equals(PINRE)){
                    builder.setMessage("The entered PINs don't match. Please try again!");
                }
                else if (!(PIN.length() ==4)){
                    builder.setMessage("Your PIN must have exactly 4 digits!");
                }
                else {
                    enterCardDetailsIntoDB();
                    builder.setMessage("Your card has been created!");
                    getCardObID();
                    Intent intent = new Intent(getApplicationContext(), CardActivity.class);
                    startActivity(intent);
                }
                builder.show();
            }
        });
    }

    public char randomDigit(){
        return DIGITS.charAt(random.nextInt(DIGITS.length()));
    }

    public String randomCvv(){
        StringBuilder cvv = new StringBuilder();
        int length = 3;
        int spacing = 0;
        int spacer = 0;
        while(length > 0){
            if(spacer == spacing){
                spacer = 0;
            }
            length--;
            spacer++;
            cvv.append(randomDigit());
        }
        return cvv.toString();
    }

    public String randomCardNumber(){
        StringBuilder cardNum = new StringBuilder();
        int length = 16;
        int spacing = 4;
        char spacerChar = ' ';
        int spacer = 0;
        while(length > 0){
            if(spacer == spacing){
                cardNum.append(spacerChar);
                spacer = 0;
            }
            length--;
            spacer++;
            cardNum.append(randomDigit());
        }
        return cardNum.toString();
    }

    public void getCardObID(){
        ParseQuery<ParseObject> query = ParseQuery.getQuery("Cards");
        query.whereEqualTo("accountID", accountID);
        query.getFirstInBackground(new GetCallback<ParseObject>() {
            public void done(ParseObject result, ParseException e) {
                if (e == null) {
                    cardID = result.getObjectId();
                    saveCardIDInAccounts();
                }
            }
        });
    }

    public void enterCardDetailsIntoDB(){
        ParseObject entity = new ParseObject("Cards");
        entity.put("cardNumber", randomCardNumber());
        entity.put("cvv", randomCvv());
        entity.put("accountID", accountID);
        entity.put("expiryDate", LocalDate.now().getMonthValue() + "/" + LocalDate.now().plusYears(3).getYear());
        entity.put("PIN", PIN);
        entity.put("Frozen", false);
        entity.saveInBackground();
    }

    public void saveCardIDInAccounts(){
        ParseQuery<ParseObject> query = ParseQuery.getQuery("Accounts");
        query.whereEqualTo("accountOwner", userObjectId);
        query.whereEqualTo("accountType", "current");
        query.getFirstInBackground(new GetCallback<ParseObject>() {
            public void done(ParseObject entity, ParseException e) {
                if (e == null) {
                    entity.put("card", cardID);
                    entity.saveInBackground();
                }
            }
        });
    }

    public void getAccountID(){
        ParseQuery<ParseObject> query = ParseQuery.getQuery("Accounts");
        query.whereEqualTo("accountOwner", userObjectId);
        query.whereEqualTo("accountType", "current");
        query.getFirstInBackground(new GetCallback<ParseObject>() {
            public void done(ParseObject account, ParseException e) {
                if (e == null) {
                    accountID = account.getObjectId();
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
//        Intent intent = new Intent(getApplicationContext(), CardActivity.class);
//        startActivity(intent);
    }

    public void gearsButtonOnClick(View v){
        Intent intent = new Intent(getApplicationContext(), SettingsActivity.class);
        startActivity(intent);
    }
}