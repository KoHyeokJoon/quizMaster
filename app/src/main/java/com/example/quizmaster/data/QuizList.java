package com.example.quizmaster.data;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity
public class QuizList {

    @PrimaryKey(autoGenerate = true) //자동 ID 할당
    public int seq;

    @ColumnInfo(name = "queGb")
    public String queGb;

    @ColumnInfo(name = "question")
    public String question;

    @ColumnInfo(name = "answer")
    public String answer;

    @ColumnInfo(name = "useYn")
    public String useYn;



    public Integer getSeq() {
        return this.seq;
    }

    public void setQueGb(String queGb) {
        this.queGb = queGb;
    }

    public String getQueGb() {
        return this.queGb;
    }

    public void setQuestion(String question) {
        this.question = question;
    }

    public String getQuestion() {
        return this.question;
    }

    public void setAnswer(String answer) {
        this.answer = answer;
    }

    public String getAnswer() {
        return this.answer;
    }
}
