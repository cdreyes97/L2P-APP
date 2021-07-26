package com.example.l2p_app;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.example.l2p_app.models.Room;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import static java.lang.String.valueOf;

public class EditRoomActivity extends AppCompatActivity {

    private TextInputLayout roomName, roomDesc, roomPlayers;
    private FirebaseAuth firebaseAuth;
    private DatabaseReference db;
    private Room room;
    private Button cancelBtn, updateBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_room);



        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        room = (Room) getIntent().getExtras().getSerializable("room");

        setTitle("Editar " + room.getName());

        roomName = findViewById(R.id.editRoomName);
        roomDesc = findViewById(R.id.editRoomDesc);
        roomPlayers = findViewById(R.id.editRoomPlayers);
        updateBtn = findViewById(R.id.editRoomBtn);
        cancelBtn = findViewById(R.id.cancelEditRoomBtn);

        roomName.getEditText().setText(room.getName());
        roomDesc.getEditText().setText(room.getDescription());
        roomPlayers.getEditText().setText(valueOf(room.getRoomCapacity()));

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
                    DatabaseReference roomUpdated = db.child(roomUID);
                    room.setUID(null);
                    roomUpdated.setValue(room);
                    room.setUID(roomUID);

                    AlertDialog.Builder confDialogBuilder = new AlertDialog.Builder(EditRoomActivity.this)
                            .setTitle("Confirmación")
                            .setMessage("Sala actualizada")
                            .setIcon(android.R.drawable.ic_dialog_alert)
                            .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {

                                public void onClick(DialogInterface dialog, int whichButton) {
                                    finish();
                                }});

                    AlertDialog confDialog = confDialogBuilder.create();
                    confDialog.show();

                }
            }
        });

        cancelBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

    }

    public boolean validateFields() {
        String name = roomName.getEditText().getText().toString();
        String description = roomDesc.getEditText().getText().toString();
        String numPlayers = roomPlayers.getEditText().getText().toString();
        int integerNumPlayers = Integer.parseInt(numPlayers);
        if (name.isEmpty() || description.isEmpty() || numPlayers.isEmpty()) {
            Toast.makeText(this, "Por favor ingrese correctamente los datos", Toast.LENGTH_SHORT).show();
            return false;
        }

        if (integerNumPlayers < room.getCapacityUsed()) {
            Toast.makeText(this, "El número de participantes es menor a la cantidad que ya está en la sala.", Toast.LENGTH_LONG).show();
            return false;
        }

        return true;

    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        switch (item.getItemId()) {
            case android.R.id.home:
                this.finish();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }
}