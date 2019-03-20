package com.billy.persistence;

import android.app.Application;

import java.util.List;

import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

public class PlayerViewModel extends AndroidViewModel {

    private DataRepo mDataRepo;
    private LiveData<List<Player>> mAllPlayers;

    public PlayerViewModel (Application application) {
        super(application);
        mDataRepo = new DataRepo(application);
        mAllPlayers = mDataRepo.getAllPlayers();
    }

    public LiveData<List<Player>> getAllPlayers() {
        return mAllPlayers;
    }

    public void insert(Player player) {
        mDataRepo.insert(player);
    }

}
