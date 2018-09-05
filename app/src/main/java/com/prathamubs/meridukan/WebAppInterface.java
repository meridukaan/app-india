package com.prathamubs.meridukan;

import android.arch.lifecycle.LiveData;
import android.content.Context;
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

    private final DateFormat dateTimeFormat = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss", Locale.ENGLISH);

    LiveData<List<Student>> mStudentsLiveData;

    WebAppInterface(Context context) {
        mContext = context;
        mRepository = new DataRepository(context);
        mDeviceId = Settings.Secure.getString(mContext.getContentResolver(), Settings.Secure.ANDROID_ID);
        if (mDeviceId == null) {
            mDeviceId = "0000";
        }

        mStudentsLiveData = mRepository.getStudents();
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
    public void storeStudent(String firstName, String middleName, String lastName, int age, int cls,
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

    private String currentTime() {
        return dateTimeFormat.format(Calendar.getInstance().getTime());
    }
}
