package com.prathamubs.meridukan;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.webkit.WebView;

import com.prathamubs.meridukan.push.PushDataWorker;

import androidx.work.Constraints;
import androidx.work.NetworkType;
import androidx.work.OneTimeWorkRequest;
import androidx.work.WorkManager;

public class MainActivity extends AppCompatActivity {

    private WebView mWebView;

    @Override
    protected void onPause() {
        super.onPause();
        mWebView.onPause();
        mWebView.pauseTimers();
    }

    @Override
    protected void onResume() {
        super.onResume();
        mWebView.resumeTimers();
        mWebView.onResume();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mWebView.destroy();
        mWebView = null;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mWebView = findViewById(R.id.activity_main_webview);
        mWebView.getSettings().setJavaScriptEnabled(true);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
            mWebView.getSettings().setMediaPlaybackRequiresUserGesture(false);
        }
        mWebView.getSettings().setDomStorageEnabled(true);
        mWebView.setWebViewClient(new GameWebViewClient());

        mWebView.addJavascriptInterface(new WebAppInterface(this), "Android");

        mWebView.loadUrl("file:///android_asset/www/index.html");

        scheduleDataPush();
    }

    private void scheduleDataPush() {
        Constraints constraints = new Constraints.Builder()
                .setRequiredNetworkType(NetworkType.CONNECTED)
                .build();
        OneTimeWorkRequest workRequest = new OneTimeWorkRequest.Builder(PushDataWorker.class)
                .setConstraints(constraints)
                .build();
        WorkManager.getInstance().enqueue(workRequest);
    }

    @Override
    public void onBackPressed() {
        new AlertDialog.Builder(this)
                .setMessage(R.string.exit_msg)
                .setCancelable(false)
                .setPositiveButton(R.string.res_yes, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        MainActivity.super.onBackPressed();
                    }
                })
                .setNegativeButton(R.string.res_no, null)
                .show();
    }
}
