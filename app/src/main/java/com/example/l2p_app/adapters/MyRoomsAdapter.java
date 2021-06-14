package com.example.l2p_app.adapters;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.l2p_app.R;
import com.example.l2p_app.RoomDetail;
import com.example.l2p_app.models.Room;

import java.util.ArrayList;

public class MyRoomsAdapter extends RecyclerView.Adapter<MyRoomsAdapter.MyViewHolder>{

    Context context;

    ArrayList<Room> rooms;

    public MyRoomsAdapter(Context context, ArrayList<Room> rooms) {
        this.context = context;
        this.rooms = rooms;
    }

    @NonNull
    @Override
    public MyRoomsAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(context).inflate(R.layout.my_room_item, parent, false);
        return new MyRoomsAdapter.MyViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull MyRoomsAdapter.MyViewHolder holder, int position) {

        Room room = rooms.get(position);
        holder.name.setText(room.getName());
        //holder.description.setText(room.getDescription());
        holder.roomUID.setText(room.getUID());
        //holder.roomOwner.setText(room.getOwnerName());
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

        private TextView name, roomUID, ownerUID;
        public View view;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            view = itemView;
            name = itemView.findViewById(R.id.myRoomName);
            //description = itemView.findViewById(R.id.roomDesc);
            roomUID = itemView.findViewById(R.id.myRoomUID);
            //roomOwner = itemView.findViewById(R.id.roomOwner);
            ownerUID = itemView.findViewById(R.id.myRoomOwnerUID);

        }
    }
}
