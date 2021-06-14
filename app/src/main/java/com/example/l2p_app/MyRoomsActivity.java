package com.example.l2p_app;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

//import com.example.l2p_app.adapters.MyRoomsAdapter;
import com.example.l2p_app.adapters.RoomAdapter;
import com.example.l2p_app.databinding.ActivityMainBinding;
import com.example.l2p_app.databinding.ActivityMyRoomsBinding;
import com.example.l2p_app.databinding.NavHeaderMainBinding;
import com.example.l2p_app.models.Room;
import com.google.android.material.tabs.TabLayoutMediator;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class MyRoomsActivity extends AppCompatActivity {

    private RecyclerView rv;
    private DatabaseReference db;
    private ArrayList<Room> rooms;
    private String game;
    //private MyRoomsAdapter roomAdapter;
    private TextView[] dots;
    private int[] layouts;
    private ActivityMyRoomsBinding binding;
    // tab titles
    private String[] titles = new String[]{"Mis salas", "Salas donde participo"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //setContentView(R.layout.activity_my_rooms);
        binding = ActivityMyRoomsBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        getSupportActionBar().setElevation(0);

        rooms = new ArrayList<>();
        //roomAdapter = new MyRoomsAdapter(this);
        //binding.pager.setAdapter(roomAdapter);

        new TabLayoutMediator(binding.tabLayout, binding.pager,
                (tab, position) -> tab.setText(titles[position])).attach();


        // = getIntent().getStringExtra("game");

        //Log.d("Game recieved", game);

        /*rv = findViewById(R.id.roomsList);
        db = FirebaseDatabase.getInstance().getReference("Rooms/Valorant");
        rv.setHasFixedSize(true);
        rv.setLayoutManager(new LinearLayoutManager(this));


        roomAdapter = new RoomAdapter(this, rooms);

        rv.setAdapter(roomAdapter);

        db.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                for (DataSnapshot ds : snapshot.getChildren()) {
                    Room room = ds.getValue(Room.class);
                    rooms.add(room);
                }
                roomAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });*/
        //init();
    }


    /*private void init() {
        // layouts of all welcome sliders
        // add few more layouts if you want
        layouts = new int[]{
                R.layout.slide_one,
                R.layout.slide_two,
                R.layout.slide_three,
                R.layout.slide_four};

        mAdapter = new ViewsSliderAdapter();
        binding.viewPager.setAdapter(mAdapter);
        binding.viewPager.registerOnPageChangeCallback(pageChangeCallback);

        binding.btnSkip.setOnClickListener(v -> launchHomeScreen());

        binding.btnNext.setOnClickListener(v -> {
            // checking for last page
            // if last page home screen will be launched
            int current = getItem(+1);
            if (current < layouts.length) {
                // move to next screen
                binding.viewPager.setCurrentItem(current);
            } else {
                launchHomeScreen();
            }
        });

        binding.iconMore.setOnClickListener(view -> {
            showMenu(view);
        });

        // adding bottom dots
        addBottomDots(0);
    }*/
}