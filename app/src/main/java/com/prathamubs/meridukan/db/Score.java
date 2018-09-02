package com.prathamubs.meridukan.db;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.Ignore;
import android.support.annotation.NonNull;

@Entity(tableName = "Scores", primaryKeys =
        {"SessionID", "DeviceID", "ResourceID", "QuestionID", "StartDateTime"})
public class Score {

    @NonNull
    @ColumnInfo(name = "SessionID")
    public String sessionId;

    @ColumnInfo(name = "GroupID")
    public String groupId;

    @NonNull
    @ColumnInfo(name = "DeviceID")
    public String deviceId;

    @NonNull
    @ColumnInfo(name = "ResourceID")
    public String resourceId;

    @NonNull
    @ColumnInfo(name = "QuestionID")
    public int questionId;

    @NonNull
    @ColumnInfo(name = "ScoredMarks")
    public int scoredMarks;

    @NonNull
    @ColumnInfo(name = "TotalMarks")
    public int totalMarks;

    @NonNull
    @ColumnInfo(name = "StartDateTime")
    public String startDateTime;

    @ColumnInfo(name = "EndDateTime")
    public String endDateTime;

    @ColumnInfo(name = "Level")
    public int level = 1;

    @ColumnInfo(name = "Label")
    public String label;

    public Score(String sessionId, String groupId, String deviceId, String resourceId,
                 int questionId, int scoredMarks, int totalMarks, String startDateTime,
                 String endDateTime, String label) {
        this.sessionId = sessionId;
        this.groupId = groupId;
        this.deviceId = deviceId;
        this.resourceId = resourceId;
        this.questionId = questionId;
        this.scoredMarks = scoredMarks;
        this.totalMarks = totalMarks;
        this.startDateTime = startDateTime;
        this.endDateTime = endDateTime;
        this.label = label;
    }

    @Ignore
    public Score(String sessionId, String groupId, String deviceId, String resourceId,
                 int questionId, int scoredMarks, int totalMarks, String startDateTime,
                 String endDateTime, int level, String label) {
        this(sessionId, groupId, deviceId, resourceId, questionId, scoredMarks, totalMarks,
                startDateTime, endDateTime, label);
        this.level = level;
    }
}
