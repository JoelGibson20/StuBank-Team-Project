package com.back4app.java.example.ui.login;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import com.back4app.java.example.R;
import com.parse.GetCallback;
import com.parse.Parse;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import com.parse.SaveCallback;
import com.parse.SignUpCallback;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        TextView textView = findViewById(R.id.textView);
        TextView textView2 = findViewById(R.id.textView2);

        ParseUser user = new ParseUser();
        user.setUsername("yes@email.com");
        user.setPassword("Password1");
        user.setEmail("yes@email.com");

        // other fields can be set just like with ParseObject
        user.put("firstName", "Test");
        user.put("surname", "User");
        user.put("phone", "650-253-0000");

        user.signUpInBackground(new SignUpCallback() {
            public void done(ParseException e) {
                if (e == null) {
                    textView.setText(String.format("Welcome %s", user.getString("firstName") ));

                } else {
                    // Sign up didn't succeed. Look at the ParseException
                    // to figure out what went wrong
                    textView.setText(String.format("LOGIN FAILED: %s", e ));
                }
            }
        });





    }
}