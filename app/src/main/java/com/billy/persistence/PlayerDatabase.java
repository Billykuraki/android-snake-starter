package com.billy.persistence;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

@Database(entities = {Player.class}, version = 1, exportSchema = false)
public abstract class PlayerDatabase extends RoomDatabase {

    private static volatile PlayerDatabase INSTANCE;
    private static final String DB = "player.db";

    public static PlayerDatabase getInstance(Context context) {
        if (INSTANCE == null) {
            synchronized (PlayerDatabase.class) {
                INSTANCE = Room.databaseBuilder(context.getApplicationContext(),
                        PlayerDatabase.class, DB).build();
            }
        }
        return INSTANCE;
    }

    public abstract PlayerDao playerDao();
}
