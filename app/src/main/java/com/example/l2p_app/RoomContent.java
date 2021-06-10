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
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import com.example.l2p_app.databinding.FragmentRoomContentBinding;
import com.example.l2p_app.models.Room;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class RoomContent extends Fragment {

    private FragmentRoomContentBinding binding;
    private Room room;
    private View roomView;
    private DatabaseReference db;
    private TextView roomName, roomOwner, roomDescription;
    private ListView membersListView;
    private Button joinRoomBtn;
    private String gameName, roomUID, ownerName;
    private ArrayAdapter<String> adapter;
    private ArrayList<String> members;


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
        joinRoomBtn = binding.joinRoomBtn;
        membersListView = binding.listOfMembers;




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
                roomOwner.setText(room.getOwnerName());
                Log.d("snapshot", room.getUID());

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        members = new ArrayList<>();

        adapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_list_item_1, members);

        membersListView.setAdapter(adapter);

        db = FirebaseDatabase.getInstance().getReference("room_participants/" + roomUID);

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
                Log.d("join", "yes");
            }
        });


        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }


}