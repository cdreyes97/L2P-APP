package com.example.l2p_app;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.navigation.fragment.NavHostFragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;

import com.example.l2p_app.models.Room;
import com.example.l2p_app.models.User;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;


public class RoomCreation extends Fragment {

    private View roomCreationView;
    private TextInputLayout nameInput = null;
    private TextInputLayout descriptionInput = null;
    private Spinner  gameInput = null;
    private TextInputLayout playersInput = null;
    private DatabaseReference db;
    private FirebaseAuth firebaseAuth;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // Inflate the layout for this fragment

        roomCreationView = inflater.inflate(R.layout.fragment_room_creation, container, false);


        nameInput =  roomCreationView.findViewById(R.id.form_name);
        descriptionInput = roomCreationView.findViewById(R.id.form_description);
        gameInput =  roomCreationView.findViewById(R.id.form_games);
        playersInput = roomCreationView.findViewById(R.id.form_players);



        return  roomCreationView;
    }

    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

         roomCreationView.findViewById(R.id.form_button).setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {



                firebaseAuth = FirebaseAuth.getInstance();

                String[] games = new String[] {"Valorant", "Overwatch", "League of Legends", "Rocket League"};
                String name = nameInput.getEditText().getText().toString();
                String game = gameInput.getSelectedItem().toString();
//                String description = descriptionInput.getText().toString();
                String description = descriptionInput.getEditText().getText().toString();
                String ownerUID = firebaseAuth.getCurrentUser().getUid();
                String ownerName = firebaseAuth.getCurrentUser().getDisplayName();
//                Integer numPlayers = Integer.parseInt(playersInput.getText().toString());
                Integer numPlayers = Integer.parseInt(playersInput.getEditText().getText().toString());

                db = FirebaseDatabase.getInstance().getReference("Rooms/" + game);

                DatabaseReference newRoom = db.push();
                newRoom.setValue(new Room(name, ownerUID, ownerName, game, description, numPlayers,1));
                String roomKey = newRoom.getKey();

                //User user = new User("a@a.com","Admin");

                db = FirebaseDatabase.getInstance().getReference("room_participants");
                DatabaseReference newRoomParticipants = db.child(roomKey);
                newRoomParticipants.child("game").setValue(game);
                newRoomParticipants.child("roomName").setValue(name);
                newRoomParticipants.child("participants").child(ownerUID).setValue(ownerName);


                db = FirebaseDatabase.getInstance().getReference("room_owner");
                DatabaseReference newRoomOwner = db.child(ownerUID).child(game).child(roomKey);
                newRoomOwner.setValue(name);

                //Log.d("a",nameInput.getText().toString());
                //Log.d("a",gameInput.getSelectedItem().toString());
                Log.d("Creating Room", roomKey);

                NavHostFragment.findNavController(RoomCreation.this).popBackStack();

            }
        });
        roomCreationView.findViewById(R.id.form_button_cancel).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                NavHostFragment.findNavController(RoomCreation.this).popBackStack();
            }
        });

    }




}