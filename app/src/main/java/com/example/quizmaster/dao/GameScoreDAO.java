package com.example.quizmaster.dao;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.RawQuery;
import androidx.room.Update;
import androidx.sqlite.db.SupportSQLiteQuery;

import com.example.quizmaster.data.GameScore;
import com.example.quizmaster.data.QuizList;

import java.util.List;

@Dao
public interface GameScoreDAO {

    // game score
    @Insert
    public void insertGameScore(GameScore gameScore);

    @Update
    public void updateGameScore(GameScore gameScore);

    @Query("SELECT COUNT(1) FROM GameScore WHERE queGb = :queGb")
    Integer getGameScoreCnt(String queGb);

    @Query("SELECT COUNT(1) FROM GameScore WHERE queGb = :queGb AND score > :score")
    Integer getRank(String queGb, Integer score);

}
