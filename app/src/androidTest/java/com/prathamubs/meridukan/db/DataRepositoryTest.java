package com.prathamubs.meridukan.db;

import android.arch.persistence.room.Room;
import android.content.Context;
import android.support.test.InstrumentationRegistry;
import android.support.test.runner.AndroidJUnit4;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.concurrent.ExecutionException;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

@RunWith(AndroidJUnit4.class)
public class DataRepositoryTest {
    DataRepository repository;
    AppDatabase database;

    @Before
    public void createDb() {
        Context context = InstrumentationRegistry.getContext();
        database = Room.inMemoryDatabaseBuilder(context, AppDatabase.class)
                .allowMainThreadQueries()
                .build();
        repository = new DataRepository(database);
    }

    @After
    public void closeDb() {
        database.close();
    }

    @Test
    public void writeScoreAndReadInList() throws InterruptedException, ExecutionException {
        Score score = new Score(
                "xxx", "xxx", "xxx", "xxx", 1,
                1, 1, Calendar.getInstance().getTime(), Calendar.getInstance().getTime(), "xxx");
        repository.insertScore(score).get();
        List<Score> scores = database.scoreDao().getAll();
        assertThat(scores.size(), is(equalTo(1)));
        assertThat(scores.get(0).SessionID, is(equalTo(score.SessionID)));
        assertThat(scores.get(0).TotalMarks, is(equalTo(score.TotalMarks)));
        assertThat(scores.get(0).Label, is(equalTo(score.Label)));
    }

    @Test
    public void queryModifiedAfter() throws InterruptedException, ExecutionException {
        Date now = Calendar.getInstance().getTime();
        Date past = Date.from(now.toInstant().minusSeconds(60));
        Date future = Date.from(now.toInstant().plusSeconds(60));
        Score scorePast = new Score(
                "pas", "pas", "pas", "pas", 1,
                1, 1, past, past, "pas");
        Score scoreFuture = new Score(
                "fut", "fut", "fut", "fut", 1,
                1, 1, future, future, "fut");
        repository.insertScore(scorePast).get();
        repository.insertScore(scoreFuture).get();
        List<Score> scores = database.scoreDao().getAll();
        assertThat(scores.size(), is(equalTo(2)));

        scores = database.scoreDao().getModifiedAfter(now);
        assertThat(scores.size(), is(equalTo(1)));
    }

    @Test
    public void writeStudentAndReadInList() throws InterruptedException, ExecutionException {
        Student student = new Student();
        student.StudentID = "xxx";
        repository.insertStudent(student).get();
        List<Student> students = database.studentDao().getAll();
        assertThat(students.size(), is(equalTo(1)));
        assertThat(students.get(0).StudentID, is(equalTo("xxx")));
        assertThat(students.get(0).StudentUID, is(equalTo("")));
    }
}