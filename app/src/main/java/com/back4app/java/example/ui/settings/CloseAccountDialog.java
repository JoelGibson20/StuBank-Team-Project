package com.back4app.java.example.ui.settings;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatDialogFragment;

import com.back4app.java.example.ui.databaseMethods;
import com.back4app.java.example.ui.login.LoginActivity;
import com.parse.ParseObject;
import com.parse.ParseUser;

// dialog box to allow the user to confirm whether or not they want to close their account
public class CloseAccountDialog extends AppCompatDialogFragment {
    private CloseAccountDialogListener listener;

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState){
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        // sets attributes of dialog box
        builder.setTitle("Attention!")
                .setMessage("Are you sure you want to close all your accounts?")
                .setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                    }
                })
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Log.d("SettingsActivity", "here");
                        listener.onYesClicked();
                    }
                });
        return builder.create();
    }

    // allows for onclick method to be written in SettingsActivity
    public interface CloseAccountDialogListener {
        void onYesClicked();
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        try {
            listener = (CloseAccountDialogListener) context;
        }
        catch (ClassCastException e){
            throw new ClassCastException(context.toString() + "Must implement CloseAccountDialogListener");
        }
    }


}
