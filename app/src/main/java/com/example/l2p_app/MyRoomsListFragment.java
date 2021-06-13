package com.example.l2p_app;

import android.os.Bundle;

import androidx.annotation.AnyRes;
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

import com.example.l2p_app.databinding.FragmentMyRoomListBinding;
import com.example.l2p_app.databinding.FragmentRoomsBinding;
import com.example.l2p_app.models.Room;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;



public class MyRoomsListFragment extends Fragment {

    private View roomsList;
    private RecyclerView rv;
    private DatabaseReference db;
    private ArrayList<Room> rooms;
    private CardView card;
    private FragmentMyRoomListBinding binding;
    private FirebaseAuth firebaseAuth;
    private String gameName;
    private int tabNumber;
    private String userUID;

    public MyRoomsListFragment(int position) {
        this.tabNumber = position;
        // Required empty public constructor
    }
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {


        firebaseAuth = FirebaseAuth.getInstance();
        userUID = firebaseAuth.getCurrentUser().getUid();
        userUID = "h45rDL5PZBQqVtaqRyJwo8I4uED3";
        if(this.tabNumber == 0){
            db = FirebaseDatabase.getInstance().getReference("room_owner/" + userUID);
            db = FirebaseDatabase.getInstance().getReference("room_owner/h45rDL5PZBQqVtaqRyJwo8I4uED3");
            Log.d("POSITION", "BUSCAR MIS SALAS");
        }
        else if(this.tabNumber == 1){
            db = FirebaseDatabase.getInstance().getReference("room_participants");
            Log.d("POSITION", "BUSCAR SALAS DONDE PARTICIPO");
        }


        binding = FragmentMyRoomListBinding.inflate(inflater, container, false);

        rooms = new ArrayList<>();

        rv = binding.myRoomsListFragment;
        rv.setLayoutManager(new LinearLayoutManager(getContext()));

        db.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                if(tabNumber == 0 ) {
                    for (DataSnapshot ds : snapshot.getChildren()) {

                        String roomName = ds.getValue().toString();
                        Room room = new Room();
                        room.setName(roomName);
                        room.setUID(ds.getKey());
                        Log.d("OBTENER SALA", ds.getValue().toString());
                        //Log.d("OBTENER SALA",ds.getValue().toString());
                        rooms.add(room);
                        Log.d("UID", room.getUID());

                    }
                }else if(tabNumber == 1){
                    for (DataSnapshot ds : snapshot.getChildren()) {
                        if (ds.hasChild(userUID)){
                            Log.d("HAS", "TIENE al usuario cdreyes");
                            Room room = new Room();
                            ds.child("participants");
                            //room.setName();
                            //room.setUID(ds.getKey());


                        }
                        //String roomName = ds.getValue().toString();
                        //Room room = new Room();
                        //room.setName(roomName);
                        //room.setUID(ds.getKey());
                        Log.d("OBTENER SALA tab 1","sala: "+ ds.getKey() + " participantes: " + ds.getValue().toString());
                        Log.d("VALOR OBJECTO DS",ds.getValue().toString());
                        Log.d("OBJECTO DS",ds.toString());

                        //rooms.add(room);
                        //Log.d("UID", room.getUID());
                    }

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
                new FirebaseRecyclerOptions.Builder<Object>()
                        .setQuery(db, Object.class)
                        .build();

        FirebaseRecyclerAdapter<Object, MyViewHolder> adapter = new FirebaseRecyclerAdapter<Object, MyViewHolder>(options) {
            @Override
            protected void onBindViewHolder(@NonNull MyRoomsListFragment.MyViewHolder holder, int position, @NonNull Object model) {
                Room room = rooms.get(position);
                //String room = rooms.get(position);
                holder.name.setText(room.getName());
                //holder.description.setText(room.getDescription());
                holder.roomUID.setText(room.getUID());
                //holder.roomOwner.setText(room.getOwnerName());
                //holder.ownerUID.setText(room.getOwnerUID());

            }

            @NonNull
            @Override
            public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.my_room_item, parent, false);
                return new MyRoomsListFragment.MyViewHolder(v);
            }
        };

        rv.setAdapter(adapter);
        adapter.startListening();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {

        private TextView name, description, roomUID, roomOwner, ownerUID;
        public View view;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            view = itemView;
            name = itemView.findViewById(R.id.myRoomName);
            //description = itemView.findViewById(R.id.roomDesc);
            roomUID = itemView.findViewById(R.id.myRoomUID);
            //roomOwner = itemView.findViewById(R.id.roomOwner);
            //ownerUID = itemView.findViewById(R.id.ownerUID);


            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Log.d("Card clicked", "yes");
                    /*RoomsFragmentDirections.ActionNavRoomsToRoomContent action;
                    String stringRoomUID = roomUID.getText().toString();
                    String stringRoomOwner = ownerUID.getText().toString();
                    action = RoomsFragmentDirections.actionNavRoomsToRoomContent(stringRoomUID, gameName, stringRoomOwner);
                    NavHostFragment.findNavController(MyRoomsListFragment.this)
                            .navigate((NavDirections) action);*/


                }
            });
        }

    }
}