package com.back4app.java.example;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.back4app.java.example.ui.accountPage.AccountPage;
import com.back4app.java.example.ui.card.CardActivity;
import com.back4app.java.example.ui.graph.GraphActivity;
import com.back4app.java.example.ui.pound.PoundActivity;
import com.back4app.java.example.ui.settings.SettingsActivity;
import com.back4app.java.example.ui.vaultPage.VaultPage;
import com.parse.ParseException;
import com.parse.ParseObject;

import java.util.ArrayList;
import java.util.List;
import com.back4app.java.example.ui.databaseMethods;

public class HomeScreen extends AppCompatActivity {
    @RequiresApi(api = Build.VERSION_CODES.Q)
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
        final Button newVaultButton = findViewById(R.id.newVaultButton);

        ParseObject userDetails = null;
        //Gets the user's name to greet them by name
        greeting.setText(String.format("Welcome %s", databaseMethods.getCurrentUser().getString("firstName")));


        //Create cards for the user's accounts
        List<ParseObject> accountsList = new ArrayList<ParseObject>();
        try {
            accountsList = databaseMethods.getAccounts();
        } catch (ParseException e) {
            e.printStackTrace();
        }
        createMyCardView(accountsList);

    }


    @RequiresApi(api = Build.VERSION_CODES.Q)
    public void homeButtonOnClick(View v){
        //Disabled for home screen as unnecessary when already on home screen
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
    public void accountClicked(ParseObject accountParseObject){
        //If the account clicked is a current account redirect to current account page
        if(accountParseObject.getString("accountType").equals("currentAccount")){
            Intent intent = new Intent(getApplicationContext(), AccountPage.class);
            intent.putExtra("accountParseObject", accountParseObject);
            startActivity(intent);
        }
        //If account clicked is a vault redirect to vault page
        else if(accountParseObject.getString("accountType").equals("vault")){
            Intent intent = new Intent(getApplicationContext(), VaultPage.class);
            intent.putExtra("accountParseObject", accountParseObject);
            startActivity(intent);
        }


    }
    public void newVaultButtonOnClick(View v){
        //Load progress bar (loading circle)
        final ProgressBar loading = findViewById(R.id.vaultsLoading);
        //Inflate the layout of the popup window
        LayoutInflater inflater = (LayoutInflater)
                getSystemService(LAYOUT_INFLATER_SERVICE);
        View popupView = inflater.inflate(R.layout.activity_popup, null);

        //Set the properties for the popup window
        int width = LinearLayout.LayoutParams.MATCH_PARENT;
        int height = LinearLayout.LayoutParams.WRAP_CONTENT;
        boolean focusable = true; // lets taps outside the popup also dismiss it
        final PopupWindow popupWindow = new PopupWindow(popupView, width, height, focusable);

        //Show the popup window
        popupWindow.showAtLocation(findViewById(R.id.greeting), Gravity.CENTER, 0, 0);

        //Load the buttons on the popup window
        Button backButton = popupView.findViewById(R.id.backButton);
        Button createButton = popupView.findViewById(R.id.createButton);
        EditText accountNameInput = popupView.findViewById(R.id.accountNameInput);

        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                popupWindow.dismiss();
                //If the back button is clicked close the popup
            }
        });

        createButton.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.Q)
            @Override
            public void onClick(View v) {
                //Creates vault using the name provided
                try {
                    databaseMethods.retrieveAccountsBeforeCreation("vault", accountNameInput.getText().toString());
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                popupWindow.dismiss(); //Close popup window after account creation
                loading.setVisibility(View.VISIBLE); //Show loading circle

                /*This forces the program to wait 1 seconds (1000ms) before refreshing the page
                as it would refresh too quick prior, and the new vault object wouldn't be retrieved
                and displayed */
                Handler handler = new Handler();
                handler.postDelayed(new Runnable() {
                    public void run() {
                        onRestart();
                    }
                }, 1000);

            }
        });

    }

    @RequiresApi(api = Build.VERSION_CODES.Q)
    public void createMyCardView(List<ParseObject> accountsList){
        CardView currentAccount = findViewById(R.id.currentAccount);
        TextView currentAccountNumber = findViewById(R.id.currentAccountNumber);
        TextView currentAccountSortCode = findViewById(R.id.currentAccountSortCode);
        TextView currentAccountBalance = findViewById(R.id.currentAccountBalance);


        LinearLayout scrollViewLinearLayout = findViewById(R.id.scrollViewLinearLayout);
        scrollViewLinearLayout.removeAllViews();
        //Wipes any content in the linearlayout (allows refreshing)
        //Layout params which will be applied to the cardView
        LinearLayout.LayoutParams layoutparams = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
        );

        for(int i = 0; i < accountsList.size(); i++){
            if(accountsList.get(i).getString("accountType").equals("currentAccount")){
                currentAccountNumber.setText(accountsList.get(i).getString("accountNumber"));
                currentAccountSortCode.setText(accountsList.get(i).getString("sortCode"));
                currentAccountBalance.setText(accountsList.get(i).getString("balance"));

                int finalI1 = i;
                currentAccount.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        accountClicked(accountsList.get(finalI1));
                    }
                });
            }
            else{ // Initialise the CardView
                CardView cardview = new CardView(getApplicationContext());

                //Set params for CardView
                cardview.setUseCompatPadding(true);
                cardview.setLayoutParams(layoutparams);
                cardview.setPreventCornerOverlap(true);
                cardview.setPadding(0,0,0,80);
                cardview.setCardBackgroundColor(Color.parseColor("#FF03DAC5"));
                cardview.setRadius(50);
                cardview.setMaxCardElevation(20);
                cardview.setPadding(0,100,0,20);
                cardview.setClipToPadding(true);

            /*Add onClickListener so when the account is clicked, the account ParseObject can be
            passed to the account page to */
                int finalI = i;
                cardview.setOnClickListener(new View.OnClickListener() {
                    public void onClick(View v) {
                        accountClicked(accountsList.get(finalI));
                    }
                });


                //Create a TextView for the account name
                TextView accountNameText = new TextView(getApplicationContext());
                accountNameText.setPadding(15,30,0,30);
                accountNameText.setGravity(Gravity.START);
                accountNameText.setLayoutParams(layoutparams);
                accountNameText.setText(accountsList.get(i).getString("accountName"));
                accountNameText.setTextAppearance(android.R.style.TextAppearance_Material_Headline);
                accountNameText.setTextColor(Color.WHITE);
                accountNameText.setTextSize(16);
                accountNameText.setTypeface(accountNameText.getTypeface(), Typeface.BOLD);

                //Create a TextView for the account balance
                TextView balanceText = new TextView(getApplicationContext());
                balanceText.setPadding(0,30,15,30);
                balanceText.setGravity(Gravity.END);
                balanceText.setLayoutParams(layoutparams);
                balanceText.setText(accountsList.get(i).getString("balance"));
                balanceText.setTextAppearance(android.R.style.TextAppearance_Material_Headline);
                balanceText.setTextColor(Color.WHITE);
                balanceText.setTextSize(16);
                balanceText.setTypeface(accountNameText.getTypeface(), Typeface.BOLD);

                //Add the TextViews to the CardView
                cardview.addView(accountNameText);
                cardview.addView(balanceText);


                //Add the CardView to the linear layout in the ScrollView
                scrollViewLinearLayout.addView(cardview);}

        }
    }
    @RequiresApi(api = Build.VERSION_CODES.Q)
    @Override
    protected void onRestart() {
        super.onRestart();
        //Load progress bar
        final ProgressBar loading = findViewById(R.id.vaultsLoading);
        loading.setVisibility(View.INVISIBLE);
        //Hide the loading circle again
        //Create cards for the user's accounts
        List<ParseObject> accountsList = new ArrayList<ParseObject>();
        try {
            accountsList = databaseMethods.getAccounts();
        } catch (ParseException e) {
            e.printStackTrace();
        }
        createMyCardView(accountsList);

    }

}