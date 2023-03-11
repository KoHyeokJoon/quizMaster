package com.example.quizmaster;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;


public class EndPage extends AppCompatActivity {


    Button button = null;
    TextView textView = null;
    Integer score;
    Integer stage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_end_page);

        button = findViewById(R.id.endBtn);
        textView = findViewById(R.id.totalScore);

        Intent intent = getIntent();
        if (intent.hasExtra("score")) {
            String msg = "";
            score = intent.getIntExtra("score", 0);
            stage = intent.getIntExtra("stage", 0) - 1;

//            if (stage - score <= 2) {
//                msg = "";
//            }

            textView.setText(score + "/" + stage);
        }else {
            //잘못된접근
            Toast.makeText(getApplicationContext(), "잘못된접근입니다.", Toast.LENGTH_SHORT).show();
            Intent b = new Intent(getApplicationContext(), MainActivity.class);
            finish();
            startActivity(b);
        }

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                finish();
                startActivity(intent);
                overridePendingTransition(R.anim.none, R.anim.right_to_left); //자연스럽게 이동
            }
        });

    }
}