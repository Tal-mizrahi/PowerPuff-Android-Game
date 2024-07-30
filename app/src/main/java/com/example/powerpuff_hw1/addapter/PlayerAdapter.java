package com.example.powerpuff_hw1.addapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.powerpuff_hw1.R;
import com.example.powerpuff_hw1.interfaces.PlayerCallBack;
import com.example.powerpuff_hw1.models.Player;
import com.google.android.material.imageview.ShapeableImageView;
import com.google.android.material.textview.MaterialTextView;


import java.util.ArrayList;

public class PlayerAdapter extends RecyclerView.Adapter<PlayerAdapter.PlayerViewHolder> {

    private ArrayList<Player> players;
    private PlayerCallBack playerCallBack;

    private static final double DEFAULT_LATITUDE_NO_PERMISSION = 999.0;
    private static final double DEFAULT_LONGITUDE_NO_PERMISSION = 999.0;

    public PlayerAdapter(ArrayList<Player> players) {
        this.players = players;
    }

    public void setPlayerCallBack(PlayerCallBack playerCallBack) {
        this.playerCallBack = playerCallBack;
    }

    @NonNull
    @Override
    public PlayerViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.player_score_item, parent, false);

        return new PlayerViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull PlayerViewHolder holder, int position) {
        Player player = getItem(position);

        holder.ppg_LBL_name.setText(player.getName());
        holder.ppg_LBL_score.setText(String.valueOf(player.getScore()));
        if (player.getLat() != DEFAULT_LATITUDE_NO_PERMISSION
                && player.getLng() != DEFAULT_LONGITUDE_NO_PERMISSION) {
            holder.ppg_IMG_location.setImageResource(R.drawable.ic_location_pin);
        } else {
            holder.ppg_IMG_location.setImageResource(R.drawable.ic_location_off);
            holder.isLocationOn = false;

        }
}

    @Override
    public int getItemCount() {
        return players == null ? 0 : players.size();
    }

    public Player getItem(int position) {
        return players.get(position);
    }

    public class PlayerViewHolder extends RecyclerView.ViewHolder {

        private final ShapeableImageView ppg_IMG_location;
        private final MaterialTextView ppg_LBL_name;
        private final MaterialTextView ppg_LBL_score;
        private boolean isLocationOn = true;

        public PlayerViewHolder(@NonNull View itemView) {
            super(itemView);
            ppg_IMG_location = itemView.findViewById(R.id.ppg_IMG_location);
            ppg_LBL_name = itemView.findViewById(R.id.ppg_LBL_name);
            ppg_LBL_score = itemView.findViewById(R.id.ppg_LBL_score);
            ppg_IMG_location.setOnClickListener(v -> {
                if (playerCallBack != null && isLocationOn)
                    playerCallBack.showLocationOnMap(getItem(getAdapterPosition()));
            });
        }
    }
}