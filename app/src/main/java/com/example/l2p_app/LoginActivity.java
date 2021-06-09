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
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class LoginActivity extends AppCompatActivity {

    private EditText name;
    private EditText password;
    private Button loginButton;
    private TextView userRegistration;
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


        /*if(sp.getBoolean("logged",false)){
            startActivity(new Intent(LoginActivity.this, MainActivity.class));
            finish();
        }*/

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
                    Toast.makeText(LoginActivity.this, "Ingreso correcto", Toast.LENGTH_SHORT).show();
                    startActivity(new Intent(LoginActivity.this, MainActivity.class));
                    finish();
                    //checkEmailVerification();
                }else{
                    Toast.makeText(LoginActivity.this, "Ingreso Fallido", Toast.LENGTH_SHORT).show();
                }
            }
        });

    }


    /*private void checkEmailVerification(){
        FirebaseUser firebaseUser = firebaseAuth.getInstance().getCurrentUser();
        Boolean emailflag = firebaseUser.isEmailVerified();

        startActivity(new Intent(LoginActivity.this, MainActivity.class));
        finish();

//        if(emailflag){
//            finish();
//            startActivity(new Intent(MainActivity.this, SecondActivity.class));
//        }else{
//            Toast.makeText(this, "Verify your email", Toast.LENGTH_SHORT).show();
//            firebaseAuth.signOut();
//        }
    }*/
}