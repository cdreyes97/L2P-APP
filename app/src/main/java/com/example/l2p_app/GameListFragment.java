package com.example.l2p_app;

import android.media.Image;
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
import androidx.navigation.Navigation;
import androidx.navigation.fragment.NavHostFragment;

import com.example.l2p_app.databinding.GameListBinding;

public class GameListFragment extends Fragment implements View.OnClickListener {

    private GameListBinding binding;

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

        binding.buttonFirst.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                NavHostFragment.findNavController(GameListFragment.this)
                        .navigate(R.id.SecondFragment);
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
        switch (view.getId()){
            case R.id.imageView2:
                Navigation.findNavController(view)
                        .navigate(R.id.nav_rooms);
                break;
            case R.id.imageView:
                Navigation.findNavController(view)
                        .navigate(R.id.nav_home);
                    break;
        }
    }

}