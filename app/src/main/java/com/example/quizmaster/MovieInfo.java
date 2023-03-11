package com.example.quizmaster;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

import com.example.quizmaster.idiom.impl.IdiomFriendServiceImpl;
import com.example.quizmaster.idiom.impl.IdiomSingleServiceImpl;
import com.example.quizmaster.movie.impl.MovieFriendServiceImpl;
import com.example.quizmaster.movie.impl.MovieSingleServiceImpl;

public class MovieInfo extends AppCompatActivity {

    Button movieFriend = null;
    Button movieSingle = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.movie_info);

        movieFriend = findViewById(R.id.movieFriend);
        movieSingle = findViewById(R.id.movieSingle);

        Animation animation = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.show);
        movieFriend.startAnimation(animation);
        movieSingle.startAnimation(animation);

        movieSingle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), MovieSingleServiceImpl.class);
                startActivity(intent);
                overridePendingTransition(R.anim.none, R.anim.right_to_left); //자연스럽게 이동
                finish();
            }
        });

        movieFriend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), MovieFriendServiceImpl.class);
                startActivity(intent);
                overridePendingTransition(R.anim.none, R.anim.right_to_left); //자연스럽게 이동
                finish();
            }
        });
    }
}