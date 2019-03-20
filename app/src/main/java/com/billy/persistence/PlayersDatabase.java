package com.billy.persistence;

import android.content.Context;

import androidx.room.Room;
import androidx.room.RoomDatabase;

public abstract class PlayersDatabase extends RoomDatabase {

    private static volatile PlayersDatabase INSTANCE;
    private static final String DB = "player.db";

    public static PlayersDatabase getInstance(Context context) {
        if (INSTANCE == null) {
            synchronized (PlayersDatabase.class) {
                INSTANCE = Room.databaseBuilder(context.getApplicationContext(),
                        PlayersDatabase.class, DB).build();
            }
        }
        return INSTANCE;
    }

    public abstract PlayerDao playerDao();
}
