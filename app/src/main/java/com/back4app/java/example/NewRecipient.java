package com.back4app.java.example;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class NewRecipient extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_recipient);

        Button submit = (Button) findViewById(R.id.sendbutton);
        submit.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), TransferComplete.class);
                startActivity(intent);
                String outgoingEmail = ((EditText) findViewById(R.id.emailInput)).getText().toString();
                if (!outgoingEmail.equals("")) {

                }
            }
        });



    }



}