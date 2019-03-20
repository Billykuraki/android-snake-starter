package com.billy.persistence;


import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "player")
public class Player {

    @PrimaryKey(autoGenerate = true)
    private int playerId;

    @ColumnInfo(name = "name")
    private String name;

    @ColumnInfo(name = "score")
    private String score;

    public Player(String name, String score) {
        this.name = name;
        this.score = score;
    }

    public void setPlayerId(int id) {
        playerId = id;
    }

    public int getPlayerId() {
        return playerId;
    }

    public String getName(){
        return name;
    }

    public String getScore() {
        return score;
    }

}
