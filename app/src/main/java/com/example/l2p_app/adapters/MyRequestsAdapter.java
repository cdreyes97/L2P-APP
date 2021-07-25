package com.example.l2p_app.adapters;

import android.content.Context;
import android.content.DialogInterface;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.l2p_app.R;
import com.example.l2p_app.models.MyRequest;
import com.example.l2p_app.models.Request;
import com.example.l2p_app.models.Room;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class MyRequestsAdapter extends RecyclerView.Adapter<MyRequestsAdapter.MyViewHolder> {

    Context context;
    ArrayList<MyRequest> requests;

    public MyRequestsAdapter(Context context, ArrayList<MyRequest> requests) {
        this.context = context;
        this.requests = requests;
    }

    @NonNull
    @Override
    public MyRequestsAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.my_requests_item, parent, false);
        return new MyRequestsAdapter.MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyRequestsAdapter.MyViewHolder holder, int position) {

        MyRequest request = requests.get(position);
        holder.description.setText(request.getMessage());
        holder.status.setText(request.getStatus().toString());
        holder.game.setText(request.getGame());

        DatabaseReference db = FirebaseDatabase.getInstance().getReference("Rooms/"
                + request.getGame() + "/" + request.getRoomUID());

        db.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()){
                    Room room = snapshot.getValue(Room.class);

                    holder.roomOwner.setText(room.getOwnerName());
                    holder.name.setText(room.getName());
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }

        });


        holder.deleteBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new MaterialAlertDialogBuilder(v.getContext())
                        .setTitle("Desea eliminar esta solicitud?")
                        .setNegativeButton("No", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                            }
                        })
                        .setPositiveButton("Si", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                deleteRequest(request.getRequestUID(), request.getGame(),request.getRoomUID(), FirebaseAuth.getInstance().getUid(), position);
                                dialog.dismiss();
                            }
                        })
                        .show();
            }
        });
    }

    @Override
    public int getItemCount() {
        return requests.size();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder {

        private TextView name, description, roomOwner, game, status;
        private Button deleteBtn;
        public View view;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            view = itemView;
            name = itemView.findViewById(R.id.requestRoomName);
            description = itemView.findViewById(R.id.requestDesc);
            roomOwner = itemView.findViewById(R.id.requestRoomOwner);
            game = itemView.findViewById(R.id.requestRoomGame);
            status = itemView.findViewById(R.id.requestStatus);
            deleteBtn = itemView.findViewById(R.id.myRequestDeleteBtn);

        }
    }


    void deleteRequest(String requestUID, String game, String roomUID, String userUID,int position){
        Log.d("Delete request", requestUID + "a");
        requests.remove(position);

        DatabaseReference removePerRoom = FirebaseDatabase.getInstance().getReference("request_per_room/" + game + "/" + roomUID + "/" + requestUID);
        DatabaseReference removePerUser = FirebaseDatabase.getInstance().getReference("request_per_users/" + userUID + "/" + requestUID);

        removePerUser.removeValue();
        removePerRoom.removeValue().addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void unused) {
                MyRequestsAdapter.this.notifyItemRemoved(position);
            }
        });
    }
}
