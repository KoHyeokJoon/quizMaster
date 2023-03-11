package com.example.quizmaster.db;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

import com.example.quizmaster.dao.QuizListDAO;
import com.example.quizmaster.data.QuizList;

@Database(entities = {QuizList.class}, version = 2)
public abstract class DataBase extends RoomDatabase {

    private static DataBase INSTANCE = null;

    public abstract QuizListDAO quizListDAO();

    public static DataBase getInstance(Context context) {
        if (INSTANCE == null) {
            INSTANCE = Room.databaseBuilder(context.getApplicationContext(),
                    DataBase.class, "quiz-master.db").build();

        }
        return INSTANCE;
    }

    public static void destroyInstance() {
        INSTANCE = null;
    }
}
