package com.example.l2p_app;

import android.app.Dialog;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.example.l2p_app.databinding.FragmentSendRequestBinding;
import com.example.l2p_app.models.MyRequest;
import com.example.l2p_app.models.MySingleton;
import com.example.l2p_app.models.Request;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.messaging.FirebaseMessaging;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class SendRequest extends DialogFragment {

    final private String FCM_API = "https://fcm.googleapis.com/fcm/send";
    final private String serverKey = "key=" + "AAAAoac6FsI:APA91bEXNuvc8k9Ln9wLGUOpkdlTi7OW3sR_VCHWtXopWnz-AO4c0zJGGas2F0zAIL5pauRf-rdO-Nzgnfm8qSApWO1WwYNvtqGzggmo7s1NCR8Q-kdNXhJkq_lgIp8tzPQIQ9WB7DY6";
    final private String contentType = "application/json";
    final String TAG = "NOTIFICATION TAG";

    private FragmentSendRequestBinding binding;
    private TextInputLayout msgRequest;
    private Button sendRequestBtn, cancelBtn;
    private FirebaseAuth firebaseAuth;
    private DatabaseReference db;
    private String game, roomUID, roomOwner, roomOwnerUID;


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

        new MaterialAlertDialogBuilder(getContext()).show();

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
                String stringMsgRequest = msgRequest.getEditText().getText().toString();

                Request request = new Request(userName, userUID, stringMsgRequest, roomUID, Request.Status.PENDIENTE);

                db = FirebaseDatabase.getInstance().getReference("request_per_room/" + game + "/" + roomUID);
                DatabaseReference requestPerRoom = db.push();
                String requestUID = requestPerRoom.getKey();
                requestPerRoom.setValue(request);

                MyRequest myRequest = new MyRequest(stringMsgRequest, roomUID, game, requestUID,MyRequest.Status.PENDIENTE);

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
                /*
                {
                    to: "Topic"  or REGISTRATION_ID-token unico de la aplicacion para cada dispositivo
                    data: {
                        title:
                        message:
                    }
                    time_to_live: "600" opcional
                }
                */
                /*
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
                        Toast.makeText(getContext(), "Request error", Toast.LENGTH_LONG).show();
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
        MySingleton.getInstance(getContext().getApplicationContext()).addToRequestQueue(jsonObjectRequest);
    }
}