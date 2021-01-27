package com.back4app.java.example.ui.signup;

import android.app.Activity;

import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.annotation.StringRes;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Handler;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.back4app.java.example.HomeScreen;
import com.back4app.java.example.R;
import com.back4app.java.example.ui.databaseMethods;
import com.parse.ParseException;
/* App page for sign up, based off the template provided in Android Studio
Creator: Joel Gibson (B9020460)
 */

public class SignUpActivity extends AppCompatActivity {

    private LoginViewModel loginViewModel;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);
        loginViewModel = new ViewModelProvider(this, new LoginViewModelFactory())
                .get(LoginViewModel.class);

        //Loads all the edit text boxes on the sign up form
        final EditText firstNameEditText = findViewById(R.id.firstName);
        final EditText surnameEditText = findViewById(R.id.surname);
        final EditText phoneNoEditText = findViewById(R.id.phoneNo);
        final EditText emailEditText = findViewById(R.id.email);
        final EditText passwordEditText = findViewById(R.id.password);
        //Loads the register button
        final Button registerButton = findViewById(R.id.register);
        final ProgressBar loadingProgressBar = findViewById(R.id.loading);

        loginViewModel.getLoginFormState().observe(this, new Observer<LoginFormState>() {
            @Override
            public void onChanged(@Nullable LoginFormState loginFormState) {
                if (loginFormState == null) {
                    return;
                    //If signup form is untouched do nothing
                }
                //If data input in login form is valid enable button
                registerButton.setEnabled(loginFormState.isDataValid());
                if (loginFormState.getUsernameError() != null) {
                    //Present error text for username
                    emailEditText.setError(getString(loginFormState.getUsernameError()));
                }
                if (loginFormState.getPasswordError() != null) {
                    //Present error text for password
                    passwordEditText.setError(getString(loginFormState.getPasswordError()));
                }
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
                        passwordEditText.getText().toString(),firstNameEditText.getText().toString(),
                        surnameEditText.getText().toString(),phoneNoEditText.getText().toString());
            }
        };
        emailEditText.addTextChangedListener(afterTextChangedListener);
        passwordEditText.addTextChangedListener(afterTextChangedListener);

        passwordEditText.setOnEditorActionListener(new TextView.OnEditorActionListener() {

            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                //Clicking enter to submit form rather than button
                if (actionId == EditorInfo.IME_ACTION_DONE) {
                    if(registerButton.isEnabled()) {
                        submitForm();
                    }
                }
                return false;
            }
        });


        registerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Listens for clicking the register button
                submitForm();

            }
        });
    }

    private void updateUiWithUser(LoggedInUserView model) {
        String welcome = getString(R.string.welcome) + model.getDisplayName();
        // TODO : initiate successful logged in experience
        Toast.makeText(getApplicationContext(), welcome, Toast.LENGTH_LONG).show();
    }

    private void showLoginFailed(@StringRes Integer errorString) {
        Toast.makeText(getApplicationContext(), errorString, Toast.LENGTH_SHORT).show();
    }

    private void submitForm(){
        final EditText firstNameEditText = findViewById(R.id.firstName);
        final EditText surnameEditText = findViewById(R.id.surname);
        final EditText phoneNoEditText = findViewById(R.id.phoneNo);
        final EditText emailEditText = findViewById(R.id.email);
        final EditText passwordEditText = findViewById(R.id.password);
        final ProgressBar loadingProgressBar = findViewById(R.id.loading);

        loadingProgressBar.setVisibility(View.VISIBLE);
        try {
            //New account added to database
            databaseMethods.addToDatabase(firstNameEditText.getText().toString(),
                    surnameEditText.getText().toString(), phoneNoEditText.getText().toString(),
                    emailEditText.getText().toString().toLowerCase(), passwordEditText.getText().toString());
        } catch (ParseException e) {
            e.printStackTrace();
        }

        //Creates a current account for the user on sign-up
        try {
            databaseMethods.retrieveAccountsBeforeCreation("currentAccount", "Current Account");
        } catch (ParseException e) {
            e.printStackTrace();
        }
                               /*This forces the program to wait 1 seconds (1000ms) before loading
                the homepage, delaying enough time for the current account to be saved so it can
                 be properly displayed on the homescreen */
        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            public void run() {
                //Directs to home page
                Intent intent = new Intent(getApplicationContext(), HomeScreen.class);
                startActivity(intent);
            }
        }, 1000);
    }
}