package com.example.l2p_app;

import android.content.Intent;
import android.media.Image;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Observer;
import androidx.navigation.NavController;
import androidx.navigation.NavDirections;
import androidx.navigation.Navigation;
import androidx.navigation.fragment.NavHostFragment;

import com.bumptech.glide.Glide;
import com.example.l2p_app.databinding.ActivityMainBinding;
import com.example.l2p_app.databinding.GameListBinding;
import com.example.l2p_app.databinding.NavHeaderMainBinding;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

public class GameListFragment extends Fragment implements View.OnClickListener {

    private View gameListView;
    private GameListBinding binding;
    private NavHeaderMainBinding navHeaderMainBinding;
    private FirebaseStorage firebaseStorage;
    private FirebaseAuth firebaseAuth;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        firebaseAuth = FirebaseAuth.getInstance();
        firebaseStorage = FirebaseStorage.getInstance();

    }

    @Override
    public View onCreateView(
            LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState
    ) {

        binding = GameListBinding.inflate(inflater, container, false);

        ImageView valorant = binding.imageView2;
        ImageView overwatch = binding.imageView;
        valorant.setOnClickListener(this);
        overwatch.setOnClickListener(this);

        return binding.getRoot();

    }

    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);


        StorageReference storageReference = firebaseStorage.getReference();

        storageReference.child(firebaseAuth.getUid()).child("Images/Profile Pic").getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri) {
                Log.d("TEST","LLEGO ACA");
                //Glide.with(getActivity()).load(uri).centerCrop().into(navHeaderMainBinding.headerImage);
            }
        });

        binding.fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Navigation.findNavController(view).navigate(R.id.action_global_roomCreation);
            }
        });
    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

    @Override
    public void onClick(View view) {
        String game;
        GameListFragmentDirections.ActionNavHomeToNavRooms action;
        switch (view.getId()){
            case R.id.imageView:
                game = "Valorant";
                action = GameListFragmentDirections.actionNavHomeToNavRooms(game);
                NavHostFragment.findNavController(GameListFragment.this)
                        .navigate((NavDirections) action);
                break;
            case R.id.imageView2:
                game = "Overwatch";
                action = GameListFragmentDirections.actionNavHomeToNavRooms(game);
                NavHostFragment.findNavController(GameListFragment.this)
                        .navigate((NavDirections) action);
                break;
        }

    }

}