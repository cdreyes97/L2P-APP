package com.example.l2p_app;

import android.os.Bundle;

import androidx.annotation.AnyRes;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavDirections;
import androidx.navigation.Navigation;
import androidx.navigation.fragment.NavHostFragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.l2p_app.adapters.MyRoomsAdapter;
import com.example.l2p_app.adapters.RoomAdapter;
import com.example.l2p_app.databinding.FragmentMyRoomListBinding;
import com.example.l2p_app.databinding.FragmentRoomsBinding;
import com.example.l2p_app.models.Room;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;



public class MyRoomsListFragment extends Fragment {

    private View roomsList;
    private RecyclerView rv;
    private DatabaseReference db;
    private ArrayList<Room> rooms;
    private CardView card;
    private FragmentMyRoomListBinding binding;
    private FirebaseAuth firebaseAuth;
    private String gameName;
    private String userUID;
    private MyRoomsAdapter adapter;

    public MyRoomsListFragment() {
        // Required empty public constructor
    }
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {


        firebaseAuth = FirebaseAuth.getInstance();
        userUID = firebaseAuth.getCurrentUser().getUid();
        db = FirebaseDatabase.getInstance().getReference("room_owner/" + userUID);
        Log.d("POSITION", "BUSCAR MIS SALAS");



        binding = FragmentMyRoomListBinding.inflate(inflater, container, false);

        rooms = new ArrayList<>();

        rv = binding.myRoomsListRv;
        rv.setHasFixedSize(true);
        rv.setLayoutManager(new LinearLayoutManager(getContext()));

        adapter = new MyRoomsAdapter(getContext(), rooms);

        rv.setAdapter(adapter);

        db.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                for (DataSnapshot ds : snapshot.getChildren()) {
                    for (DataSnapshot dsRoom : ds.getChildren()) {
                        String roomName = dsRoom.getValue().toString();
                        String game = ds.getKey();
                        String roomUID = dsRoom.getKey();
                        gameName = game;
                        //room.setName(roomName);
                        DatabaseReference roomReference = FirebaseDatabase.getInstance().getReference("Rooms/"+gameName+"/"+roomUID);
                        roomReference.addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull  DataSnapshot snapshot) {
                                if(snapshot.exists()){
                                    Room roomObject = snapshot.getValue(Room.class);
                                    roomObject.setUID(roomUID);
                                    rooms.add(roomObject);
                                    //Room room = new Room();
                                    //room = roomReference.get().getResult().getValue(Room.class);
                                    //room.setGame(game);
                                    Log.d("OBTENER SALA", roomObject.getName());
                                    //Log.d("OBTENER SALA GAME", game);
                                    //room.setUID(dsRoom.getKey());
                                    //rooms.add(room);
                                    //Log.d("UID", room.getUID());
                                    adapter.notifyDataSetChanged();

                                }
                                else{
                                    adapter.notifyDataSetChanged();
                                }
                            }

                            @Override
                            public void onCancelled(@NonNull  DatabaseError error) {

                            }
                        });
                    }

                }


            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });


        return binding.getRoot();
    }


    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }



    /*@Override
    public void onStart() {
        super.onStart();
        FirebaseRecyclerOptions options =
                new FirebaseRecyclerOptions.Builder<Object>()
                        .setQuery(db, Object.class)
                        .build();

        FirebaseRecyclerAdapter<Object, MyViewHolder> adapter = new FirebaseRecyclerAdapter<Object, MyViewHolder>(options) {
            @Override
            protected void onBindViewHolder(@NonNull MyRoomsListFragment.MyViewHolder holder, int position, @NonNull Object model) {
                if(rooms.size() != 0){
                    Room room = rooms.get(position);
                    //String room = rooms.get(position);
                    holder.name.setText(room.getName());
                    //holder.description.setText(room.getDescription());
                    holder.gameTextView.setText(room.getGame());
                    holder.roomUID.setText(room.getUID());
                    //holder.roomOwner.setText(room.getOwnerName());
                    //holder.ownerUID.setText(room.getOwnerUID());
                }

            }

            @NonNull
            @Override
            public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.my_room_item, parent, false);
                return new MyRoomsListFragment.MyViewHolder(v);
            }
        };

        rv.setAdapter(adapter);
        adapter.startListening();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {

        private TextView name, description, roomUID, roomOwner, ownerUID, gameTextView;
        public View view;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            view = itemView;
            name = itemView.findViewById(R.id.myRoomName);
            gameTextView = itemView.findViewById(R.id.game);
            //description = itemView.findViewById(R.id.roomDesc);
            roomUID = itemView.findViewById(R.id.myRoomUID);
            //roomOwner = itemView.findViewById(R.id.roomOwner);
            //ownerUID = itemView.findViewById(R.id.ownerUID);


            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Log.d("Card clicked", "yes");
                    MyRoomsListFragmentDirections.ActionMyRoomsListFragmentToRoomContent action;
                    //RoomsFragmentDirections.ActionNavRoomsToRoomContent action;
                    String stringRoomUID = roomUID.getText().toString();
                    String game = gameTextView.getText().toString();
                    Fragment fragment = new RoomContent();
                    Bundle bundle = new Bundle();
                    bundle.putString("UID",stringRoomUID);
                    Log.d("bundle",game);
                    bundle.putString("game",game);
                    bundle.putString("owner", userUID);
                    fragment.setArguments(bundle);
                    getParentFragmentManager()
                            .beginTransaction()
                            .replace(R.id.MyRoomsActivityLayout,fragment)
                            .addToBackStack(null)
                            .commit();
                    getChildFragmentManager()
                            .beginTransaction()
                            .replace(binding.MyRoomsView.getId(),fragment)
                            .addToBackStack(null)
                            .commit();
                    //String stringRoomOwner = ownerUID.getText().toString();
                    //action = MyRoomsListFragmentDirections.actionMyRoomsListFragmentToRoomContent(stringRoomUID, gameName, "DGGC7N78yESopoiqWdd8U9Ld8Zz1");
                    //Navigation.findNavController(v).navigate(action);
                    //NavHostFragment.findNavController(MyRoomsListFragment.this)
                            //.navigate((NavDirections) action);


                }
            });
        }

    }*/
}