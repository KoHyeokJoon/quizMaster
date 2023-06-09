package com.example.quizmaster.data;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity
public class GameScore {

    @PrimaryKey(autoGenerate = true) //자동 ID 할당
    public int seq;

    @ColumnInfo(name = "queGb")
    public String queGb;

    @ColumnInfo(name = "score")
    public Integer score;

    @ColumnInfo(name = "gameGb")
    public String gameGb; // single or friend


    public void setQueGb(String queGb) {
        this.queGb = queGb;
    }

    public String getQueGb() {
        return this.queGb;
    }


    public void setScore(Integer score) {
        this.score = score;
    }

    public Integer getScore() {
        return this.score;
    }

    public void setGameGb(String gameGb) {
        this.gameGb = gameGb;
    }

    public String getGameGb() {
        return this.gameGb;
    }
}
