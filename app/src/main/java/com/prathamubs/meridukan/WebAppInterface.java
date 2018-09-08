package com.prathamubs.meridukan;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.preference.PreferenceManager;
import android.provider.Settings;
import android.util.Log;
import android.webkit.JavascriptInterface;

import com.prathamubs.meridukan.db.Converters;
import com.prathamubs.meridukan.db.DataRepository;
import com.prathamubs.meridukan.db.Score;
import com.prathamubs.meridukan.db.Student;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.ExecutionException;

public class WebAppInterface {
    Context mContext;
    DataRepository mRepository;
    String mDeviceId;
    SharedPreferences mSharedPreferences;

    private final static String TAG = WebAppInterface.class.getName();
    private final static String SELECTED_STUDENT_KEY = "SelectedStudent";
    private final static String SELECTED_LANGUAGE_KEY = "SelectedLanguage";

    AsyncTask<?, ?, List<Student>> mStudentsQueryTask;

    WebAppInterface(Context context) {
        mContext = context;

        mRepository = new DataRepository(context);
        mStudentsQueryTask = mRepository.getStudentsAsync();

        mDeviceId = Settings.Secure.getString(mContext.getContentResolver(), Settings.Secure.ANDROID_ID);
        if (mDeviceId == null) {
            mDeviceId = "0000";
        }

        mSharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
    }

    @JavascriptInterface
    public void addScore(String studentId, int questionId, int scorefromGame, int totalMarks,
                         int level, String startTime, String label) {
        // TODO confirm significance with Pratham team and populate accordingly
        String sessionId = "sid";
        String groupId = "";
        Date endDateTime = Calendar.getInstance().getTime();
        Date startDateTime = Converters.dateFromString(startTime);
        Score score = new Score(sessionId, groupId, mDeviceId, studentId, questionId, scorefromGame,
                totalMarks, startDateTime, endDateTime, level, label);
        mRepository.insertScore(score);
    }

    @JavascriptInterface
    public List<Student> getStudentList() {
        return new ArrayList();
    }

    @JavascriptInterface
    public List<Student> getStoredStudentList() {
        List<Student> students = null;
        try {
            students = mStudentsQueryTask.get();
        } catch (ExecutionException e) {
            Log.e(TAG, "Error getting students", e);
        } catch (InterruptedException e) {
            Log.w(TAG, "Error getting students", e);
        }
        return students != null ? students : new ArrayList();
    }

    @JavascriptInterface
    public void addStudent(String firstName, String middleName, String lastName, int age, int cls,
                             String gender) {
        Student student = new Student();
        student.StudentID = UUID.randomUUID().toString();
        student.FirstName = firstName;
        student.MiddleName = middleName;
        student.LastName = lastName;
        student.Age = age;
        student.Class = cls;
        student.Gender = gender;
        student.UpdatedDate = student.CreatedOn = Calendar.getInstance().getTime();
        student.appName = mContext.getString(R.string.app_name);
        mRepository.insertStudent(student);
    }

    @JavascriptInterface
    public String getLastSelectedStudent() {
        return mSharedPreferences.getString(SELECTED_STUDENT_KEY, "");
    }

    @JavascriptInterface
    public void saveLastSelectedStudent(String studentId) {
        SharedPreferences.Editor editor = mSharedPreferences.edit();
        editor.putString(SELECTED_STUDENT_KEY, studentId);
        editor.apply();
    }

    @JavascriptInterface
    public String getLastSelectedLanguage() {
        return mSharedPreferences.getString(SELECTED_LANGUAGE_KEY, "");
    }

    @JavascriptInterface
    public void saveLastSelectedLanguage(String language) {
        SharedPreferences.Editor editor = mSharedPreferences.edit();
        editor.putString(SELECTED_LANGUAGE_KEY, language);
        editor.apply();
    }

}
