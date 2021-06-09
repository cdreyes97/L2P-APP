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
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;


public class RoomCreation extends Fragment {

    private View roomCreationView;
    private EditText nameInput = null;
    private EditText descriptionInput = null;
    private Spinner  gameInput = null;
    private Spinner playersInput = null;
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

                String name = nameInput.getText().toString();
                String game = gameInput.getSelectedItem().toString();
                String description = descriptionInput.getText().toString();
                String ownerUID = firebaseAuth.getCurrentUser().getUid();
                String ownerName = firebaseAuth.getCurrentUser().getDisplayName();

                db = FirebaseDatabase.getInstance().getReference("Rooms/" + game);

                DatabaseReference newRoom = db.push();
                newRoom.setValue(new Room(name, ownerUID, ownerName, game, description, 5,1));
                String roomKey = newRoom.getKey();

                //User user = new User("a@a.com","Admin");

                db = FirebaseDatabase.getInstance().getReference("room_participants");
                DatabaseReference newRoomParticipants = db.child(roomKey).child(ownerUID);
                newRoomParticipants.setValue(ownerName);


                db = FirebaseDatabase.getInstance().getReference("room_owner");
                DatabaseReference newRoomOwner = db.child(ownerUID).child(roomKey);
                newRoomOwner.setValue(name);

                //Log.d("a",nameInput.getText().toString());
                //Log.d("a",gameInput.getSelectedItem().toString());
                Log.d("Creating Room", roomKey);
            }
        });

    }


}