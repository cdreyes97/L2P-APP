package com.example.l2p_app;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;

import com.example.l2p_app.adapters.RoomRequestAdapter;
import com.example.l2p_app.models.MyRequest;
import com.example.l2p_app.models.Request;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class RoomRequestActivity extends AppCompatActivity {

    private String roomUID, game;
    private RecyclerView rv;
    private DatabaseReference db;
    private RoomRequestAdapter adapter;
    private ArrayList<Request> requests;
    private FirebaseAuth firebaseAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_room_request);

        Intent intent = this.getIntent();

        roomUID = intent.getStringExtra("roomUID");
        game = intent.getStringExtra("game");

        rv = findViewById(R.id.roomRequestsList);
        rv.setLayoutManager(new LinearLayoutManager(this));
        requests = new ArrayList<>();
        adapter = new RoomRequestAdapter(this, requests, game);

        rv.setAdapter(adapter);

        db = FirebaseDatabase.getInstance().getReference("request_per_room/" + game + "/" + roomUID);

        db.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    for (DataSnapshot ds : snapshot.getChildren()) {
                        Request request = ds.getValue(Request.class);
                        request.setRequestUID(ds.getKey());
                        requests.add(request);
                    }
                    adapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }

}