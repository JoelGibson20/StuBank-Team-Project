package com.back4app.java.example.ui;

import com.parse.LogInCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import java.util.List;

public class databaseMethods {

    public static void addToDatabase(String firstName, String surname, String phoneNo, String email, String password) throws ParseException {
        //Might add a return type to return correct error message to display on screen?
        ParseUser user = new ParseUser();
        user.setUsername(email);
        user.setPassword(password);
        user.setEmail(email);

        // other fields can be set just like with ParseObject
        user.put("firstName", firstName);
        user.put("surname", surname);
        user.put("phone", phoneNo);
        user.signUp();
        ParseUser.becomeInBackground("session-token-here", new LogInCallback() {
            public void done(ParseUser user, ParseException e) {
                if (user != null) {
                    // The current user is now set to user.
                } else {
                    // The token could not be validated.
                }
            }
        });//

    }

    public static boolean attemptLogin(String email, String password) throws ParseException {
        boolean loginSuccess = true;
        try {
            ParseUser test = ParseUser.logIn(email, password);
            ParseUser.becomeInBackground("session-token-here", new LogInCallback() {
                public void done(ParseUser user, ParseException e) {
                    if (user != null) {
                        // The current user is now set to user.
                    } else {
                        // The token could not be validated.
                    }
                }
            });
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
            userDetails = query.getFirst(); //Tries to execute query
        } catch (ParseException e) {
            System.out.println(e);
        }
        return(userDetails);
    }

    public static ParseObject getCurrentUser(){
       return(ParseUser.getCurrentUser());
    }

    public static List<ParseObject> getAccounts() throws ParseException {
        ParseObject currentUser = getCurrentUser();

        ParseQuery<ParseObject> query = ParseQuery.getQuery("Accounts");
        query.whereEqualTo("accountOwner", currentUser.getObjectId());
        return(query.find());
    }

    public static void changeAccountName(ParseObject accountParseObject, String newName) throws ParseException {
        accountParseObject.put("accountName", newName);
        accountParseObject.save();
    }
}


