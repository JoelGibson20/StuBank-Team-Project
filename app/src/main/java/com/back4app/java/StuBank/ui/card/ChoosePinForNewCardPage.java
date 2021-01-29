package com.back4app.java.StuBank.ui.card;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.back4app.java.StuBank.ui.HomeScreen;
import com.back4app.java.StuBank.R;
import com.back4app.java.StuBank.ui.databaseMethods;
import com.back4app.java.StuBank.ui.graph.GraphActivity;
import com.back4app.java.StuBank.ui.pound.PoundActivity;
import com.back4app.java.StuBank.ui.settings.SettingsActivity;
import com.parse.GetCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;

import java.security.SecureRandom;
import java.time.LocalDate;
import java.util.Random;


public class ChoosePinForNewCardPage extends AppCompatActivity {
    private String PIN;
    private String PINREENTER;
    static final private String DIGITS = "0123456789";
    final private Random random = new SecureRandom();
    private final String userObjectId = databaseMethods.getCurrentUser().getObjectId();
    private String accountID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_choose_pin_for_new_card);
        getAccountID();
        randomDigit();

        //confirm to create new card on click
        Button button = (Button) findViewById(R.id.confirmNewCard);
        button.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.O)
            @Override
            public void onClick(View v) {
                EditText pin = (EditText) findViewById(R.id.PINReenter);
                EditText pin2 = (EditText) findViewById(R.id.PIN);
                PIN = pin.getText().toString();
                PINREENTER = pin2.getText().toString();
                AlertDialog.Builder builder = new AlertDialog.Builder(ChoosePinForNewCardPage.this);
                if(!PIN.equals(PINREENTER)){
                    builder.setMessage("The entered PINs don't match. Please try again!");
                }
                else if (!(PIN.length() ==4)){
                    builder.setMessage("Your PIN must have exactly 4 digits!");
                }
                else {
                    enterCardDetailsIntoDB();
                    builder.setMessage("Your card has been created!");
                    Handler delayHandler = new Handler();
                    delayHandler.postDelayed(new Runnable() {

                        @Override
                        public void run() {
                            Intent intent = new Intent(getApplicationContext(), CardPage.class);
                            startActivity(intent);
                        }
                    }, 1000L);
                }
                AlertDialog dialog4 = builder.show();
                TextView messageText = (TextView) dialog4.findViewById(android.R.id.message);
                assert messageText != null;
                messageText.setGravity(Gravity.CENTER);
                dialog4.show();
            }
        });
    }

    //generates random digit
    public char randomDigit(){
        return DIGITS.charAt(random.nextInt(DIGITS.length()));
    }

    //generates random three digit number to use as cvv
    public String randomCvv(){
        StringBuilder cvv = new StringBuilder();
        int length = 3;
        while(length > 0){
            length--;
            cvv.append(randomDigit());
        }
        return cvv.toString();
    }

    //generates random 16 digit number to use as card number
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

    //enters generated card information and users details into DB
    @RequiresApi(api = Build.VERSION_CODES.O)
    public void enterCardDetailsIntoDB(){
        ParseObject entity = new ParseObject("Cards");
        entity.put("cardNumber", randomCardNumber());
        entity.put("cvv", randomCvv());
        entity.put("cardOwner", userObjectId);
        entity.put("expiryDate", LocalDate.now().getMonthValue() + "/" + LocalDate.now().plusYears(3).getYear());
        entity.put("PIN", PIN);
        entity.put("Frozen", false);
        entity.put("accountID", accountID);
        entity.saveInBackground();
    }

    //returns account ID
    public void getAccountID(){
        ParseQuery<ParseObject> query = ParseQuery.getQuery("Accounts");
        query.whereEqualTo("accountOwner", userObjectId);
        query.whereEqualTo("accountType", "currentAccount");
        query.getFirstInBackground(new GetCallback<ParseObject>() {
            public void done(ParseObject account, ParseException e) {
                if (e == null) {
                    accountID = account.getObjectId();
                }
                else {
                    e.printStackTrace();
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

}