package com.prathamubs.meridukan.db;

import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;

import java.util.Date;
import java.util.List;

@Dao
public interface StudentDao extends Insertable<Student> {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(Student student);

    @Query("SELECT * FROM Student")
    LiveData<List<Student>> getAll();

    @Query("SELECT * FROM Student WHERE CreatedOn > :date OR UpdatedDate > :date")
    List<Student> getModifiedAfter(Date date);
}