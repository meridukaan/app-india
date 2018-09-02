package com.prathamubs.meridukan.db;

import android.content.Context;
import android.os.AsyncTask;

public class DataRepository {
    private ScoreDao mScoreDao;

    public DataRepository(Context context) {
        AppDatabase db = AppDatabase.getDatabase(context);
        mScoreDao = db.scoreDao();
    }

    public void insertScore(Score score) {
        new insertScoreTask(mScoreDao).execute(score);
    }

    private static class insertScoreTask extends AsyncTask<Score, Void, Void> {

        private final ScoreDao mAsyncTaskDao;

        private insertScoreTask(ScoreDao dao) {
            mAsyncTaskDao = dao;
        }

        @Override
        protected Void doInBackground(final Score... params) {
            mAsyncTaskDao.insert(params[0]);
            return null;
        }
    }
}
