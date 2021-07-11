package com.example.l2p_app.adapters;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.l2p_app.EditRoom;
import com.example.l2p_app.R;
import com.example.l2p_app.RoomDetail;
import com.example.l2p_app.models.Room;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

public class MyRoomsParticipantAdapter extends RecyclerView.Adapter< MyRoomsParticipantAdapter.MyViewHolder>{

    Context context;
    ArrayList<Room> rooms;

    public  MyRoomsParticipantAdapter(Context context, ArrayList<Room> rooms) {
        this.context = context;
        this.rooms = rooms;
    }


    @NonNull
    @Override
    public  MyRoomsParticipantAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(context).inflate(R.layout.my_room_item, parent, false);
        return new  MyRoomsParticipantAdapter.MyViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull  MyRoomsParticipantAdapter.MyViewHolder holder, int position) {

        Room room = rooms.get(position);
        holder.name.setText(room.getName());
        //holder.description.setText(room.getDescription());
        holder.roomUID.setText(room.getUID());
        //holder.roomOwner.setText(room.getOwnerName());
        holder.ownerUID.setText(room.getOwnerUID());
        holder.editBtn.setVisibility(View.GONE);
        holder.deleteBtn.setVisibility(View.GONE);

        MyRoomsParticipantAdapter adapter = this;


        holder.view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, RoomDetail.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                intent.putExtra("room", room);
                context.startActivity(intent);
            }
        });


    }


    @Override
    public int getItemCount() {
        return rooms.size();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder {

        private TextView name, roomUID, ownerUID;
        public View view;
        private Button deleteBtn;
        private Button editBtn;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            view = itemView;
            name = itemView.findViewById(R.id.myRoomName);
            //description = itemView.findViewById(R.id.roomDesc);
            roomUID = itemView.findViewById(R.id.myRoomUID);
            //roomOwner = itemView.findViewById(R.id.roomOwner);
            ownerUID = itemView.findViewById(R.id.myRoomOwnerUID);
            deleteBtn = itemView.findViewById(R.id.MRDeleteBtn);
            editBtn = itemView.findViewById(R.id.MREditBtn);


        }
    }


    void deleteRoom(String roomUID, String game, String ownerUID, int position){
        rooms.remove(position);
        DatabaseReference roomReference = FirebaseDatabase.getInstance().getReference("Rooms/"+game+"/" + roomUID);
        DatabaseReference roomParticipants  = FirebaseDatabase.getInstance().getReference("room_participants/"+ roomUID);
        DatabaseReference myRoom  = FirebaseDatabase.getInstance().getReference("room_owner/"+ ownerUID + "/"+ game+ "/" + roomUID);
        myRoom.removeValue().addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void unused) {
                roomParticipants.removeValue().addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {
                        notifyItemRemoved(position);
                    }
                });
            }
        });
        roomReference.removeValue();



    }


}
