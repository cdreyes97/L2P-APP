package com.example.l2p_app.adapters;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.navigation.NavDirections;
import androidx.navigation.fragment.NavHostFragment;
import androidx.recyclerview.widget.RecyclerView;

import com.example.l2p_app.GameListFragment;
import com.example.l2p_app.R;
import com.example.l2p_app.RoomDetail;
import com.example.l2p_app.RoomsActivity;
import com.example.l2p_app.RoomsFragment;
import com.example.l2p_app.RoomsFragmentDirections;
import com.example.l2p_app.models.Room;

import java.util.ArrayList;

public class RoomAdapter extends RecyclerView.Adapter<RoomAdapter.MyViewHolder>{

    Context context;

    ArrayList<Room> rooms;

    public RoomAdapter(Context context, ArrayList<Room> rooms) {
        this.context = context;
        this.rooms = rooms;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(context).inflate(R.layout.room_item, parent, false);
        return new MyViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull RoomAdapter.MyViewHolder holder, int position) {

        Room room = rooms.get(position);
        holder.name.setText(room.getName());
        holder.description.setText(room.getDescription());
        holder.roomUID.setText(room.getUID());
        holder.roomOwner.setText(room.getOwnerName());
        holder.ownerUID.setText(room.getOwnerUID());

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

        private TextView name, description, roomUID, roomOwner, ownerUID;
        public View view;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            view = itemView;
            name = itemView.findViewById(R.id.roomName);
            description = itemView.findViewById(R.id.roomDesc);
            roomUID = itemView.findViewById(R.id.roomUID);
            roomOwner = itemView.findViewById(R.id.roomOwner);
            ownerUID = itemView.findViewById(R.id.ownerUID);


        }
    }




}
