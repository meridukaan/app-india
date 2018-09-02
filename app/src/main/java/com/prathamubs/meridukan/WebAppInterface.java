package com.prathamubs.meridukan;

import android.content.Context;
import android.provider.Settings;
import android.webkit.JavascriptInterface;

import com.prathamubs.meridukan.db.DataRepository;
import com.prathamubs.meridukan.db.Score;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

public class WebAppInterface {
    Context mContext;
    DataRepository mRepository;
    String mDeviceId;

    private final DateFormat timeFormat = new SimpleDateFormat("HH:mm:ss", Locale.ENGLISH);

    WebAppInterface(Context context) {
        mContext = context;
        mRepository = new DataRepository(context);
        mDeviceId = Settings.Secure.getString(mContext.getContentResolver(), Settings.Secure.ANDROID_ID);
        if (mDeviceId == null) {
            mDeviceId = "0000";
        }
    }

    @JavascriptInterface
    public void addScore(String studentId, int questionId, int scorefromGame, int totalMarks,
                         int level, String startTime, String label) {
        // TODO confirm significance with Pratham team and populate accordingly
        String sessionId = "sid";
        String groupId = "gid";
        String endTime = timeFormat.format(Calendar.getInstance().getTime());
        Score score = new Score(sessionId, groupId, mDeviceId, studentId, questionId, scorefromGame,
                totalMarks, startTime, endTime, level, label);
        mRepository.insertScore(score);
    }
}
