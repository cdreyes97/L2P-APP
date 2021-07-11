package com.example.l2p_app.adapters;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.l2p_app.R;

import java.util.ArrayList;

public class ParticipantsAdapter  extends ArrayAdapter<String> {
    public ParticipantsAdapter(Context context, ArrayList<String> participants){
        super(context,0,participants);
    }




    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {

        String user = getItem(position);

        if(convertView == null){
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.participants_list_item, parent, false);
        }
        TextView userName = convertView.findViewById(R.id.participantUserName);
        userName.setText(user);

        return convertView;//super.getView(position, convertView, parent);
    }


}
