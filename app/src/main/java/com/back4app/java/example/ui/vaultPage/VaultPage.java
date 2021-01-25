package com.back4app.java.example.ui.vaultPage;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.back4app.java.example.R;
import com.back4app.java.example.ui.databaseMethods;
import com.parse.ParseException;
import com.parse.ParseObject;

public class VaultPage extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_vault_page);
        final EditText accountName = findViewById(R.id.accountName);
        final TextView accountNumber = findViewById(R.id.accountNumber);
        final TextView sortCode = findViewById(R.id.sortCode);
        final TextView balance = findViewById(R.id.balance);

        //Load buttons to set onclick listeners for them
        final Button renameButton = findViewById(R.id.renameButton);
        final Button lockButton = findViewById(R.id.lockButton);
        final Button shareDetailsButton = findViewById(R.id.shareDetailsButton);

        //Retrieve the account in question from the intent
        ParseObject accountParseObject = (ParseObject) getIntent().getExtras().get("accountParseObject");

        accountName.setText(accountParseObject.getString("accountName"));
        balance.setText(accountParseObject.getString("balance"));

        //Call method to set the button text based on whether the account is locked or not
        setLockButtonText(accountParseObject,lockButton);

        renameButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) { //After rename button is clicked
                accountName.setFocusableInTouchMode(true); //Allows account name text to be edited
                accountName.requestFocus(); //Account name EditText gets focus
                InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.showSoftInput(accountName, InputMethodManager.SHOW_IMPLICIT);
                //On-screen keyboard shown
                accountName.setSelection(accountName.getText().length());
                //Sets position in EditText to the end

                accountName.setOnEditorActionListener(new EditText.OnEditorActionListener() {
                    @Override
                    public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                        if (actionId == EditorInfo.IME_ACTION_SEARCH ||
                                actionId == EditorInfo.IME_ACTION_DONE ||
                                event != null &&
                                        event.getAction() == KeyEvent.ACTION_DOWN &&
                                        event.getKeyCode() == KeyEvent.KEYCODE_ENTER) {
                            if (event == null || !event.isShiftPressed()) {
                                // The user exits the edit text
                                changeAccountName(); //Change account name in database
                            }
                        }
                        return false; // pass on to other listeners.
                    }
                });
            }
        });

        lockButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                System.out.println("Lock button clicked");
                try {
                    getIntent().putExtra("accountParseObject",databaseMethods.toggleAccountLock((ParseObject) getIntent().getExtras().get("accountParseObject")));
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                /*Toggles the locking of the account, changes the extra in intent (where all the
                account details are stored) to match this change */
                setLockButtonText((ParseObject) getIntent().getExtras().get("accountParseObject"),lockButton);
                //Change lock button text
            }
        });

        shareDetailsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ParseObject accountParseObject = (ParseObject) getIntent().getExtras().get("accountParseObject");

                //Use Android Sharesheet to bring up the Android share menu
                Intent sendIntent = new Intent(Intent.ACTION_SEND);
                //Below is the text which will be sent
                sendIntent.putExtra(Intent.EXTRA_TEXT, String.format("Here's my StuBank account details - \n" +
                                "Sort code: %s\nAccount number: %s",accountParseObject.getString("sortCode"),
                        accountParseObject.getString("accountNumber")));
                sendIntent.setType("text/plain");
                Intent shareIntent = Intent.createChooser(sendIntent, null);
                startActivity(shareIntent);
            }
        });

    }
    public void changeAccountName(){
        final EditText accountName = findViewById(R.id.accountName);
        ParseObject accountParseObject = (ParseObject) getIntent().getExtras().get("accountParseObject");
        try {
            databaseMethods.changeAccountName(accountParseObject, accountName.getText().toString());
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }

    public void setLockButtonText(ParseObject accountParseObject, Button lockButton){
        ImageView locked = findViewById(R.id.lockedImage);
        ImageView unlocked = findViewById(R.id.unlockedImage);
        //Correct text set for lock button based on whether or not the account is locked
        if(accountParseObject.getBoolean("locked")) {
            System.out.println("TRIGGERED 1");
            unlocked.setVisibility(View.INVISIBLE);
            locked.setVisibility(View.VISIBLE);
            lockButton.setText("Unlock");
        }
        else{
            lockButton.setText("Lock");
            locked.setVisibility(View.INVISIBLE);
            unlocked.setVisibility(View.VISIBLE);
            System.out.println("TRIGGERED 2");
        }
    }

}