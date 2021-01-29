package com.back4app.java.example.ui.pound;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.back4app.java.example.R;
import com.back4app.java.example.ui.HomeScreen;
import com.back4app.java.example.ui.pound.PoundActivity;

public class TransferComplete extends AppCompatActivity {
    //Allows user to be redirected to desired page
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_transfer_complete);
        Button anotherTransfer = (Button) findViewById(R.id.anotherTransfer);
        anotherTransfer.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), PoundActivity.class);
                startActivity(intent);
            }});
                Button returnHome = (Button) findViewById(R.id.returnHome);
                returnHome.setOnClickListener(new View.OnClickListener() {
                    public void onClick(View view) {
                        Intent intent = new Intent(getApplicationContext(), HomeScreen.class);
                        startActivity(intent);

}});}}