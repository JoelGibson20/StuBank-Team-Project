package com.back4app.java.example.ui;

import android.util.Log;

import com.parse.FindCallback;
import com.parse.LogInCallback;
import com.parse.Parse;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import com.parse.SignUpCallback;

import java.sql.SQLOutput;
import java.util.List;

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

    public static boolean attemptLogin(String email, String password) throws ParseException {
        boolean loginSuccess = true;
        try {
            ParseUser test = ParseUser.logIn(email, password);
        } catch (ParseException e) {
            System.out.println(e);
            loginSuccess = false;
            //Exception will occur if username/password pair isn't found

        }
        return (loginSuccess);
    }


    public static ParseObject retrieveUserDetails(String username) throws ParseException {
        ParseObject userDetails = null;
        ParseQuery<ParseObject> query = ParseQuery.getQuery("_User");
        query.whereEqualTo("username", username);
        try {
            userDetails = query.getFirst();
        } catch (ParseException e) {
            System.out.println(e);
        }
        return(userDetails);
    }
}


