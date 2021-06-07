package com.example.l2p_app;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.l2p_app.adapters.RoomAdapter;
import com.example.l2p_app.models.Room;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class RoomsActivity extends AppCompatActivity {

    private RecyclerView rv;
    private DatabaseReference db;
    private RoomAdapter adapter;
    private ArrayList<Room> rooms;
    private String game;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rooms);

        game = getIntent().getStringExtra("game");

        Log.d("Game recieved", game);

        rv = findViewById(R.id.roomsList);
        db = FirebaseDatabase.getInstance().getReference("Rooms/Valorant");
        rv.setHasFixedSize(true);
        rv.setLayoutManager(new LinearLayoutManager(this));

        rooms = new ArrayList<>();
        adapter = new RoomAdapter(this, rooms);

        rv.setAdapter(adapter);

        db.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                for (DataSnapshot ds : snapshot.getChildren()) {
                    Room room = ds.getValue(Room.class);
                    rooms.add(room);
                }
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

}