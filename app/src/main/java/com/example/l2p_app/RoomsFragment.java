package com.example.l2p_app;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

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

        roomsList = inflater.inflate(R.layout.fragment_rooms, container, false);

        rooms = new ArrayList<>();

        rv = roomsList.findViewById(R.id.roomsListFragment);
        rv.setLayoutManager(new LinearLayoutManager(getContext()));

        db = FirebaseDatabase.getInstance().getReference("Rooms/Valorant");
        db.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                for (DataSnapshot ds : snapshot.getChildren()) {
                    Room room = ds.getValue(Room.class);
                    rooms.add(room);
                }
                //adapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });


        return roomsList;
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

    public static class MyViewHolder extends RecyclerView.ViewHolder {

        private TextView name, description;
        public View view;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            view = itemView;
            name = itemView.findViewById(R.id.roomName);
            description = itemView.findViewById(R.id.roomDesc);
        }
    }

}