package com.prathamubs.meridukan.db;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.Ignore;
import android.support.annotation.NonNull;

@Entity(tableName = "Scores", primaryKeys =
        {"SessionID", "DeviceID", "ResourceID", "QuestionID", "StartDateTime"})
public class Score {

    @NonNull
    public String SessionID;
    public String GroupID;
    @NonNull
    public String DeviceID;
    @NonNull
    public String ResourceID;
    @NonNull
    public int QuestionID;
    @NonNull
    public int ScoredMarks;
    @NonNull
    public int TotalMarks;
    @NonNull
    public String StartDateTime;
    public String EndDateTime;
    public int Level = 1;
    public String Label;

    public Score(String SessionID, String GroupID, String DeviceID, String ResourceID,
                 int QuestionID, int ScoredMarks, int TotalMarks, String StartDateTime,
                 String EndDateTime, String Label) {
        this.SessionID = SessionID;
        this.GroupID = GroupID;
        this.DeviceID = DeviceID;
        this.ResourceID = ResourceID;
        this.QuestionID = QuestionID;
        this.ScoredMarks = ScoredMarks;
        this.TotalMarks = TotalMarks;
        this.StartDateTime = StartDateTime;
        this.EndDateTime = EndDateTime;
        this.Label = Label;
    }

    @Ignore
    public Score(String SessionID, String GroupID, String DeviceID, String ResourceID,
                 int QuestionID, int ScoredMarks, int TotalMarks, String StartDateTime,
                 String EndDateTime, int Level, String Label) {
        this(SessionID, GroupID, DeviceID, ResourceID, QuestionID, ScoredMarks, TotalMarks,
                StartDateTime, EndDateTime, Label);
        this.Level = Level;
    }
}
