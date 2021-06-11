package com.example.l2p_app;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.l2p_app.models.Request;
import com.example.l2p_app.models.Room;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class RequestsFragment extends Fragment {

    private View requestList;
    private RecyclerView rv;
    private DatabaseReference db;
    private ArrayList<Request> requests;

    public RequestsFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstance) {
        super.onCreate(savedInstance);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        requestList = inflater.inflate(R.layout.fragment_requests, container, false);

        requests = new ArrayList<>();

        rv = requestList.findViewById(R.id.roomsListFragment);
        rv.setLayoutManager(new LinearLayoutManager(getContext()));

        db = FirebaseDatabase.getInstance().getReference("Requests");
        db.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                for (DataSnapshot ds : snapshot.getChildren()) {
                    Request request = ds.getValue(Request.class);
                    requests.add(request);
                }
                //adapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });


        return requestList;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }

    @Override
    public void onStart() {
        super.onStart();
        FirebaseRecyclerOptions options =
                new FirebaseRecyclerOptions.Builder<Request>()
                        .setQuery(db, Request.class)
                        .build();

        FirebaseRecyclerAdapter<Request, MyViewHolder> adapter = new FirebaseRecyclerAdapter<Request, MyViewHolder>(options) {

            @Override
            protected void onBindViewHolder(@NonNull RequestsFragment.MyViewHolder holder, int position, @NonNull Request model) {
                Request request = requests.get(position);
                holder.user.setText(request.getUserName());
                holder.message.setText(request.getMessage());
            }

            @NonNull
            @Override
            public RequestsFragment.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.request_button, parent, false);
                return new MyViewHolder(v);
            }
        };

        rv.setAdapter(adapter);
        adapter.startListening();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder {

        private TextView user, message;
        public View view;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            view = itemView;
            user = itemView.findViewById(R.id.requestUser);
            //message = itemView.findViewById(R.id.requestMessage);
        }
    }


}