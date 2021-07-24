package com.example.l2p_app;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.example.l2p_app.models.MyRequest;
import com.example.l2p_app.models.MySingleton;
import com.example.l2p_app.models.Request;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.messaging.FirebaseMessaging;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class SendRequestActivity extends AppCompatActivity {

    final private String FCM_API = "https://fcm.googleapis.com/fcm/send";
    final private String serverKey = "key=" + "AAAAoac6FsI:APA91bEXNuvc8k9Ln9wLGUOpkdlTi7OW3sR_VCHWtXopWnz-AO4c0zJGGas2F0zAIL5pauRf-rdO-Nzgnfm8qSApWO1WwYNvtqGzggmo7s1NCR8Q-kdNXhJkq_lgIp8tzPQIQ9WB7DY6";
    final private String contentType = "application/json";
    final String TAG = "NOTIFICATION TAG";
    private String TOPIC,NOTIFICATION_TITLE,NOTIFICATION_MESSAGE,USER_TOKEN;

    private TextInputLayout msgRequest;
    private Button sendRequestBtn, cancelBtn;
    private FirebaseAuth firebaseAuth;
    private DatabaseReference db, roomReference;
    private String game, roomUID, roomName,ownerUID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_send_request);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        setTitle("Enviar Solicitud");

        Intent intent = this.getIntent();

        roomUID = intent.getStringExtra("roomUID");
        game = intent.getStringExtra("game");
        roomName = intent.getStringExtra("roomName");
        ownerUID = intent.getStringExtra("ownerUID");


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

                //TOPIC = "/topics/userABC"; //topic must match with what the receiver subscribed to
                NOTIFICATION_TITLE = "Nueva solicitud";
                NOTIFICATION_MESSAGE = "Tienes una nueva solicitud para la sala " + roomName; //tomar nombre de sala

                roomReference = FirebaseDatabase.getInstance().getReference("Users/" + ownerUID);
                roomReference.child("token").get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
                    @Override
                    public void onComplete(@NonNull  Task<DataSnapshot> task) {
                        USER_TOKEN = task.getResult().getValue().toString();
                        JSONObject notification = new JSONObject();
                        JSONObject notifcationBody = new JSONObject();


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

                    }
                });


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


    private void sendNotification(JSONObject notification) {
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(FCM_API, notification,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Log.i(TAG, "onResponse: " + response.toString());
                        //edtTitle.setText("");
                        //edtMessage.setText("");
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(SendRequestActivity.this, "Request error", Toast.LENGTH_LONG).show();
                        Log.i(TAG, "onErrorResponse: Didn't work");
                    }
                }){
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("Authorization", serverKey);
                params.put("Content-Type", contentType);
                return params;
            }
        };
        MySingleton.getInstance(this.getApplicationContext()).addToRequestQueue(jsonObjectRequest);
    }
}