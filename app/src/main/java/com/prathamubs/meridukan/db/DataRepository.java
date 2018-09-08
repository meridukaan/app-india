package com.prathamubs.meridukan.db;

import android.arch.lifecycle.LiveData;
import android.content.Context;
import android.os.AsyncTask;

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

        private final Insertable<T> mAsyncTaskDao;

        private insertTask(Insertable<T> dao) {
            mAsyncTaskDao = dao;
        }

        @Override
        protected Void doInBackground(final T... params) {
            mAsyncTaskDao.insert(params[0]);
            return null;
        }
    }

    public LiveData<List<Student>> getStudents() {
        return mStudentDao.getAll();
    }

    public List<Score> getScores() {
        return mScoreDao.getAll();
    }
}
