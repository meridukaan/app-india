package com.prathamubs.meridukan;

import android.arch.lifecycle.LiveData;
import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.provider.Settings;
import android.webkit.JavascriptInterface;

import com.prathamubs.meridukan.db.DataRepository;
import com.prathamubs.meridukan.db.Score;
import com.prathamubs.meridukan.db.Student;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;
import java.util.UUID;

public class WebAppInterface {
    Context mContext;
    DataRepository mRepository;
    String mDeviceId;
    SharedPreferences mSharedPreferences;

    private final static DateFormat DATE_TIME_FORMAT = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss", Locale.ENGLISH);
    private final static String SELECTED_STUDENT_KEY = "SelectedStudent";

    LiveData<List<Student>> mStudentsLiveData;

    WebAppInterface(Context context) {
        mContext = context;

        mRepository = new DataRepository(context);
        mStudentsLiveData = mRepository.getStudents();

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
        String groupId = "gid";
        String endTime = currentTime();
        Score score = new Score(sessionId, groupId, mDeviceId, studentId, questionId, scorefromGame,
                totalMarks, startTime, endTime, level, label);
        mRepository.insertScore(score);
    }

    @JavascriptInterface
    public List<Student> getStudentList() {
        return new ArrayList();
    }

    @JavascriptInterface
    public List<Student> getStoredStudentList() {
        return mStudentsLiveData.getValue();
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
        student.UpdatedDate = student.CreatedOn = currentTime();
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

    private String currentTime() {
        return DATE_TIME_FORMAT.format(Calendar.getInstance().getTime());
    }
}
