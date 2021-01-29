package com.back4app.java.example.ui.accountPage;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Build;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.back4app.java.example.R;
import com.back4app.java.example.ui.databaseMethods;
import com.parse.ParseException;
import com.parse.ParseObject;

import java.util.List;

/* App page for user's current account
Creator: Joel Gibson (B9020460)
 */
public class AccountPage extends AppCompatActivity {

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_account_page);
        //Load the TextViews for modification
        final EditText accountName = findViewById(R.id.accountName);
        final TextView accountNumber = findViewById(R.id.accountNumber);
        final TextView sortCode = findViewById(R.id.sortCode);
        final TextView balance = findViewById(R.id.balance);

        //Load buttons to set onclick listeners for them
        final Button lockButton = findViewById(R.id.lockButton);
        final Button shareDetailsButton = findViewById(R.id.shareDetailsButton);

        //Retrieve the account in question from the intent
        ParseObject accountParseObject = (ParseObject) getIntent().getExtras().get("accountParseObject");

        accountName.setText(accountParseObject.getString("accountName"));
        accountNumber.setText(String.format("Account Number: %s",accountParseObject.getString("accountNumber")));
        sortCode.setText(String.format("Sort Code: %s",accountParseObject.getString("sortCode")));
        balance.setText(accountParseObject.getString("balance"));

        //Call method to set the button text based on whether the account is locked or not
        setLockButtonText(accountParseObject,lockButton);

        try { //Create the cards for the transactions list
            createMyCardView(databaseMethods.getTransactionsForAccount(accountParseObject.getString("sortCode") + " " + accountParseObject.getString("accountNumber")));
        } catch (ParseException e) {
            e.printStackTrace();
        }

        lockButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    getIntent().putExtra("accountParseObject",databaseMethods.toggleAccountLock((ParseObject) getIntent().getExtras().get("accountParseObject")));
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                /*Toggles the locking of the account, changes the extra in intent (where all the
                account details are stored) to match this change */
                setLockButtonText((ParseObject) getIntent().getExtras().get("accountParseObject"),lockButton);
                //Change lock button text
            }
        });

        shareDetailsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ParseObject accountParseObject = (ParseObject) getIntent().getExtras().get("accountParseObject");

                //Use Android Sharesheet to bring up the Android share menu
                Intent sendIntent = new Intent(Intent.ACTION_SEND);
                //Below is the text which will be sent
                sendIntent.putExtra(Intent.EXTRA_TEXT, String.format("Here's my StuBank account details - \n" +
                        "Sort code: %s\nAccount number: %s",accountParseObject.getString("sortCode"),
                        accountParseObject.getString("accountNumber")));
                sendIntent.setType("text/plain");
                Intent shareIntent = Intent.createChooser(sendIntent, null);
                startActivity(shareIntent);
            }
        });

    }
    public void changeAccountName(){
        final EditText accountName = findViewById(R.id.accountName);
        ParseObject accountParseObject = (ParseObject) getIntent().getExtras().get("accountParseObject");
        try {
            databaseMethods.changeAccountName(accountParseObject, accountName.getText().toString());
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }

    public void setLockButtonText(ParseObject accountParseObject, Button lockButton){
        ImageView locked = findViewById(R.id.lockedImage);
        ImageView unlocked = findViewById(R.id.unlockedImage);
        //Correct text set for lock button based on whether or not the account is locked
        if(accountParseObject.getBoolean("locked")) {
            unlocked.setVisibility(View.INVISIBLE);
            locked.setVisibility(View.VISIBLE);
            lockButton.setText("Unlock");
        }
        else{
            lockButton.setText("Lock");
            locked.setVisibility(View.INVISIBLE);
            unlocked.setVisibility(View.VISIBLE);
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    public void createMyCardView(List<ParseObject> transactionsList){
        LinearLayout scrollViewLinearLayout = findViewById(R.id.accountTransactionsLayout);
        scrollViewLinearLayout.removeAllViews();
        //Wipes any content in the linearlayout (allows refreshing)
        //Layout params which will be applied to the cardView
        LinearLayout.LayoutParams layoutparams = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
        );
        ParseObject accountParseObject = (ParseObject) getIntent().getExtras().get("accountParseObject");

        for(int i = 0; i < transactionsList.size(); i++){
            // Initialise the CardView
            CardView cardview = new CardView(getApplicationContext());

            //Set params for CardView
            cardview.setUseCompatPadding(true);
            cardview.setLayoutParams(layoutparams);
            cardview.setPreventCornerOverlap(true);
            cardview.setPadding(0,0,0,80);
            cardview.setCardBackgroundColor(Color.parseColor("#FF018786"));
            cardview.setRadius(50);
            cardview.setMaxCardElevation(20);
            cardview.setPadding(0,100,0,20);
            cardview.setClipToPadding(true);

            //Create a TextView for the transaction date
            TextView dateText = new TextView(getApplicationContext());
            dateText.setPadding(15,120,0,15);
            dateText.setGravity(Gravity.START);
            dateText.setLayoutParams(layoutparams);
            String dateString = transactionsList.get(i).getDate("transactionDate").toString();
            //Get date the transaction was made
            String[] dateStringArray = dateString.split(" ");
            //Split so can extract just the date and time (remove unnecessary detail like day)
            dateString = dateStringArray[1] + " " + dateStringArray[2] + " " + dateStringArray[5] + " " + dateStringArray[3];
            //Extract date, time, and year only
            dateText.setText(dateString);
            dateText.setTextAppearance(android.R.style.TextAppearance_Material_Headline);
            dateText.setTextColor(Color.WHITE);
            dateText.setTextSize(13);

            //Create a TextView for the transaction reference
            TextView referenceText = new TextView(getApplicationContext());
            referenceText.setPadding(15,15,0,0);
            referenceText.setGravity(Gravity.START);
            referenceText.setLayoutParams(layoutparams);
            referenceText.setText(transactionsList.get(i).getString("reference"));
            referenceText.setTextAppearance(android.R.style.TextAppearance_Material_Headline);
            referenceText.setTextColor(Color.WHITE);
            referenceText.setTextSize(20);
            referenceText.setTypeface(dateText.getTypeface(), Typeface.BOLD);


            //Create a TextView for the transaction value
            TextView valueText = new TextView(getApplicationContext());
            valueText.setPadding(0,60,80,0);
            valueText.setGravity(Gravity.END);
            valueText.setLayoutParams(layoutparams);
            //Set text here without + or - in case the if conditions somehow fail
            valueText.setText(transactionsList.get(i).getString("value"));

            if(transactionsList.get(i).getString("outgoingAccount").equals(accountParseObject.getString("sortCode") + " " + accountParseObject.getString("accountNumber"))){
                //If the user's account is the outgoing account for the transaction money is being taken out
                valueText.setText("-" + transactionsList.get(i).getString("value"));
            }
            else if (transactionsList.get(i).getString("ingoingAccount").equals(accountParseObject.getString("sortCode") + " " + accountParseObject.getString("accountNumber"))){
                //If the user's account is the ingoing account for the transaction money is being put into the account
                valueText.setText("+" + transactionsList.get(i).getString("value"));
            }

            valueText.setTextAppearance(android.R.style.TextAppearance_Material_Headline);
            valueText.setTextColor(Color.WHITE);
            valueText.setTextSize(20);
            valueText.setTypeface(dateText.getTypeface(), Typeface.BOLD);

            //Add the TextViews to the CardView
            cardview.addView(dateText);
            cardview.addView(referenceText);
            cardview.addView(valueText);


            //Add the CardView to the linear layout in the ScrollView
            scrollViewLinearLayout.addView(cardview);}

        }
}

