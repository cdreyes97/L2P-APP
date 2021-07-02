package com.example.l2p_app;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import com.example.l2p_app.models.Room;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class RoomDetail extends AppCompatActivity {

    private Room room;
    private DatabaseReference db;
    private TextView roomName, roomOwner, roomDescription;
    private ListView membersListView;
    private Button joinRoomBtn, editRoomBtn, deleteRoomBtn, viewRequestBtn;
    private String gameName, roomUID, ownerUID;
    private ArrayAdapter<String> adapter;
    private ArrayList<String> members;
    private FirebaseAuth firebaseAuth;
    private FragmentManager manager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_room_content);


        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        manager = getSupportFragmentManager();

        room = (Room) getIntent().getExtras().getSerializable("room");

        setTitle(room.getName());

        roomName = findViewById(R.id.roomName);
        roomOwner = findViewById(R.id.roomOwner);
        roomDescription = findViewById(R.id.roomDescription);
        membersListView = findViewById(R.id.listOfMembers);
        joinRoomBtn = findViewById(R.id.joinRoomBtn);
        editRoomBtn = findViewById(R.id.editBtn);
        deleteRoomBtn = findViewById(R.id.deleteBtn);
        viewRequestBtn = findViewById(R.id.viewRequestBtn);
        firebaseAuth = FirebaseAuth.getInstance();

        if (room.getOwnerUID().equals(firebaseAuth.getCurrentUser().getUid())) {
            joinRoomBtn.setVisibility(View.GONE);
            editRoomBtn.setVisibility(View.VISIBLE);
            viewRequestBtn.setVisibility(View.VISIBLE);
            deleteRoomBtn.setVisibility(View.GONE);
        } else {
            joinRoomBtn.setVisibility(View.VISIBLE);
            editRoomBtn.setVisibility(View.GONE);
            viewRequestBtn.setVisibility(View.GONE);
            deleteRoomBtn.setVisibility(View.GONE);
        }

        Log.d("room name", room.getUID());
        room.setUID(room.getUID());
        roomName.setText(room.getName());
        roomDescription.setText(room.getDescription());
        roomOwner.setText(room.getOwnerName());

        members = new ArrayList<>();

        adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, members);

        membersListView.setAdapter(adapter);

        db = FirebaseDatabase.getInstance().getReference("room_participants/" + room.getUID() + "/participants");

        db.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                for (DataSnapshot ds : snapshot.getChildren()) {
                    String member = ds.getValue().toString();
                    Log.d("member", member);
                    members.add(member);
                    adapter.notifyDataSetChanged();
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        joinRoomBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle bundle = new Bundle();
                bundle.putString("game", room.getGame());
                bundle.putString("roomUID", room.getUID());
                bundle.putString("roomOwnerName", room.getOwnerName());
                bundle.putString("roomOwnerUID", room.getOwnerUID());
                SendRequest fragment = new SendRequest();
                fragment.setArguments(bundle);
                fragment.show(getSupportFragmentManager(), "Solicitud");
                //getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, fragment).commit();
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