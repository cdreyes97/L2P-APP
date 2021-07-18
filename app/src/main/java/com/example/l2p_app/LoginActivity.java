package com.example.l2p_app;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class LoginActivity extends AppCompatActivity {

    private TextInputEditText name;
    private TextInputEditText password;
    private Button loginButton;
    private MaterialButton userRegistration;
    private ProgressBar pgsBar;

    private FirebaseAuth firebaseAuth;

    String userName = "";
    String userPassword = "";

    boolean isValid = false;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        /* Bind the XML views to Java Code Elements */
        name = findViewById(R.id.name);
        password = findViewById(R.id.password);
        loginButton = findViewById(R.id.btnLogin);
        userRegistration = findViewById(R.id.register);
        pgsBar = findViewById(R.id.pBar);


        firebaseAuth = FirebaseAuth.getInstance();

        FirebaseUser user = firebaseAuth.getCurrentUser();




        //Log.d("A",user.toString());
        if(user != null) {
            startActivity(new Intent(LoginActivity.this, MainActivity.class));
            finish();
        }

        /* Describe the logic when the login button is clicked */
        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                pgsBar.setVisibility(View.VISIBLE);
                validate(name.getText().toString(), password.getText().toString());
            }
        });

        userRegistration.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(LoginActivity.this, RegistrationActivity.class));
                finish();
            }
        });

    }


    /* Validate the credentials */
    private void validate(String userName, String userPassword) {

        if(userName.isEmpty() || userPassword.isEmpty()){
            Toast.makeText(LoginActivity.this, "Porfavor ingrese todas las credenciales", Toast.LENGTH_SHORT).show();
            pgsBar.setVisibility(View.GONE);
            return;
        }
        firebaseAuth.signInWithEmailAndPassword(userName, userPassword).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if(task.isSuccessful()){
                    checkIfEmailVerified();
                    /*Toast.makeText(LoginActivity.this, "Ingreso correcto", Toast.LENGTH_SHORT).show();
                    startActivity(new Intent(LoginActivity.this, MainActivity.class));
                    finish();*/
                    //checkEmailVerification();
                }else{
                    Toast.makeText(LoginActivity.this, "Ingreso Fallido", Toast.LENGTH_SHORT).show();
                }
            }
        });

    }


    private void checkIfEmailVerified()
    {
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

        if (user.isEmailVerified())
        {
            Toast.makeText(LoginActivity.this, "Ingreso correcto", Toast.LENGTH_SHORT).show();
            startActivity(new Intent(LoginActivity.this, MainActivity.class));
            finish();
        }
        else
        {
            // email is not verified, so just prompt the message to the user and restart this activity.
            // NOTE: don't forget to log out the user.
            Toast.makeText(this, "Por favor verifique su email", Toast.LENGTH_SHORT).show();
            pgsBar.setVisibility(View.INVISIBLE);
            sendVerificationEmail();
            FirebaseAuth.getInstance().signOut();

            //restart this activity

        }
    }

    private void sendVerificationEmail()
    {
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

        user.sendEmailVerification()
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            // email sent
                        }
                        else
                        {
                            // email not sent, so display message and restart the activity or do whatever you wish to do

                            //restart this activity
                            overridePendingTransition(0, 0);
                            finish();
                            overridePendingTransition(0, 0);
                            startActivity(getIntent());

                        }
                    }
                });
    }

}