package com.example.l2p_app;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.example.l2p_app.databinding.FragmentRoomContentBinding;
import com.example.l2p_app.models.Room;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class RoomContent extends Fragment {

    private FragmentRoomContentBinding binding;
    private Room room;
    private View roomView;
    private DatabaseReference db;
    private TextView roomName, roomOwner, roomDescription;
    private String gameName;
    private String roomUID;
    private ArrayAdapter<Room> adapter;

    public RoomContent() {
        // Required empty public constructor
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        binding = FragmentRoomContentBinding.inflate(inflater, container, false);

        roomName = binding.roomName;
        roomDescription = binding.roomDescription;
        roomOwner = binding.roomOwner;




        gameName = RoomContentArgs.fromBundle(getArguments()).getGame();
        roomUID = RoomContentArgs.fromBundle(getArguments()).getUID();

        Log.d("Game recieved", gameName);
        Log.d("Room UID", roomUID);

        db = FirebaseDatabase.getInstance().getReference("Rooms/" + gameName + "/" + roomUID);

        db.addValueEventListener(new ValueEventListener() {

            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                room = new Room();
                room = snapshot.getValue(Room.class);
                room.setUID(snapshot.getKey());
                roomName.setText(room.getName());
                roomDescription.setText(room.getDescription());
                Log.d("snapshot", room.getUID());

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        //Log.d("snapshot", room.getUID());

        //roomName.setText(room.getName());
        //roomDescription.setText(room.getDescription());

        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }
}