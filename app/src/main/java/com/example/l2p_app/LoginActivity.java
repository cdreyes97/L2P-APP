package com.example.l2p_app;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class LoginActivity extends AppCompatActivity {

    SharedPreferences sp;

    private EditText name;
    private EditText password;
    private Button loginButton;

    String userName = "";
    String userPassword = "";

    /* Class to hold credentials */
    class Credentials
    {
        String name = "Admin";
        String password = "123456";
    }

    boolean isValid = false;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        /* Bind the XML views to Java Code Elements */
        name = findViewById(R.id.name);
        password = findViewById(R.id.password);
        loginButton = findViewById(R.id.btnLogin);


        sp = getSharedPreferences("login",MODE_PRIVATE);

        if(sp.getBoolean("logged",false)){
            startActivity(new Intent(LoginActivity.this, MainActivity.class));
        }

        /* Describe the logic when the login button is clicked */
        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                /* Obtain user inputs */
                userName = name.getText().toString();
                userPassword = password.getText().toString();

                /* Check if the user inputs are empty */
                if(userName.isEmpty() || userPassword.isEmpty())
                {
                    /* Display a message toast to user to enter the details */
                    Toast.makeText(LoginActivity.this, "Porfavor ingrese nombre y contraseña!", Toast.LENGTH_LONG).show();

                }else {

                    /* Validate the user inputs */
                    isValid = validate(userName, userPassword);

                    /* Validate the user inputs */

                    /* If not valid */
                    if (!isValid) {

                        /* Display error message */
                        Toast.makeText(LoginActivity.this, "Credenciales incorrectas, porfavor intente nuevamente!", Toast.LENGTH_LONG).show();
                    }
                    /* If valid */
                    else {

                        /* Allow the user in to your app by going into the next activity */
                        startActivity(new Intent(LoginActivity.this, MainActivity.class));

                        sp.edit().putBoolean("logged",true).apply();
                    }

                }
            }
        });
    }


    /* Validate the credentials */
    private boolean validate(String userName, String userPassword)
    {
        /* Get the object of Credentials class */
        Credentials credentials = new Credentials();

        /* Check the credentials */
        if(userName.equals(credentials.name) && userPassword.equals(credentials.password))
        {
            return true;
        }

        return false;
    }
}