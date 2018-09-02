package com.prathamubs.meridukan;

import android.content.Context;
import android.webkit.JavascriptInterface;

public class WebAppInterface {
    Context mContext;

    WebAppInterface(Context context) {
        mContext = context;
    }

    @JavascriptInterface
    public void addScore(String studentId, int questionId, int scorefromGame, int totalMarks,
                         int level, String startTime, String label) {
        // TODO store in room database
    }
}
