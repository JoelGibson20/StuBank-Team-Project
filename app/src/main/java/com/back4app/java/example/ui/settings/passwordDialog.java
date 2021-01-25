package com.back4app.java.example.ui.settings;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatDialogFragment;

import com.back4app.java.example.R;

public class passwordDialog extends AppCompatDialogFragment {
    private EditText editTextTypePassword;
    private EditText editTextRetypePassword;
    private  passwordDialogListener listener;

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        LayoutInflater inflater = getActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.layout_dialog, null);

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
                        String password1 = editTextTypePassword.getText().toString();
                        String password2 = editTextRetypePassword.getText().toString();
                        listener.applyTexts(password1, password2);
                    }
                });

        editTextTypePassword = view.findViewById(R.id.type_password);
        editTextRetypePassword = view.findViewById(R.id.retype_password);

        return builder.create();

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
