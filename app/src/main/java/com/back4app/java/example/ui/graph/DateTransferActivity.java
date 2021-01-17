package com.back4app.java.example.ui.graph;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.back4app.java.example.R;

public class DateTransferActivity extends CalendarActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_datetransfer);


        final Button backbutton = findViewById(R.id.backbutton);



    }

    public void backbuttonOnClick(View v) {
        Intent intent = new Intent(getApplicationContext(), CalendarActivity.class);
        startActivity(intent);


    }}
