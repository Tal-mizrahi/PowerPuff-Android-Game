package com.example.powerpuff_hw1.utilities;

import static android.content.Context.MODE_PRIVATE;

import android.content.Context;
import android.content.SharedPreferences;

import com.example.powerpuff_hw1.models.Player;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import java.lang.reflect.Type;
import java.util.ArrayList;

public class MySharedPreference {
    public static final int NUM_OF_TO_PLAYERS = 10;
    private static MySharedPreference instance;
    private ArrayList<Player> allPlayers;
    private SharedPreferences prefs;
    private final Gson gson;



    private MySharedPreference(Context context) {
        prefs = context.getSharedPreferences("MyPreference", MODE_PRIVATE);
        gson = new Gson();
        allPlayers = loadAllPlayers("PLAYERS");
        if (allPlayers == null)
            allPlayers = new ArrayList<>();
    }

    public static void init(Context context) {
        if (instance == null) {
            instance = new MySharedPreference(context);
        }
    }

    public static MySharedPreference getInstance() {
        return instance;
    }

    public void addPlayer(Player player) {
        if (allPlayers.size() >= NUM_OF_TO_PLAYERS) {
            if(allPlayers.get(NUM_OF_TO_PLAYERS - 1).getScore() < player.getScore()){
                allPlayers.set(NUM_OF_TO_PLAYERS - 1, player);
                sortPlayers();
            }
        } else {
            allPlayers.add(player);
            sortPlayers();
        }
    }

    public void saveAllPlayers(String key){
        String playersJson = gson.toJson(this.allPlayers);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putString(key, playersJson);
        editor.apply();
    }


    public ArrayList<Player> loadAllPlayers(String key){
        String playersJson = prefs.getString(key, "");
        Type type = new TypeToken<ArrayList<Player>>(){}.getType();
        return gson.fromJson(playersJson, type);
    }

    public void putString(String key, String value) {
        SharedPreferences.Editor editor = prefs.edit();
        editor.putString(key, value);
        editor.apply();
    }

    public String getString(String key, String def) {
        return prefs.getString(key, def);
    }

    public void putInt(String key, int value) {
        SharedPreferences.Editor editor = prefs.edit();
        editor.putInt(key, value);
        editor.apply();
    }

    public int getInt(String key, int def) {
        return prefs.getInt(key, def);
    }



    public ArrayList<Player> getAllPlayers() {
        return allPlayers;
    }

    public void sortPlayers() {
        allPlayers.sort((p1, p2) -> p2.getScore() - p1.getScore());
    }




}



