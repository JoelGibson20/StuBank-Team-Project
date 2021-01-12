package com.back4app.java.example.ui.graph;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import com.back4app.java.example.ui.graph.CalendarActivity;
import com.back4app.java.example.HomeScreen;
import com.back4app.java.example.R;
import com.back4app.java.example.ui.card.CardActivity;
import com.back4app.java.example.ui.pound.PoundActivity;
import com.back4app.java.example.ui.settings.SettingsActivity;

public class GraphActivity extends AppCompatActivity {

    //ImageButton calendarButton;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_graph);

        final ImageButton homeImageButton = findViewById(R.id.homeImageButton);
        final ImageButton graphImageButton = findViewById(R.id.graphImageButton);
        final ImageButton poundImageButton = findViewById(R.id.poundImageButton);
        final ImageButton cardImageButton = findViewById(R.id.cardImageButton);
        final ImageButton gearsImageButton = findViewById(R.id.gearsImageButton);
        final ImageButton calendarImageButton = findViewById(R.id.calendarImageButton);

        //calendarButton = findViewById(R.id.calendarImageButton);

        //calendarButton.setOnClickListener(v -> {
            //Intent intent = new Intent(GraphActivity.this, CalendarActivity.class);
            //startActivity(intent);
        //});




    }
    public void openCalendar(){
        Intent intent = new Intent(GraphActivity.this, CalendarActivity.class);
        startActivity(intent);
    }

    public void homeButtonOnClick(View v) {
        Intent intent = new Intent(getApplicationContext(), HomeScreen.class);
        startActivity(intent);
    }

    public void graphButtonOnClick(View v) {
/*        Intent intent = new Intent(getApplicationContext(), GraphActivity.class);
        startActivity(intent);*/
    }

    public void poundButtonOnClick(View v) {
        Intent intent = new Intent(getApplicationContext(), PoundActivity.class);
        startActivity(intent);
    }

    public void cardButtonOnClick(View v) {
        Intent intent = new Intent(getApplicationContext(), CardActivity.class);
        startActivity(intent);
    }

    public void gearsButtonOnClick(View v) {
        Intent intent = new Intent(getApplicationContext(), SettingsActivity.class);
        startActivity(intent);
    }

    public void calendarButtonOnClick(View v) {
        Intent intent = new Intent(getApplicationContext(), CalendarActivity.class);
        startActivity(intent);
    }
}