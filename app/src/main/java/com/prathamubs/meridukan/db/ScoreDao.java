package com.prathamubs.meridukan.db;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;

import java.util.Date;
import java.util.List;

@Dao
public interface ScoreDao extends DataSource<Score> {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(Score score);

    @Update(onConflict = OnConflictStrategy.REPLACE)
    void update(Score score);

    @Delete
    void delete(Score...scores);

    @Query("SELECT * FROM Scores")
    List<Score> getAll();

    @Query("SELECT * FROM Scores WHERE EndDateTime > :date")
    List<Score> getModifiedAfter(Date date);
}
