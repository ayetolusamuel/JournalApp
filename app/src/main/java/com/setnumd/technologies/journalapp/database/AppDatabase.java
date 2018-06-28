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

    public static AppDatabase getInstance(final Context context) {
        if (INSTANCE == null){
            synchronized (lock){
                if (INSTANCE == null){
                    //create database Here......
                    INSTANCE = Room.databaseBuilder(context.getApplicationContext(),
                            AppDatabase.class, "customer_database")
                            .allowMainThreadQueries()
                            .build();
                }
            }
        }
        return INSTANCE;
    }


}
