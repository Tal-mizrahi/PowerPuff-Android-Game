package com.example.powerpuff_hw1.views.fragments;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.powerpuff_hw1.R;
import com.example.powerpuff_hw1.addapter.PlayerAdapter;
import com.example.powerpuff_hw1.interfaces.ListCallBack;
import com.example.powerpuff_hw1.models.Player;
import com.example.powerpuff_hw1.utilities.MySharedPreference;
import com.google.android.material.textview.MaterialTextView;

import java.util.ArrayList;

public class ListFragment extends Fragment {
    private RecyclerView ppg_LST_players;
    private MaterialTextView ppg_LBL_no_players;
    private ListCallBack listCallBack;


    public ListFragment() {
        // Required empty public constructor
    }

    public void setListCallBack(ListCallBack listCallBack) {
        this.listCallBack = listCallBack;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view =  inflater.inflate(R.layout.fragment_list, container, false);
        ppg_LST_players = view.findViewById(R.id.ppg_LST_players);
        ppg_LBL_no_players = view.findViewById(R.id.ppg_LBL_no_players);
        initViews();
        return view;
    }

    private void initViews() {
        ArrayList<Player> allPlayers = MySharedPreference.getInstance().getAllPlayers();
        if (allPlayers.isEmpty() || allPlayers == null) {
            ppg_LBL_no_players.setVisibility(View.VISIBLE);
        } else {
            ppg_LBL_no_players.setVisibility(View.GONE);
            PlayerAdapter playerAdapter = new PlayerAdapter(allPlayers);
            LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this.getContext());
            linearLayoutManager.setOrientation(RecyclerView.VERTICAL);
            ppg_LST_players.setLayoutManager(linearLayoutManager);
            ppg_LST_players.setAdapter(playerAdapter);
            playerAdapter.setPlayerCallBack((player) -> {
                if (listCallBack != null) {
                    listCallBack.showLocationOnMap(
                            player.getLat(),
                            player.getLng()
                    );
                }

            });
        }
    }



}