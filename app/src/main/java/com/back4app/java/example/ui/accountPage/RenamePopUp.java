package com.back4app.java.example.ui.accountPage;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.DisplayMetrics;

import com.back4app.java.example.R;

public class RenamePopUp extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rename_pop_up);

        /*//Get the dimensions of the screen
        DisplayMetrics displayMetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        //Set window size to be 1/3 of the screen so it acts as a pop-up
        getWindow().setLayout(displayMetrics.widthPixels, (int) (displayMetrics.heightPixels * .3));
        System.out.println("POPUP WORKED");*/


    }
}