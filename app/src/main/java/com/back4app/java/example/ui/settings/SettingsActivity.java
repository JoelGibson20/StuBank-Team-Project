package com.back4app.java.example.ui.settings;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.Toast;

import com.back4app.java.example.HomeScreen;
import com.back4app.java.example.R;
import com.back4app.java.example.ui.CreditsPage;
import com.back4app.java.example.ui.card.CardActivity;
import com.back4app.java.example.ui.databaseMethods;
import com.back4app.java.example.ui.graph.GraphActivity;
import com.back4app.java.example.ui.login.LoginActivity;
import com.back4app.java.example.ui.pound.PoundActivity;

import com.back4app.java.example.ui.databaseMethods;
import com.parse.Parse;
import com.parse.ParseObject;
import com.parse.ParseUser;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class SettingsActivity extends AppCompatActivity implements passwordDialog.passwordDialogListener{


    private static final String TAG = "SettingsActivity";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

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
            public void onClick(View v) { //TODO need to add conformation alert before account is deleted
                if (checkEmptyBalance()){
                    deleteAccountAllAccounts();
                    logOutUser();
                }
                else {
                    Toast.makeText(getApplicationContext(), "Please empty all accounts and vaults before closing account", Toast.LENGTH_LONG);
                }

            }
        });

        Button logoutButton = findViewById(R.id.logoutbutton);
        logoutButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                logOutUser();

            }
        });

    }

    private void deleteAccountAllAccounts(){
        for(ParseObject account : databaseMethods.getAllAccountsOfOneUser()){
            account.deleteInBackground();
        }
        ParseObject currentUser = databaseMethods.getCurrentUser();
        currentUser.deleteInBackground();

    }

    private void logOutUser(){
        ParseUser.logOut();
        Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
        startActivity(intent);

    }


    private boolean checkEmptyBalance(){
        Boolean valid = true;
        ParseObject currentUser =  databaseMethods.getCurrentUser();
        List<ParseObject> accounts = databaseMethods.getAllAccountsOfOneUser();
        List<String> validForms = new ArrayList<>(Arrays.asList("£0","£0.0","£0.00","0","0.0","0.00"));
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
        Intent intent = new Intent(getApplicationContext(), CardActivity.class);
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

    @Override
    public void applyTexts(String password1, String password2) {
        Log.d(TAG, "----------------");
        Log.d(TAG, password1);
        Log.d(TAG, password2);

        databaseMethods.changePassword(password1);





    }
}