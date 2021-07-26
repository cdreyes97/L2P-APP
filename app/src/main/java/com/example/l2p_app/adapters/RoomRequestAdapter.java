package com.example.l2p_app.adapters;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.AuthFailureError;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.example.l2p_app.R;
import com.example.l2p_app.RoomDetail;
import com.example.l2p_app.SendRequestActivity;
import com.example.l2p_app.models.MyRequest;
import com.example.l2p_app.models.MySingleton;
import com.example.l2p_app.models.Request;
import com.example.l2p_app.models.Room;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class RoomRequestAdapter extends RecyclerView.Adapter<RoomRequestAdapter.MyViewHolder>{

    Context context;
    ArrayList<Request> requests;
    String game;
    Room room;
    Room updatedRoom;

    final private String FCM_API = "https://fcm.googleapis.com/fcm/send";
    final private String serverKey = "key=" + "AAAAoac6FsI:APA91bEXNuvc8k9Ln9wLGUOpkdlTi7OW3sR_VCHWtXopWnz-AO4c0zJGGas2F0zAIL5pauRf-rdO-Nzgnfm8qSApWO1WwYNvtqGzggmo7s1NCR8Q-kdNXhJkq_lgIp8tzPQIQ9WB7DY6";
    final private String contentType = "application/json";
    final String TAG = "NOTIFICATION TAG";
    private String TOPIC,NOTIFICATION_TITLE,NOTIFICATION_MESSAGE,USER_TOKEN;
    private DatabaseReference db;

    public RoomRequestAdapter(Context context, ArrayList<Request> requests, String game, Room room) {
        this.context = context;
        this.requests = requests;
        this.game = game;
        this.room = room;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.request_item, parent, false);
        return new RoomRequestAdapter.MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RoomRequestAdapter.MyViewHolder holder, int position) {
        Request request = requests.get(position);
        holder.userName.setText(request.getUserName());
        holder.userMsg.setText(request.getMessage());
        holder.roomName.setText(room.getName());

        holder.acceptBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DatabaseReference newestRoom = FirebaseDatabase.getInstance().getReference("Rooms/" + game);
                newestRoom.child(room.getUID()).get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DataSnapshot> task) {
                        if (!task.isSuccessful()) {
                            Log.e("firebase", "Error getting data", task.getException());
                        }
                        else {
                            Log.d("firebase", String.valueOf(task.getResult().getValue()));
                            updatedRoom = task.getResult().getValue(Room.class);
                            if (updatedRoom.getRoomCapacity() > updatedRoom.getCapacityUsed()) {

                                DatabaseReference db = FirebaseDatabase.getInstance().getReference("request_per_room/" + game);
                                DatabaseReference addParticipant = FirebaseDatabase.getInstance().getReference("room_participants/" + request.getRoomUID() + "/participants");
                                addParticipant.child(request.getUserUID()).setValue(request.getUserName());
                                deleteRequests(request, game, position);

                                NOTIFICATION_TITLE = "Solicitud aceptada";
                                NOTIFICATION_MESSAGE = "Su solicitud para la sala " + room.getName() +" a sido aceptada"; //tomar nombre de sala

                                updatedRoom.setCapacityUsed(updatedRoom.getCapacityUsed() + 1);
                                String roomUID = room.getUID();
                                updatedRoom.setUID(null);
                                DatabaseReference roomRef = FirebaseDatabase.getInstance().getReference("Rooms/" + game);
                                roomRef.child(roomUID).setValue(updatedRoom);
                                updatedRoom.setUID(roomUID);


                                db = FirebaseDatabase.getInstance().getReference("Users/" + request.getUserUID());
                                db.child("token").get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
                                    @Override
                                    public void onComplete(@NonNull Task<DataSnapshot> task) {
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
                            } else {
                                AlertDialog.Builder confDialogBuilder = new AlertDialog.Builder(context)
                                        .setTitle("Cuidado")
                                        .setMessage("La sala está llena")
                                        .setIcon(android.R.drawable.ic_dialog_alert)
                                        .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {

                                            public void onClick(DialogInterface dialog, int whichButton) {
                                                dialog.dismiss();
                                            }});

                                AlertDialog confDialog = confDialogBuilder.create();
                                confDialog.show();
                            }
                        }
                    }
                });




                
            }
        });

        holder.rejectBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                deleteRequests(request, game, position);
                NOTIFICATION_TITLE = "Solicitud rechazada";
                NOTIFICATION_MESSAGE = "Su solicitud para la sala " + room.getName() +" a sido rechazada"; //tomar nombre de sala

                db = FirebaseDatabase.getInstance().getReference("Users/" + request.getUserUID());
                db.child("token").get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DataSnapshot> task) {
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
            }
        });
    }

    @Override
    public int getItemCount() {
        return requests.size();
    }

    public void deleteRequests(Request request, String game, int position){
        requests.remove(position);

        DatabaseReference removePerRoom = FirebaseDatabase.getInstance().getReference("request_per_room/" + game + "/" + request.getRoomUID() + "/" + request.getRequestUID());
        DatabaseReference removePerUser = FirebaseDatabase.getInstance().getReference("request_per_users/" + request.getUserUID() + "/" + request.getRequestUID());
        removePerRoom.removeValue().addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void unused) {
                notifyItemRemoved(position);
            }
        });

        removePerUser.removeValue();
    }


    public static class MyViewHolder extends RecyclerView.ViewHolder {

        private TextView userName, userMsg, roomName;
        private Button acceptBtn, rejectBtn;
        public View view;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            view = itemView;
            roomName = itemView.findViewById(R.id.roomName);
            userName = itemView.findViewById(R.id.userName);
            userMsg = itemView.findViewById(R.id.userMessage);
            acceptBtn = itemView.findViewById(R.id.acceptBtn);
            rejectBtn = itemView.findViewById(R.id.rejectBtn);

        }
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
                        Toast.makeText(context, "Request error", Toast.LENGTH_LONG).show();
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
        MySingleton.getInstance(context).addToRequestQueue(jsonObjectRequest);
    }
}
