package com.example.l2p_app;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;

import com.bumptech.glide.Glide;
import com.example.l2p_app.databinding.NavHeaderMainBinding;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.navigation.NavigationView;
import com.google.android.material.snackbar.Snackbar;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.provider.MediaStore;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;

import androidx.drawerlayout.widget.DrawerLayout;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.fragment.NavHostFragment;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.example.l2p_app.databinding.ActivityMainBinding;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.messaging.FirebaseMessaging;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import org.w3c.dom.Text;

import java.io.IOException;

public class MainActivity extends AppCompatActivity {

    private FirebaseAuth firebaseAuth;
    private AppBarConfiguration appBarConfiguration;
    private ActivityMainBinding binding;
    private NavHeaderMainBinding navHeaderMainBinding;
    private FirebaseUser user;
    private FirebaseStorage firebaseStorage;
    private FirebaseDatabase firebaseDatabase;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //FirebaseMessaging.getInstance().subscribeToTopic("userABC");

        firebaseAuth = FirebaseAuth.getInstance();
        user = FirebaseAuth.getInstance().getCurrentUser();
        firebaseStorage = FirebaseStorage.getInstance();
        firebaseDatabase = FirebaseDatabase.getInstance();

        DatabaseReference db = firebaseDatabase.getReference("Users");
        final DatabaseReference userReference = db.child(firebaseAuth.getUid());

        if(user== null){
            startActivity(new Intent(MainActivity.this, LoginActivity.class));
            finish();
        }
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        navHeaderMainBinding = NavHeaderMainBinding.inflate(getLayoutInflater());
        binding.navView.addHeaderView(navHeaderMainBinding.getRoot());
        setContentView(binding.getRoot());


        userReference.get().addOnSuccessListener(new OnSuccessListener<DataSnapshot>() {
            @Override
            public void onSuccess(DataSnapshot dataSnapshot) {

                navHeaderMainBinding.headerUsername.setText(dataSnapshot.child("name").getValue().toString());
                navHeaderMainBinding.headerEmail.setText(dataSnapshot.child("email").getValue().toString());
            }
        });

        setSupportActionBar(binding.appBarMain.toolbar);

        NavigationView navigationView = binding.navView;
        DrawerLayout drawer = binding.drawerLayout;

        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_content_main);
        appBarConfiguration = new AppBarConfiguration.Builder(R.id.nav_home).setOpenableLayout(drawer).build();
        NavigationUI.setupActionBarWithNavController(this, navController, appBarConfiguration);
        NavigationUI.setupWithNavController(navigationView, navController);

        StorageReference storageReference = firebaseStorage.getReference();

        storageReference.child(firebaseAuth.getUid()).child("Images/Profile Pic").getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri) {
                Glide.with(MainActivity.this).load(uri).centerCrop().into(navHeaderMainBinding.headerImage);
            }
        });

        navigationView.getMenu().findItem(R.id.logout).setOnMenuItemClickListener(menuItem -> {
            firebaseAuth.signOut();
            startActivity(new Intent(MainActivity.this, LoginActivity.class));
            finish();
            return true;
        });

        navHeaderMainBinding.headerLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                navController.navigate(R.id.action_global_profileFragment);
            }
        });
        /*binding.appBarMain.fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                navController.navigate(R.id.action_global_roomCreation);
            }
        });*/

        //Notificaciones Obtener token FCM
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
                  //Toast.makeText(MainActivity.this, msg, Toast.LENGTH_SHORT).show();
                }
            });

    }



    /*@Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }*/

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        Log.d("a","no funciona");
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        switch (id){
            case R.id.logout: {
                Log.d("a","no funciona");
                Logout();
                break;
            }
            default:
                Log.d("id", Integer.toString(id));
                break;
        }

        /*//noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }*/
        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onSupportNavigateUp() {
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_content_main);
        return NavigationUI.navigateUp(navController, appBarConfiguration)
                || super.onSupportNavigateUp();
    }


    private void Logout(){
        firebaseAuth.signOut();
        startActivity(new Intent(MainActivity.this, LoginActivity.class));
        finish();
    }

    public void TEST(String name, Uri imagePath){
        final StorageReference storageReference = firebaseStorage.getReference();
        navHeaderMainBinding.headerUsername.setText(name);
        if(imagePath != null) {
            try {
                navHeaderMainBinding.headerImage.setImageBitmap(MediaStore.Images.Media.getBitmap(getContentResolver(),imagePath));
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }


}