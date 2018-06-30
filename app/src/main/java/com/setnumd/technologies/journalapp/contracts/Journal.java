package com.setnumd.technologies.journalapp.contracts;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.Ignore;
import android.arch.persistence.room.PrimaryKey;

@Entity(tableName = "journal")
public class Journal {
   @PrimaryKey(autoGenerate = true)
    int id;
String title;
    String content;

    @Ignore
    public Journal( String title, String content) {

        this.title = title;
        this.content = content;
    }

    public Journal(int id, String title, String content) {
        this.id = id;
        this.title = title;
        this.content = content;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }


    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }
}
