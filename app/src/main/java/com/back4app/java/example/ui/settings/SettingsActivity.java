package com.back4app.java.example.ui.settings;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.Toast;

import com.back4app.java.example.ui.HomeScreen;
import com.back4app.java.example.R;
import com.back4app.java.example.ui.CreditsPage;
import com.back4app.java.example.ui.card.CardPage;
import com.back4app.java.example.ui.card.CreateCardPage;
import com.back4app.java.example.ui.databaseMethods;
import com.back4app.java.example.ui.graph.GraphActivity;
import com.back4app.java.example.ui.login.LoginActivity;
import com.back4app.java.example.ui.pound.PoundActivity;

import com.parse.ParseObject;
import com.parse.ParseUser;

import java.util.List;

//the settings tab
public class SettingsActivity extends AppCompatActivity implements passwordDialog.passwordDialogListener, CloseAccountDialog.CloseAccountDialogListener {

    private static final String TAG = "SettingsActivity";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        //gets the buttons from the page.
        final ImageButton homeImageButton = findViewById(R.id.homeImageButton);
        final ImageButton graphImageButton = findViewById(R.id.graphImageButton);
        final ImageButton poundImageButton = findViewById(R.id.poundImageButton);
        final ImageButton cardImageButton = findViewById(R.id.cardImageButton);
        final ImageButton gearsImageButton = findViewById(R.id.gearsImageButton);
        final Button passwordbutton = findViewById(R.id.passwordbutton);
        final Button creditsButton = findViewById(R.id.button_credits);


        Button closeAccountButton = findViewById(R.id.closeAccountButton);
        closeAccountButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //allows for account closing if all are empty
                if (checkEmptyBalance()){
                    openDialogCloseAccount();
                }
                //prevents account being closed if there are any that contain money
                else {
                    Toast.makeText(getApplicationContext(), "Please empty all accounts and vaults before closing account", Toast.LENGTH_LONG).show();
                }
            }
        });

        // logs the user out
        Button logoutButton = findViewById(R.id.logoutbutton);
        logoutButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                logOutUser();
            }
        });
    }

    public void openDialogCloseAccount(){
        CloseAccountDialog closeAccountDialog = new CloseAccountDialog();
        closeAccountDialog.show(getSupportFragmentManager(), "close account dialog");
    }

    public void deleteAllAccounts(){
        //removes current account and all vaults
        for(ParseObject account : databaseMethods.getAllAccountsOfOneUser()){
            account.deleteInBackground();
        }
        //removes the users log in details from database
        ParseObject currentUser = databaseMethods.getCurrentUser();
        currentUser.deleteInBackground();
    }

    //logs user out of session and redirects them to sign in page
    private void logOutUser(){
        ParseUser.logOut();
        Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
        startActivity(intent);
    }

    // checks that all of the current users account are empty, returns result of this check
    private boolean checkEmptyBalance(){
        boolean valid = true;
        List<ParseObject> accounts = databaseMethods.getAllAccountsOfOneUser();
        for (ParseObject account:accounts) {
            String balance = account.getString("balance");
            if (balance.equals("£0") || balance.equals("£0.0") || balance.equals("£0.00")){
                continue;
            }
            else{
                valid = false;
                break;
            }
        }
        return valid;
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
        Intent intent;
        if (databaseMethods.hasCard){
            intent = new Intent(getApplicationContext(), CardPage.class);
        }
        else {
            intent = new Intent(getApplicationContext(), CreateCardPage.class);
        }
        startActivity(intent);
    }
    public void gearsButtonOnClick(View v){
/*        Intent intent = new Intent(getApplicationContext(), SettingsActivity.class);
        startActivity(intent);*/
    }

    public void creditsButtonOnClick(View v){
        Intent intent = new Intent(getApplicationContext(), CreditsPage.class);
        startActivity(intent);
    }

    public void passwordButtonOnClick(View v){
        openDialog();

    }

    public void openDialog(){
        passwordDialog PasswordDialog = new passwordDialog();
        PasswordDialog.show(getSupportFragmentManager(), "password dialog");
    }
    //gets the password that is inputted to change.

    @Override
    public void applyTexts(String password1, String password2) {
        //database methods function is called and the password is passed.
        databaseMethods.changePassword(password1);
    }

    @Override
    public void onYesClicked() {


        Log.d(TAG, "Deleted");
        deleteAllAccounts();
        logOutUser();

    }

}