package com.prathamubs.meridukan.db;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;

import java.util.List;

@Dao
public interface StudentDao extends Insertable<Student> {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(Student student);

    @Query("SELECT * from Student")
    List<Student> getAll();
}