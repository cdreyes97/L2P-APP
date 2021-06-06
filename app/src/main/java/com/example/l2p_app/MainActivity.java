package com.example.l2p_app;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import com.example.l2p_app.databinding.NavHeaderMainBinding;
import com.google.android.material.navigation.NavigationView;
import com.google.android.material.snackbar.Snackbar;

import androidx.appcompat.app.AppCompatActivity;

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

import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import org.w3c.dom.Text;

public class MainActivity extends AppCompatActivity {

    private FirebaseAuth firebaseAuth;
    private AppBarConfiguration appBarConfiguration;
    private ActivityMainBinding binding;
    private NavHeaderMainBinding navHeaderMainBinding;


    TextView navUsername;
    ImageView navProfilePic;
    TextView navEmail;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        firebaseAuth = FirebaseAuth.getInstance();
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        navHeaderMainBinding = NavHeaderMainBinding.inflate(getLayoutInflater());
        binding.navView.addHeaderView(navHeaderMainBinding.getRoot());
        setContentView(binding.getRoot());


        navHeaderMainBinding.headerUsername.setText(user.getDisplayName());
        navHeaderMainBinding.headerEmail.setText(user.getEmail());


        setSupportActionBar(binding.appBarMain.toolbar);

        NavigationView navigationView = binding.navView;
        DrawerLayout drawer = binding.drawerLayout;


        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_content_main);
        appBarConfiguration = new AppBarConfiguration.Builder(R.id.nav_home).setOpenableLayout(drawer).build();
        NavigationUI.setupActionBarWithNavController(this, navController, appBarConfiguration);
        NavigationUI.setupWithNavController(navigationView, navController);



        navigationView.getMenu().findItem(R.id.logout).setOnMenuItemClickListener(menuItem -> {
            firebaseAuth.signOut();
            startActivity(new Intent(MainActivity.this, LoginActivity.class));
            finish();
            return true;
        });
        /*binding.appBarMain.fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                navController.navigate(R.id.action_global_roomCreation);
            }
        });*/

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

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
            }
            default:
                Log.d("id", Integer.toString(id));
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



}