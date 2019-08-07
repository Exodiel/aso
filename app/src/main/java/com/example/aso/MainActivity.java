package com.example.aso;

import android.content.Intent;
import android.support.constraint.ConstraintLayout;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TextInputEditText;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;


public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    private final AppCompatActivity activity = MainActivity.this;

    private Button btnLogin, btnExit;
    private TextInputEditText txtUser, txtPassword;
    private TextView btnRegister;

    private TextInputLayout textInputLayoutEmail;
    private TextInputLayout textInputLayoutPassword;

    private ConstraintLayout layout;


    InputValidation inputValidation;
    ConexionSQLiteHelper conn;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initViews();
        initObjects();
        initListeners();
    }

    private void initViews(){
        textInputLayoutEmail = findViewById(R.id.textInputLayout);
        textInputLayoutPassword = findViewById(R.id.textInputLayout2);
        btnLogin = findViewById(R.id.btnLogin);
        btnRegister = findViewById(R.id.btnRegister);
        btnExit = findViewById(R.id.btnExit);
        txtPassword = findViewById(R.id.txtPassword);
        txtUser = findViewById(R.id.txtUser);

        layout = findViewById(R.id.ConstraintLayoutL);
    }

    private void initObjects() {
        conn = new ConexionSQLiteHelper(activity);
        inputValidation = new InputValidation(activity);
    }

    private void initListeners(){
        btnLogin.setOnClickListener(this);
        btnRegister.setOnClickListener(this);
        btnExit.setOnClickListener(this);
    }


    private void verifyFromSQLite() {
        if (!inputValidation.isInputEditTextFilled(txtUser, textInputLayoutEmail, "Ingrese correo")) {
            return;
        }
        if (!inputValidation.isInputEditTextEmail(txtUser, textInputLayoutEmail, "Ingrese correo")) {
            return;
        }
        if (!inputValidation.isInputEditTextFilled(txtPassword, textInputLayoutPassword, "Ingrese contraseña")) {
            return;
        }

        if (conn.checkUser(txtUser.getText().toString().trim(), txtPassword.getText().toString().trim())) {

            Intent accountsIntent = new Intent(activity, Home.class);
            startActivity(accountsIntent);
            emptyInputEditText();

        } else {
            // Snack Bar to show success message that record is wrong
            Snackbar.make(layout, "Correo o Contraseña incorrecta", Snackbar.LENGTH_LONG).show();
        }
    }

    private void emptyInputEditText() {
        txtUser.setText(null);
        txtPassword.setText(null);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btnLogin:
                verifyFromSQLite();
                break;
            case R.id.btnRegister:
                Intent intent = new Intent(getApplicationContext(), Register.class);
                startActivity(intent);
                break;
            case R.id.btnExit:
                finish();
                break;
        }
    }
}
