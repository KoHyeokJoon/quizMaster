package com.example.quizmaster.sudo;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

import com.example.quizmaster.R;
import com.example.quizmaster.idiom.impl.IdiomFriendServiceImpl;
import com.example.quizmaster.idiom.impl.IdiomSingleServiceImpl;
import com.example.quizmaster.sudo.impl.SudoFriendServiceImpl;
import com.example.quizmaster.sudo.impl.SudoSingleServiceImpl;

public class SudoInfo extends AppCompatActivity {

    Button sudoFriend = null;
    Button sudoSingle = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.sudo_info);

        sudoFriend = findViewById(R.id.sudoFriend);
        sudoSingle = findViewById(R.id.sudoSingle);

        Animation animation = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.show);
        sudoFriend.startAnimation(animation);
        sudoSingle.startAnimation(animation);

        sudoSingle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), SudoSingleServiceImpl.class);
                startActivity(intent);
                overridePendingTransition(R.anim.none, R.anim.right_to_left); //자연스럽게 이동
                finish();
            }
        });

        sudoFriend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), SudoFriendServiceImpl.class);
                startActivity(intent);
                overridePendingTransition(R.anim.none, R.anim.right_to_left); //자연스럽게 이동
                finish();
            }
        });
    }
}