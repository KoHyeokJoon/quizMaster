package com.example.quizmaster;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.motion.widget.MotionLayout;

import com.example.quizmaster.data.QuizList;
import com.example.quizmaster.db.RunDataBase;
import com.example.quizmaster.idiom.IdiomInfo;
import com.example.quizmaster.movie.MovieInfo;
import com.example.quizmaster.sudo.SudoInfo;

import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;

public class MainActivity extends AppCompatActivity {
    private int selectedIndex;
    private HashMap _$_findViewCache;

    InputStream in = null;
    CustomDialog customDialog = null;
    Intent intent = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        intent = new Intent(getApplicationContext(), MainActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);//액티비티 스택제거

        final MotionLayout motionLayout = (MotionLayout)this.findViewById(R.id.motion_container);

        View mainSudoQuiz = this.findViewById(R.id.mainSudoQuiz);
        View mainIdiomQuiz = this.findViewById(R.id.mainIdiomQuiz);
        View mainMovieQuiz = this.findViewById(R.id.mainMovieQuiz);
        View addDataBtn = this.findViewById(R.id.addDataBtn);

        if (isStart()) {
            Log.d("Loading", "데이터 받아오는중 ...");
            // 최초실행시 DATA 받아오기
            customDialog = new CustomDialog(this);
            //로딩창을 투명하게
            customDialog.setContentView(R.layout.custom_dialog);
            customDialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
            customDialog.setCancelable(false);
            customDialog.show();

            QuizList quizList = new QuizList();

            String s = "";

            in = getResources().openRawResource(R.raw.data);
            try {
                byte[] b = new byte[in.available()];
                in.read(b);
                s = new String(b);
            } catch (Exception e) {
                e.printStackTrace();
            }

            String[] dataList = s.split("D000");

            for (String d : dataList) {
                if (d.length() < 1) {
                    continue;
                }
                RunDataBase runDataBase = new RunDataBase(getApplicationContext());

                String queGb = d.substring(0, 10).replace(" ", "");
                String question = d.substring(d.indexOf("question") + 8, d.indexOf("answer")).replaceAll("(\r\n|\r|\n|\n\r)", "")
                        .replace(" ", "");
                String answer = d.substring(d.indexOf("answer") + 6, d.length()).replaceAll("(\r\n|\r|\n|\n\r)", "")
                        .replace(" ", "");

                quizList.setQueGb(queGb);
                quizList.setQuestion(question);
                quizList.setAnswer(answer);

                runDataBase.setQuizList(quizList);
                runDataBase.setOrder("insert");
                runDataBase.start();

                try {
                    runDataBase.join();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

//                if (!"0000".equals(runDataBase.getCode())) {
//                    //error or duplicated
//                    break;
//                }
            }

            Log.d("Loading", "데이터 받아오기 완료 ...");

            try {
                in.close();
            } catch (IOException e) {
                e.printStackTrace();
                Log.d("Loading", "데이터 받아오기 실패 ...");

            }finally {
                customDialog.dismiss();
            }
        }


        /**
         * drag
         */
//        motionLayout.setOnTouchListener(new View.OnTouchListener() {
//            @Override
//            public boolean onTouch(View view, MotionEvent motionEvent) {
//
//                float x = 0;
//                if (motionEvent.getAction() == MotionEvent.ACTION_DOWN) {
//                    // 최초 터치 x값 저장
//                    x = motionEvent.getX();
//                    Log.d("down touch", String.valueOf(motionEvent.getX()));
//                }
//
//                if (motionEvent.getAction() == MotionEvent.ACTION_UP) {
//                    // 손가락을 뗐을때
//                    x -= motionEvent.getX();
//
//                    Log.d("up touch", String.valueOf(motionEvent.getX()));
//
//                    if (Math.abs(x) < 100) {
//                        return false;
//                    }
//
//                    if (x > 0) {
//                        // right -> left
//                        switch (MainActivity.this.selectedIndex) {
//                            case 0:
//                                //현재 sudo인 상태
//
//                        }
//                    }
//
//                }
//
//                return false;
//            }
//        });

        mainSudoQuiz.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int num = MainActivity.this.selectedIndex;

                if (num == 0) {
                    //자기자신
                    intent = new Intent(getApplicationContext(), SudoInfo.class);
                    startActivity(intent);
                    overridePendingTransition(R.anim.none, R.anim.right_to_left); //자연스럽게 이동
                }else {
                    if (num == 2) {
                        // 2번일때 1번으로
                        motionLayout.setTransition(R.id.s3, R.id.s2);
                        motionLayout.transitionToEnd();
                        MainActivity.this.selectedIndex = 1;
                    }else {
                        // 1번일때 0번으로
                        motionLayout.setTransition(R.id.s2, R.id.s1);
                        motionLayout.transitionToEnd();
                        MainActivity.this.selectedIndex = 0;
                    }
                }
            }
        });
//
        mainIdiomQuiz.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int num = MainActivity.this.selectedIndex;

                if (num == 1) {
                    //자기자신
                    intent = new Intent(getApplicationContext(), IdiomInfo.class);
                    startActivity(intent);
                    overridePendingTransition(R.anim.none, R.anim.right_to_left); //자연스럽게 이동
                }else {
                    if (num == 3) {
                        // 3번일때 2번으로
                        motionLayout.setTransition(R.id.s4, R.id.s3);
                        motionLayout.transitionToEnd();
                        MainActivity.this.selectedIndex = 2;
                    }else if(num == 2){
                        // 2번일때 1번으로
                        motionLayout.setTransition(R.id.s3, R.id.s2);
                        motionLayout.transitionToEnd();
                        MainActivity.this.selectedIndex = 1;
                    }else {
                        // 좌측에서 우측
                        motionLayout.setTransition(R.id.s1, R.id.s2);
                        motionLayout.transitionToEnd();
                        MainActivity.this.selectedIndex = 1;
                    }
                }
            }
        });

        mainMovieQuiz.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int num = MainActivity.this.selectedIndex;

                if (num == 2) {
                    //자기자신
                    intent = new Intent(getApplicationContext(), MovieInfo.class);
                    startActivity(intent);
                    overridePendingTransition(R.anim.none, R.anim.right_to_left); //자연스럽게 이동
                }else {
                    if (num == 3) {
                        // 3번일때 2번으로
                        motionLayout.setTransition(R.id.s4, R.id.s3);
                        motionLayout.transitionToEnd();
                        MainActivity.this.selectedIndex = 2;
                    }else {
                        // 좌측에서 우측
                        motionLayout.setTransition(R.id.s2, R.id.s3);
                        motionLayout.transitionToEnd();
                        MainActivity.this.selectedIndex = 2;
                    }
                }
            }
        });


        addDataBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int num = MainActivity.this.selectedIndex;

                if (num == 3) {
                    //자기자신
                    intent = new Intent(getApplicationContext(), AddData.class);
                    startActivity(intent);
                    overridePendingTransition(R.anim.none, R.anim.right_to_left); //자연스럽게 이동
                }else {
                    // 좌측에서 우측
                    motionLayout.setTransition(R.id.s3, R.id.s4);
                    motionLayout.transitionToEnd();
                    MainActivity.this.selectedIndex = 3;
                }
            }
        });
    }

    public View _$_findCachedViewById(int var1) {
        if (this._$_findViewCache == null) {
            this._$_findViewCache = new HashMap();
        }

        View var2 = (View)this._$_findViewCache.get(var1);
        if (var2 == null) {
            var2 = this.findViewById(var1);
            this._$_findViewCache.put(var1, var2);
        }

        return var2;
    }

    public void _$_clearFindViewByIdCache() {
        if (this._$_findViewCache != null) {
            this._$_findViewCache.clear();
        }

    }


    private boolean isStart() {

        SharedPreferences pref = getSharedPreferences("isFirst", Activity.MODE_PRIVATE);
        boolean first = pref.getBoolean("isFirst", false);

        if(first==false){
            SharedPreferences.Editor editor = pref.edit();
            editor.putBoolean("isFirst",true);
            editor.commit();
            return true;
        }else{
            return false;
        }
    }
}
