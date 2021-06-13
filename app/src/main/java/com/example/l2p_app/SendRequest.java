package com.example.l2p_app;

import android.app.Dialog;
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

import com.example.l2p_app.databinding.FragmentSendRequestBinding;
import com.example.l2p_app.models.Request;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class SendRequest extends DialogFragment {

    private FragmentSendRequestBinding binding;
    private EditText msgRequest;
    private Button sendRequestBtn, cancelBtn;
    private FirebaseAuth firebaseAuth;
    private DatabaseReference db;
    private String game, roomUID;


    public SendRequest() {
        // Required empty public constructor
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        binding = FragmentSendRequestBinding.inflate(inflater, container, false);

        game = SendRequestArgs.fromBundle(getArguments()).getGame();
        roomUID = SendRequestArgs.fromBundle(getArguments()).getRoomUID();
        cancelBtn = binding.cancelBtn;

        msgRequest = binding.msgRequest;
        sendRequestBtn = binding.sendRequest;



        return binding.getRoot();
    }


    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        sendRequestBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                firebaseAuth = FirebaseAuth.getInstance();

                String userName = firebaseAuth.getCurrentUser().getDisplayName();
                String userUID = firebaseAuth.getCurrentUser().getUid();
                String stringMsgRequest = msgRequest.getText().toString();

                Request request = new Request(userName, userUID, stringMsgRequest, roomUID, Request.Status.PENDING);

                db = FirebaseDatabase.getInstance().getReference("request_per_room/" + game + "/" + roomUID);
                DatabaseReference requestPerRoom = db.push();
                String requestUID = requestPerRoom.getKey();
                requestPerRoom.setValue(request);

                db = FirebaseDatabase.getInstance().getReference("request_per_users/" + userUID);
                DatabaseReference requestPerUsers = db.child(requestUID);
                requestPerUsers.setValue(request);

                getDialog().dismiss();

            }
        });

        cancelBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getDialog().dismiss();
            }
        });

    }
}