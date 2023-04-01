package com.example.quizmaster.sudo.impl;

import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
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

public class SudoSingleServiceImpl extends AppCompatActivity implements StartGameService {

    TextView question = null;
    EditText idiomAnswer = null;
    Button idiomResult = null;
    Button idiomGiveup = null;
    QuizList quizList = new QuizList();
    Integer nextIndex;
    Integer stage;
    Integer score;
    Boolean isNext = true;

    static List<QuizList> quizLists = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_sudo_single);
        int seq = 0;

        idiomAnswer = findViewById(R.id.idiomAnswer);
        idiomResult = findViewById(R.id.idiomResult);
        idiomGiveup = findViewById(R.id.idiomGiveup);
        question = findViewById(R.id.question);

        Intent intent = getIntent();

        start(intent);

        idiomResult.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //확인버튼
                String res = idiomAnswer.getText().toString();

                if ("".equals(res)) {
                    Toast.makeText(getApplicationContext(), "정답을 입력해주세요.", Toast.LENGTH_SHORT).show();
                    return;
                }

                if (res.replace(" ", "").equals(quizList.getAnswer().replace(" ", ""))) {
                    //정답 !
                }else {
                    //오답 !
                }

                move();
            }
        });

        idiomGiveup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // 포기버튼 / move end page
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
        score = intent.hasExtra("score") ? intent.getIntExtra("score", 0) : 0;
        nextIndex = intent.hasExtra("nextIndex") ? intent.getIntExtra("nextIndex", 0) : 0;

        if (stage == 1) {
            //첫 스테이지 세팅

            RunDataBase runDataBase = new RunDataBase(getApplicationContext(), quizList);

            String query = "SELECT * FROM QuizList WHERE queGb = 'sudo' ";

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

        try {
            quizList = quizLists.get((nextIndex)); //index 처리를 위함
        } catch (IndexOutOfBoundsException indexOutOfBoundsException) {
            //다음 index 없음. 종료처리
            end();
        }

        question.setText(quizList.getQuestion());
    }

    @Override
    public void move() {
        Intent intent1 = new Intent(getApplicationContext(), SudoSingleServiceImpl.class);
        intent1.putExtra("nextIndex", nextIndex + 1);
        intent1.putExtra("stage", stage + 1);
        intent1.putExtra("score", this.score);
        startActivity(intent1);
        finish();
    }

    @Override
    public void end() {

        Intent intent = new Intent(getApplicationContext(), EndPage.class);
        intent.putExtra("score", this.score);
        intent.putExtra("stage", this.stage);
        finish();
        startActivity(intent);
        overridePendingTransition(R.anim.none, R.anim.right_to_left); //자연스럽게 이동
    }
}