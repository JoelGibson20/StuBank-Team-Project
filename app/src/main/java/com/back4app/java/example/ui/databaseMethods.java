package com.back4app.java.example.ui;

import com.parse.LogInCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import java.util.Date;
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
        return (userDetails);
    }

    public static ParseObject getCurrentUser() {
        return (ParseUser.getCurrentUser());
    }

    public static List<ParseObject> getAccounts() throws ParseException {
        ParseObject currentUser = getCurrentUser();
        ParseQuery<ParseObject> query = ParseQuery.getQuery("Accounts");
        query.whereEqualTo("accountOwner", currentUser.getObjectId());
        return (query.find());
    }

    public static void changeAccountName(ParseObject accountParseObject, String newName) throws ParseException {
        accountParseObject.put("accountName", newName);
        accountParseObject.save();
    }

    public static List<ParseObject> currentAccount(String outgoingAccount) throws ParseException {
        ParseQuery<ParseObject> query = ParseQuery.getQuery("Accounts");
        query.whereEqualTo("accountOwner", getCurrentUser().getObjectId());
        query.whereEqualTo("accountName", outgoingAccount);
        String fromAccount = query.find().toString();
        return (query.find());
    }

    //Creates parse object from selected outgoing account
    public static ParseObject outgoingAccount(String outgoingAccount) throws ParseException {
        ParseObject account = null;
        List<ParseObject> accounts = null;
        ParseQuery<ParseObject> query = ParseQuery.getQuery("Accounts");
        query.whereEqualTo("accountName", outgoingAccount);
        query.whereEqualTo("accountOwner", getCurrentUser().getObjectId());
        query.find();
        account = query.getFirst();
        return (account);
    }

    //Using account parse object to change corresponding balance after transfer
    public static void updateBalance(ParseObject account, String newBalance) {
        // ParseObject balance = account.getFirst();
        //String name = (String) account.get("accountName");
        //ParseQuery<ParseObject> query = ParseQuery.getQuery("Accounts");
        //query.whereEqualTo("accountName", name);
        //balance.put("balance", newBalance);
    }

    public static ParseObject getAccount(String sortcode, String accountNumber) throws ParseException {
        ParseQuery<ParseObject> query = ParseQuery.getQuery("Accounts");
        query.whereEqualTo("accountNumber", accountNumber);
        query.whereEqualTo("sortCode", sortcode);
        ParseObject account = query.getFirst();
        return account;
    }

    public static String getUser(String email) throws ParseException {
        ParseQuery<ParseUser> query = ParseUser.getQuery();
        query.whereEqualTo("email", email);
        query.find();
        ParseObject userPO = query.getFirst();
        String accountOwner = userPO.getObjectId();
        return accountOwner;
    }

    public static String getUserByPhone(String phone) throws ParseException {
        ParseQuery<ParseUser> query = ParseUser.getQuery();
        query.whereEqualTo("phone", phone);
        query.find();
        ParseObject userPO = query.getFirst();
        String accountOwner = userPO.getObjectId();
        return accountOwner;
    }


    public static ParseObject getUserCurrentAccount(String accountOwner) throws ParseException {
        ParseQuery<ParseObject> query = ParseQuery.getQuery("Accounts");
        query.whereEqualTo("accountOwner", accountOwner);
        query.whereEqualTo("accountName", "Current Account");
        query.find();
        ParseObject account = query.getFirst();
        return account;


    }

    public static void createTransaction(String outgoingAccount, String reference,
                                         String value, String ingoingAccount,
                                         boolean saved, String currencySymbol) throws ParseException {
        ParseObject transactions = new ParseObject("Transactions");
        Date todayDate = new Date();
        value = currencySymbol + value;
        transactions.put("outgoingAccount", outgoingAccount);
        transactions.put("reference", reference);
        transactions.put("transactionDate", todayDate);
        transactions.put("value", value);
        transactions.put("ingoingAccount", ingoingAccount);
        transactions.put("transactionType", "payment");
        transactions.put("Saved", saved);
        transactions.save();
    }

    public static String getUserNames(String user) throws ParseException {
        ParseQuery<ParseUser> query = ParseUser.getQuery();
        query.whereEqualTo("objectId", user);
        query.find();
        ParseObject userPO = query.getFirst();
        String firstName = userPO.get("firstName").toString();
        String lastName = userPO.get("surname").toString();
        String names = firstName + " " + lastName;
        return names;
    }

    public static List<ParseObject> getSavedTransactions(String outgoingAccount) throws ParseException {
        ParseQuery<ParseObject> query = ParseQuery.getQuery("Transactions");
        query.whereEqualTo("outgoingAccount", outgoingAccount);
        query.whereEqualTo("Saved", true);
        query.find();
        return query.find();
    }
        public static boolean payeeAlreadySaved (String accountNumber) throws ParseException {
        ParseQuery<ParseObject> query = ParseQuery.getQuery("Transactions");
        query.whereEqualTo("outgoingAccount", accountNumber);
        query.whereEqualTo("Saved", true);
        List<ParseObject> exists = query.find();
        if (exists.size()>0) {
            return true;
        }
        else {
            return false;
        }
        }
        public static ParseObject getAccountByNumber(String accountNumber) throws ParseException {
        ParseQuery<ParseObject> query = ParseQuery.getQuery("Accounts");
        String sortCode = accountNumber.substring(0,8);
        accountNumber = accountNumber.substring(accountNumber.length()-8);
        query.whereEqualTo("sortCode", sortCode);
        query.whereEqualTo("accountNumber", accountNumber);
        query.find();
        ParseObject account = query.getFirst();

        return account;
    }



}




