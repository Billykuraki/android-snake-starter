package com.billy.persistence;

import android.app.Application;
import android.os.AsyncTask;

import java.util.List;

import androidx.lifecycle.LiveData;

public class DataRepo {

    private PlayerDao mPlayerDao;
    private LiveData<List<Player>> mAllData;

    public DataRepo (Application application) {
        PlayerDatabase db = PlayerDatabase.getInstance(application);
        mPlayerDao = db.playerDao();
        mAllData = mPlayerDao.findAllPlayers();
    }

    public LiveData<List<Player>> getAllPlayers() {
        return mAllData;
    }

    public void insert(final Player player) {
        AsyncTask.execute(new Runnable() {
            @Override
            public void run() {
                mPlayerDao.insertPlayer(player);
            }
        });
    }

}
