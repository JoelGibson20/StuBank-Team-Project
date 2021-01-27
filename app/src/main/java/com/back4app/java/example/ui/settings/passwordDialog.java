package com.back4app.java.example.ui.settings;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatDialogFragment;

import com.back4app.java.example.R;
import com.google.android.material.textfield.TextInputLayout;

import java.util.regex.Pattern;

public class passwordDialog extends AppCompatDialogFragment {
    private EditText editTextTypePassword;
    private EditText editTextRetypePassword;
    private  passwordDialogListener listener;
    private TextInputLayout textInputPassword1;
    private  TextInputLayout textInputPassword2;

    private static final String TAG = "PasswordDialog";
    //password regex pattern is created to validate the password entered.
    private static final Pattern PASSWORD_PATTERN =
            Pattern.compile("^(?=.*?[A-Z])(?=.*?[a-z])(?=.*?[0-9])(?=.*?[#?!@$%^&*-]).{8,}$");


    //creates a custom dialog for the popout when the button is clicked.
    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        LayoutInflater inflater = getActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.layout_dialog, null);
        //passwords entered are passed.
        editTextTypePassword = view.findViewById(R.id.type_password);
        editTextRetypePassword = view.findViewById(R.id.retype_password);

        textInputPassword1 = view.findViewById(R.id.type_password_layout);
        textInputPassword2 = view.findViewById(R.id.retype_password_layout);

        //builds the buttons on the dialog and sets the title.
        builder.setView(view)
                .setTitle("Change Password")
                //cancel button is created to go back to main settings page.
                .setNegativeButton("cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                })
                //ok button is created for when the user types in the passwords.
                .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        //if statement for when the passwords entered are invalid.
                        if (!validatePassword() | !validatePassword2()){
                            //pop up message is displayed on the screen for the user.
                            Toast.makeText(getContext(), "Password Invalid", Toast.LENGTH_SHORT).show();
                            //Also checks whether the passwords match to confirm the password.
                        }else if(!(editTextTypePassword.getText().toString().equals(editTextRetypePassword.getText().toString()))){
                            //pop up message is shown when the passwords do not match.
                            Toast.makeText(getContext(), "Passwords do not match", Toast.LENGTH_SHORT).show();
                        }
                        else{
                            //if the passwords are valid then the input is saved and passed.
                            String password1 = editTextTypePassword.getText().toString();
                            String password2 = editTextRetypePassword.getText().toString();
                            listener.applyTexts(password1, password2);
                        }
                    }
                });

        return builder.create();

    }

    //validation for the password for the error message so the user knows the password is invalid.
    //error message works however ok button always dismisses the dialog so messaged are never shown to the user
    //doesn't work due to it being a custom dialog.
    private boolean validatePassword(){
        String passwordInput = textInputPassword1.getEditText().getText().toString();

        if(passwordInput.isEmpty()){
            textInputPassword1.setError("Please input new Password");
            return false;
        }else if(!PASSWORD_PATTERN.matcher(passwordInput).matches()){
            textInputPassword1.setError("Required: 8+ characters, 1 uppercase digit and a special character");
            return false;
        }
        else{
            textInputPassword1.setError(null);
            return true;
        }

    }

    //does the same as the other validate password.
    private boolean validatePassword2(){
        String passwordInput =  textInputPassword2.getEditText().getText().toString();

        if(passwordInput.isEmpty()){
            textInputPassword2.setError("Please input new Password");
            return false;
        }else if(!PASSWORD_PATTERN.matcher(passwordInput).matches()){
            textInputPassword1.setError("Required: 8+ characters, 1 uppercase digit and a special character");
            return false;
        }
        else{
            textInputPassword2.setError(null);
            return true;
        }

    }




    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);

        try {
            listener = (passwordDialogListener) context;
        } catch (ClassCastException e) {
            throw new ClassCastException(context.toString()+
                    "must implemement PasswordDialogListener");
        }

    }
    //password is passed to the settings class.
    public interface passwordDialogListener{
        void applyTexts(String password1, String password2);


    }
}
