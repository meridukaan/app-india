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
public interface StudentDao extends DataSource<Student> {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(Student student);

    @Update(onConflict = OnConflictStrategy.REPLACE)
    void update(Student student);

    @Delete
    void delete(Student...students);

    @Query("SELECT * FROM Student")
    List<Student> getAll();

    @Query("SELECT * FROM Student WHERE StudentID IN(:params)")
    List<Student> findByKey(String...params);

    @Query("SELECT * FROM Student WHERE CreatedOn > :date OR UpdatedDate > :date")
    List<Student> getModifiedAfter(Date date);
}