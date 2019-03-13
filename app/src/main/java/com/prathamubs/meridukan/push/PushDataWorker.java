package com.prathamubs.meridukan.push;

import android.content.Context;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.preference.PreferenceManager;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.prathamubs.meridukan.BuildConfig;
import com.prathamubs.meridukan.R;
import com.prathamubs.meridukan.db.DataRepository;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.UUID;

import androidx.work.Worker;

public class PushDataWorker extends Worker {

    private static final String TAG = PushDataWorker.class.getName();
    private static final String LAST_SYNC_DATE_KEY = "LastSync";

    private DataRepository mRepository;
    private String mDeviceId;
    private SharedPreferences mSharedPreferences;

    private void init() {
        if (mRepository == null) {
            Context context = getApplicationContext();
            mRepository = new DataRepository(context);
            mDeviceId = Settings.Secure.getString(context.getContentResolver(), Settings.Secure.ANDROID_ID);
            if (mDeviceId == null) {
                mDeviceId = "0000";
            }

            mSharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
        }
    }

    @NonNull
    @Override
    public Result doWork() {
        init();
        try {
            long lastSyncTime = mSharedPreferences.getLong(LAST_SYNC_DATE_KEY, 0);
            Date lastSync = new Date(lastSyncTime);
            JSONArray scores = toJson(mRepository.getScoresModifiedAfter(lastSync));
            JSONArray students = toJson(mRepository.getStudentsModifiedAfter(lastSync));

            if (scores.length() == 0 && students.length() == 0) {
                Log.i(TAG, "No data to be sent");
                return Result.SUCCESS;
            }

            JSONObject empty = new JSONObject();
            String requestString = "{ \"metadata\": " + getMetaData(scores.length(), students.length())
                    + ", \"scoreData\": " + scores + ", \"LogsData\": " + empty + ", \"attendanceData\": "
                    + empty + ", \"newStudentsData\": " + empty + ", \"newCrlsData\": " + empty +
                    ", \"newGroupsData\": " + empty + ", \"AserTableData\": " + empty + "}";

            String url = getApplicationContext().getString(R.string.pratham_url);

            Log.i(TAG, "Sending data to server @" + url);
            Log.v(TAG, requestString);
            if (!HttpPost.send(url, requestString)) {
                Log.w(TAG, "Unable to send data");
                return Result.FAILURE;
            }
            Log.i(TAG, "Data sent");
        } catch (JSONException e) {
            Log.e(TAG, "Error occurred in creating JSON data", e);
            return Result.FAILURE;
        } catch (IOException e) {
            if (!isConnected()) {
                Log.w(TAG, "Connectivity issues in uploading data", e);
                return Result.RETRY;
            }
            Log.e(TAG, "Error occurred in uploading data", e);
            return Result.FAILURE;
        }
        SharedPreferences.Editor editor = mSharedPreferences.edit();
        editor.putLong(LAST_SYNC_DATE_KEY, Calendar.getInstance().getTimeInMillis());
        editor.commit();
        return Result.SUCCESS;
    }

    private <T> JSONArray toJson(List<T> items) throws JSONException {
        JSONArray json = new JSONArray();
        if (items != null) {
            for (T item : items) {
                Gson gson = new GsonBuilder().create();
                json.put(new JSONObject(gson.toJson(item)));
            }
        }
        return json ;
    }

    private JSONObject getMetaData(int scoresCount, int studentsCount) {
        JSONObject obj = new JSONObject();
        try {
            obj.put("ScoreCount", scoresCount);
            obj.put("AttendanceCount", 0);
            obj.put("CRLID", "");
            obj.put("NewStudentsCount", studentsCount);
            obj.put("NewCrlsCount", 0);
            obj.put("NewGroupsCount", 0);
            obj.put("AserDataCount", 0);
            obj.put("TransId", UUID.randomUUID());
            obj.put("DeviceId", mDeviceId);
            obj.put("MobileNumber", "0");
            obj.put("ActivatedDate", "");
            obj.put("ActivatedForGroups", "");

            // new status table fields
            obj.put("Latitude", "");
            obj.put("Longitude", "");
            obj.put("GPSDateTime", "");
            obj.put("AndroidID", mDeviceId);
            obj.put("SerialID", "");
            obj.put("apkVersion", BuildConfig.VERSION_NAME);
            obj.put("appName", getApplicationContext().getString(R.string.app_name));
            obj.put("gpsFixDuration", "");
            obj.put("wifiMAC", "");
            obj.put("apkType", "");
            obj.put("prathamCode", "");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return obj;
    }

    private boolean isConnected() {
        ConnectivityManager cm =
                (ConnectivityManager)getApplicationContext().getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        return activeNetwork != null && activeNetwork.isConnectedOrConnecting();
    }
}
