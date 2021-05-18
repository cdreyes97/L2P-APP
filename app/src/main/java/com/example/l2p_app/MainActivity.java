package com.example.l2p_app;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ListView;

public class MainActivity extends AppCompatActivity {

    String[] COUNTRIES = { "Argentina", "Bolivia","Chile"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ArrayAdapter adaptador = new ArrayAdapter(this, R.layout.landing_activity, COUNTRIES);

        ListView gamelist = findViewById(R.id.gamelist);

        gamelist.setAdapter(adaptador);
    }
}