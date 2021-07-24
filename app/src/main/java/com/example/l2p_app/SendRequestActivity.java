package com.example.l2p_app;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

import com.example.l2p_app.models.MyRequest;
import com.example.l2p_app.models.Request;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.messaging.FirebaseMessaging;

public class SendRequestActivity extends AppCompatActivity {

    final private String FCM_API = "https://fcm.googleapis.com/fcm/send";
    final private String serverKey = "key=" + "AAAAoac6FsI:APA91bEXNuvc8k9Ln9wLGUOpkdlTi7OW3sR_VCHWtXopWnz-AO4c0zJGGas2F0zAIL5pauRf-rdO-Nzgnfm8qSApWO1WwYNvtqGzggmo7s1NCR8Q-kdNXhJkq_lgIp8tzPQIQ9WB7DY6";
    final private String contentType = "application/json";
    final String TAG = "NOTIFICATION TAG";

    private TextInputLayout msgRequest;
    private Button sendRequestBtn, cancelBtn;
    private FirebaseAuth firebaseAuth;
    private DatabaseReference db;
    private String game, roomUID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_send_request);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        setTitle("Enviar Solicitud");

        Intent intent = this.getIntent();

        roomUID = intent.getStringExtra("roomUID");
        game = intent.getStringExtra("game");

        msgRequest = findViewById(R.id.msgRequest);
        sendRequestBtn = findViewById(R.id.sendRequest);
        cancelBtn = findViewById(R.id.cancelBtn);


        sendRequestBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                firebaseAuth = FirebaseAuth.getInstance();
                String userName = firebaseAuth.getCurrentUser().getDisplayName();
                String userUID = firebaseAuth.getCurrentUser().getUid();
                String stringMsgRequest = msgRequest.getEditText().getText().toString();

                Request request = new Request(userName, userUID, stringMsgRequest, roomUID, Request.Status.PENDIENTE);

                db = FirebaseDatabase.getInstance().getReference("request_per_room/" + game + "/" + roomUID);
                DatabaseReference requestPerRoom = db.push();
                String requestUID = requestPerRoom.getKey();
                requestPerRoom.setValue(request);

                MyRequest myRequest = new MyRequest(stringMsgRequest, roomUID, game, MyRequest.Status.PENDIENTE);

                db = FirebaseDatabase.getInstance().getReference("request_per_users/" + userUID);
                DatabaseReference requestPerUsers = db.child(requestUID);
                requestPerUsers.setValue(myRequest);
                FirebaseMessaging.getInstance().subscribeToTopic(roomUID);

                /*
                TOPIC = "/topics/userABC"; //topic must match with what the receiver subscribed to
                NOTIFICATION_TITLE = "Nueva solicitud";
                NOTIFICATION_MESSAGE = "Tienes una nueva solicitud para la sala NUEVA SALA"; //tomar nombre de sala
                USER_TOKEN = "eLsbe_nnRpKRQhv19Fnuy2:APA91bE9WJrRYjTAWJhjrHd7_2NtJz1gujGnpBTbZKqszDZsX9JrNMLmDfK2oElYmtwzAZLYWJsq3mPnK1U46Vay9635IAzQJYWMVZ7G2ybW445U0ratFU2w6La61DP5TqiM9sNuoxvf";

                JSONObject notification = new JSONObject();
                JSONObject notifcationBody = new JSONObject();

                {
                    to:
                    "Topic" or REGISTRATION_ID -token unico de la aplicacion para cada dispositivo
                    data:
                    {
                        title:
                        message:
                    }
                    time_to_live:
                    "600" opcional
                }

                try {
                    notifcationBody.put("title", NOTIFICATION_TITLE);
                    notifcationBody.put("message", NOTIFICATION_MESSAGE);

                    notification.put("to", USER_TOKEN);
                    notification.put("data", notifcationBody);
                    notification.put("time_to_live", 600);
                } catch (JSONException e) {
                    Log.e(TAG, "onCreate: " + e.getMessage() );
                }
                sendNotification(notification);
                */

                AlertDialog.Builder confDialogBuilder = new AlertDialog.Builder(SendRequestActivity.this)
                        .setTitle("Confirmación")
                        .setMessage("Solicitud enviada")
                        .setIcon(android.R.drawable.ic_dialog_alert)
                        .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {

                            public void onClick(DialogInterface dialog, int whichButton) {
                                finish();
                            }});

                AlertDialog confDialog = confDialogBuilder.create();
                confDialog.show();
            }
        });

        cancelBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
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