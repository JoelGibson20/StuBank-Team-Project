package com.back4app.java.example.ui.login;

import android.app.Activity;

import androidx.appcompat.app.AlertDialog;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.annotation.StringRes;
import androidx.appcompat.app.AppCompatActivity;

import android.text.Editable;
import android.text.TextWatcher;
import android.view.ContextThemeWrapper;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.back4app.java.example.HomeScreen;
import com.back4app.java.example.R;
import com.back4app.java.example.ui.databaseMethods;
import com.back4app.java.example.ui.signup.SignUpActivity;
import com.parse.ParseException;
/* App page for login, based off the template provided in Android Studio
Creator: Joel Gibson (B9020460)
 */
public class LoginActivity extends AppCompatActivity {

    private LoginViewModel loginViewModel;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        loginViewModel = new ViewModelProvider(this, new LoginViewModelFactory())
                .get(LoginViewModel.class);

        //Load all the text input boxes, buttons etc
        final EditText emailEditText = findViewById(R.id.username);
        final EditText passwordEditText = findViewById(R.id.password);
        final Button loginButton = findViewById(R.id.register);
        final TextView dontHaveAccount = findViewById(R.id.dontHaveAccount);
        final ProgressBar loadingProgressBar = findViewById(R.id.loading);

        loginViewModel.getLoginFormState().observe(this, new Observer<LoginFormState>() {
            @Override
            public void onChanged(@Nullable LoginFormState loginFormState) {
                if (loginFormState == null) {
                    return;
                    //If login form is untouched do nothing
                }
                //If data input in login form is valid enable button
                loginButton.setEnabled(loginFormState.isDataValid());
            }
        });

        loginViewModel.getLoginResult().observe(this, new Observer<LoginResult>() {
            @Override
            public void onChanged(@Nullable LoginResult loginResult) {
                if (loginResult == null) {
                    return;
                }
                loadingProgressBar.setVisibility(View.GONE);
                if (loginResult.getError() != null) {
                    showLoginFailed(loginResult.getError());
                }
                if (loginResult.getSuccess() != null) {
                    updateUiWithUser(loginResult.getSuccess());
                }
                setResult(Activity.RESULT_OK);

                //Complete and destroy login activity once successful
                finish();
            }
        });

        TextWatcher afterTextChangedListener = new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                // ignore
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                // ignore
            }

            @Override
            public void afterTextChanged(Editable s) {
                loginViewModel.loginDataChanged(emailEditText.getText().toString(),
                        passwordEditText.getText().toString());
            }
        };
        emailEditText.addTextChangedListener(afterTextChangedListener);
        passwordEditText.addTextChangedListener(afterTextChangedListener);

        //This section submits form when enter is clicked, but doesn't check if form is completed
        passwordEditText.setOnEditorActionListener(new TextView.OnEditorActionListener() {

            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_DONE) {
                    if(loginButton.isEnabled()) {
                        tryLogin();
                    }
                }
                return false;
            }
        });

        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            //Listens for clicking the sign in button
            public void onClick(View v) {
                tryLogin();

            }
        });

        dontHaveAccount.setOnClickListener(new View.OnClickListener(){
            //Listens for clicking of the don't have account text
            public void onClick(View v){
                //Redirects to sign up page
                Intent intent = new Intent(getApplicationContext(), SignUpActivity.class);
                startActivity(intent);
            }
        });
    }

    private void updateUiWithUser(LoggedInUserView model) {
        String welcome = getString(R.string.welcome) + model.getDisplayName();
        Toast.makeText(getApplicationContext(), welcome, Toast.LENGTH_LONG).show();
    }

    private void showLoginFailed(@StringRes Integer errorString) {
        Toast.makeText(getApplicationContext(), errorString, Toast.LENGTH_SHORT).show();
    }

    private void tryLogin(){
        final EditText emailEditText = findViewById(R.id.username);
        final EditText passwordEditText = findViewById(R.id.password);
        try {
            if(databaseMethods.attemptLogin(emailEditText.getText().toString().toLowerCase(),passwordEditText.getText().toString())){
                //If email and password match
                Intent intent = new Intent(getApplicationContext(), HomeScreen.class);
                startActivity(intent);
                //Directs to home page
            }
            else{ //Login failed, so present error message
                //Define a builder to create an AlertDialog (popup window)
                AlertDialog.Builder builder = new AlertDialog.Builder(new ContextThemeWrapper(LoginActivity.this, R.style.AlertDialogCustom));
                builder.setCancelable(true);
                //Create a TextView for the title and set its attributes
                TextView titleText = new TextView(LoginActivity.this);
                titleText.setText(getString(R.string.failed_login));
                titleText.setGravity(Gravity.CENTER);
                titleText.setPadding(10,10,10,10);
                titleText.setTextSize(20);
                builder.setCustomTitle(titleText); //Set the title for the AlertDialog

                builder.setNegativeButton(getString(R.string.backButton), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });
                AlertDialog alert = builder.create(); //Creates AlertDialog with specified properties
                alert.requestWindowFeature(Window.FEATURE_NO_TITLE);
                alert.show(); //Shows AlertDialog

            }

        } catch (ParseException e) {
            e.printStackTrace();
        }
    }
}