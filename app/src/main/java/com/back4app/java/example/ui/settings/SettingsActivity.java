package com.back4app.java.example.ui.settings;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;

import com.back4app.java.example.HomeScreen;
import com.back4app.java.example.R;
import com.back4app.java.example.ui.CreditsPage;
import com.back4app.java.example.ui.card.CardActivity;
import com.back4app.java.example.ui.databaseMethods;
import com.back4app.java.example.ui.graph.GraphActivity;
import com.back4app.java.example.ui.pound.PoundActivity;

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
        final Button closeAccountButton = findViewById(R.id.closeAccountButton);





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

    public void closeAccountButtonOnClick(View v){
        Intent intent = new Intent(getApplicationContext(), CloseAccountActivity.class);
        startActivity(intent);
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