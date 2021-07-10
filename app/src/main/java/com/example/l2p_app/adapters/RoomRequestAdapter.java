package com.example.l2p_app.adapters;

import android.content.Context;
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
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

public class RoomRequestAdapter extends RecyclerView.Adapter<RoomRequestAdapter.MyViewHolder>{

    Context context;
    ArrayList<Request> requests;
    String game;

    public RoomRequestAdapter(Context context, ArrayList<Request> requests, String game) {
        this.context = context;
        this.requests = requests;
        this.game = game;
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

        holder.acceptBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DatabaseReference db = FirebaseDatabase.getInstance().getReference("request_per_room/" + game);
                DatabaseReference addParticipant = FirebaseDatabase.getInstance().getReference("room_participants/" + request.getRoomUID() + "/participants");
                addParticipant.child(request.getUserUID()).setValue(request.getUserName());
                deleteRequests(request, game, position);
            }
        });

        holder.rejectBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                deleteRequests(request, game, position);
            }
        });
    }

    @Override
    public int getItemCount() {
        return requests.size();
    }

    public void deleteRequests(Request request, String game, int position){
        requests.remove(position);

        DatabaseReference removePerRoom = FirebaseDatabase.getInstance().getReference("request_per_room/" + game + "/" + request.getRequestUID());
        DatabaseReference removePerUser = FirebaseDatabase.getInstance().getReference("request_per_user/" + request.getUserUID() + "/" + request.getRequestUID());
        removePerRoom.removeValue().addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void unused) {
                notifyItemRemoved(position);
            }
        });

        removePerUser.removeValue();
    }


    public static class MyViewHolder extends RecyclerView.ViewHolder {

        private TextView userName, userMsg;
        private Button acceptBtn, rejectBtn;
        public View view;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            view = itemView;
            userName = itemView.findViewById(R.id.userName);
            userMsg = itemView.findViewById(R.id.userMessage);
            acceptBtn = itemView.findViewById(R.id.acceptBtn);
            rejectBtn = itemView.findViewById(R.id.rejectBtn);

        }
    }
}
