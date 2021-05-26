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



public class RoomCreation extends Fragment {

    private View roomCreationView;
    private EditText nameInput = null;
    private EditText descriptionInput = null;
    private Spinner  gameInput = null;
    private Spinner playersInput = null;

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

                Log.d("a",nameInput.getText().toString());
                Log.d("a",gameInput.getSelectedItem().toString());
            }
        });

    }


}