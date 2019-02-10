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

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.ExecutionException;

public class WebAppInterface {
    private Context mContext;
    private DataRepository mRepository;
    private String mDeviceId;
    private SharedPreferences mSharedPreferences;

    private static final String TAG = WebAppInterface.class.getName();
    private static final String SELECTED_STUDENT_KEY = "SelectedStudent";
    private static final String SELECTED_LANGUAGE_KEY = "SelectedLanguage";

    private AsyncTask<?, ?, List<Student>> mStudentsQueryTask;
    private String mSessionId = null;

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
    public boolean isOnlineVersion() {
        return true;
    }

    @JavascriptInterface
    public void addScore(String studentId, int questionId, int scorefromGame, int totalMarks,
                         int level, String startTime, String label) {
        if (mSessionId == null) {
            startSession();
        }
        String sessionId = mSessionId;
        String groupId = "";
        Date endDateTime = Calendar.getInstance().getTime();
        Date startDateTime = Converters.dateFromString(startTime);
        Score score = new Score(sessionId, groupId, mDeviceId, studentId, questionId, scorefromGame,
                totalMarks, startDateTime, endDateTime, level, label);
        mRepository.insertScore(score);
    }

    @JavascriptInterface
    public String getStudentList() throws JSONException {
        List<Student> students = null;
        try {
            students = mStudentsQueryTask.get();
        } catch (ExecutionException e) {
            Log.e(TAG, "Error getting students", e);
        } catch (InterruptedException e) {
            Log.w(TAG, "Error getting students", e);
        }
        JSONArray json = new JSONArray();
        for (Student s : students) {
            JSONObject jo = new JSONObject();
            jo.put("StudentId", s.StudentID);
            String name = s.FirstName + (s.MiddleName.isEmpty() ? "" : " " + s.MiddleName) +
                    (s.LastName.isEmpty() ? "" : " " + s.LastName);
            jo.put("StudentName", name);
            jo.put("StudentAge", s.Age);
            jo.put("StudentGender", s.Gender);
            json.put(jo);
        }
        return json.toString();
    }

    @JavascriptInterface
    public void addStudents(String data) throws JSONException {
        JSONArray json = new JSONArray(data);
        for (int i = 0; i < json.length(); i++) {
            addStudent(json.getString(i));
        }
        mStudentsQueryTask = mRepository.getStudentsAsync();
    }

    @JavascriptInterface
    public void addStudent(String data) throws JSONException {
        JSONObject json = new JSONObject(data);
        Student student = new Student();
        student.StudentID = UUID.randomUUID().toString();
        student.FirstName = json.optString("firstName", json.getString("name"));
        student.MiddleName = json.optString("middleName");
        student.LastName = json.optString("lastName");
        student.Age = json.getInt("age");
        student.Class = json.optInt("cls");
        student.Gender = json.getString("gender");
        student.UpdatedDate = student.CreatedOn = Calendar.getInstance().getTime();
        student.appName = mContext.getString(R.string.app_name);
        mRepository.insertStudent(student);
    }

    @JavascriptInterface
    public void updateStudent(String data) throws JSONException {
        JSONObject json = new JSONObject(data);
        Student student = new Student();
        student.StudentID = json.getString("StudentID");
        student.FirstName = json.optString("firstName", json.getString("StudentName"));
        student.MiddleName = json.optString("middleName");
        student.LastName = json.optString("lastName");
        student.Age = json.getInt("StudentAge");
        student.Class = json.optInt("cls");
        student.Gender = json.getString("StudentGender");
        student.UpdatedDate = student.CreatedOn = Calendar.getInstance().getTime();
        student.appName = mContext.getString(R.string.app_name);
        mRepository.updateStudent(student);
    }

    @JavascriptInterface
    public void deleteStudent(String studentId) {
        mRepository.deleteStudent(studentId);
    }

    @JavascriptInterface
    public String getLastSelectedStudent() {
        return mSharedPreferences.getString(SELECTED_STUDENT_KEY, "");
    }

    @JavascriptInterface
    public void saveLastSelectedStudent(String studentId) {
        startSession();
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

    @JavascriptInterface
    public void startSession() {
        mSessionId = UUID.randomUUID().toString();
        addScore(getLastSelectedStudent(), 0, 0, 0, 0,
                Converters.dateToString(Calendar.getInstance().getTime()), "");
    }

    @JavascriptInterface
    public void endSession() {
        mSessionId = null;
    }

}