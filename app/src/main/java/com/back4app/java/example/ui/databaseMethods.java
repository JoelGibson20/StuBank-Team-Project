package com.back4app.java.example.ui;

import com.parse.LogInCallback;
import com.parse.ParseException;
import com.parse.ParseUser;
import com.parse.SignUpCallback;

import java.sql.SQLOutput;

public class databaseMethods {

    public static void addToDatabase(String firstName, String surname, String phoneNo, String email, String password) {
        //Might add a return type to return correct error message to display on screen?
        ParseUser user = new ParseUser();
        user.setUsername(email);
        user.setPassword(password);
        user.setEmail(email);

        // other fields can be set just like with ParseObject
        user.put("firstName", firstName);
        user.put("surname", surname);
        user.put("phone", phoneNo);

        user.signUpInBackground(new SignUpCallback() {
            public void done(ParseException e) {
                if (e == null) {
                    //Sign up succeeded without exception
                    System.out.println("placeholder");

                } else {
                    // Sign up didn't succeed. Look at the ParseException
                    // to figure out what went wrong
                    //Would give user an error message on screen to let them know
                    System.out.println("placeholder 2");
                    System.out.println(e);
                }

            }
        });
    }
    public static void attemptLogin(String email, String password){
        ParseUser.logInInBackground(email, password, new LogInCallback() {
            public void done(ParseUser user, ParseException e) {
                if (user != null) {
                    // Hooray! The user is logged in.
                    System.out.println("SUCCESSFUL LOGIN!");
                } else {
                    // Signup failed. Look at the ParseException to see what happened.
                    System.out.println("unsuccessful login");
                }
            }
        });
    }

}


