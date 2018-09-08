package com.prathamubs.meridukan.db;

import android.content.Context;
import android.os.AsyncTask;

import java.util.Date;
import java.util.List;

public class DataRepository {
    private ScoreDao mScoreDao;
    private StudentDao mStudentDao;

    protected DataRepository(AppDatabase db) {
        mScoreDao = db.scoreDao();
        mStudentDao = db.studentDao();
    }

    public DataRepository(Context context) {
        this(AppDatabase.getDatabase(context));
    }

    public AsyncTask insertScore(Score score) {
        return new insertTask(mScoreDao).execute(score);
    }

    public AsyncTask insertStudent(Student student) {
        return new insertTask(mStudentDao).execute(student);
    }

    private static class insertTask<T> extends AsyncTask<T, Void, Void> {

        private final DataSource<T> mAsyncTaskDao;

        private insertTask(DataSource<T> dao) {
            mAsyncTaskDao = dao;
        }

        @Override
        protected Void doInBackground(final T... params) {
            mAsyncTaskDao.insert(params[0]);
            return null;
        }
    }

    public static class queryTask<T> extends AsyncTask<Void, Void, List<T>> {

        private final DataSource<T> mAsyncTaskDao;

        private queryTask(DataSource<T> dao) {
            mAsyncTaskDao = dao;
        }

        @Override
        protected List<T> doInBackground(final Void... params) {
            return mAsyncTaskDao.getAll();
        }
    }

    public AsyncTask<Void, Void, List<Student>> getStudentsAsync() {
        return new queryTask(mStudentDao).execute();
    }

    public List<Student> getStudentsModifiedAfter(Date date) {
        return mStudentDao.getModifiedAfter(date);
    }

    public List<Score> getScoresModifiedAfter(Date date) {
        return mScoreDao.getModifiedAfter(date);
    }
}
