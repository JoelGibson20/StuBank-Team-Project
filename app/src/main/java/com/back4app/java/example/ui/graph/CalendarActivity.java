package com.back4app.java.example.ui.graph;

import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CalendarView;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.back4app.java.example.HomeScreen;
import com.back4app.java.example.R;
import com.back4app.java.example.ui.card.CardActivity;
import com.back4app.java.example.ui.pound.PoundActivity;
import com.back4app.java.example.ui.settings.SettingsActivity;

public class CalendarActivity extends GraphActivity {

    private static final String TAG = "CalendarActivity";
    private CalendarView calendarview;
    TextView myDate;

    String selectedAccount;
    String accountnumber;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_calendar);
        calendarview = (CalendarView) findViewById(R.id.calendarView);
        myDate  = (TextView) findViewById(R.id.myDate);

        Intent intent = getIntent();
        selectedAccount = intent.getStringExtra("selectedAccount");
        accountnumber = intent.getStringExtra("accountname");
        Log.d(TAG, "------------------------------------------------------------------");
        Log.d(TAG, "------------------------------------------------------------------");
        Log.d(TAG, selectedAccount);
        Log.d(TAG, "------------------------------------------------------------------");
        Log.d(TAG, "------------------------------------------------------------------");



        //changes the date depending on which date is clicked on the calendar.
        calendarview.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {
            @Override
            public void onSelectedDayChange(@NonNull CalendarView view, int year, int month, int dayOfMonth) {
                //String date = dayOfMonth + "/" + (month + 1) + "/" + year;
                //Log.d(TAG, "DateSelected: " + date);
                String month2 = getmonth(month + 1);
                String date = dayOfMonth + " " + (month2) + " " + year;

                myDate.setText(date);
            }
        });

        final ImageButton homeImageButton = findViewById(R.id.homeImageButton);
        final ImageButton graphImageButton = findViewById(R.id.graphImageButton);
        final ImageButton poundImageButton = findViewById(R.id.poundImageButton);
        final ImageButton cardImageButton = findViewById(R.id.cardImageButton);
        final ImageButton gearsImageButton = findViewById(R.id.gearsImageButton);
        final Button cancelbutton = findViewById(R.id.button_cancel);
        final Button okbutton = findViewById(R.id.button_ok);


    }

    public String getmonth(int month){
        String month2 = null;

        if (month == 1){
            month2 = ("Jan");
        }
        if (month == 2){
            month2 = ("Feb");
        }
        if (month == 3){
            month2 = ("Mar");
        }
        if (month == 4){
            month2 = ("Apr");
        }
        if (month == 5){
            month2 = ("May");
        }
        if (month == 6){
            month2 = ("Jun");
        }
        if (month == 7){
            month2 = ("Jul");
        }
        if (month == 8){
            month2 = ("Aug");
        }
        if (month == 9){
            month2 = ("Sep");
        }
        if (month == 10){
            month2 = ("Oct");
        }
        if (month == 11){
            month2 = ("Nov");
        }
        if (month == 12){
            month2 = ("Dec");
        }
        return month2;

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
        Intent intent = new Intent(getApplicationContext(), SettingsActivity.class);
        startActivity(intent);
    }

    public void cancelbuttonOnClick(View v){
        Intent intent = new Intent(getApplicationContext(), GraphActivity.class);
        startActivity(intent);
    }

    public void okbuttonOnClick(View V){
        Intent intent = new Intent(getApplicationContext(), DateTransferActivity.class);
        intent.putExtra("selectedAccount", selectedAccount);
        intent.putExtra("accountnumber", accountnumber);
        startActivity(intent);
    }

}
