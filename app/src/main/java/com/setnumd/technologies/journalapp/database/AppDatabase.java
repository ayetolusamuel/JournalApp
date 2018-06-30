package com.setnumd.technologies.journalapp.database;

import android.arch.persistence.room.Database;
import android.arch.persistence.room.Room;
import android.arch.persistence.room.RoomDatabase;
import android.content.Context;

import com.setnumd.technologies.journalapp.contracts.Journal;
import com.setnumd.technologies.journalapp.dao.JournalDao;


@Database(entities = {Journal.class}, version = 1, exportSchema = false)
public abstract class AppDatabase extends RoomDatabase{
    private static AppDatabase INSTANCE;
    private static Object lock = new Object();
    public abstract JournalDao diaryDao();
    private static final String DATABASE_NAME ="journal_database";

    public static AppDatabase getInstance(final Context context) {
        if (INSTANCE == null){
            synchronized (lock){
                if (INSTANCE == null){
                    //create database Here......
                    INSTANCE = Room.databaseBuilder(context.getApplicationContext(),
                            AppDatabase.class, DATABASE_NAME)
                          //  .allowMainThreadQueries()
                            .build();
                }
            }
        }
        return INSTANCE;
    }


}
