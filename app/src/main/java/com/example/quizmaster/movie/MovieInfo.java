package com.example.quizmaster.movie;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

import com.example.quizmaster.R;
import com.example.quizmaster.movie.impl.MovieFriendServiceImpl;
import com.example.quizmaster.movie.impl.MovieSingleServiceImpl;

public class MovieInfo extends AppCompatActivity {

    Button startFriend = null;
    Button startSingle = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.game_info);

        startFriend = findViewById(R.id.startFriend);
        startSingle = findViewById(R.id.startSingle);

        Animation animation = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.show);
        startFriend.startAnimation(animation);
        startSingle.startAnimation(animation);

        startSingle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), MovieSingleServiceImpl.class);
                startActivity(intent);
                overridePendingTransition(R.anim.none, R.anim.right_to_left); //자연스럽게 이동
                finish();
            }
        });

        startFriend.setOnClickListener(new View.OnClickListener() {
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