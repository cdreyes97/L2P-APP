package com.example.l2p_app;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavDirections;
import androidx.navigation.fragment.NavHostFragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.l2p_app.adapters.RoomAdapter;
import com.example.l2p_app.databinding.FragmentRoomsBinding;
import com.example.l2p_app.models.Room;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;


public class RoomsFragment extends Fragment {

    private View roomsList;
    private RecyclerView rv;
    private DatabaseReference db;
    private ArrayList<Room> rooms;
    private CardView card;
    private FragmentRoomsBinding binding;
    private String gameName;

    public RoomsFragment() {
        // Required empty public constructor
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        gameName = RoomsFragmentArgs.fromBundle(getArguments()).getGame();
        Log.d("game", gameName);


        binding = FragmentRoomsBinding.inflate(inflater, container, false);

        //roomsList = inflater.inflate(R.layout.fragment_rooms, container, false);

        rooms = new ArrayList<>();

        rv = binding.roomsListFragment;
        rv.setLayoutManager(new LinearLayoutManager(getContext()));

        db = FirebaseDatabase.getInstance().getReference("Rooms/" + gameName);
        db.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                for (DataSnapshot ds : snapshot.getChildren()) {
                    Room room = ds.getValue(Room.class);
                    room.setUID(ds.getKey());
                    rooms.add(room);
                    //Log.d("UID", room.getUID());
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });


        return binding.getRoot();
    }


    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }



    @Override
    public void onStart() {
        super.onStart();
        FirebaseRecyclerOptions options =
                new FirebaseRecyclerOptions.Builder<Room>()
                .setQuery(db, Room.class)
                .build();

        FirebaseRecyclerAdapter<Room, MyViewHolder> adapter = new FirebaseRecyclerAdapter<Room, MyViewHolder>(options) {
            @Override
            protected void onBindViewHolder(@NonNull RoomsFragment.MyViewHolder holder, int position, @NonNull Room model) {
                Room room = rooms.get(position);
                holder.name.setText(room.getName());
                holder.description.setText(room.getDescription());
                holder.roomUID.setText(room.getUID());
                holder.roomOwner.setText(room.getOwnerName());


            }

            @NonNull
            @Override
            public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.room_item, parent, false);
                return new MyViewHolder(v);
            }
        };

        rv.setAdapter(adapter);
        adapter.startListening();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {

        private TextView name, description, roomUID, roomOwner;
        public View view;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            view = itemView;
            name = itemView.findViewById(R.id.roomName);
            description = itemView.findViewById(R.id.roomDesc);
            roomUID = itemView.findViewById(R.id.roomUID);
            roomOwner = itemView.findViewById(R.id.roomOwner);


            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //Log.d("Card clicked", "yes");
                    RoomsFragmentDirections.ActionNavRoomsToRoomContent action;
                    String stringRoomUID = roomUID.getText().toString();
                    String stringRoomOwner = roomOwner.getText().toString();
                    action = RoomsFragmentDirections.actionNavRoomsToRoomContent(stringRoomUID, gameName);
                    NavHostFragment.findNavController(RoomsFragment.this)
                            .navigate((NavDirections) action);


                }
            });
        }

    }



}