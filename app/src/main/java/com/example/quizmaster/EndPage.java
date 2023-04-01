package com.example.quizmaster;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.quizmaster.data.GameScore;
import com.example.quizmaster.db.RunDataBase;


public class EndPage extends AppCompatActivity {


    Button button = null;
    TextView textView = null;
    Integer score;
    Integer stage;
    String gameGb;
    String queGb;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_end_page);

        button = findViewById(R.id.endBtn);
        textView = findViewById(R.id.totalScore);

        Intent intent = getIntent();

        // gameGb 'F' : Friend, 'S' : Single
        if (intent.hasExtra("gameGb")) {
            String msg = "";
            score = intent.getIntExtra("score", 0);
            stage = intent.getIntExtra("stage", 0) - 1;
            gameGb = intent.getStringExtra("gameGb"); // friend or single
            queGb = intent.getStringExtra("queGb");

            if (gameGb.equals("F")) {
                // Friend 처리
                textView.setText("사람들과 재밌게 플레이 하셨나요?\n자신만의 질문과 답변을 만들어보세요!\n(설정에서 질문과 답변을 만들 수 있습니다.)");
            } else if (gameGb.equals("S")) {
                // Single 처리 상위 퍼센트로 텍스트 다르게처리
                
                // 하,, ui가 더 어렵다
                int point = 100 / stage; // 문제당 점수
                int singleScore = score * point; // 최종점수

                RunDataBase runDataBase = new RunDataBase(getApplicationContext());
                GameScore gameScore = new GameScore();

                gameScore.setQueGb(queGb);
                gameScore.setScore(singleScore);

                runDataBase.setOrder("score_select");
                runDataBase.setGameScore(gameScore);

                runDataBase.start();

                try {
                    runDataBase.join();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

                int per = runDataBase.getPer(); //상위 %

                /**
                 * score insert ***************
                 * */
                runDataBase.setOrder("score_insert");

                runDataBase.start();

                try {
                    runDataBase.join();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

//                textView.setText(score + "/" + stage);
                if (per <= 30) {
                    // 상위 30이상
                    textView.setText("상위 " + per + "% 입니다. 축하드립니다!\n자신만의 질문과 답변을 만들어보세요!\n(설정에서 질문과 답변을 만들 수 있습니다.)");
                }else if (per <= 60) {
                    // 상위 60이상
                    textView.setText("상위 " + per + "% 입니다. 조금만 더 노력해보세요!\n자신만의 질문과 답변을 만들어보세요!\n(설정에서 질문과 답변을 만들 수 있습니다.)");
                }else {
                    textView.setText("상위 " + per + "% 입니다. 많이 노력하셔야 겠군요!\n자신만의 질문과 답변을 만들어보세요!\n(설정에서 질문과 답변을 만들 수 있습니다.)");
                }
            }

//            if (stage - score <= 2) {
//                msg = "";
//            }

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