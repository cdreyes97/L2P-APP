package com.example.l2p_app;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.l2p_app.adapters.MyRoomsParticipantAdapter;
import com.example.l2p_app.databinding.FragmentRoomsInBinding;
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


public class RoomsInFragment extends Fragment {
    private View roomsList;
    private RecyclerView rv;
    private DatabaseReference db;
    private ArrayList<Room> rooms;
    private CardView card;
    private FragmentRoomsInBinding binding;
    private FirebaseAuth firebaseAuth;
    private String gameName;
    private int tabNumber;
    private String userUID;
    private MyRoomsParticipantAdapter adapter;


    public RoomsInFragment() {
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
        db = FirebaseDatabase.getInstance().getReference("room_participants");
        Log.d("POSITION", "BUSCAR SALAS DONDE PARTICIPO");


        binding = FragmentRoomsInBinding.inflate(inflater, container, false);


        rooms = new ArrayList<>();

        rv = binding.RoomsInRv;
        rv.setHasFixedSize(true);
        rv.setLayoutManager(new LinearLayoutManager(getContext()));

        adapter = new  MyRoomsParticipantAdapter(getContext(), rooms);

        rv.setAdapter(adapter);

        db.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                rooms.clear();
                for (DataSnapshot ds : snapshot.getChildren()) {
                    String roomUID = ds.getKey();
                    String gameName = ds.child("game").getValue().toString();
                    DataSnapshot participants = ds.child("participants");
                    if (participants.hasChild(userUID)){
                        Log.d("HAS", "TIENE al usuario cdreyes");
                        Log.d("ROOMUID", roomUID);
                        DatabaseReference roomReference = FirebaseDatabase.getInstance().getReference("Rooms/"+gameName+"/"+roomUID);
                        roomReference.addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot snapshot) {
                                if (snapshot.exists()){
                                    Room roomObject = snapshot.getValue(Room.class);
                                    roomObject.setUID(roomUID);
                                    rooms.add(roomObject);
                                    adapter.notifyDataSetChanged();
                                }
                            }

                            @Override
                            public void onCancelled(@NonNull  DatabaseError error) {

                            }
                        });

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
    public void onResume() {
        super.onResume();
        adapter.notifyDataSetChanged();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }


}