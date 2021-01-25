package com.back4app.java.example.ui.settings;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatDialogFragment;

import com.back4app.java.example.R;
import com.google.android.material.textfield.TextInputLayout;

public class passwordDialog extends AppCompatDialogFragment {
    private EditText editTextTypePassword;
    private EditText editTextRetypePassword;
    private  passwordDialogListener listener;
    private TextInputLayout textInputPassword1;
    private  TextInputLayout textInputPassword2;

    private static final String TAG = "PasswordDialog";


    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        LayoutInflater inflater = getActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.layout_dialog, null);

        editTextTypePassword = view.findViewById(R.id.type_password);
        editTextRetypePassword = view.findViewById(R.id.retype_password);

        textInputPassword1 = view.findViewById(R.id.type_password_layout);
        textInputPassword2 = view.findViewById(R.id.retype_password_layout);

        builder.setView(view)
                .setTitle("Change Password")
                .setNegativeButton("cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                })
                .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if(!validatePassword() | !validatePassword2()){
                            String password1 = editTextTypePassword.getText().toString();
                            String password2 = editTextRetypePassword.getText().toString();
                            listener.applyTexts(password1, password2);
                        }
                    }
                });




        return builder.create();

    }


    private boolean validatePassword(){
        String passwordInput = editTextRetypePassword.getText().toString();

        if(passwordInput.isEmpty()){
            textInputPassword1.setError("Please input new Password");
            Log.d(TAG, "PLEASE INPUT NEW PASSWORD");
            return false;
        }else{
            textInputPassword1.setError(null);
            Log.d(TAG, "RETURNS TRUE");
            return true;
        }

    }

    private boolean validatePassword2(){
        String passwordInput = editTextTypePassword.getText().toString();

        if(passwordInput.isEmpty()){
            textInputPassword2.setError("Please input new Password");
            return false;
        }else{
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

    public interface passwordDialogListener{
        void applyTexts(String password1, String password2);



    }
}
