package com.example.aso;

import android.support.constraint.ConstraintLayout;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TextInputEditText;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.example.aso.entitys.User;


public class Register extends AppCompatActivity implements View.OnClickListener{
    private final AppCompatActivity activity = Register.this;

    private Button btnRegister;
    private TextView btnLogin;
    private TextInputEditText userT, password, ced;
    private ConexionSQLiteHelper conn;
    private InputValidation inputValidation;
    private User user;

    private TextInputLayout textInputLayout9;
    private TextInputLayout textInputLayout6;
    private TextInputLayout textInputLayout7;

    private ConstraintLayout layout;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        initViews();
        initObjects();
        initListeners();

    }

    private void initViews() {
        btnRegister = findViewById(R.id.btnRegisterR);
        btnLogin = findViewById(R.id.btnLoginR);

        textInputLayout9 = findViewById(R.id.textInputLayout9);
        textInputLayout7 = findViewById(R.id.textInputLayout7);
        textInputLayout6 = findViewById(R.id.textInputLayout6);

        ced = findViewById(R.id.txtCedR);
        userT = findViewById(R.id.txtUserR);
        password = findViewById(R.id.txtPasswordR);

        layout = findViewById(R.id.ConstraintLayoutID);
    }

    private void initListeners(){
        btnRegister.setOnClickListener(this);
        btnLogin.setOnClickListener(this);
    }

    private void initObjects(){
        inputValidation = new InputValidation(activity);
        conn = new ConexionSQLiteHelper(activity);
        user = new User();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {

            case R.id.btnRegisterR:
                postDataToSQLite();
                break;

            case R.id.btnLoginR:
                finish();
                break;
        }
    }


    private void postDataToSQLite() {
        if (!inputValidation.isInputEditTextFilled(ced, textInputLayout9, "Ingrese la cédula")) {
            return;
        }
        if (!inputValidation.isInputEditTextFilled(userT, textInputLayout6, "Ingrese correo")) {
            return;
        }
        if (!inputValidation.isInputEditTextEmail(userT, textInputLayout6,"Ingrese correo")) {
            return;
        }
        if (!inputValidation.isInputEditTextFilled(password, textInputLayout7, "Ingrese contraseña")) {
            return;
        }

        if (!conn.checkUser(userT.getText().toString().trim(),password.getText().toString().trim())) {

            user.setCed(ced.getText().toString().trim());
            user.setUser(userT.getText().toString().trim());
            user.setPassword(password.getText().toString().trim());

            conn.addUser(user);

            // Snack Bar to show success message that record saved successfully
            Snackbar.make(layout, "Usuario Registrado", Snackbar.LENGTH_LONG).show();

            emptyInputEditText();


        } else {
            // Snack Bar to show error message that record already exists
            Snackbar.make(layout, "Correo ya existe", Snackbar.LENGTH_LONG).show();
        }


    }


    private void emptyInputEditText() {
        ced.setText(null);
        userT.setText(null);
        password.setText(null);
    }


}
