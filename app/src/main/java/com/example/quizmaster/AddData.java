package com.example.quizmaster;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.quizmaster.data.QuizList;
import com.example.quizmaster.db.RunDataBase;

import org.apache.commons.lang3.StringUtils;

import java.sql.Time;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

public class AddData extends AppCompatActivity {

    Spinner queGbList = null;
    Button saveBtn = null;
    Button cancelBtn = null;
    Button hiddenAdm = null;
    Button hiddenAdmList = null;
    EditText addQuestion = null;
    EditText addAnswer = null;
    int hidden = 0;
    boolean isHid = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_data);

        queGbList = findViewById(R.id.queGbList);
        saveBtn = findViewById(R.id.saveBtn);
        cancelBtn = findViewById(R.id.cancelBtn);
        addQuestion = findViewById(R.id.addQuestion);
        hiddenAdm = findViewById(R.id.hiddenAdm);
        hiddenAdmList = findViewById(R.id.hiddenAdmList);
        addAnswer = findViewById(R.id.addAnswer);

        QuizList quizList = new QuizList();

        String[] valList = getResources().getStringArray(R.array.queListVal);


        ArrayAdapter adapter = ArrayAdapter.createFromResource(this, R.array.queList, android.R.layout.simple_spinner_dropdown_item);

        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        queGbList.setAdapter(adapter);

        /**
         * 구분 콤보 클릭 이벤트
         * quizlist 객체에 구분값을 저장한다
         */
        queGbList.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                quizList.setQueGb(valList[position]);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        /**
         * 저장버튼 클릭 이벤트
         * 데이터를 저장한다
         */
        saveBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                quizList.setQuestion(addQuestion.getText().toString().replace(" ", ""));
                quizList.setAnswer(addAnswer.getText().toString().replace(" ", ""));

                if (StringUtils.isEmpty(quizList.getQueGb()) || "".equals(quizList.getQueGb())) {
                    Toast.makeText(getApplicationContext(), "구분을 선택해주세요", Toast.LENGTH_SHORT).show();
                    return;
                }

                if (StringUtils.isEmpty(quizList.getQuestion()) || "".equals(quizList.getQuestion())) {
                    Toast.makeText(getApplicationContext(), "질문을 입력해주세요", Toast.LENGTH_SHORT).show();
                    return;
                }

                if (StringUtils.isEmpty(quizList.getAnswer()) || "".equals(quizList.getAnswer())) {
                    Toast.makeText(getApplicationContext(), "정답을 입력해주세요", Toast.LENGTH_SHORT).show();
                    return;
                }

                //정답 replace
                addAnswer.setText(addAnswer.getText().toString().replace(" ", ""));

                // main 접근불가, Thread 따기
                RunDataBase runDataBase = new RunDataBase(getApplicationContext()
                        , quizList);
                runDataBase.setOrder("insert");
//                runDataBase.setOrder("dynamic");
//                runDataBase.setQuery("SELECT * FROM QuizList");
                runDataBase.start();

                try {
                    runDataBase.join();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

                if (runDataBase.getCode().equals("0000")) {
                    Toast.makeText(getApplicationContext(), "저장되었습니다.", Toast.LENGTH_SHORT).show();
                } else if (runDataBase.getCode().equals("8888")) {
                    Toast.makeText(getApplicationContext(), "[저장실패] 중복된 정답이 있습니다.", Toast.LENGTH_SHORT).show();
                } else if (runDataBase.getCode().equals("2222")) {
                    Toast.makeText(getApplicationContext(), "[저장실패] 시스템 오류", Toast.LENGTH_SHORT).show();
                }

            }
        });

        /**
         * 취소버튼 클릭 이벤트
         * 화면을 종료시킨다
         */
        cancelBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                Intent intent = new Intent(getApplicationContext(), MainActivity.class);
//                startActivity(intent);
//                overridePendingTransition(R.anim.none, R.anim.right_to_left); //자연스럽게 이동
                finish();
            }
        });

        /**
         * 왼쪽 하단 터치시 이벤트
         * 모든데이터를 삭제처리하고 첫 사용자 플래그값을 true로 변경한다.
         */
        hiddenAdm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Timer t = new Timer();
                TimerTask timerTask = new TimerTask() {
                    @Override
                    public void run() {
                        hidden = 0;
                        if (isHid) {
                            return;
                        }
                        isHid = false;
                    }
                };

                t.schedule(timerTask, 1000);

                //1초안에 5번정도?
                hidden++;
                Log.d("MSG", "COUNT ->" + hidden);

                if (hidden > 5) {
                    //'모든데이터' 삭제시
                    if (addQuestion.getText().toString().equals("모든데이터")) {
                        RunDataBase runDataBase = new RunDataBase(getApplicationContext(), quizList);
                        runDataBase.setOrder("allDelete");

                        runDataBase.start();


                        try {
                            runDataBase.join();
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }

                        if (runDataBase.getCode().equals("0000")) {
                            SharedPreferences pref = getSharedPreferences("isFirst", Activity.MODE_PRIVATE);
                            SharedPreferences.Editor editor = pref.edit();
                            editor.putBoolean("isFirst",true);
                            editor.commit();
                            Toast.makeText(getApplicationContext(), "[모든데이터]삭제완료", Toast.LENGTH_SHORT).show();
                        }


                    }else {
                        //일부 데이터 삭제시
                        if (StringUtils.isEmpty(quizList.getQueGb()) || "".equals(quizList.getQueGb())) {
                            Toast.makeText(getApplicationContext(), "구분을 선택해주세요", Toast.LENGTH_SHORT).show();
                            return;
                        }

                        if (isHid) {
                            Toast.makeText(getApplicationContext(), "이미 삭제가 완료되었습니다.", Toast.LENGTH_SHORT).show();
                            return;
                        }
                        isHid = true;


                        RunDataBase runDataBase = new RunDataBase(getApplicationContext(), quizList);
                        runDataBase.setOrder("delete");

                        runDataBase.start();


                        try {
                            runDataBase.join();
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }

                        if (runDataBase.getCode().equals("0000")) {
                            SharedPreferences pref = getSharedPreferences("isFirst", Activity.MODE_PRIVATE);
                            SharedPreferences.Editor editor = pref.edit();
                            editor.putBoolean("isFirst",true);
                            editor.commit();
                            Toast.makeText(getApplicationContext(), "삭제완료", Toast.LENGTH_SHORT).show();
                        }
                    }

                }
            }
        });

        /**
         * 오른쪽 하단 터치시 이벤트
         * 데이터를 조회하여 LOG로 표출한다
         */
        hiddenAdmList.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Timer t = new Timer();
                TimerTask timerTask = new TimerTask() {
                    @Override
                    public void run() {
                        hidden = 0;
                        isHid = false;
                    }
                };

                t.schedule(timerTask, 1000);

                //1초안에 5번정도?
                hidden++;
                Log.d("MSG", "COUNT ->" + hidden);

                if (hidden > 5) {

                    isHid = true;

                    String query = "SELECT * FROM QuizList ORDER BY queGb ";

                    RunDataBase runDataBase = new RunDataBase(getApplicationContext(), quizList);
                    runDataBase.setOrder("dynamic");
                    runDataBase.setQuery(query);

                    runDataBase.start();


                    try {
                        runDataBase.join();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }finally {
//                        isHid = false; //한 액티비티에 한번만으로 변경 2023.02.19

                    }

                    List<QuizList> list = runDataBase.getResList();

                    //TODO : 일단 log로 처리
                    for (QuizList item : list) {
                        Log.d("QUE_GB -- DETAIL", item.getQueGb());
                        Log.d("QUESTION -- DETAIL", item.getQuestion());
                        Log.d("ANSWER -- DETAIL", item.getAnswer());
                    }

                }
            }
        });

        /**
         * ENTER 이벤트
         * 엔터를 눌렀을 시 키보드를 내린다.
         */
        addQuestion.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View view, int i, KeyEvent keyEvent) {
                switch (i){
                    case KeyEvent.KEYCODE_ENTER:
                        InputMethodManager imm = (InputMethodManager) getSystemService(Activity.INPUT_METHOD_SERVICE);
                        imm.hideSoftInputFromWindow(addQuestion.getWindowToken(), 0);    //hide keyboard
                        break;
                }
                return false;

            }
        });

        /**
         * ENTER 이벤트
         * 엔터를 눌렀을 시 키보드를 내린다.
         */
        addAnswer.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View view, int i, KeyEvent keyEvent) {
                switch (i){
                    case KeyEvent.KEYCODE_ENTER:
                        InputMethodManager imm = (InputMethodManager) getSystemService(Activity.INPUT_METHOD_SERVICE);
                        imm.hideSoftInputFromWindow(addAnswer.getWindowToken(), 0);    //hide keyboard
                        break;
                }
                return false;

            }
        });
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

}