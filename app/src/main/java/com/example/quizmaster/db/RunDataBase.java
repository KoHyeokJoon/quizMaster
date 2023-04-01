package com.example.quizmaster.db;

import android.content.Context;
import android.os.Handler;
import android.os.Looper;

import androidx.sqlite.db.SimpleSQLiteQuery;

import com.example.quizmaster.dao.GameScoreDAO;
import com.example.quizmaster.dao.QuizListDAO;
import com.example.quizmaster.data.GameScore;
import com.example.quizmaster.data.QuizList;

import org.apache.commons.lang3.StringUtils;

import java.util.List;

public class RunDataBase extends Thread {

    private static DataBase dataBase = null;
    private Context context;
    private static QuizListDAO quizListDAO = null;
    private static GameScoreDAO gameScoreDAO = null;
    private String order;
    private QuizList quizList;
    private String query;
    private List<QuizList> resList = null;
    private String code = "9999";
    private GameScore gameScore = null;
    private int per = 0;


    public RunDataBase(Context context) {
        //실행, db 생성, 접근
        dataBase = DataBase.getInstance(context);

        //dao 정의
        quizListDAO = dataBase.quizListDAO();

        this.context = context;
    }

    //생성자, order, model 필수
    public RunDataBase(Context context, QuizList quizList) {
        //실행, db 생성, 접근
        dataBase = DataBase.getInstance(context);

        //dao 정의
        quizListDAO = dataBase.quizListDAO();

        this.context = context;
        this.quizList = quizList;
    }


    public void run() {

        try {
            if (StringUtils.isEmpty(order)) {
                // order가 없을 시 핸들러로 감싸고 메세지 띄우기
                code = "1111";
            }else {
                // start order
                switch (order) {
                    case "insert":
                        //INSERT
                        if (quizListDAO.dupCount(quizList.getQueGb(), quizList.getAnswer()) > 0) {
                            //duplicate
                            code = "8888";
                            break;
                        }else {
                            quizListDAO.insertIdiomList(quizList);
                            code = "0000";
                            break;
                        }
                    case "update":
                        quizListDAO.updateIdiom(quizList);
                        code = "0000";
                        break;
                    case "delete":
                        quizListDAO.deleteItems(quizList.getQueGb());
                        code = "0000";
                        break;
                    case "dynamic":
                        if (StringUtils.isEmpty(query)) {
                            code = "3333";
                            throw new Exception("");
                        }
                        SimpleSQLiteQuery simpleSQLiteQuery = new SimpleSQLiteQuery(query);
                        resList = quizListDAO.dynamic(simpleSQLiteQuery);
                        code = "0000";
                        break;
                    case "score_insert":
                        gameScoreDAO.insertGameScore(gameScore);
                        code = "0000";
                        break;
                    case "score_select":
                        int totCnt = gameScoreDAO.getGameScoreCnt(gameScore.getQueGb());
                        int rank = gameScoreDAO.getRank(gameScore.getQueGb(), gameScore.getScore()) + 1;

                        this.per = rank / totCnt * 100;

                        code = "0000";
                        break;
                }

            }
        }catch (Exception e) {
            e.printStackTrace();
            code = "2222";
        }finally {
            Handler handler = new Handler(Looper.getMainLooper());
            String finalCode = code;

            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
//                    if (!finalCode.equals("0000")) {
//                        Toast.makeText(context, "오류가 발생했습니다.", Toast.LENGTH_SHORT).show();
//                    }
                }
            }, 0);
        }

    }

    public List<QuizList> getResList() {
        return resList;
    }

    public String getCode() {
        return code;
    }

    public Integer getPer() {
        return per;
    }

    public void setOrder(String order) {
        this.order = order;
    }

    public void setQuery(String query) {
        this.query = query;
    }

    public void setQuizList(QuizList quizList) {
        this.quizList = quizList;
    }

    public void setGameScore(GameScore gameScore) {
        this.gameScore = gameScore;
    }
}
