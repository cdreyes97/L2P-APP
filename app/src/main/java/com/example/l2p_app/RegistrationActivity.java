package com.example.l2p_app;

import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.ContentResolver;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.l2p_app.databinding.ActivityRegistrationBinding;
import com.example.l2p_app.models.User;
import com.google.android.gms.auth.api.signin.internal.Storage;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.snackbar.BaseTransientBottomBar;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.messaging.FirebaseMessaging;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.IOException;

public class RegistrationActivity extends AppCompatActivity {

    private EditText userName, userPassword, userEmail;
    private Button regButton;
    private TextView userLogin;
    private ImageView userProfilePic;
    private ActivityRegistrationBinding binding;
    private FirebaseAuth firebaseAuth;
    private FirebaseStorage firebaseStorage;
    private StorageReference storageReference;
    private FirebaseAuth.AuthStateListener mAuthListener;
    private Snackbar mySnackbar;

    Uri imagePath;
    String email, name, password;


    ActivityResultLauncher mGetContent = registerForActivityResult(new ActivityResultContracts.GetContent(),
            new ActivityResultCallback<Uri>() {
                @Override
                public void onActivityResult(Uri uri) {
                    try {
                        if(uri != null){
                            imagePath = uri;
                            Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(),uri);
                            userProfilePic.setImageBitmap(bitmap);
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    // Handle the returned Uri
                }
            });

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);
        setupUIViews();

        binding = ActivityRegistrationBinding.inflate(getLayoutInflater());

        firebaseAuth = FirebaseAuth.getInstance();
        firebaseStorage = FirebaseStorage.getInstance();

        storageReference = firebaseStorage.getReference();

        //mySnackbar = Snackbar.make(binding.getRoot(), "emailSended", 1000000);

        userProfilePic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mGetContent.launch("image/*");

            }
        });
        regButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(validate()){
                    //Upload data to the database
                    String user_email = userEmail.getText().toString().trim();
                    String user_password = userPassword.getText().toString().trim();
                    String user_name = userName.getText().toString();

                    UserProfileChangeRequest profileUpdates = new UserProfileChangeRequest.Builder()
                            .setDisplayName(user_name).build();


                    firebaseAuth.createUserWithEmailAndPassword(user_email, user_password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {

                            if(task.isSuccessful()){
                                //sendEmailVerification()
                                FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                                user.updateProfile(profileUpdates);
                                if(imagePath == null){
                                    imagePath = Uri.parse("android.resource://"+"com.example.l2p_app"+"/drawable/blank_profile_picture");
                                }
                                sendUserData();
                                if (user != null) {
                                    // User is signed in
                                    // NOTE: this Activity should get onpen only when the user is not signed in, otherwise
                                    // the user will receive another verification email.
                                    sendVerificationEmail();
                                } else {
                                    // User is signed out
                                    FirebaseAuth.getInstance().signOut();
                                }
                                //firebaseAuth.signOut();
                                //finish();
                                //startActivity(new Intent(RegistrationActivity.this, LoginActivity.class));*/
                            }else{
                                Toast.makeText(RegistrationActivity.this, "Registro fallido", Toast.LENGTH_SHORT).show();
                            }

                        }
                    });
                }
            }
        });


        userLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(RegistrationActivity.this, LoginActivity.class));
                finish();
            }
        });

    }


    private void setupUIViews(){
        userName = (EditText)findViewById(R.id.userName);
        userPassword = (EditText)findViewById(R.id.userPassword);
        userEmail = (EditText)findViewById(R.id.userEmail);
        regButton = (Button)findViewById(R.id.btnRegister);
        userLogin = (TextView)findViewById(R.id.userLogin);
        userProfilePic = findViewById(R.id.regProfilePicture);
    }

    private Boolean validate(){
        Boolean result = false;

        name = userName.getText().toString();
        password = userPassword.getText().toString();
        email = userEmail.getText().toString();


        if(name.isEmpty() || password.isEmpty() || email.isEmpty()  ){
            Toast.makeText(this, "Por favor ingrese todo los campos", Toast.LENGTH_SHORT).show();
        }else{
            result = true;
        }

        return result;
    }


    private void sendUserData(){
        FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();

        DatabaseReference db = firebaseDatabase.getReference("Users");
        DatabaseReference newUser = db.child(firebaseAuth.getUid());
        StorageReference imageReference = storageReference.child(firebaseAuth.getUid()).child("Images").child("Profile Pic");
        UploadTask uploadTask = imageReference.putFile(imagePath);
        uploadTask.addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                //Toast.makeText(RegistrationActivity.this, "Subida fallida!", Toast.LENGTH_SHORT).show();
            }
        }).addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {
                //Toast.makeText(RegistrationActivity.this, "Subida exitosa!", Toast.LENGTH_SHORT).show();
            }
        });

        FirebaseMessaging.getInstance().getToken()
                .addOnCompleteListener(new OnCompleteListener<String>() {
                    @Override
                    public void onComplete(@NonNull Task<String> task) {
                        if (!task.isSuccessful()) {
                            Log.w("Main ACtivity", "Fetching FCM registration token failed", task.getException());
                            return;
                        }

                        // Get new FCM registration token
                        String token = task.getResult();

                        // Log and toast
                        String msg = getString(R.string.msg_token_fmt, token);
                        Log.d("Main ACtivity", msg);
                        User objectUser = new User(email,name);
                        objectUser.setToken(token);
                        newUser.setValue(objectUser);
                        //Toast.makeText(MainActivity.this, msg, Toast.LENGTH_SHORT).show();
                    }
                });
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
                            /*Snackbar.make(findViewById(R.id.RegistrationCoorditanorLayout),
                                    "Se le ha enviado un correo de verificación a " + user.getEmail()
                                    ,30000).show();*/
                            Toast.makeText(RegistrationActivity.this, "Se le ha enviado un correo de verificación a " + user.getEmail(), Toast.LENGTH_LONG).show();
                            // after email is sent just logout the user and finish this activity
                            FirebaseAuth.getInstance().signOut();
                            startActivity(new Intent(RegistrationActivity.this, LoginActivity.class));
                            finish();
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