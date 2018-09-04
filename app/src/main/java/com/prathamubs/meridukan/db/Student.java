package com.prathamubs.meridukan.db;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;
import android.support.annotation.NonNull;

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
    public String GroupID;
    public String CreatedBy;
    public boolean NewFlag;
    public String StudentUID;
    public boolean IsSelected;
    public String sharedBy;
    public String SharedAtDateTime;
    public String appVersion;
    public String appName;
    public String CreatedOn;
}