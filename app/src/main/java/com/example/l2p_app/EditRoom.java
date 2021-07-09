package com.example.l2p_app;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.l2p_app.databinding.FragmentEditRoomBinding;
import com.example.l2p_app.models.Room;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import static java.lang.String.valueOf;


public class EditRoom extends DialogFragment {

    private FragmentEditRoomBinding binding;
    private TextInputLayout roomName, roomDesc, roomPlayers;
    private FirebaseAuth firebaseAuth;
    private DatabaseReference db;
    private Room room;
    private Button cancelBtn, updateBtn;


    public EditRoom() {

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Bundle bundle = this.getArguments();
        room = (Room) bundle.getSerializable("room");


    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {


        new MaterialAlertDialogBuilder(getContext()).show();

        binding = FragmentEditRoomBinding.inflate(inflater, container, false);

        roomName = binding.editRoomName;
        roomDesc = binding.editRoomDesc;
        roomPlayers = binding.editRoomPlayers;
        cancelBtn = binding.cancelEditRoomBtn;
        updateBtn = binding.editRoomBtn;

        roomName.getEditText().setText(room.getName());
        roomDesc.getEditText().setText(room.getDescription());
        roomPlayers.getEditText().setText(valueOf(room.getRoomCapacity()));


        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        updateBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (validateFields()) {
                    String name = roomName.getEditText().getText().toString();
                    String description = roomDesc.getEditText().getText().toString();
                    String numPlayers = roomPlayers.getEditText().getText().toString();
                    String roomUID = room.getUID();


                    room.setDescription(description);
                    room.setName(name);
                    room.setRoomCapacity(Integer.parseInt(numPlayers));

                    db = FirebaseDatabase.getInstance().getReference("Rooms/" + room.getGame());
                    DatabaseReference roomUpdated = db.child(room.getUID());
                    room.setUID(null);
                    roomUpdated.setValue(room);

                    
                    getDialog().dismiss();


                }
            }
        });

        cancelBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getDialog().dismiss();
            }
        });
    }

    public boolean validateFields() {
        String name = roomName.getEditText().getText().toString();
        String description = roomDesc.getEditText().getText().toString();
        String numPlayers = roomPlayers.getEditText().getText().toString();

        if (name.isEmpty() || description.isEmpty() || numPlayers.isEmpty()) {
            Toast.makeText(getActivity(), "Por favor ingrese correctamente los datos", Toast.LENGTH_SHORT).show();
            return false;
        }

        return true;

    }
}