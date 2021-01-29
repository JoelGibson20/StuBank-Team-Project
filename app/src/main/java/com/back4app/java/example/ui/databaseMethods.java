package com.back4app.java.example.ui;

import android.util.Log;

import com.parse.FindCallback;
import com.parse.GetCallback;
import com.parse.LogInCallback;
import com.parse.Parse;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import com.parse.SaveCallback;

import java.sql.SQLOutput;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import static android.content.ContentValues.TAG;

import java.util.Date;

public class databaseMethods {
    public static boolean hasCard;
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

    public static ParseObject getCurrentUser(){
        return(ParseUser.getCurrentUser());
    }

    public static List<ParseObject> getAccounts() throws ParseException {
        ParseObject currentUser = getCurrentUser();
        ParseQuery<ParseObject> query = ParseQuery.getQuery("Accounts");
        query.whereEqualTo("accountOwner", currentUser.getObjectId());
        query.orderByAscending("accountName");
        return(query.find());
    }

    public static void changeAccountName(ParseObject accountParseObject, String newName) throws ParseException {
        accountParseObject.put("accountName", newName);
        accountParseObject.saveInBackground();
    }

    public static ParseObject toggleAccountLock(ParseObject accountParseObject) throws ParseException {
        if(accountParseObject.getBoolean("locked")){
            accountParseObject.put("locked", false);
        }
        else{
            accountParseObject.put("locked", true);
        }
        accountParseObject.save();
        return(accountParseObject);
    }

    public static void retrieveAccountsBeforeCreation(String accountType, String accountName) throws ParseException {
        ParseQuery<ParseObject> accountNumberQuery = ParseQuery.getQuery("Accounts");
        accountNumberQuery.findInBackground(new FindCallback<ParseObject>() {
            public void done(List<ParseObject> accountsList, ParseException e) {
                if (e == null) {
                    try {
                        createAccount(accountsList, accountType, accountName);
                    } catch (ParseException parseException) {
                        parseException.printStackTrace();
                    }
                } else {

                }
            }
        });


    }
    public static void createAccount(List<ParseObject> accountsList, String accountType, String accountName) throws ParseException {
        //Create account ParseObject
        ParseObject account = new ParseObject("Accounts");
        //Assign attributes we set
        account.put("accountOwner",getCurrentUser().getObjectId());
        account.put("accountType", accountType);
        if(accountName.equals("")){
            //If the user provides no name assign default name to prevent an error
            account.put("accountName","New Vault");
        }
        else{
            account.put("accountName", accountName);
        }
        account.put("balance", "£0");
        account.put("locked", false);

        //Initialise lists which will hold all pre-existing account numbers and sort codes
        ArrayList<String> existingAccountNumbers = new ArrayList<>();
        ArrayList<String> existingSortCodes = new ArrayList<>();

        for(int x = 0; x < accountsList.size();x++){
            //Saves all existing account numbers and sort codes to the lists
            existingAccountNumbers.add(accountsList.get(x).getString("accountNumber"));
            existingSortCodes.add(accountsList.get(x).getString("sortCode"));
        }


        String accountNumber = "";

        //Will generate account number until it creates one which doesn't already exist
        while ((existingAccountNumbers.contains(accountNumber)) || (accountNumber.equals(""))){
            accountNumber = "";
            Random rd = new Random();

            for(int x = 0; x < 8; x++){
                accountNumber += rd.nextInt(10);
            }
        }


        String sortCode = "";

        //Will generate sort code until it creates one which doesn't already exist
        while ((existingSortCodes.contains(sortCode)) || (sortCode.equals(""))){
            sortCode = "";
            Random rd = new Random();

            for (int y = 0; y < 6; y++){
                sortCode += rd.nextInt(10);
            }
            sortCode = sortCode.substring(0,2) + "-" + sortCode.substring(2,4) + "-" + sortCode.substring(4,6);
        }

        account.put("accountNumber", accountNumber);
        account.put("sortCode",sortCode);
        account.save();

    }

    public static List<ParseObject> getAllAccountsOfOneUser() {
        ParseQuery<ParseObject> query = ParseQuery.getQuery("Accounts");

        query.whereEqualTo("accountOwner", getCurrentUser().getObjectId());
        try {
            return (query.find());
        }
        catch (ParseException e){
            Log.d("DatabaseMethods", e.toString());
            return null;
        }
    }
    public static List<ParseObject> getAllOutgoingTransactionsFromOneAccount(String outgoingAccountNumber) {
        ParseQuery<ParseObject> query = ParseQuery.getQuery("Transactions");

        query.whereEqualTo("outgoingAccount", outgoingAccountNumber);
        try {
            return (query.find());
        }
        catch (ParseException e){
            Log.d("DatabaseMethods", e.toString());
            return null;
        }
    }

    public static List<ParseObject> getTransaction(String Accountnum, String date){
        ParseQuery<ParseObject> accountquery = ParseQuery.getQuery("Transactions");


        accountquery.whereEqualTo("outgoingAccount", Accountnum);

        ParseQuery<ParseObject> datequery = ParseQuery.getQuery("Transactions");

        datequery.whereEqualTo("transactionDate", date);

        List<ParseQuery<ParseObject>> queries = new ArrayList<ParseQuery<ParseObject>>();

        queries.add(accountquery);
        queries.add(datequery);

        ParseQuery<ParseObject> mainquery = ParseQuery.or(queries);

        try{
            return (mainquery.find());

        }
        catch (ParseException e){
            Log.d("DatabaseMethods", e.toString());
            return null;
        }

    }

    public static void changePassword(String Password){
        ParseObject currentUser = getCurrentUser();

        currentUser.put("password", Password);

        currentUser.saveInBackground(new SaveCallback() {
            @Override
            public void done(ParseException e) {

            }
        });
    }



    public static List<ParseObject> getTransactionsForAccount(String sortCodeAccountNumber) throws ParseException {
        ParseQuery<ParseObject> outgoingTransactions = ParseQuery.getQuery("Transactions");
        outgoingTransactions.whereEqualTo("outgoingAccount", sortCodeAccountNumber);
        //Get outgoing transactions

        ParseQuery<ParseObject> ingoingTransactions = ParseQuery.getQuery("Transactions");
        ingoingTransactions.whereEqualTo("ingoingAccount", sortCodeAccountNumber);
        //Get ingoing transactions

        List<ParseQuery<ParseObject>> queries = new ArrayList<ParseQuery<ParseObject>>();
        queries.add(outgoingTransactions);
        queries.add(ingoingTransactions);


        ParseQuery<ParseObject> query = ParseQuery.or(queries);
        query.setLimit(20); //Get only the last 20 transactions to show
        query.orderByDescending("transactionDate"); //Order by most recent
        List<ParseObject> transactionsList = new ArrayList<>(query.find());

        return(transactionsList);
    }
    public static void checkIfHasCard() {
        ParseQuery<ParseObject> query = ParseQuery.getQuery("Cards");
        // The query will search for a ParseObject, given its objectId.
        // When the query finishes running, it will invoke the GetCallback
        // with either the object, or the exception thrown
        query.whereEqualTo("cardOwner", getCurrentUser().getObjectId());
        query.getFirstInBackground(new GetCallback<ParseObject>() {
            public void done(ParseObject result, ParseException e) {
                if (e == null) {
                    hasCard=true;
                }
                else {
                    hasCard=false;
                }
            }
        });
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

//Get account by sort code and account number
    public static ParseObject getAccount(String sortcode, String accountNumber) throws ParseException {
        ParseQuery<ParseObject> query = ParseQuery.getQuery("Accounts");
        query.whereEqualTo("accountNumber", accountNumber);
        query.whereEqualTo("sortCode", sortcode);
        ParseObject account = query.getFirst();
        return account;
    }
//Get account owner by their email address
    public static String getUser(String email) throws ParseException {
        ParseQuery<ParseUser> query = ParseUser.getQuery();
        query.whereEqualTo("email", email);
        query.find();
        ParseObject userPO = query.getFirst();
        String accountOwner = userPO.getObjectId();
        return accountOwner;
    }
//Get account owner by phone
    public static String getUserByPhone(String phone) throws ParseException {
        ParseQuery<ParseUser> query = ParseUser.getQuery();
        query.whereEqualTo("phone", phone);
        query.find();
        ParseObject userPO = query.getFirst();
        String accountOwner = userPO.getObjectId();
        return accountOwner;
    }

//Return parse object of a user's current account
    public static ParseObject getUserCurrentAccount(String accountOwner) throws ParseException {
        ParseQuery<ParseObject> query = ParseQuery.getQuery("Accounts");
        query.whereEqualTo("accountOwner", accountOwner);
        query.whereEqualTo("accountName", "Current Account");
        query.find();
        ParseObject account = query.getFirst();
        return account;


    }
//Create transaction to display in Transactions table
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
//Get the full name of a user
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
//Get all saved transactions
    public static List<ParseObject> getSavedTransactions(String outgoingAccount) throws ParseException {
        ParseQuery<ParseObject> query = ParseQuery.getQuery("Transactions");
        query.whereEqualTo("outgoingAccount", outgoingAccount);
        query.whereEqualTo("Saved", true);
        query.find();
        return query.find();
    }
   //Check if a payee is already saved to prevent duplicates
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
    //Get account by its number
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
//Get the transactions for an account, but only once every time method called
    public static ParseObject getTransactions(String account) throws ParseException {
        ParseQuery query = ParseQuery.getQuery("Transactions");
                query.whereEqualTo("ingoingAccount", account);
                query.find();
                return query.getFirst();


    }














   }




