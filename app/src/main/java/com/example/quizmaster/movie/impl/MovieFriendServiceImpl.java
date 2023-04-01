package com.example.quizmaster.movie.impl;

import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.quizmaster.EndPage;
import com.example.quizmaster.idiom.IdiomInfo;
import com.example.quizmaster.MainActivity;
import com.example.quizmaster.R;
import com.example.quizmaster.data.QuizList;
import com.example.quizmaster.db.RunDataBase;
import com.example.quizmaster.service.StartGameService;

import java.util.List;

public class MovieFriendServiceImpl extends AppCompatActivity implements StartGameService {

    TextView question = null;
    TextView gameTimeInfo = null;
    TextView answer = null;
    Button giveup = null;
    QuizList quizList = new QuizList();
    Integer nextIndex;
    Integer stage;
    Integer score;
    Boolean isNext = true;
    CountDownTimer countDownTimer = null;

    static List<QuizList> quizLists = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game_friend);

        answer = findViewById(R.id.answer);
        giveup = findViewById(R.id.giveup);
        question = findViewById(R.id.question);
        gameTimeInfo = findViewById(R.id.gameTimeInfo);

        Integer time = Integer.parseInt(gameTimeInfo.getText().toString());

        countDownTimer = new CountDownTimer(5000, 1000) {
            public void onTick(long millisUntilFinished) {

                int num = (int) (millisUntilFinished / 1000);
                gameTimeInfo.setText(Integer.toString(num + 1));
            }

            public void onFinish() {
                move();
            }
        };

        int seq = 0;

        Intent intent = getIntent();

        start(intent);


        giveup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // 포기버튼 / move end page
                countDownTimer.cancel();
                Intent intent1 = new Intent(getApplicationContext(), IdiomInfo.class);
                startActivity(intent1);
                finish();
            }
        });
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        //뒤로가기 버튼 막음
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            return true;
        }

        return false;
    }


    @Override
    public void start(Intent intent) {


        stage = intent.hasExtra("stage") ? intent.getIntExtra("stage", 1) : 1;
//        score = intent.hasExtra("score") ? intent.getIntExtra("score", 0) : 0;
        nextIndex = intent.hasExtra("nextIndex") ? intent.getIntExtra("nextIndex", 0) : 0;

        if (stage == 1) {
            //첫 스테이지 세팅

            RunDataBase runDataBase = new RunDataBase(getApplicationContext(), quizList);

            String query = "SELECT * FROM QuizList WHERE queGb = 'movie' ";

            runDataBase.setQuery(query);
            runDataBase.setOrder("dynamic");
            runDataBase.start();

            try {
                runDataBase.join();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            quizLists = runDataBase.getResList();

            int maxNum = quizLists.size() - 9 < 1 ? 1 : quizLists.size() - 9;

            nextIndex = (int) (Math.random() * maxNum); // start number

            if (quizLists.size() == 0) {
                // list empty
                Toast.makeText(getApplicationContext(), "데이터를 가져올 수 없습니다.",  Toast.LENGTH_SHORT).show();
                Intent backIntent = new Intent(getApplicationContext(), MainActivity.class);
                finish();
                startActivity(backIntent);
                return;
            }

            if (stage >= 10) {
                end();
            }


        }

        countDownTimer.start();
        try {
            quizList = quizLists.get((nextIndex)); //index 처리를 위함
        } catch (IndexOutOfBoundsException indexOutOfBoundsException) {
            //다음 index 없음. 종료처리
            end();
        }

        question.setText(quizList.getQuestion());
        answer.setText(quizList.getAnswer());
    }

    @Override
    public void move() {
        countDownTimer.cancel();
        Intent intent1 = new Intent(getApplicationContext(), MovieFriendServiceImpl.class);
        intent1.putExtra("nextIndex", nextIndex + 1);
        intent1.putExtra("stage", stage + 1);
        startActivity(intent1);
        finish();
    }

    @Override
    public void end() {
        countDownTimer.cancel();
        Intent intent = new Intent(getApplicationContext(), EndPage.class);
//        intent.putExtra("score", this.score);
        intent.putExtra("stage", this.stage);
        intent.putExtra("gameGb", "F");
        intent.putExtra("queGb", "movie");
        finish();
        startActivity(intent);
        overridePendingTransition(R.anim.none, R.anim.right_to_left); //자연스럽게 이동
    }
}