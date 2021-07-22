package com.example.l2p_app;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.app.Dialog;
import android.content.Intent;
import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.view.ContextMenu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.l2p_app.adapters.MyRoomsAdapter;
import com.example.l2p_app.adapters.ParticipantsAdapter;
import com.example.l2p_app.models.Room;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class RoomDetail extends AppCompatActivity {

    private Room room;
    private DatabaseReference db,participantRef;
    private TextView roomName, roomOwner, roomDescription;
    private ListView membersListView;
    private Button joinRoomBtn, leaveRoomBtn, editRoomBtn, deleteRoomBtn, viewRequestBtn;
    private String gameName, roomUID, ownerUID;
    private ArrayAdapter<String> adapter;
    private ArrayList<String> members,membersUID;
    private FirebaseAuth firebaseAuth;
    private FragmentManager manager;
    private boolean imInRoom = false;
    private String userUID;






    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_room_content);


        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        manager = getSupportFragmentManager();

        room = (Room) getIntent().getExtras().getSerializable("room");

        firebaseAuth = FirebaseAuth.getInstance();

        db = FirebaseDatabase.getInstance().getReference("room_participants/" + room.getUID() + "/participants");

        userUID = firebaseAuth.getCurrentUser().getUid();



        setTitle(room.getName());

        roomName = findViewById(R.id.roomName);
        roomOwner = findViewById(R.id.roomOwner);
        roomDescription = findViewById(R.id.roomDescription);
        membersListView = findViewById(R.id.listOfMembers);
        joinRoomBtn = findViewById(R.id.joinRoomBtn);
        leaveRoomBtn = findViewById(R.id.leaveRoomBtn);
        editRoomBtn = findViewById(R.id.editBtn);
        deleteRoomBtn = findViewById(R.id.deleteBtn);
        viewRequestBtn = findViewById(R.id.viewRequestBtn);

        members = new ArrayList<>();
        membersUID = new ArrayList<>();

        db.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                members.clear();
                membersUID.clear();
                for (DataSnapshot ds : snapshot.getChildren()) {
                    String member = ds.getValue().toString();
                    if(ds.getKey().equals(userUID)){
                        imInRoom = true;
                    }
                    members.add(member);
                    membersUID.add(ds.getKey());
                    adapter.notifyDataSetChanged();
                }
                if (room.getOwnerUID().equals(userUID)) {
                    leaveRoomBtn.setVisibility(View.GONE);
                    joinRoomBtn.setVisibility(View.GONE);
                    editRoomBtn.setVisibility(View.GONE);
                    viewRequestBtn.setVisibility(View.VISIBLE);
                    deleteRoomBtn.setVisibility(View.VISIBLE);
                } else {
                    if(imInRoom){
                        joinRoomBtn.setVisibility(View.GONE);
                        leaveRoomBtn.setVisibility(View.VISIBLE);
                    }
                    else {
                        joinRoomBtn.setVisibility(View.VISIBLE);
                        leaveRoomBtn.setVisibility(View.GONE);
                    }
                    editRoomBtn.setVisibility(View.GONE);
                    viewRequestBtn.setVisibility(View.GONE);
                    deleteRoomBtn.setVisibility(View.GONE);
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        Log.d("BOOLEAN", String.valueOf(imInRoom));



        Log.d("room name", room.getUID());
        room.setUID(room.getUID());
        roomName.setText(room.getName());
        roomDescription.setText(room.getDescription());
        roomOwner.setText(room.getOwnerName());


        adapter = new ParticipantsAdapter(this, members);//new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, members);


        membersListView.setAdapter(adapter);


        membersListView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                if(userUID.equals(membersUID.get(position))){
                    return false;
                }
                MaterialAlertDialogBuilder a = new MaterialAlertDialogBuilder(RoomDetail.this)
                        .setTitle("Escoja una opción")
                        .setItems(R.array.participantsOptions, new DialogInterface.OnClickListener(){
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                        new MaterialAlertDialogBuilder(RoomDetail.this)
                                .setTitle("Desea eliminar este participante?")
                                .setNegativeButton("No", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        dialog.dismiss();
                                    }
                                })
                                .setPositiveButton("Si", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        participantRef = FirebaseDatabase.getInstance().getReference("room_participants/"+room.getUID() + "/participants/" + membersUID.get(position));
                                        participantRef.removeValue().addOnSuccessListener(new OnSuccessListener<Void>() {
                                            @Override
                                            public void onSuccess(Void unused) {
                                                adapter.notifyDataSetChanged();
                                            }
                                        });
                                        dialog.dismiss();
                                    }
                                })
                                .show();

                    }
                });
                a.show();

                return true;
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

        viewRequestBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(RoomDetail.this, RoomRequestActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                intent.putExtra("roomUID", room.getUID());
                intent.putExtra("game", room.getGame());
                RoomDetail.this.startActivity(intent);
            }
        });

        leaveRoomBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new MaterialAlertDialogBuilder(v.getContext())
                        .setTitle("Desea salir de la sala?")
                        .setNegativeButton("No", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                            }
                        })
                        .setPositiveButton("Si", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                DatabaseReference delete = db.child(userUID);
                                delete.removeValue();
                                dialog.dismiss();
                                //Intent intent = new Intent(RoomDetail.this, MyRoomsActivity.class);
                                //.this.startActivity(intent);

                                finish();
                            }
                        })
                        .show();
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