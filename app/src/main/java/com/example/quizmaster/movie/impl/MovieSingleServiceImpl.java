package com.example.quizmaster.movie.impl;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.quizmaster.EndPage;
import com.example.quizmaster.MainActivity;
import com.example.quizmaster.R;
import com.example.quizmaster.data.QuizList;
import com.example.quizmaster.db.RunDataBase;
import com.example.quizmaster.movie.MovieInfo;
import com.example.quizmaster.service.StartGameService;

import java.util.List;

public class MovieSingleServiceImpl extends AppCompatActivity implements StartGameService {

    TextView question = null;
    EditText answer = null;
    Button result = null;
    Button giveup = null;
    QuizList quizList = new QuizList();
    Integer nextIndex;
    Integer stage;
    Integer score;
    Boolean isNext = true;

    static List<QuizList> quizLists = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_game_single);
        int seq = 0;

        answer = findViewById(R.id.answer);
        result = findViewById(R.id.result);
        giveup = findViewById(R.id.giveup);
        question = findViewById(R.id.question);

        Intent intent = getIntent();

        start(intent);

        /**************************************************************************
         * ************************************************************************
         * Event
         * ************************************************************************
         * ************************************************************************
         */

        result.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(checkResult()) {
                    move();
                }
            }
        });

        giveup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // 포기버튼 / move end page
                Intent intent1 = new Intent(getApplicationContext(), MovieInfo.class);
                startActivity(intent1);
                finish();
            }
        });

        /** EditText 관련 event */
        answer.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int i, KeyEvent keyEvent) {
                InputMethodManager imm = (InputMethodManager) getSystemService(Activity.INPUT_METHOD_SERVICE);

                if(checkResult()) {
                    imm.hideSoftInputFromWindow(answer.getWindowToken(), 0);    //hide keyboard
                    move();
                }
                return true;
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

    /**
     * editText 외 터치시 키보드 내리기
     * @param ev
     * @return
     */
    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        View view = getCurrentFocus();
        if (view != null && (ev.getAction() == MotionEvent.ACTION_UP || ev.getAction() == MotionEvent.ACTION_MOVE) && view instanceof EditText && !view.getClass().getName().startsWith("android.webkit.")) {
            int scrcoords[] = new int[2];
            view.getLocationOnScreen(scrcoords);
            float x = ev.getRawX() + view.getLeft() - scrcoords[0];
            float y = ev.getRawY() + view.getTop() - scrcoords[1];
            if (x < view.getLeft() || x > view.getRight() || y < view.getTop() || y > view.getBottom())
                ((InputMethodManager)this.getSystemService(Context.INPUT_METHOD_SERVICE)).hideSoftInputFromWindow((this.getWindow().getDecorView().getApplicationWindowToken()), 0);
        }

        return super.dispatchTouchEvent(ev);
    }

    @Override
    public void start(Intent intent) {

        stage = intent.hasExtra("stage") ? intent.getIntExtra("stage", 1) : 1;
        score = intent.hasExtra("score") ? intent.getIntExtra("score", 0) : 0;
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

            int maxNum = quizLists.size() - 4 < 1 ? 1 : quizLists.size() - 4;

            nextIndex = (int) (Math.random() * maxNum); // start number

            if (quizLists.size() == 0) {
                // list empty
                Toast.makeText(getApplicationContext(), "데이터를 가져올 수 없습니다.",  Toast.LENGTH_SHORT).show();
                Intent backIntent = new Intent(getApplicationContext(), MainActivity.class);
                finish();
                startActivity(backIntent);
                return;
            }

        }


        /**
         * 리스트 초과 또는 스테이지설정값 초과시 end (5로 고정) 2023.04.01
         */

        if (stage >= 6) {
            end();
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
        Intent intent1 = new Intent(getApplicationContext(), MovieSingleServiceImpl.class);
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
        intent.putExtra("gameGb", "S");
        intent.putExtra("queGb", "movie");
        finish();
        startActivity(intent);
        overridePendingTransition(R.anim.none, R.anim.right_to_left); //자연스럽게 이동
    }

    private boolean checkResult() {
        //확인버튼
        String res = answer.getText().toString();

        if ("".equals(res)) {
            Toast.makeText(getApplicationContext(), "정답을 입력해주세요.", Toast.LENGTH_SHORT).show();
            return false;
        }

        if (res.replace(" ", "").equals(quizList.getAnswer().replace(" ", ""))) {
            //정답 !
            this.score ++;
        }else {
            //오답 !
        }

        return true;
    }
}