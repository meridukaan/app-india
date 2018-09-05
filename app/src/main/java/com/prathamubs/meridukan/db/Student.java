package com.prathamubs.meridukan.db;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;
import android.support.annotation.NonNull;

import com.prathamubs.meridukan.BuildConfig;
import com.prathamubs.meridukan.R;

@Entity
public class Student {
    @PrimaryKey @NonNull
    public String StudentID;
    public String FirstName;
    public String MiddleName;
    public String LastName;
    public int Age;
    public int Class;
    public String UpdatedDate;
    public String Gender;
    public String GroupID = "";
    public String CreatedBy = "";
    public boolean NewFlag = true;
    public String StudentUID; // TODO confirm value
    public boolean IsSelected = false;
    public String sharedBy = "";
    public String SharedAtDateTime = "";
    public String appVersion = BuildConfig.VERSION_NAME;
    public String appName;
    public String CreatedOn;
}