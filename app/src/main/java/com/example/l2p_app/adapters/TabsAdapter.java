package com.example.l2p_app.adapters;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import com.example.l2p_app.GameListFragment;
import com.example.l2p_app.MyRoomsListFragment;
import com.example.l2p_app.RoomCreation;
import com.example.l2p_app.RoomsFragment;
import com.example.l2p_app.RoomsInFragment;

public class TabsAdapter extends  FragmentStateAdapter{

    public TabsAdapter(@NonNull FragmentActivity fragmentActivity) {
        super(fragmentActivity);
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        switch (position) {
            case 0:
                return new MyRoomsListFragment();
            case 1:
                return new RoomsInFragment();
        }
        return new GameListFragment();
    }

    @Override
    public int getItemCount() {
        return 2;
    }

}
