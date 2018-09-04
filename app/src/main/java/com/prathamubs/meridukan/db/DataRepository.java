package com.prathamubs.meridukan.db;

import android.content.Context;
import android.os.AsyncTask;

public class DataRepository {
    private ScoreDao mScoreDao;

    public DataRepository(Context context, AppDatabase db) {
        mScoreDao = db.scoreDao();
    }

    public DataRepository(Context context) {
        this(context, AppDatabase.getDatabase(context));
    }

    public AsyncTask<Score, Void, Void> insertScore(Score score) {
        return new insertScoreTask(mScoreDao).execute(score);
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
