package com.setnumd.technologies.journalapp.dao;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;

import com.setnumd.technologies.journalapp.contracts.Journal;

import java.util.ArrayList;
import java.util.List;

@Dao
public interface JournalDao {
    @Query("SELECT * FROM journal ORDER BY user")
    List<Journal> loadAllDiaries();

    @Insert
    void insertDiary(Journal journal);

    @Update(onConflict = OnConflictStrategy.REPLACE)
    void updateDiary(Journal journal);

    @Query("SELECT * FROM journal where id =:id")
    Journal loadDiaryById(int id);


}
