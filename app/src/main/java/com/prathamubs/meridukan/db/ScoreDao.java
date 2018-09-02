package com.prathamubs.meridukan.db;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;

import java.util.List;

@Dao
public interface ScoreDao {
    @Insert
    void insert(Score score);

    @Query("SELECT * from Scores")
    List<Score> getAll();
}
