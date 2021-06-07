package com.example.l2p_app;

import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.NavDirections;
import androidx.navigation.Navigation;
import androidx.navigation.fragment.NavHostFragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.l2p_app.models.User;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;


public class ProfileFragment extends Fragment {

    private View profileView;
    private ImageView profilePic;
    private TextView profileName, profileEmail;
    private Button editBtn;
    private ProgressBar pgsBar;
    private FirebaseAuth firebaseAuth;
    private FirebaseDatabase firebaseDatabase;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        profileView = inflater.inflate(R.layout.fragment_profile, container, false);



        profilePic = profileView.findViewById(R.id.profilePic);
        profileName = profileView.findViewById(R.id.profileName);
        profileEmail = profileView.findViewById(R.id.profileEmail);
        editBtn = profileView.findViewById(R.id.btnProfileUpdate);

        pgsBar = profileView.findViewById(R.id.profileProgressBar);
        firebaseAuth = FirebaseAuth.getInstance();
        firebaseDatabase = FirebaseDatabase.getInstance();

        pgsBar.setVisibility(View.VISIBLE);


        DatabaseReference db = firebaseDatabase.getReference("Users");
        DatabaseReference userReference = db.child(firebaseAuth.getUid());

        userReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                pgsBar.setVisibility(View.VISIBLE);
                User user = snapshot.getValue(User.class);
                pgsBar.setVisibility(View.GONE);
                profileName.setText("Usuario: " + user.getName());
                profileEmail.setText("Correo: " + user.getEmail());
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(getActivity(),error.getCode(), Toast.LENGTH_SHORT).show();
            }
        });

        editBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                NavDirections action = ProfileFragmentDirections.actionProfileFragmentToEditProfile();
                Navigation.findNavController(v).navigate(action);
            }
        });

        return profileView;
    }


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        MainActivity activity = (MainActivity) context;
        DrawerLayout drawer = (DrawerLayout) activity.findViewById(R.id.drawer_layout);
        drawer.close();
    }
}