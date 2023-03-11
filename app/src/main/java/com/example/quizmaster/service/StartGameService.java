package com.example.quizmaster.service;


import android.content.Intent;

public interface StartGameService {

    void start(Intent intent);

    void move();

    void end() ;

}
