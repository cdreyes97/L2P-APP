package com.example.l2p_app;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.MenuItem;

import com.example.l2p_app.adapters.MyRequestsAdapter;
import com.example.l2p_app.models.MyRequest;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class MyRequests extends AppCompatActivity {

    private RecyclerView rv;
    private DatabaseReference db;
    private FirebaseAuth firebaseAuth;
    private ArrayList<MyRequest> requests;
    private MyRequestsAdapter adapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_requests);

        setTitle("Mis Solicitudes");

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        firebaseAuth = FirebaseAuth.getInstance();

        String userUID = firebaseAuth.getCurrentUser().getUid();

        rv = findViewById(R.id.requestsList);
        db = FirebaseDatabase.getInstance().getReference("request_per_users/" + userUID);
        rv.setLayoutManager(new LinearLayoutManager(this));

        requests = new ArrayList<>();
        adapter = new MyRequestsAdapter(this, requests);

        rv.setAdapter(adapter);

        db.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                requests.clear();
                for (DataSnapshot ds : snapshot.getChildren()) {
                    MyRequest request = ds.getValue(MyRequest.class);
                    requests.add(request);
                }
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        switch (item.getItemId()) {
            case android.R.id.home:
                this.finish();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }
}