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
        return new insertTask<>(mScoreDao).execute(score);
    }

    public AsyncTask insertStudent(Student student) {
        return new insertTask<>(mStudentDao).execute(student);
    }

    public AsyncTask updateStudent(Student student) {
        return new updateTask<>(mStudentDao).execute(student);
    }

    public void deleteStudent(String studentId) {
        new deleteStudent(mStudentDao).execute(studentId);
    }

    private static abstract class changeTask<T> extends AsyncTask<T, Void, Void> {
        protected final DataSource<T> mAsyncTaskDao;
        private changeTask(DataSource<T> dao) {
            mAsyncTaskDao = dao;
        }
    }

    private static class updateTask<T> extends changeTask<T> {
        private updateTask(DataSource<T> dao) {
            super(dao);
        }

        @Override
        protected Void doInBackground(final T... params) {
            mAsyncTaskDao.update(params[0]);
            return null;
        }
    }

    private static class insertTask<T> extends changeTask<T> {
        private insertTask(DataSource<T> dao) {
            super(dao);
        }

        @Override
        protected Void doInBackground(final T... params) {
            mAsyncTaskDao.insert(params[0]);
            return null;
        }
    }

    private static class deleteStudent extends AsyncTask<String, Void, List<Student>> {
        protected final StudentDao mAsyncTaskDao;

        private deleteStudent(StudentDao dao) {
            this.mAsyncTaskDao = dao;
        }

        @Override
        protected List<Student> doInBackground(String... params) {
            List<Student> items = mAsyncTaskDao.findByKey(params);
            mAsyncTaskDao.delete(items.toArray(new Student[items.size()]));
            return items;
        }
    }

    private static class queryAllTask<T> extends AsyncTask<Void, Void, List<T>> {

        private final DataSource<T> mAsyncTaskDao;

        private queryAllTask(DataSource<T> dao) {
            mAsyncTaskDao = dao;
        }

        @Override
        protected List<T> doInBackground(final Void... params) {
            return mAsyncTaskDao.getAll();
        }
    }

    public AsyncTask<Void, Void, List<Student>> getStudentsAsync() {
        return new queryAllTask<>(mStudentDao).execute();
    }

    public List<Student> getStudentsModifiedAfter(Date date) {
        return mStudentDao.getModifiedAfter(date);
    }

    public List<Score> getScoresModifiedAfter(Date date) {
        return mScoreDao.getModifiedAfter(date);
    }
}
