package com.example.quizmaster;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;

import com.example.quizmaster.idiom.impl.IdiomFriendServiceImpl;
import com.example.quizmaster.idiom.impl.IdiomSingleServiceImpl;

public class IdiomInfo extends AppCompatActivity {

    Button idiomFriend = null;
    Button idiomSingle = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.idiom_info);

        idiomFriend = findViewById(R.id.idiomFriend);
        idiomSingle = findViewById(R.id.idiomSingle);

        Animation animation = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.show);
        idiomFriend.startAnimation(animation);
        idiomSingle.startAnimation(animation);

        idiomSingle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), IdiomSingleServiceImpl.class);
                startActivity(intent);
                overridePendingTransition(R.anim.none, R.anim.right_to_left); //자연스럽게 이동
                finish();
            }
        });

        idiomFriend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), IdiomFriendServiceImpl.class);
                startActivity(intent);
                overridePendingTransition(R.anim.none, R.anim.right_to_left); //자연스럽게 이동
                finish();
            }
        });
    }
}