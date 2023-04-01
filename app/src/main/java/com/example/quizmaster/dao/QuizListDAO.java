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
public interface QuizListDAO {

    @Query("SELECT * FROM QuizList WHERE useYn = 'Y' ")
    List<QuizList> getAllList();

    @Query("SELECT * FROM QuizList WHERE useYn = 'Y' AND queGb = :queGb")
    List<QuizList> getList(String queGb);

    @RawQuery
    List<QuizList> dynamic(SupportSQLiteQuery query);

    @Insert
    public void insertIdiomList(QuizList idiomQuizList);

    @Update
    public void updateIdiom(QuizList idiomQuizList);

    @Query("DELETE FROM QuizList WHERE queGb = :queGb")
    public void deleteItems(String queGb);

    @Query("SELECT COUNT(1) FROM QuizList WHERE queGb = :queGb AND answer = :answer")
    public int dupCount(String queGb, String answer);



}
