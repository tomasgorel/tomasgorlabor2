package com.example.rename;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class RegisterActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        EditText username = findViewById(R.id.username);
        EditText password = findViewById(R.id.password);
        EditText email = findViewById(R.id.email);
        Button register = findViewById(R.id.register);
        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String usernameStr = username.getText().toString();
                String passwordStr = password.getText().toString();
                String emailStr = email.getText().toString();
                username.setError(null);

                if (Validation.isUsernameValid(usernameStr) && Validation.isPasswordValid(passwordStr) && Validation.isEmailValid(emailStr)) {
                    Toast.makeText(RegisterActivity.this, "Prisijungimo vardas: " + usernameStr + "\nSlaptažodis: " + passwordStr + "\nEl. paštas: "+ emailStr, Toast.LENGTH_LONG).show();
                    Intent goToLoginActivity = new Intent(RegisterActivity.this, LoginActivity.class); //parametrai: iš kur (visad su this, nes šita klasė), į kur (visad su class)
                    startActivity(goToLoginActivity);
                }
                else {
                    username.setError(getResources().getString(R.string.register_invalid_credentials));
                    username.requestFocus();
                }
            }
        });
    }
}